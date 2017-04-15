# git checkout baseCode_1.0.0
mvn package -P dev
cp ./target/ROOT.war ~/tomcat/uiservice-dev/webapps/
