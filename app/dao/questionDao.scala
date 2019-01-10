package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class questionDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def getAllQuestion : Future[Seq[QuestionRow]] = {
    db.run(Question.result)
  }


  def getQuestionById(id:Int) : Future[QuestionRow] ={
    db.run(Question.filter(_.id === id).result.head)
  }

  def getQuestionByIds(id:Seq[Int]) : Future[Seq[QuestionRow]] = {
    db.run(Question.filter(_.id.inSetBind(id)).result)
  }

  def getQuestionByType(c1:Seq[String],c2:Seq[String]) : Future[Seq[QuestionRow]] = {
    db.run(Question.filter(_.`class`.inSetBind(c1)).filter(_.classMin.inSetBind(c2)).result)
  }

}
