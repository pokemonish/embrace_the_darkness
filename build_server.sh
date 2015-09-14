mvn -f ./kurve-server/pom.xml compile assembly:single
cp ./kurve-server/target/server-1.0-jar-with-dependencies.jar ./server.jar