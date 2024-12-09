package frp.exercise1_1.user

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.H2Profile.api.*

class UserRepository(db: Database)(implicit ec: ExecutionContext) {

  def fetchById(id: Int): Future[Option[User]] = {
    db.run(users.filter(_.id === id).result.headOption)
  }

  def fetchAll(): Future[Seq[User]] = {
    db.run(users.result)
  }
}
