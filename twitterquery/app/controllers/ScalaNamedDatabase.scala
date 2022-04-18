package controllers

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc._
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}


class ScalaNamedDatabase @Inject()(protected val dbConfigProvider: DatabaseConfigProvider, val cc: ControllerComponents)
                                  (implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._


  //  def countTweets:Result = {
  //    val query = sql"select count(*) from tweets;".as[String]
  //    val queryResult: Future[Vector[String]] = db.run(query)
  ////    queryResult.map(_.headOption)
  //    Ok(Await.result(queryResult.map(_.headOption), Duration(10, "seconds")).get)
  //  }
  def countTweets() = Action { implicit request: Request[AnyContent] =>

    val query = sql"select sum(count_no) from counttweets;".as[String]
    val queryResult: Future[Vector[String]] = db.run(query)
    //    queryResult.map(_.headOption)
    val res = Await.result(queryResult.map(_.headOption), Duration(10, "seconds")).get
    //    println(res)
    //  println("****")
    //  println(queryResult)
    Ok("count= " + res)

  }

  def countTweetsByCountry(country: String) = Action.async { implicit request: Request[AnyContent] =>
    val query = sql"select * from countlocation where location = $country;".as[(String, String)]
    val queryResult: Future[Vector[(String, String)]] = db.run(query)
    Await.result(queryResult.map(_.headOption), Duration(10, "seconds")).get
    //    println(res)
    //    println("****")
    println(queryResult)

    queryResult.map(i => Ok("Got result: " + i + " \n"))
  }

  def top100Words() = Action.async { implicit request: Request[AnyContent] =>

    val query = sql"select word,count_no from wordcount order by 2 desc limit 100 ".as[(String, String)]

    val queryResult: Future[Vector[(String, String)]] = db.run(query)
    val res = Await.result(queryResult.map(_.headOption), Duration(10, "seconds")).get
    queryResult.map(i => Ok("Got result: " + i + " \n"))


  }

  def top100WordsByCountry(country: String) = Action.async { implicit request: Request[AnyContent] =>

    val query = sql"select location,word,count_no from wordcountcountry where location = $country  order by 3 desc limit 100".as[(String, String, String)]
    val queryResult: Future[Vector[(String, String, String)]] = db.run(query)
    Await.result(queryResult.map(_.headOption), Duration(10, "seconds")).get
    queryResult.map(i => Ok("Got result: " + i + " \n"))

  }

}

