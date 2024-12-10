package frp.exercise1_1

import frp.exercise1_1.user.User.printResult
import frp.exercise1_1.user.{User, UserHashmapRepository}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

class UserHashmapRepositoryTests extends AnyFunSpec with BeforeAndAfterEach {

  implicit val ec: ExecutionContext = ExecutionContext.global

  var userRepo: UserHashmapRepository = uninitialized
  var userTable: scala.collection.mutable.HashMap[Int, User] = uninitialized

  override def beforeEach(): Unit = {
    val userTable: mutable.HashMap[Int, User] = mutable.HashMap()
    userRepo = new UserHashmapRepository(userTable)
  }

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
