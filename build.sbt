import sbt.Keys.{libraryDependencies, resolvers}
import sbt.file

lazy val root = project
  .in(file("."))
  .settings(
    globalSettings,
    assemblySettings,
    name := "spark-aggregation-framework",
    version := "1.0.1",
    organization := "cloud.spark",
    scalaVersion := "2.12.14",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26" % Provided,
    libraryDependencies ++= projectDependencies // all slf4j dependencies removed
    )

lazy val globalSettings = Seq(
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
  //  "-language:existential",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-deprecation",
    "-encoding",
    "utf8"
  ),
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    "Maven Repo" at "https://repo.maven.apache.org/maven2/",
    //"Confluent Repo" at "https://packages.confluent.io/maven/",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),

  )
)

lazy val dependencies =
  new {
    val vTypesafeConfig = "1.2.1"
    val vSpark = "3.0.1"
    val vDbutils = "0.0.4"

    val typesafeConfig = "com.typesafe" % "config" % vTypesafeConfig
    val dbutils = "com.databricks" %% "dbutils-api" % vDbutils
    val sparkCore = "org.apache.spark" %% "spark-core" % vSpark
    val sparkSql = "org.apache.spark" %% "spark-sql" % vSpark
  }

lazy val projectDependencies = Seq(
  dependencies.typesafeConfig % Provided,
  dependencies.dbutils % Provided,
  dependencies.sparkCore % Provided,
  dependencies.sparkSql % Provided
).map(_.exclude("org.slf4j","*")) // remove all slf4j dependencies

// define assembly jar name
lazy val fullJarName = taskKey[String]("assembly jar name")
fullJarName := s"""${name.value}_${scalaBinaryVersion.value}-${version.value}-assembly.jar"""

// Assembly Jar Settings
lazy val assemblySettings = Seq(
  assemblyJarName in assembly := fullJarName.value,
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case x => MergeStrategy.first
  },
  assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
)