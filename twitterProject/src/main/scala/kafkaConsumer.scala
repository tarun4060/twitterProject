
import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import scala.collection.JavaConverters._

object kafkaConsumer {

  def createConsumer(): KafkaConsumer[String,String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("group.id", "consumer-group")
     new KafkaConsumer[String, String](props)
  }

  def consumeFromKafka(topic: String): Unit = {

    val consumer = createConsumer()

    consumer.subscribe(util.Arrays.asList(topic))

    while (true) {
      val record = consumer.poll(1000).asScala
      for (data <- record.iterator)
        println(data.value())
    }

  }

  def main(args: Array[String]): Unit = {
    consumeFromKafka("covid1")
  }

}