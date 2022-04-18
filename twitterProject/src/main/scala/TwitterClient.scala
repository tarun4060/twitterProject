import twitter4j.{Query, Status, TwitterFactory}
import twitter4j.conf.ConfigurationBuilder

import java.util


object TwitterClient {

  def tweetData(): util.List[Status] = {

    // (1) config work to create a twitter object
    val cb = new ConfigurationBuilder
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey("888888888")
      .setOAuthConsumerSecret("****************")
      .setOAuthAccessToken("-************")
      .setOAuthAccessTokenSecret("*****************")

    val tf = new TwitterFactory(cb.build)
    val twitter = tf.getInstance
    //    println(twitter)
    //    println("*****")

    val query = new Query("(covid) OR (coronavirus) OR (corona virus) OR (covid19)")

    //    val filterQuery = new FilterQuery
    //    val itemsToTrack: Array[String] = Array("covid", "covid19", "coronavirus", "corona virus")
    //    filterQuery.track("covid", "covid19", "coronavirus", "corona virus")
    query.setCount(100)
    val res = twitter.search(query)
    //println(res)
    val tweets = res.getTweets()

    tweets
  }

  def main(args: Array[String]): Unit = {
    val tweets = tweetData()
    val it = tweets.iterator

    while (it.hasNext) {

      val tweet = it.next
      val tweetId = tweet.getId
      val tweetText = tweet.getText
      var location = "unknown"
      if (tweet.getUser != null) {
        location = tweet.getUser.getLocation
        if (location == null || location == "" || location == " ") {
          location = "unknown"
        }
      }


      val tweetDate = tweet.getCreatedAt
      val tweetCsv = "\"" + tweetId + "\",\"" + tweet.getText + "\",\"" + location + "\",\"" + tweetDate + "\""
      //      println(tweetCsv)
      //      println("******************")
    }
  }

}
