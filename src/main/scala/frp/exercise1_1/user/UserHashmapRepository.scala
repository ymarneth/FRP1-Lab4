package frp.exercise1_1.user

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

class UserHashmapRepository(db: mutable.HashMap[Int, User])(implicit ec: ExecutionContext) {

  def fetchAndValidateUser(id: Int): Future[Option[User]] = fetchUserById(id).map {
    case Some(user) if validateUser(user) => Some(user)
    case _ => None
  }

  def fetchUserById(id: Int): Future[Option[User]] = Future {
    try {
      db.get(id) match {
        case Some(user) => Some(user)
        case None => None
      }
    } catch {
      case e: Exception => None
    }
  }

  def addUser(user: User): Future[User] = Future {
    db += (user.id.get -> user)
    user
  }

  private def validateUser(user: User): Boolean = {
    val emailRegex: Regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".r
    user.name.nonEmpty && emailRegex.matches(user.email)
  }
}
