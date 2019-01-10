package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class resultDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  def addResult(row : ResultRow) : Future[Unit] ={
    db.run(Result += row).map(_=>())
  }


  def getAll : Future[Seq[ResultRow]] = {
    db.run(Result.result)
  }

  def getByUserId(id:Int) : Future[Seq[ResultRow]] ={
    db.run(Result.filter(_.userId === id).result)
  }

  def getByUserIds(id:Seq[Int]): Future[Seq[ResultRow]] ={
    db.run(Result.filter(_.userId.inSetBind(id)).result)
  }

  def getByQid(id:Int) : Future[Seq[ResultRow]] = {
    db.run(Result.filter(_.questionId === id).result)
  }

  def getCorrectByQid(id:Int) : Future[Seq[ResultRow]] = {
    db.run(Result.filter(_.questionId === id).filter(_.isRight === 1).result)
  }

}
