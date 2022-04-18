import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import java.util.Properties

object summary {

  def countTweets(df: DataFrame, spark: SparkSession, url: String, pgProperties: Properties): Unit = {
    df.createOrReplaceTempView("df")
    val d1 = spark.sql("select count(*) as count_no,date from df group by date ")
    d1.write.mode(SaveMode.Overwrite).jdbc(url, "counttweets", pgProperties)
    //d1.show()
  }

  def countByLocation(df: DataFrame, spark: SparkSession, url: String, pgProperties: Properties): Unit = {

    df.createOrReplaceTempView("df")
    val d2 = spark.sql("select count(*) as count_no,location from df group by location")
    d2.write.mode(SaveMode.Overwrite).jdbc(url, "countlocation", pgProperties)
    //d2.show()
  }

  def top100Words(df: DataFrame, spark: SparkSession, url: String, pgProperties: Properties): Unit = {

    df.createOrReplaceTempView("df")

    val d2 = spark.sql(" with X as (SELECT  explode(split(text, ' ')) as word   FROM   df  )" +
      "select word, count(*) as count_no from x  group by word having char_length(word) > 3 and" +
      " word not in ('that','this','what','have','with','will','they','just','from','like','your')order by 2 desc limit 100")

    d2.write.mode(SaveMode.Overwrite).jdbc(url, "wordcount", pgProperties)
    //d2.show()
  }

  def top100WordsByCountry(df: DataFrame, spark: SparkSession, url: String, pgProperties: Properties): Unit = {

    df.createOrReplaceTempView("df")

    val d2 = spark.sql(" with X as (SELECT location, explode(split(text, ' ')) as word   FROM   df  )" +
      "select location,word, count(*) as count_no from x  group by word,location having char_length(word) > 3 and" +
      " word not in ('that','this','what','have','with','will','they','just','from','like','your') order by 3 desc")

    d2.write.mode(SaveMode.Overwrite).jdbc(url, "wordcountcountry", pgProperties)
    d2.show()
  }


  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession.builder.appName("summaryTable").master("local[*]").getOrCreate()

    val url = "jdbc:postgresql://localhost:5432/twitterData"
    val tableRead = "tweets"

    val pgProperties = new Properties()
    pgProperties.put("user", "postgres")
    pgProperties.put("password", "12345")

    val df = spark.read.jdbc(url, tableRead, pgProperties)
    //df.createOrReplaceTempView("df")
    //    df.show()
    //    println("*****")

    countTweets(df, spark, url, pgProperties)
    countByLocation(df, spark, url, pgProperties)
    top100Words(df, spark, url, pgProperties)
    top100WordsByCountry(df, spark, url, pgProperties)

    // d1.write.mode(SaveMode.Append).jdbc(url,tableWrite,pgProperties)


  }


}


