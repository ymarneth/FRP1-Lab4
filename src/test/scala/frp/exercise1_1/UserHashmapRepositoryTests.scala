package frp.exercise1_1

import frp.exercise1_1.user.User.printResult
import frp.exercise1_1.user.{User, UserHashmapRepository}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec
import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext}
import scala.compiletime.uninitialized
import scala.concurrent.duration.Duration

given ExecutionContext = ExecutionContext.global

class UserHashmapRepositoryTests extends AnyFunSpec with BeforeAndAfterEach {

  var userRepository: UserHashmapRepository = uninitialized
  var userTable: scala.collection.mutable.HashMap[Int, User] = uninitialized

  override def beforeEach(): Unit = {
    val userTable: mutable.HashMap[Int, User] = mutable.HashMap()
    userRepository = new UserHashmapRepository(userTable)
  }

  it("should fetch a user by ID") {
    val user = User(Some(1), "John Doe", "john.doe@example.com")
    Await.result(userRepository.addUser(user), Duration.Inf)

    val fetchedUser$ = userRepository.fetchUserById(1)
    printResult(fetchedUser$)

    val fetchedUser = Await.result(fetchedUser$, Duration.Inf)
    assert(fetchedUser.isSuccess)
  }

  it("should return error when fetching a user by an ID that does not exist") {
    val fetchedUser = Await.result(userRepository.fetchUserById(999), Duration.Inf)
    assert(fetchedUser.isFailure)
    assert(fetchedUser.failed.get.getMessage == "User with ID 999 not found")
  }

  it("should validate that user name is not empty when fetching by ID") {
    val user = User(Some(1), "", "john.doe@example.com")
    Await.result(userRepository.addUser(user), Duration.Inf)

    val fetchedUser = Await.result(userRepository.fetchAndValidateUser(1), Duration.Inf)
    assert(fetchedUser.isFailure)
    assert(fetchedUser.failed.get.getMessage == "User with ID 1 is invalid")
  }

  it("should validate that the email is valid when fetching by ID") {
    val user = User(Some(1), "John Doe", "john.example.com")
    Await.result(userRepository.addUser(user), Duration.Inf)

    val fetchedUser = Await.result(userRepository.fetchAndValidateUser(1), Duration.Inf)
    assert(fetchedUser.isFailure)
    assert(fetchedUser.failed.get.getMessage == "User with ID 1 is invalid")
  }
}
