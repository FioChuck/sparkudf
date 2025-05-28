gcloud dataproc batches submit \
--project spark-webserver-demo \
--region us-central1 spark \
--batch batch-2 \
--jar gs://cf-spark-jobs-temp/jars/sparkudf-1.0.0-jar-with-dependencies.jar \
--files gs://java-resources/cacerts.simple \
--properties spark.driver.extraJavaOptions="-Djavax.net.ssl.trustStore=cacerts.simple -Djavax.net.ssl.trustStorePassword=changeit"


properties="spark.driver.extraJavaOptions=-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=pwd -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=pwd,spark.dataproc.diagnostics.enabled=true" \