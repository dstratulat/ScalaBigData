import sbt._

name := "ScalaBigData"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.4",
  "org.scalatest" % "scalatest_2.12" % "3.0.1" % "test"
)