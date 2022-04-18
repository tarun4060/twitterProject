package controllers
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._

import javax.inject._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */


class HomeController @Inject()(val cc: ControllerComponents,protected val dbConfigProvider: DatabaseConfigProvider)
  extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  def index() = Action { implicit request: Request[AnyContent] =>

    Ok(views.html.index())
  }


}
