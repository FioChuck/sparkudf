package com.example.serverless.sparkudf;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import java.util.Arrays;
import java.util.List;

public class IcebergExample {

    public static void main(String[] args) {
        // Define your GCS warehouse path and Hive Metastore URI
        String gcsWarehousePath = "gs://cf-iceberg-datalakeiceberg_warehouse"; // Replace with your GCS bucket and warehouse path
        String hiveMetastoreUri = "thrift://10.6.64.27:9083"; // Replace with your Hive Metastore URI
        String catalogName = "hive_catalog"; // Or any name you prefer for your Iceberg catalog

        SparkSession spark = SparkSession.builder()
                .appName("SparkIceberg")
                .master("local[*]")
                .config("spark.sql.adaptive.enabled", "false")
                .config("spark.default.parallelism", "2000")
                .config("spark.eventLog.enabled", "true")
                .config("spark.eventLog.dir", "gs://cf-phs/spark-job-history")
                .config("spark.hadoop.fs.gs.project.id", "cf-data-analytics")
                .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
                .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
                .config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
                .config(
                        "spark.hadoop.google.cloud.auth.service.account.json.keyfile",
                        "/home/user/IdeaProjects/sparkudf/spark-webserver-demo-f455e9845281.json"
                )
                .config("spark.sql.catalog." + catalogName + ".uri", hiveMetastoreUri)
                .config("spark.hadoop.hive.metastore.uris", hiveMetastoreUri) // For Spark's Hive support
                .config("spark.sql.catalog." + catalogName + ".warehouse", gcsWarehousePath)
                .config("spark.sql.catalog." + catalogName + ".type", "hive") // Use HiveCatalog
                .config("spark.sql.catalog." + catalogName + ".io-impl", "org.apache.iceberg.gcp.gcs.GCSFileIO")

                // Iceberg Spark Extensions
                .config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions")

                // Spark packages (alternative to providing JARs via spark-submit)
                // Not strictly needed if using a fat JAR or if JARs are in Spark's classpath
                // .config("spark.jars.packages", "org.apache.iceberg:iceberg-spark-runtime-3.5_2.12:1.5.0,org.apache.iceberg:iceberg-gcp-bundle:1.5.0")

                // GCS Connector Configuration (authentication will be handled by the environment typically)
                .config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
                .config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
                // For GCS authentication, ensure your environment is set up (e.g., service account on Dataproc VM,
                // or GOOGLE_APPLICATION_CREDENTIALS environment variable).
                // You might need to configure project ID if not implicitly available:
                // .config("spark.hadoop.google.cloud.auth.service.account.enable", "true") // If using a service account JSON key
                // .config("spark.hadoop.google.cloud.auth.service.account.json.keyfile", "/path/to/your/service-account.json") // If keyfile is used
                // .config("spark.hadoop.fs.gs.project.id", "your-gcp-project-id") // Often needed

                .enableHiveSupport() // Enable Hive support for SparkSession
                .getOrCreate();

        System.out.println("SparkSession configured successfully for Iceberg with Hive Metastore and GCS.");

        // Define your database and table name
        String dbName = "iceberg_db";
        String tableName = "example_gcs_table";
        String fullTableName = catalogName + "." + dbName + "." + tableName;

        // Create a database if it doesn't exist (using the configured catalog)
        spark.sql("CREATE DATABASE IF NOT EXISTS " + catalogName + "." + dbName + " LOCATION '" + gcsWarehousePath + "/" + dbName + ".db'");
        System.out.println("Database " + dbName + " ensured in catalog " + catalogName);


        // Create sample data
        List<SampleData> data = Arrays.asList(
                new SampleData(1, "Alice", 30),
                new SampleData(2, "Bob", 24),
                new SampleData(3, "Charlie", 35)
        );
        Dataset<Row> df = spark.createDataFrame(data, SampleData.class);
        System.out.println("Sample DataFrame created:");
        df.show();

        // Write DataFrame to Iceberg table in GCS
        // Option 1: Using DataFrameWriterV2 API (Recommended for programmatic writes)
        System.out.println("Writing DataFrame to Iceberg table: " + fullTableName);
        df.writeTo(fullTableName)
                .tableProperty("write.format.default", "parquet") // Optional: set default format
                .createOrReplace(); // or .append() or .overwritePartitions()

        System.out.println("Successfully wrote data to Iceberg table: " + fullTableName);

        // Option 2: Using Spark SQL (Alternative)
        // df.createOrReplaceTempView("temp_source_data");
        // spark.sql("CREATE TABLE IF NOT EXISTS " + fullTableName +
        //           " (id INT, name STRING, age INT)" +
        //           " USING iceberg" +
        //           " TBLPROPERTIES ('format-version'='2')" + // Optional: specify Iceberg format version
        //           " LOCATION '" + gcsWarehousePath + "/" + dbName + ".db/" + tableName + "'"); // Location for HadoopCatalog, HiveCatalog infers from warehouse
        // spark.sql("INSERT INTO " + fullTableName + " SELECT * FROM temp_source_data");
        // System.out.println("Successfully wrote data using Spark SQL to Iceberg table: " + fullTableName);


        // Read data from the Iceberg table to verify
        System.out.println("Reading data from Iceberg table: " + fullTableName);
        Dataset<Row> icebergTableDF = spark.read().table(fullTableName);
        icebergTableDF.show();

        System.out.println("Job completed.");
        spark.stop();
    }

    // Sample data class (must be public for Spark to create DataFrame)
    public static class SampleData implements java.io.Serializable {
        private int id;
        private String name;
        private int age;

        public SampleData() {}

        public SampleData(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }
}
