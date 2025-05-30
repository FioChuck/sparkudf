package com.example.serverless.sparkudf;

import org.apache.spark.sql.api.java.UDF0;
import org.apache.spark.TaskContext;


public class HttpServiceUDF implements UDF0<String> {

    @Override
    public String call() throws Exception {

        TaskContext tc = TaskContext.get();
        String executorLog = "";

        if (tc != null) {
            executorLog = " - LOGS: UDF EXECUTION CONFIRMED -> Stage: " + tc.stageId() +
                    ", Partition: " + tc.partitionId() +
                    ", Task Attempt: " + tc.taskAttemptId();
        } else {

            executorLog = " - LOGS: TaskContext is not available.";
        }

        System.out.println(executorLog);

        WebhookService service = new WebhookService();
        String ip = service.getPublicIpAddress();
        String output = ip.concat(executorLog);
        return output;
    }
}