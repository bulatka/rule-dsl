scalaVersion := "2.12.10"

name := "rule-dsl"
organization := "org.bulatnig"
version := "1.0"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value

libraryDependencies += "org.typelevel" %% "cats-core" % "2.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"