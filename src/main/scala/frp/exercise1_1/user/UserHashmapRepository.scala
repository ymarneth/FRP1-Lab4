package frp.exercise1_1.user

import frp.exercise1_1.user.User.validateUser
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class UserHashmapRepository(db: mutable.HashMap[Int, User])(implicit ec: ExecutionContext) {

  def fetchAndValidateUser(id: Int): Future[Option[User]] = fetchUserById(id).map {
    case Some(user) if validateUser(user) => Some(user)
    case _ => None
  }

  def fetchUserById(id: Int): Future[Option[User]] = Future {
    Try(db.get(id)) match {
      case Success(Some(user)) => Some(user)
      case Success(None) => None
      case Failure(_) => None
    }
  }

  def addUser(user: User): Future[User] = Future {
    db += (user.id.get -> user)
    user
  }
}
