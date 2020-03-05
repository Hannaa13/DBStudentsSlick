name := "DBFirst"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.postgresql" % "postgresql" % "42.2.10",
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % Test

)




import com.permutive.sbtliquibase.SbtLiquibase
enablePlugins(SbtLiquibase)
liquibaseUsername := "postgres"
liquibasePassword := "postgres"
liquibaseDriver   := "org.postgresql.Driver"
liquibaseUrl      := "jdbc:postgresql://localhost:5432/students"

