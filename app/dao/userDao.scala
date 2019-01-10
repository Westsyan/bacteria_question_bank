package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class userDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile]{


  import profile.api._

  def addUser(row : UserRow) : Future[Int] = {
    db.run(User returning User.map(_.id) += row)
  }

  def getByUserId(id:Int) : Future[UserRow] ={
    db.run(User.filter(_.id === id).result.head)
  }

  def getUserByPosition(career : Option[Seq[String]], isoperation: Option[String], ismanager: Option[String], lab: Option[Seq[String]],
                        workyear: Option[String], istrain: Option[String], traintime: Option[String]) : Future[Seq[Int]] = {
    val isca = career.isEmpty
    val isop = isoperation.isEmpty
    val isma = ismanager.isEmpty
    val isla = lab.isEmpty
    val iswo = workyear.isEmpty
    val istr = istrain.isEmpty
    val isti = traintime.isEmpty
    db.run(User.filter(_.career === career.getOrElse(Seq("")).mkString(",") || isca).filter(_.q2 === isoperation.getOrElse("") || isop).
      filter(_.q3 === ismanager.getOrElse("") || isma).filter(_.q4 === lab.getOrElse(Seq("")).mkString(",") || isla).
      filter(_.q5 === workyear.getOrElse("") || iswo).filter(_.q6 === istrain.getOrElse("") || istr).
      filter(_.q6 === traintime.getOrElse("") || isti).map(_.id).result)
  }
}
