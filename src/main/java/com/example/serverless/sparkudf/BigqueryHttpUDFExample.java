package com.example.serverless.sparkudf;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.types.DataTypes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.callUDF;

public class BigqueryHttpUDFExample {
    public static void main(String[] args) throws IOException {

        SparkSession spark = SparkSession.builder()
                .appName("Serverless Demo")
//				.master("local[*]")
//                .config("spark.sql.adaptive.enabled", "false")
//                .config("spark.default.parallelism", "2000")
//				.config("spark.eventLog.enabled", "true")
//				.config("spark.eventLog.dir", "gs://cf-phs/spark-job-history")
//				.config("spark.hadoop.fs.gs.project.id", "cf-data-analytics")
//				.config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
//				.config("spark.hadoop.fs.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFileSystem")
//				.config("spark.hadoop.fs.AbstractFileSystem.gs.impl", "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
//				.config("spark.hadoop.fs.gs.auth.type", "APPLICATION_DEFAULT")
                .getOrCreate();

		spark.udf().register("getHttpResponse", new HttpServiceUDF(), DataTypes.StringType);

//		System.out.print("done");

		Dataset<Row> aggregatedDF = spark.read()
                .format("bigquery")
                .option("table", "bigquery-public-data.wikipedia.pageviews_2024")
                .load()
                .filter(functions.to_date(functions.col("datehour")).between("2024-01-01", "2024-01-01"))
                .filter(functions.col("wiki").equalTo("en"))
                .withColumn("month", functions.date_format(functions.col("datehour"), "MMMM"))
                .groupBy(functions.col("title"), functions.col("month"))
                .agg(functions.sum("views").alias("total_views"))
                .orderBy(functions.col("total_views").desc())
                .limit(1000);

        aggregatedDF.repartition(200);
		aggregatedDF.show();

		Dataset<Row> dfWithHttpResponse = aggregatedDF.withColumn("http_response",
				callUDF("getHttpResponse"));

		dfWithHttpResponse.show(false);

		System.out.print("done");

	}
}