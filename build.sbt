
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.jasperm",
      scalaVersion := "2.12.4",
      version      := "0.2.0-SNAPSHOT"
    )),
    name := "simple-lenses",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.1" % Test,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
