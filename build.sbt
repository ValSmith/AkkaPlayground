name := "AkkaPlayground"

val AkkaVersion = "2.6.19"
version := "0.1"
val akkaPackage = "com.typesafe.akka"
libraryDependencies ++= Seq(
  akkaPackage %% "akka-actor-typed" % AkkaVersion,
  akkaPackage %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  akkaPackage %% "akka-actor-typed" % AkkaVersion,
  akkaPackage %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.13" % "test"
)

scalastyleConfig := (ThisBuild / baseDirectory).value / "project/scalastyle_config.xml"

scalaVersion := "2.13.3"
