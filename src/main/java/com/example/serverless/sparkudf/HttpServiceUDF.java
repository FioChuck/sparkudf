package com.example.serverless.sparkudf;

import org.apache.spark.sql.api.java.UDF0;
import org.apache.spark.TaskContext;


public class HttpServiceUDF implements UDF0<String> {

    @Override
    public String call() throws Exception {

        TaskContext tc = TaskContext.get();

        String executorLog = "UDF EXECUTION CONFIRMED -> Stage: " + tc.stageId() +
                ", Partition: " + tc.partitionId() +
                ", Task Attempt: " + tc.taskAttemptId();

        // This will print to the executor's stdout log
        System.out.println(executorLog);

        WebhookService service = new WebhookService();
        return service.getPublicIpAddress();
    }
}