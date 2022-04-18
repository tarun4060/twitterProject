import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

import java.util.Properties

object SparkTwitter {


  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession.builder.appName("twitterAWS").master("local[*]").getOrCreate()

    //Set this setting to parse date properly because date from tweets not in standard format that can be used to sort properly
    spark.sql("set spark.sql.legacy.timeParserPolicy=LEGACY")

    spark.sparkContext
      .hadoopConfiguration.set("fs.s3a.access.key", "****************")
    spark.sparkContext
      .hadoopConfiguration.set("fs.s3a.secret.key", "*******************")
    spark.sparkContext
      .hadoopConfiguration.set("fs.s3a.endpoint", "s3.amazonaws.com")


    val path = "s3a://tarun-kafka-connect/topics/covid1/partition=0/covid*.json"

    val tweets = spark.read.option("header", "true").json(path)

    val tweetsRearranged = tweets.select(tweets("id"), tweets("date"), lower(tweets("text")).as("text"), lower(tweets("location")).as("location"))

    val t1 = tweetsRearranged.withColumn("month", substring(col("date"), 5, 3))
      .withColumn("day", substring(col("date"), 9, 2))
      .withColumn("year", substring(col("date"), 25, 4))

    val t2 = t1.withColumn("date", concat_ws("/", col("day"), col("month"), col("year")))

    val t3 = t2.drop("month", "day", "year")
    val t4 = t3.withColumn("date", date_format(to_date(col("date"), "dd/MMMM/yyyy"), "yyyy-MM-dd"))
    //17/Jan/2022 -> 2022-01-17 so that sort/group properly

    //date_format(to_date(col("Date"),"yyyy MMMM dd E"),"MM/dd/yyyy")
    //    t4.show()
    //    println(t4.count())
    //df= df.withColumn('month',substring('date',5,3)).withColumn('day',substring('date',9,2))\
    //.withColumn('year',substring('date',25,4))
    //Wed Jan 12 16:19:26 IST 2022
    //df = df.withColumn('date',concat_ws('-','day','month','year'))

    //postgre connect

    val url = "jdbc:postgresql://localhost:5432/twitterData"
    val table = "tweets"

    val pgProperties = new Properties()
    pgProperties.put("user", "postgres")
    pgProperties.put("password", "12345")

    //    val jdbcDF = spark.read.jdbc(url,table,pgProperties)
    //    println(jdbcDF)
    //    println("***********")
    //    jdbcDF.write.jdbc(url,table,pgProperties)
    //    println(jdbcDF)

    //push tp DB here
    t4.write.mode(SaveMode.Append).jdbc(url, table, pgProperties)

    spark.stop()
  }

}