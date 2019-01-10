package controllers

import javax.inject.Inject

import dao.adminDao
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class AdminController @Inject()(admindao:adminDao) extends Controller{


  def loginBefore = Action { implicit request=>
    Ok(views.html.types.login()).withNewSession
  }

  def login(account: String, password: String) = Action.async { implicit request =>
    admindao.validAdmin(account,password).map { x =>
      if (x.nonEmpty) {
        Redirect(routes.BackgroundController.backgroundBefore()).withSession("admin" -> account)
      } else {
        Redirect(routes.AdminController.loginBefore()).flashing("info" -> "账号或密码错误!")
      }
    }
  }

  def changePasswordBefore = Action{implicit request=>
    Ok(views.html.types.password())
  }

  case class passwordData(password:String,newPassword:String)

  val passwordForm = Form(
    mapping(
      "password" -> text,
      "newPassword" -> text
    )(passwordData.apply)(passwordData.unapply)
  )

  def changePassword = Action.async{implicit request=>
    val data = passwordForm.bindFromRequest.get
    val account = request.session.get("admin").get
    admindao.validAdmin(account,data.password).map{x=>
      if(x.nonEmpty){
        Await.result(admindao.changePassword(account,data.newPassword),Duration.Inf)
        Redirect(routes.AdminController.loginBefore()).flashing("info" -> "修改成功，请重新登录！")
      }else{
        Redirect(routes.AdminController.changePasswordBefore()).flashing("info" -> "密码错误，请重新输入")
      }
    }

  }

}
