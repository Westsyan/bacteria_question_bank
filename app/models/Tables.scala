package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Admin.schema ++ Question.schema ++ Result.schema ++ User.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Admin
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param user Database column user SqlType(VARCHAR), Length(255,true)
   *  @param password Database column password SqlType(VARCHAR), Length(255,true) */
  final case class AdminRow(id: Int, user: String, password: String)
  /** GetResult implicit for fetching AdminRow objects using plain SQL queries */
  implicit def GetResultAdminRow(implicit e0: GR[Int], e1: GR[String]): GR[AdminRow] = GR{
    prs => import prs._
    AdminRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table admin. Objects of this class serve as prototypes for rows in queries. */
  class Admin(_tableTag: Tag) extends profile.api.Table[AdminRow](_tableTag, Some("bacteria_question"), "admin") {
    def * = (id, user, password) <> (AdminRow.tupled, AdminRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(user), Rep.Some(password)).shaped.<>({r=>import r._; _1.map(_=> AdminRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user SqlType(VARCHAR), Length(255,true) */
    val user: Rep[String] = column[String]("user", O.Length(255,varying=true))
    /** Database column password SqlType(VARCHAR), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Admin */
  lazy val Admin = new TableQuery(tag => new Admin(tag))

  /** Entity class storing rows of table Question
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param `class` Database column class SqlType(VARCHAR), Length(255,true)
   *  @param classMin Database column class_min SqlType(VARCHAR), Length(255,true)
   *  @param question Database column question SqlType(TEXT)
   *  @param a Database column a SqlType(TEXT)
   *  @param b Database column b SqlType(TEXT)
   *  @param c Database column c SqlType(TEXT)
   *  @param d Database column d SqlType(TEXT)
   *  @param e Database column e SqlType(TEXT)
   *  @param f Database column f SqlType(TEXT)
   *  @param answer Database column answer SqlType(TEXT)
   *  @param time Database column time SqlType(INT) */
  final case class QuestionRow(id: Int, `class`: String, classMin: String, question: String, a: String, b: String, c: String, d: String, e: String, f: String, answer: String, time: Int)
  /** GetResult implicit for fetching QuestionRow objects using plain SQL queries */
  implicit def GetResultQuestionRow(implicit e0: GR[Int], e1: GR[String]): GR[QuestionRow] = GR{
    prs => import prs._
    QuestionRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Int]))
  }
  /** Table description of table question. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: class */
  class Question(_tableTag: Tag) extends profile.api.Table[QuestionRow](_tableTag, Some("bacteria_question"), "question") {
    def * = (id, `class`, classMin, question, a, b, c, d, e, f, answer, time) <> (QuestionRow.tupled, QuestionRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(`class`), Rep.Some(classMin), Rep.Some(question), Rep.Some(a), Rep.Some(b), Rep.Some(c), Rep.Some(d), Rep.Some(e), Rep.Some(f), Rep.Some(answer), Rep.Some(time)).shaped.<>({r=>import r._; _1.map(_=> QuestionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column class SqlType(VARCHAR), Length(255,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `class`: Rep[String] = column[String]("class", O.Length(255,varying=true))
    /** Database column class_min SqlType(VARCHAR), Length(255,true) */
    val classMin: Rep[String] = column[String]("class_min", O.Length(255,varying=true))
    /** Database column question SqlType(TEXT) */
    val question: Rep[String] = column[String]("question")
    /** Database column a SqlType(TEXT) */
    val a: Rep[String] = column[String]("a")
    /** Database column b SqlType(TEXT) */
    val b: Rep[String] = column[String]("b")
    /** Database column c SqlType(TEXT) */
    val c: Rep[String] = column[String]("c")
    /** Database column d SqlType(TEXT) */
    val d: Rep[String] = column[String]("d")
    /** Database column e SqlType(TEXT) */
    val e: Rep[String] = column[String]("e")
    /** Database column f SqlType(TEXT) */
    val f: Rep[String] = column[String]("f")
    /** Database column answer SqlType(TEXT) */
    val answer: Rep[String] = column[String]("answer")
    /** Database column time SqlType(INT) */
    val time: Rep[Int] = column[Int]("time")

    /** Primary key of Question (database name question_PK) */
    val pk = primaryKey("question_PK", (id, `class`))
  }
  /** Collection-like TableQuery object for table Question */
  lazy val Question = new TableQuery(tag => new Question(tag))

  /** Entity class storing rows of table Result
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param questionId Database column question_id SqlType(INT)
   *  @param userId Database column user_id SqlType(INT)
   *  @param isRight Database column is_right SqlType(INT)
   *  @param answer Database column answer SqlType(VARCHAR), Length(255,true) */
  final case class ResultRow(id: Int, questionId: Int, userId: Int, isRight: Int, answer: String)
  /** GetResult implicit for fetching ResultRow objects using plain SQL queries */
  implicit def GetResultResultRow(implicit e0: GR[Int], e1: GR[String]): GR[ResultRow] = GR{
    prs => import prs._
    ResultRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[String]))
  }
  /** Table description of table result. Objects of this class serve as prototypes for rows in queries. */
  class Result(_tableTag: Tag) extends profile.api.Table[ResultRow](_tableTag, Some("bacteria_question"), "result") {
    def * = (id, questionId, userId, isRight, answer) <> (ResultRow.tupled, ResultRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(questionId), Rep.Some(userId), Rep.Some(isRight), Rep.Some(answer)).shaped.<>({r=>import r._; _1.map(_=> ResultRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column question_id SqlType(INT) */
    val questionId: Rep[Int] = column[Int]("question_id")
    /** Database column user_id SqlType(INT) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column is_right SqlType(INT) */
    val isRight: Rep[Int] = column[Int]("is_right")
    /** Database column answer SqlType(VARCHAR), Length(255,true) */
    val answer: Rep[String] = column[String]("answer", O.Length(255,varying=true))

    /** Primary key of Result (database name result_PK) */
    val pk = primaryKey("result_PK", (id, questionId, userId))
  }
  /** Collection-like TableQuery object for table Result */
  lazy val Result = new TableQuery(tag => new Result(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(INT), AutoInc, PrimaryKey
   *  @param date Database column date SqlType(VARCHAR), Length(255,true)
   *  @param career Database column career SqlType(TEXT)
   *  @param q2 Database column q2 SqlType(TEXT)
   *  @param q3 Database column q3 SqlType(TEXT)
   *  @param q4 Database column q4 SqlType(TEXT)
   *  @param q5 Database column q5 SqlType(TEXT)
   *  @param q6 Database column q6 SqlType(TEXT)
   *  @param q7 Database column q7 SqlType(TEXT) */
  final case class UserRow(id: Int, date: String, career: String, q2: String, q3: String, q4: String, q5: String, q6: String, q7: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends profile.api.Table[UserRow](_tableTag, Some("bacteria_question"), "user") {
    def * = (id, date, career, q2, q3, q4, q5, q6, q7) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(date), Rep.Some(career), Rep.Some(q2), Rep.Some(q3), Rep.Some(q4), Rep.Some(q5), Rep.Some(q6), Rep.Some(q7)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column date SqlType(VARCHAR), Length(255,true) */
    val date: Rep[String] = column[String]("date", O.Length(255,varying=true))
    /** Database column career SqlType(TEXT) */
    val career: Rep[String] = column[String]("career")
    /** Database column q2 SqlType(TEXT) */
    val q2: Rep[String] = column[String]("q2")
    /** Database column q3 SqlType(TEXT) */
    val q3: Rep[String] = column[String]("q3")
    /** Database column q4 SqlType(TEXT) */
    val q4: Rep[String] = column[String]("q4")
    /** Database column q5 SqlType(TEXT) */
    val q5: Rep[String] = column[String]("q5")
    /** Database column q6 SqlType(TEXT) */
    val q6: Rep[String] = column[String]("q6")
    /** Database column q7 SqlType(TEXT) */
    val q7: Rep[String] = column[String]("q7")
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}
