package frp.exercise1

import frp.exercise1.user.Users
import org.flywaydb.core.Flyway
import slick.jdbc.JdbcBackend.Database
import slick.lifted.TableQuery

object FlywayWithSlickApp extends App {
  private val flyway = Flyway.configure()
    .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
    .locations("filesystem:src/main/resources/db/migration")
    .load()

  private val migrationsApplied = flyway.migrate()
  println(s"Number of migrations applied: $migrationsApplied")

  private val db = Database.forConfig("h2mem1")
  private val users = TableQuery[Users]

  val action = users.result
  val usersResult = db.run(action)

  import scala.concurrent.Await
  import scala.concurrent.duration._

  val allUsers = Await.result(usersResult, 2.seconds)
  println(s"All users in DB: $allUsers")
}
