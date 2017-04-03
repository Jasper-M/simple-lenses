
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.jasperm",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "simple-lenses",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.1" % Test,
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
    )
  )
