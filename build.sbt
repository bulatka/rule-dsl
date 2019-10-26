ThisBuild / organization := "org.bulatnig.ruler"
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.12.10"

lazy val v1 = (project in file("v1"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies
  )

lazy val v2 = (project in file("v2"))
  .settings(
    commonSettings,
    libraryDependencies ++= commonDependencies
  )

lazy val commonDependencies = Seq(
  "org.scala-lang" % "scala-compiler" % "2.12.10",
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

lazy val commonSettings = Seq(
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.sonatypeRepo("releases")
  )
)