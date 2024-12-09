package frp.exercise1_1

import slick.jdbc.H2Profile.api.*
import org.flywaydb.core.Flyway

object FlywayWithSlickApp extends App {

  private val flyway = Flyway.configure()
    .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
    .locations("filesystem:src/main/resources/db/migration")
    .load()

  private val migrationsApplied = flyway.migrate()
  println(s"Number of migrations applied: $migrationsApplied")

  val db = Database.forConfig("h2mem1")

  db.close()
}
