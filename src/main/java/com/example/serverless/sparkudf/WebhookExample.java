package com.example.serverless.sparkudf;

public class WebhookExample {
    public static void main(String[] args) {
    WebhookService service = new WebhookService();
    System.out.println(service.getPublicIpAddress());
    }
}
