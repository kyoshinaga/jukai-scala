name := "jukai-scala"

scalaVersion := "2.11.7"

version := "0.0"

fork in run := true

parallelExecution in Test := false

crossPaths := false

scalacOptions ++= Seq("-deprecation", "-feature")

mainClass in assembly := Some("jukaiScala.main.KerasParser")

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Unidata maven repository" at "http://artifacts.unidata.ucar.edu/content/repositories/unidata-releases"
)

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.11" % "test",
  "com.novocode" % "junit-interface" % "0.10-M4" % "test->default",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "org.scalanlp" %% "breeze" % "0.12",
  "org.scalanlp" %% "breeze-natives" % "0.12",
  "org.scalanlp" %% "breeze-viz" % "0.12",
  "log4j" % "log4j" % "1.2.14",
  "edu.ucar" % "cdm" % "4.6.8",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "com.ibm.icu" % "icu4j" % "49.1"
)