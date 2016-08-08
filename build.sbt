import AssemblyKeys._

assemblySettings

name := "scala-jld"

version := "0.0"

lazy val root = (project in file("."))

scalaVersion := "2.11.7"

mainClass in assembly := Some("scalaJDL.Main")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test->default",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
  "org.scala-saddle" %% "saddle-core" %"1.3.+",
  "org.scala-saddle" %% "saddle-hdf5" % "1.3.+"
)

