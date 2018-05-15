lazy val finagleLearn = (project in file(".")).
  settings(
    name := "FinagleLearn",
    version := "1.0",
    scalaVersion := "2.12.5",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-encoding", "UTF-8",
      "-unchecked",
      "-Xlint",
      "-Ywarn-dead-code"
    )
  )

//testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")
shellPrompt := { s => Project.extract(s).currentProject.id + "> " }

//ivyScala := ivyScala.value map {
//  _.copy(overrideScalaVersion = true)
//}
//scalafmtConfig in ThisBuild := file(s"file://${Path.userHome.getAbsolutePath}/.sbt/1.0/.scalafmt.conf")
//ensimeRepositoryUrls in ThisBuild += "http://maven.aliyun.com/nexus/content/groups/public"
//ensimeIgnoreScalaMismatch in ThisBuild := true
//ensimeJavaFlags in ThisBuild := Seq("-Xss512M", "-Xmx4G", "-XX:MaxMetaspaceSize=768M")
//resolvers ++= Seq(
//  Resolver.mavenLocal,
//  MavenRepository("aliyun", "http://maven.aliyun.com/nexus/content/groups/public")
//)

//assemblyMergeStrategy in assembly := {
//  case PathList("META-INF", xs@_*) => MergeStrategy.discard
//  case _ => MergeStrategy.first
//}

lazy val log4jVersion = "2.11.0"
lazy val scalazVersion = "7.2.20"
lazy val finagleVersion = "18.5.0"
lazy val latest = "latest.integration"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % finagleVersion,
  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-jcl" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % Test,
  "junit" % "junit" % "4.12" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
)
