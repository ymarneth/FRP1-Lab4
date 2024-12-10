package frp.exercise1_1.user

import frp.exercise1_1.user.User.validateUser
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class UserHashmapRepository(db: mutable.HashMap[Int, User])(implicit ec: ExecutionContext) {

  def fetchAndValidateUser(id: Int): Future[Try[User]] = fetchUserById(id).map {
    case Success(user) if validateUser(user) => Success(user)
    case Success(user) => Failure(new Exception(s"User with ID $id is invalid"))
    case Failure(exception) => Failure(exception)
  }

  def fetchUserById(id: Int): Future[Try[User]] = Future {
    db.get(id) match {
      case Some(user) => Success(user)
      case None => Failure(new Exception(s"User with ID $id not found"))
    }
  }

  def addUser(user: User): Future[User] = Future {
    db += (user.id.get -> user)
    user
  }
}
