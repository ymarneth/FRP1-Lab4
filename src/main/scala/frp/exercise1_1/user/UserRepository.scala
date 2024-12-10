package frp.exercise1_1.user

import frp.exercise1_1.user.User.validateUser

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.H2Profile.api.*
import scala.util.{Failure, Success, Try}

class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  def fetchAndValidateUser(id: Int): Future[Option[User]] = fetchUserById(id).map {
    case Some(user) if validateUser(user) => Some(user)
    case _ => None
  }

  def fetchUserById(id: Int): Future[Option[User]] = {
    Try {
      db.run(users.filter(_.id === id).result.headOption)
    } match {
      case Success(user) => user
      case Failure(_) => Future.successful(None)
    }
  }

  def addUser(user: CreateUserCommand): Future[User] = {
    val newUser = User(None, user.name, user.email)

    db.run(users returning users.map(_.id) += newUser)
      .map(id => newUser.copy(id = Some(id)))
  }
}
