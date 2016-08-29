import AssemblyKeys._

assemblySettings

name := "jukai-scala"

scalaVersion := "2.11.7"

version := "0.0"

fork in run := true

parallelExecution in Test := false

crossPaths := false

mainClass in assembly := Some("JukaiScala.Main")

resolvers ++= Seq(
  "Unidata maven repository" at "http://artifacts.unidata.usar.edu/content/repositories/unidata-releases",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test->default",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scala-saddle" % "jhdf5" % "2.9",
  "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-natives" % "0.12",
  "org.scalanlp" %% "breeze-viz" % "0.12",
  "org.apache.spark" %% "spark-core" % "1.5.1" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.5.1" % "provided"
)

lazy val root = (project in file(".")).
  enablePlugins(BuildInfoPlugin)

