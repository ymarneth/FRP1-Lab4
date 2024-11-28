ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "3.6.1"
ThisBuild / scalacOptions := Seq("-unchecked", "-feature", "-deprecation")

val logbackVersion = "1.5.12"

lazy val root = (project in file("."))
  .settings(
    name := "frp-ue1",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "com.h2database" % "h2" % "2.2.224",
      "org.flywaydb" % "flyway-core" % "10.15.2",
      "com.typesafe.slick" %% "slick" % "3.5.0",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.5.1"
    )
  )
