package frp.exercise1

import frp.exercise1.user.{User, UserHashmapRepository}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

class UserTests extends AnyFunSpec with BeforeAndAfterEach {

  implicit val ec: ExecutionContext = ExecutionContext.global

  var userRepo: UserHashmapRepository = uninitialized
  var userTable: scala.collection.mutable.HashMap[Int, User] = uninitialized

  override def beforeEach(): Unit = {
    val userTable: mutable.HashMap[Int, User] = mutable.HashMap()
    userRepo = new UserHashmapRepository(userTable)
  }

  describe("UserHashmapRepository") {
    it("should fetch a user by ID") {
      val user = User(Some(1), "John Doe", "john.doe@example.com")
      Await.result(userRepo.addUser(user), Duration.Inf)

      val fetchedUser$ = userRepo.fetchUserById(1)
      printResult(fetchedUser$)

      val fetchedUser = Await.result(fetchedUser$, Duration.Inf)
      assert(fetchedUser.contains(user))
    }

    it("should return None when fetching a user by an ID that does not exist") {
      val fetchedUser = Await.result(userRepo.fetchUserById(999), Duration.Inf)
      assert(fetchedUser.isEmpty)
    }

    it("should validate that user name is not empty when fetching by ID") {
      val user = User(Some(1), "", "john.doe@example.com")
      Await.result(userRepo.addUser(user), Duration.Inf)

      val fetchedUser = Await.result(userRepo.fetchAndValidateUser(1), Duration.Inf)
      assert(fetchedUser.isEmpty)
    }

    it("should validate that the email is valid when fetching by ID") {
      val user = User(Some(1), "John Doe", "john.example.com")
      Await.result(userRepo.addUser(user), Duration.Inf)

      val fetchedUser = Await.result(userRepo.fetchAndValidateUser(1), Duration.Inf)
      assert(fetchedUser.isEmpty)
    }
  }
}

def printResult(user: Future[Option[User]])(implicit ec: ExecutionContext): Unit = {
  user.onComplete {
    case scala.util.Success(value) => value.foreach(printUser)
    case scala.util.Failure(exception) => println(exception)
  }
}

def printUser(user: User): Unit = {
  println(s"User: ${user.id.get}: ${user.name}, ${user.email}")
}
