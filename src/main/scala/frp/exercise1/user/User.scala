package frp.exercise1.user

import frp.exercise1.user
import slick.jdbc.H2Profile.api.*

case class User(id: Option[Int], name: String, email: String)

class Users(tag: Tag) extends Table[User](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def email = column[String]("EMAIL")

  // Map columns to case class
  def * = (id.?, name, email) <> (User.tupled, User.unapply)
}

val users = TableQuery[Users]
