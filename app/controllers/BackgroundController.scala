package controllers

import javax.inject.Inject

import dao.{questionDao, resultDao, userDao}
import models.Tables.{QuestionRow, ResultRow}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

class BackgroundController @Inject()(questiondao: questionDao, userdao: userDao, resultdao: resultDao) extends Controller {



  def index = Action{implicit request=>
    Ok(views.html.types.career())
  }

  def backgroundBefore = Action { implicit request =>
    Ok(views.html.background.index())
  }


  def questionBefore = Action { implicit request =>
    Ok(views.html.types.question())
  }


  def getRow(x: Seq[QuestionRow]): Seq[JsObject] = {
    x.map { y =>
      val q = "<a href='/background/questionInfoBefore?id="+y.id+"'>" + y.question + "</a>"
      val id = y.id
      val corrects = Await.result(resultdao.getCorrectByQid(id), Duration.Inf).length
      val alls = Await.result(resultdao.getByQid(id), Duration.Inf).length
      val per = alls match {
        case 0 => "0.00%"
        case _ => corrects / alls * 100 + "%"
      }
      Json.obj("question" -> q, "correct" -> corrects, "all" -> alls, "percent" -> per)
    }
  }

  def getAllQuestion = Action.async { implicit request =>
    questiondao.getAllQuestion.map { x =>
      val row = getRow(x)
      Ok(Json.toJson(row))
    }
  }

  case class filterData(classes: Seq[String], class_min: Seq[String])

  val filterForm = Form(
    mapping(
      "classes" -> seq(text),
      "class_min" -> seq(text)
    )(filterData.apply)(filterData.unapply)
  )

