import com.google.gson.Gson

import java.util.Properties
import org.apache.kafka.clients.producer._

object kafkaProducer {

  case class TweetClass(id: Long, text: String, location: String, date: String)

  def writeToKafka(topic: String, key: String, value: String, producer: KafkaProducer[String, String]): Unit = {

    val record = new ProducerRecord[String, String](topic, key, value)
    producer.send(record)
  }

  def createProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    new KafkaProducer[String, String](props)
  }

  def closeProducer(producer: KafkaProducer[String, String]): Unit = {
    producer.close()
  }


  def produceTweets(producer: KafkaProducer[String, String]): Unit = {

    var i = 0
    while (i != 10) {
      val client = TwitterClient
      val tweets = client.tweetData() //twitter rate limit (1 request per second) per user is 180 request in every 15 minutes
      // per app for search is 300 req in every 15 minutes
      val it = tweets.iterator

      while (it.hasNext) {
        val tweet = it.next

        val gson = new Gson
        // val tweetText = "{\"tweet\":\"" + tweet.getText + "\",\"location\":\"" + location + "\"}"

        val tweetId = tweet.getId
        val tweetText = tweet.getText

        var location = "unknown"
        if (tweet.getUser != null) {
          location = tweet.getUser.getLocation
          if (location == null || location == "" || location == " ") {
            location = "unknown"
          }
        }

        val tweetLocation = location

        val tweetDate = tweet.getCreatedAt
        val myTweet = TweetClass(tweetId, tweetText, tweetLocation, tweetDate.toString);

        val tweetJson = gson.toJson(myTweet)
        //val tweetCsv = "\""+tweetId+"\",\""+tweet.getText+"\",\""+tweetLocation +"\",\""+tweetDate+"\""

        writeToKafka("covid1", null, tweetJson, producer)
        println(tweetJson)
      }
      i += 1
      //Thread.sleep(15 * 60 * 1000)
    }

  }

  def main(args: Array[String]): Unit = {
    val producer = createProducer()

    produceTweets(producer)

    closeProducer(producer)
  }

}


