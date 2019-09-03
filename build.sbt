import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "Slovenské cvičenia",
    libraryDependencies ++= Seq(textToSpeech, argParse, javeCore, javePlatform, scalaTest % Test)
  )
