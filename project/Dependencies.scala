import sbt._

object Dependencies {
  lazy val javeCore = "ws.schild" % "jave-core" % "2.6.0"
  lazy val javePlatform = "ws.schild" % "jave-nativebin-linux64" % "2.6.0"
  lazy val textToSpeech = "com.google.cloud" % "google-cloud-texttospeech" % "0.101.0-beta"
  lazy val argParse = "net.sourceforge.argparse4j" % "argparse4j" % "0.8.1"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
}
