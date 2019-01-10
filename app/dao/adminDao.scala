package dao

import javax.inject.Inject

import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global


class adminDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends
  HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._


  def updatePassword(row: AdminRow): Future[Unit] = {
    db.run(Admin.filter(_.id === 1).update(row)).map(_ => ())
  }

  def validAdmin(account:String,password:String)  : Future[Seq[AdminRow]]= {
    db.run(Admin.filter(_.user === account).filter(_.password === password).result)
  }

  def changePassword(account:String,password:String) : Future[Unit] = {
    db.run(Admin.filter(_.user === account).map(_.password).update(password)).map(_=>())
  }

}
