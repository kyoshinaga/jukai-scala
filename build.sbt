import AssemblyKeys._

assemblySettings

name := "jukai-scala"

scalaVersion := "2.11.7"

version := "0.0"

fork in run := true

parallelExecution in Test := false

crossPaths := false

mainClass in assembly := Some("jukaiScala.main.jukaiNLP")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test->default",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-natives" % "0.12",
  "org.scalanlp" %% "breeze-viz" % "0.12",
  "org.hdfgroup" % "hdf-java" % "2.6.1"
)
