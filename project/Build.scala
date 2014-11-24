import aether.Aether
import sbt._
import sbt.Keys._
import sbtassembly.Plugin._
import scala.Some

object KafkaUtilsBuild extends Build {

  def sharedSettings = Defaults.defaultSettings ++ assemblySettings ++ Seq(
    version := "0.2.1-SNAPSHOT",
    scalaVersion := "2.10.3",
    organization := "com.quantifind",
    scalacOptions := Seq("-deprecation", "-unchecked", "-optimize"),
    unmanagedJars in Compile <<= baseDirectory map { base => (base / "lib" ** "*.jar").classpath },
    retrieveManaged := true,
    transitiveClassifiers in Scope.GlobalScope := Seq("sources"),
    resolvers ++= Seq(
      "sonatype-snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "sonatype-releases" at "http://oss.sonatype.org/content/repositories/releases",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
      "JBoss Repository" at "http://repository.jboss.org/nexus/content/repositories/releases/"),
    libraryDependencies ++= Seq(
      "log4j" % "log4j" % "1.2.17",
      "org.scalatest" %% "scalatest" % "1.9.1" % "test",
      "org.apache.kafka" %% "kafka" % "0.8.1"),
      publishTo <<= version { (v: String) =>
        if (v.endsWith("SNAPSHOT")) Some(Resolvers.finnDeploySnapshots) else Some(Resolvers.finnDeployRelease)
      }

  ) ++ Aether.aetherPublishSettings

  val slf4jVersion = "1.6.1"

  //offsetmonitor project

  lazy val offsetmonitor = Project("offsetmonitor", file("."), settings = offsetmonSettings)


  object Resolvers {

    val javaM2 = "java m2" at  "http://download.java.net/maven/2"

    val finnScala = "Finn Scala" at "http://mavenproxy.finntech.no/finntech-scala/"

    val finnRelease = "Finn release" at "http://mavenproxy.finntech.no/nexus/content/groups/finntech-release"

    val finnDeploySnapshots = "Finn snapshots" at "http://mavenproxy.finntech.no/nexus/content/repositories/finntech-internal-snapshot"

    val finnDeployRelease = "Finn deploy release" at "http://mavenproxy.finntech.no/nexus/content/repositories/finntech-internal-release"

    val twitter = "twitter repo" at "http://maven.twttr.com"
  }

  def offsetmonSettings = sharedSettings ++ Seq(
    name := "KafkaOffsetMonitor",
    libraryDependencies ++= Seq(
      "net.databinder" %% "unfiltered-filter" % "0.6.7",
      "net.databinder" %% "unfiltered-jetty" % "0.6.7",
      "net.databinder" %% "unfiltered-json" % "0.6.7",
      "com.quantifind" %% "sumac" % "0.3.0",
      "com.typesafe.slick" %% "slick" % "2.0.0",
      "org.xerial" % "sqlite-jdbc" % "3.7.2",
      "com.twitter" % "util-core" % "3.0.0"),
    resolvers ++= Seq(
      Resolvers.finnScala,
      Resolvers.finnRelease,
      Resolvers.finnDeploySnapshots,
      Resolvers.javaM2,
      Resolvers.twitter))
}
