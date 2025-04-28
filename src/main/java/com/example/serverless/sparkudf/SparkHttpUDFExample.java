package com.example.serverless.sparkudf;
// Import HttpServiceUDF from the same package (explicit import is good practice)
import com.example.serverless.sparkudf.HttpServiceUDF;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;

import java.util.Arrays;
import java.util.List;

import static org.apache.spark.sql.functions.callUDF;

public class SparkHttpUDFExample {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Spark HTTP UDF Example")
                .master("local[*]")
                .getOrCreate();

        spark.udf().register("getHttpResponse", new HttpServiceUDF(), DataTypes.StringType);

        List<Row> data = Arrays.asList(
                org.apache.spark.sql.RowFactory.create(1, "Google"),
                org.apache.spark.sql.RowFactory.create(2, "Microsoft"),
                org.apache.spark.sql.RowFactory.create(3, "Apple")
        );
        org.apache.spark.sql.types.StructType schema = DataTypes.createStructType(Arrays.asList(
                DataTypes.createStructField("id", DataTypes.IntegerType, false),
                DataTypes.createStructField("value", DataTypes.StringType, false)
        ));
        Dataset<Row> df = spark.createDataFrame(data, schema);

        Dataset<Row> dfWithHttpResponse = df.withColumn("http_response",
                callUDF("getHttpResponse"));

        dfWithHttpResponse.show(false);

    }
}