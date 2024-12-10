package frp.exercise1_1.user

import frp.exercise1_1.user.User.validateUser

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.H2Profile.api.*
import scala.util.{Failure, Success, Try}

class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  def fetchAndValidateUser(id: Int): Future[Try[User]] = fetchUserById(id).map {
    case Success(user) if validateUser(user) => Success(user)
    case Success(user) => Failure(new Exception(s"User with ID $id is invalid"))
    case Failure(exception) => Failure(exception)
  }

  def fetchUserById(id: Int): Future[Try[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
      .map {
        case Some(user) => Success(user)
        case None => Failure(new Exception(s"User with ID $id not found"))
      }
  }

  def addUser(user: CreateUserCommand): Future[User] = {
    val newUser = User(None, user.name, user.email)

    db.run(users returning users.map(_.id) += newUser)
      .map(id => newUser.copy(id = Some(id)))
  }
}