  def getFilterQuestion = Action.async { implicit request =>
    val data = filterForm.bindFromRequest.get
    val c1 = data.classes match {
      case c if c.isEmpty => Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).map(_.toString)
      case c => c
    }
    val c2 = data.class_min match {
      case c if c.isEmpty => Seq(1, 2).map(_.toString)
      case c => c
    }
    questiondao.getQuestionByType(c1, c2).map { x =>
      val row = getRow(x)
      Ok(Json.toJson(row))
    }
  }


  def questionInfoBefore(id: Int) = Action { implicit request =>
    Ok(views.html.types.questionInfo(id))
  }


  def getAllResult = Action.async { implicit request =>
    resultdao.getAll.map { x =>
      val result = getCharts(x)
      Ok(Json.obj("base" -> result._1, "speziell" -> result._2, "person" -> result._3))
    }
  }


  def getCharts(x: Seq[ResultRow]) = {
    if (x.length > 0) {
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
      (as.head, as.last, x.map(_.userId).distinct.length)
    } else {
      val a = (1 to 10).map(y => 0.0).toArray
      (a, a, 0)
    }

  }


  case class userFilterData(career: Option[Seq[String]], isoperation: Option[String], ismanager: Option[String],
                            lab: Option[Seq[String]], workyear: Option[String], istrain: Option[String], traintime: Option[String])

  val userFilterForm = Form(
    mapping(
      "career" -> optional(seq(text)),
      "isoperation" -> optional(text),
      "ismanager" -> optional(text),
      "lab" -> optional(seq(text)),
      "workyear" -> optional(text),
      "istrain" -> optional(text),
      "traintime" -> optional(text)
    )(userFilterData.apply)(userFilterData.unapply)
  )

  def getFilterResult = Action { implicit request =>
    val data = userFilterForm.bindFromRequest.get
    val career = data.career
    val isoperation = data.isoperation
    val ismanager = data.ismanager
    val lab = data.lab
    val workyear = data.workyear
    val istrain = data.istrain
    val traintime = data.traintime

    val id = Await.result(userdao.getUserByPosition(career, isoperation, ismanager, lab, workyear, istrain, traintime), Duration.Inf)
    val x = Await.result(resultdao.getByUserIds(id), Duration.Inf)
    val result = getCharts(x)
    Ok(Json.obj("base" -> result._1, "speziell" -> result._2, "person" -> result._3))

  }


  def getQuestionById(id: Int) = Action.async { implicit request =>
    questiondao.getQuestionById(id).map { x =>
      val classes = x.`class`.toInt match {
        case 1 => "生物安全相关法律法规"
        case 2 => "生物危害因子的风险评估"
        case 3 => "生物安全实验室的分级及管理"
        case 4 => "生物安全实验室设施"
        case 5 => "生物安全实验室关键设备"
        case 6 => "消毒灭菌及废物处理"
        case 7 => "突发事故的处理和应急预案"
        case 8 => "实验室个人防护"
        case 9 => "菌毒种运输及管理"
        case 10 => "实验操作"
      }

      val c_min = x.classMin.toInt match {
        case 1 => "基础知识题"
        case 2 => "专业知识题"
      }

      val answer = Array(x.a, x.b, x.c, x.d, x.e, x.f).filter(_ != "")
      val marker = Array("A", "B", "C", "D", "E", "F")
      val body = answer.zip(marker.take(answer.length)).map { y =>
        s"<p style='margin-left: 30px'>${y._2}.${y._1}</p>"
      }

      val head = s"<label>题目：</label><p style='margin-left: 20px'>${x.question}</p>"

      val suffix =
        s"""
           |                            <p><label>题目粉类：</label><span>$classes</span></p>
           |                            <p><label>难度分类：</label><span>$c_min</span></p>
           |                            <p><label>正确答案：</label><span>${x.answer}</span></p>
         """.stripMargin

      val html = head + body.mkString + suffix

      Ok(Json.toJson(html))
    }
  }

  def getChartsById(id: Int) = Action { implicit request =>

    val data = userFilterForm.bindFromRequest.get

    val ids = Await.result(userdao.getUserByPosition(data.career, data.isoperation, data.ismanager, data.lab,
      data.workyear, data.istrain, data.traintime), Duration.Inf)

    val result = Await.result(resultdao.getByQid(id), Duration.Inf)
    val array = result.filter(x=>ids.contains(x.userId)).flatMap { x =>
      val user = Await.result(userdao.getByUserId(x.userId), Duration.Inf)
      user.career.split(",").map { y =>
        (y, x.isRight)
      }
    }

    val allper = array.length match {
      case 0 => "0.00%"
      case _ =>"%.2f".format(array.count(_._2 ==1).toDouble/array.length.toDouble *100) + "%"
    }


    val stat =
      s"""
         |                                <label>符合条件人数 ： </label><span> ${array.length}人</span>
         |                                <label style="margin-left: 5%">答对人数 ： </label><span>${array.count(_._2 == 1)}人</span>
         |                                <label style="margin-left: 5%">正确率 ： </label><span>${allper}</span>
       """.stripMargin


    val row = Seq("A", "B", "C", "D", "E").map { x =>
      val classes = array.filter(_._1 == x)
      val all = classes.length
      val right = classes.count(_._2 == 1)
      val perCor = all match {
        case 0 => 0.00
        case _ => "%.2f".format(right.toDouble / all.toDouble * 100).toDouble
      }
      val perFal = all match {
        case 0 => 0.00
        case _ => "%.2f".format((1 - right.toDouble / all.toDouble) * 100).toDouble
      }
      (Json.obj("career" -> getCareer(x), "allPeople" -> all, "correctPeople" -> right, "correctPercent" -> (perCor + "%")), Seq(perCor, perFal))
    }
    val charts = row.map(_._2).transpose
    Ok(Json.obj("tableData" -> row.map(_._1), "perCor" -> charts.head, "perFal" -> charts.last,"stat"->stat))
  }

  def getCareer(option: String): String = {
    option match {
      case "A" => "临床医学"
      case "B" => "基础医学相关专业"
      case "C" => "生物学相关专业"
      case "D" => "生物安全相关"
      case "E" => "其他"
    }
  }


}
