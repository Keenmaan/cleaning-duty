name := """cleaning-duty"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaJpa,
  javaWs
)

libraryDependencies += "org.webjars" % "bootstrap" % "3.2.0"