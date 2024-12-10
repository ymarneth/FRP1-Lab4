package frp.exercise1_1.user

import slick.jdbc.H2Profile.api.*

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success, Try}

case class User(id: Option[Int], name: String, email: String) {
  override def toString: String = s"User ${id.getOrElse("N/A")}: $name, $email"
}

case class CreateUserCommand(name: String, email: String)

object User {
  def validateUser(user: User): Boolean = {
    val emailRegex: Regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".r
    user.name.nonEmpty && emailRegex.matches(user.email)
  }

  def printResult(user: Future[Try[User]])(implicit ec: ExecutionContext): Unit = {
    user.onComplete {
      case Success(value) => println(value.get)
      case Failure(exception) => println(exception)
    }
  }
}

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def name = column[String]("NAME")

  def email = column[String]("EMAIL")

  def * = (id.?, name, email) <> (User.apply.tupled, User.unapply)
}

val users = TableQuery[Users]
