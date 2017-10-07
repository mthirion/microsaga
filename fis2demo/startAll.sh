APP_DIR=/redhat/projects/workspace/fis2demo

cd $APP_DIR

java -jar gateway/target/gateway-0.0.1-SNAPSHOT.jar &
java -jar users/target/users-0.0.1-SNAPSHOT.jar &
java -jar accounts/target/accounts-0.0.1-SNAPSHOT-swarm.jar &
java -jar cards/target/cards-0.0.1-SNAPSHOT.jar &

java -jar monitor/target/monitor-0.0.1-SNAPSHOT.jar
