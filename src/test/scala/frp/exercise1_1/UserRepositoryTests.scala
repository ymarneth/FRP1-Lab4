package frp.exercise1_1

import frp.exercise1_1.user.User.printResult
import frp.exercise1_1.user.{CreateUserCommand, User, UserRepository}
import org.flywaydb.core.Flyway
import slick.jdbc.H2Profile.api.*
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec

import scala.compiletime.uninitialized
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration

given ExecutionContext = ExecutionContext.global

class UserRepositoryTests extends AnyFunSpec with BeforeAndAfterEach {

  var userRepo: UserRepository = uninitialized
  var db: Database = uninitialized

  override def beforeEach(): Unit = {
    val flyway = Flyway.configure()
      .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
      .locations("filesystem:src/main/resources/db/migration")
      .cleanDisabled(false)
      .load()

    flyway.clean()
    val migrationsApplied = flyway.migrate()
    println(s"Number of migrations applied: $migrationsApplied")

    db = Database.forConfig("h2mem1")
    userRepo = UserRepository(db)
  }

  override def afterEach(): Unit = {
    db.close()
  }

  it("should fetch a user by ID") {
    val user = User(Some(1), "John Doe", "john.doe@example.com")
    Await.result(userRepo.addUser(CreateUserCommand(user.name, user.email)), Duration.Inf)

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
    Await.result(userRepo.addUser(CreateUserCommand(user.name, user.email)), Duration.Inf)

    val fetchedUser = Await.result(userRepo.fetchAndValidateUser(1), Duration.Inf)
    assert(fetchedUser.isEmpty)
  }

  it("should validate that the email is valid when fetching by ID") {
    val user = User(Some(1), "John Doe", "john.example.com")
    Await.result(userRepo.addUser(CreateUserCommand(user.name, user.email)), Duration.Inf)

    val fetchedUser = Await.result(userRepo.fetchAndValidateUser(1), Duration.Inf)
    assert(fetchedUser.isEmpty)
  }
}
