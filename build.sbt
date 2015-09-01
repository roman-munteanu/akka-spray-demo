//enablePlugins(JavaServerAppPackaging)

organization  := "com.munteanu.demo"

version       := "0.1"

scalaVersion  := "2.11.6"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Spray Repository"    at "http://repo.spray.io")

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val sprayVersion = "1.3.3"
  Seq(
    "com.typesafe.akka"  %% "akka-actor"      % akkaVersion,
    "io.spray"           %% "spray-can"       % sprayVersion,
    "io.spray"           %% "spray-routing"   % sprayVersion,
    "io.spray"           %% "spray-json"      % "1.3.1",
    "com.typesafe.akka"  %% "akka-slf4j"      % akkaVersion,
    "com.typesafe.slick" %%  "slick"          % "3.0.1",
    "org.slf4j"          %   "slf4j-nop"      % "1.6.4",
    "c3p0"               %   "c3p0"           % "0.9.1.2",
    "mysql"              %  "mysql-connector-java" % "5.1.35",
    "ch.qos.logback"     %  "logback-classic" % "1.1.2",
    "joda-time"          %  "joda-time"       % "2.7",
    "org.joda"           %  "joda-convert"    % "1.7",
    "com.typesafe.akka"  %% "akka-testkit"    % akkaVersion  % "test",
    "io.spray"           %% "spray-testkit"   % sprayVersion % "test",
    "org.specs2"         %% "specs2"          % "2.3.13"     % "test",
    "org.scalatest"      % "scalatest_2.11"   % "2.2.4"      % "test"
  )
}

//Revolver.settings
Revolver.settings: Seq[sbt.Setting[_]]

// Assembly settings
mainClass in Global := Some("com.munteanu.demo.Main")

//jarName in assembly := "akka-spray-demo.jar"

//seq(Twirl.settings: _*)

Twirl.settings