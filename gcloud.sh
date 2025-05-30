##gcloud dataproc batches submit \
  --project spark-webserver-demo \
  --region us-central1 spark \
  --jar gs://cf-spark-jobs-temp/jars/sparkudf-1.1.0-jar-with-dependencies.jar \
  --files gs://java-resources/cacerts_demo \
  --history-server-cluster projects/spark-webserver-demo/regions/us-central1/clusters/phs \
  --properties spark.driver.extraJavaOptions="-Djavax.net.ssl.trustStore=cacerts_demo -Djavax.net.ssl.trustStorePassword=changeit",spark.executor.extraJavaOptions="-Djavax.net.ssl.trustStore=cacerts -Djavax.net.ssl.trustStorePassword=changeit"

#
#properties="spark.driver.extraJavaOptions=-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=pwd -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=pwd,spark.dataproc.diagnostics.enabled=true" \
#
#gcloud dataproc batches submit spark \
#--region=us-east4 \
#--project=gcp-daci-pltfm-dev \
#--version=1.0 \
#--jars=gs://spark-lib/bigquery/spark-bigquery-with-dependencies_2.12-0.42.3.jar,gs://daci-pltfm-temp-dev/individual/varna/spark_mongo_testing-1.0-SNAPSHOT.jar,gs://daci-pltfm-code-connexio-dev/explore/individual/chary/mongo/mongo-spark-connector_2.12-10.4.1.jar,gs://daci-pltfm-code-connexio-dev/explore/individual/chary/mongo/mongodb-driver-sync-4.9.1.jar,gs://daci-pltfm-temp-dev/individual/varna/bson-4.9.1.jar,gs://daci-pltfm-temp-dev/individual/varna/mongodb-driver-core-4.9.1.jar \
#--files=gs://daci-pltfm-temp-dev/individual/varna/spark_mongo/truststore.jks,gs://daci-pltfm-temp-dev/individual/varna/keystore.jks,gs://daci-pltfm-code-connexio-dev/stg/connexio/generic/codebase/utils/pgs_certs/server_dip.crt,gs://daci-pltfm-code-connexio-dev/stg/connexio/generic/codebase/utils/pgs_certs/root_dip.crt,gs://daci-pltfm-code-connexio-dev/stg/connexio/generic/codebase/utils/pgs_certs/server_dip.pk8 \
#--properties="spark.driver.extraJavaOptions=-Djavax.net.ssl.trustStore=truststore.jks -Djavax.net.ssl.trustStorePassword=***** -Djavax.net.ssl.keyStore=keystore.jks -Djavax.net.ssl.keyStorePassword=******,spark.dataproc.diagnostics.enabled=true" \
#--subnet=projects/gcp-d4-network-npe/regions/us-east4/subnetworks/npe-east4-daci-shared-subnet-1 \
#--service-account=svc-daci-connexio-dev@gcp-daci-pltfm-dev.iam.gserviceaccount.com \
#--class=com.example.SparkBqWriter.SparkBqToMongo

