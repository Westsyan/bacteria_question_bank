package controllers

import javax.inject.Inject

import dao._
import models.Tables.{ResultRow, UserRow}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.Utils

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class QuestionController @Inject()(questiondao: questionDao, userdao: userDao, resultdao: resultDao) extends Controller {


  def userBefore = Action { implicit request =>
    Ok(views.html.question.userPage()).withNewSession
  }

  case class userData(career: Seq[String], isoperation: String, ismanager: String, lab: Option[Seq[String]],
                      workyear: String, istrain: String, traintime: Option[String])

  val userForm = Form(
    mapping(
      "career" -> seq(text),
      "isoperation" -> text,
      "ismanager" -> text,
      "lab" -> optional(seq(text)),
      "workyear" -> text,
      "istrain" -> text,
      "traintime" -> optional(text)
    )(userData.apply)(userData.unapply)
  )

  def addUser = Action { implicit request =>

    var valid = "true"
    var message = ""
    var id = 0

    try {
      val data = userForm.bindFromRequest.get
      val career = data.career.mkString(",")
      val isoperation = data.isoperation
      val ismanager = data.ismanager
      val lab = data.lab.getOrElse(Seq("")).mkString(",")
      val workyear = data.workyear
      val istrain = data.workyear
      val traintime = data.traintime.getOrElse("")
      id = Await.result(userdao.addUser(UserRow(0, Utils.date2, career, isoperation, ismanager, lab, workyear, istrain, traintime)), Duration.Inf)
    } catch {
      case e: Exception => valid = "false"
        message = e.getMessage
    }

    Ok(Json.obj("valid" -> valid, "message" -> message, "id" -> id))
  }

  def answerPage = Action { implicit request =>
    val userId = request.session.get("userId")
    if(userId.isEmpty){
      Redirect(routes.QuestionController.userBefore())
    }else{
      Ok(views.html.question.answerPage())
    }
  }

  def answerBefore(id: Int) = Action.async { implicit request =>
    resultdao.getByUserId(id).map { x =>
      if (x.length > 50) {
        Redirect(routes.QuestionController.resultBefore()).withNewSession.withSession("userId" -> id.toString)
      } else {
        Redirect(routes.QuestionController.answerPage()).withNewSession.withSession("userId" -> id.toString)
      }
    }
  }

  def addRefreshResult(id: String) = Action.async { implicit request =>
    val userId = request.session.get("userId").get.toInt
    resultdao.addResult(ResultRow(0, id.toInt, userId, 2, "")).map { x =>
      Ok(Json.toJson("success"))
    }
  }

  def getQuestion = Action.async { implicit request =>
    val userId = request.session.get("userId").get.toInt
    val results = Await.result(resultdao.getByUserId(userId), Duration.Inf)
    val resQuestion = Await.result(questiondao.getQuestionByIds(results.map(_.questionId)), Duration.Inf)

    questiondao.getAllQuestion.map { x =>
      val question = (1 to 10).flatMap { i =>
        val existR = resQuestion.filter(_.`class` == i.toString)
        val classes = x.filter(_.`class` == i.toString)
        val common = classes.filter(_.classMin == "1")
        val major = classes.filter(_.classMin == "2")
        //选择3个普通题和2个专业题
        Utils.random(2 - existR.count(_.classMin == "1"), common.length).map(y => common(y)) ++
          Utils.random(1 - existR.count(_.classMin == "2"), major.length).map(y => major(y))
      }

      val row = Utils.shuffle(question.toArray).map { y =>

        val (types, suffix) = if (y.answer.length > 1) {
          ("checkbox", "（多选题）")
        } else {
          ("radio", "（单选题）")
        }
        val answer = Array(y.a, y.b, y.c, y.d, y.e, y.f).filter(_ != "")
        val str = Array("A", "B", "C", "D", "E", "F")
        val option = answer.zip(str.take(answer.length)).map { x =>
          s"<input type='$types' value='${x._2}' name='answers'>    ${x._1}"
        }
        Json.obj("question" -> (y.question + suffix),
          "answers" -> Utils.shuffle(option), "question_id" -> y.id)
      }
      Ok(Json.obj("row" -> row, "num" -> results.length))
    }
  }

  case class resultData(questionId: String, answers: Seq[String])

  def resultForm = Form(
    mapping(
      "questionId" -> text,
      "answers" -> seq(text)
    )(resultData.apply)(resultData.unapply)
  )

  def addResult = Action { implicit request =>

    var valid = "true"
    var message = ""
    try {
      val data = resultForm.bindFromRequest.get
      val userId = request.session.get("userId").get.toInt

      val qId = data.questionId.toInt
      val answer = data.answers.sorted.mkString

      val question = Await.result(questiondao.getQuestionById(qId.toInt), Duration.Inf)

      val isRight = if (answer == question.answer) {
        1
      } else {
        2
      }
      Await.result(resultdao.addResult(ResultRow(0, qId, userId, isRight, answer)), Duration.Inf)
    } catch {
      case e: Exception => valid = "false"
        message = e.getMessage
    }
    Ok(Json.obj("valid" -> valid, "message" -> message))
  }


  def resultBefore = Action { implicit request =>
    Ok(views.html.question.result())
  }

  def getResult = Action.async { implicit request =>
    val id = request.session.get("userId").get.toInt
    resultdao.getByUserId(id).map { x =>
      val filter1 = x.map { y =>
        val ques = Await.result(questiondao.getQuestionById(y.questionId), Duration.Inf)
        (ques.`class`, ques.classMin, y.isRight)
      }
      val filter2 = filter1.groupBy(_._1).toArray.sortBy(_._1.toInt).map { y =>
        val q = y._2.groupBy(_._2).toArray.sortBy(_._1.toInt).map { z =>
          "%.2f".format((z._2.count(_._3 == 1).toDouble / z._2.length.toDouble) * 100)
        }.map(_.toDouble)
        Array(q.head, q.last)
      }
      val as = filter2.transpose

      Ok(Json.obj("base" -> as.head, "speziell" -> as.last, "score" -> (filter1.count(_._3 == 1) * 2 + "%")))
    }
  }


}
