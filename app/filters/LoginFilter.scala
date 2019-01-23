package filters

import javax.inject.Inject

import akka.stream.Materializer
import controllers.routes
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class LoginFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  override def apply(f: (RequestHeader) => Future[Result])(rh: RequestHeader): Future[Result] = {
    if (rh.session.get("userId").isEmpty && rh.path.contains("/frontEnd/") && !rh.path.contains("/assets/")) {
        Future.successful(Results.Redirect(routes.QuestionController.userBefore()))
    }else if(rh.session.get("admin").isEmpty && rh.path.contains("/background/") && !rh.path.contains("/assets/")) {
      Future.successful(Results.Redirect(routes.AdminController.loginBefore()))
    } else {
      f(rh)
    }
  }
}
