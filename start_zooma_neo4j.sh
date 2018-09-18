java -jar \
     -Dspring.rabbitmq.host=rabbitmq \
     -Dspring.rabbitmq.activate=true \
     -Dspring.data.neo4j.uri=http://neo4j:7474 \
     -Dspring.data.neo4j.username=neo4j \
     -Dspring.data.neo4j.password=null \
     -Dspring.data.neo4j.indexes.auto=assert \
     -Dspring.jackson.serialization.write-dates-as-timestamps=false \
     /home/zooma-neo4j-3.0.0-SNAPSHOT.jar
