cat /etc/os-release

sudo apt install openjdk-17-jdk

java -version

cd /etc/ssl/certs/java/

keytool -list -keystore cacerts

sudo gcloud storage cp gs://java-resources/mysite.cert mysite.cert

sudo keytool -importcert -v -trustcacerts -alias apache-http-server -file /etc/ssl/certs/java/mysite.cert -keystore /etc/ssl/certs/java/cacerts.simple -storepass changeit

sudo keytool -importcert -v -trustcacerts -alias apache-http-server -file /etc/ssl/certs/java/mysite.cert -keystore /etc/ssl/certs/java/cacerts -storepass changeit

keytool -list -keystore cacerts

keytool -list -keystore cacerts.simple

file *

