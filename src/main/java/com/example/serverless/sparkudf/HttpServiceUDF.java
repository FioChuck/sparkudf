package com.example.serverless.sparkudf;

import com.example.serverless.sparkudf.WebhookService;
import org.apache.spark.sql.api.java.UDF0;

public class HttpServiceUDF implements UDF0<String> {

    @Override
    public String call() throws Exception {
        WebhookService service = new WebhookService();
        return service.getPublicIpAddress();
    }
}