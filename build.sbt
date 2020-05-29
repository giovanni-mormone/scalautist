name := "scalautist"

version := "0.1"

ThisBuild /scalaVersion := "2.13.2"


lazy val client = project.settings(
  mainClass := Some("view.MainClient"),
  name := "scalautist-client-scala",
  libraryDependencies ++= Seq(
    libraries.akkaHttp,
    libraries.akkaActor,
    libraries.akkaStream,
    libraries.sprayJson,
    librariesTest.testFXTest,
    librariesTest.junit,
    librariesTest.monocle,
    librariesTest.testFXCore,
    librariesTest.junitParams
  ),
  scalacOptions ++= compilerOptions,
  assemblySettings

).dependsOn(utils,event)
lazy val server = project.enablePlugins(JavaAppPackaging).
enablePlugins(DockerPlugin).settings(
  dockerBaseImage       := "openjdk:jre",
  dockerExposedPorts := Seq(8080),
  mainClass  in Compile := Some("servermodel.StartServer"),
  name := "scalautist-server-scala",
  libraryDependencies ++= Seq(
    libraries.akkaHttp,
    libraries.akkaActor,
    libraries.akkaStream,
    libraries.sprayJson,
    libraries.slick,
    libraries.slickHikaricp,
    libraries.scalaReflect,
    libraries.mssql,
    libraries.logBack,
    librariesTest.scalatest,
    librariesTest.scalaCheck,
    librariesTest.junit
  ),
  scalacOptions ++= compilerOptions,
  assemblySettings
).dependsOn(utils,event)

lazy val event = project.settings(
  name := "scalautist-event-scala",
  libraryDependencies ++= Seq(
    libraries.rabbit,
    libraries.akkaActor,
    libraries.akkaStream,
    libraries.sprayJson
  ),
  scalacOptions ++= compilerOptions,
  assemblySettings

).dependsOn(utils)
lazy val utils = project.settings(
  name := "scalautist-util-scala",
  libraryDependencies ++= Seq(
    libraries.sprayJson,
    libraries.jwt,
    libraries.akkaActor,
    libraries.akkaStream
  ),
  scalacOptions ++= compilerOptions
)

lazy val compilerOptions = Seq(
  "-encoding","utf8",
  "-explaintypes",
  "-deprecation",
  "-unchecked",
  "-Xsource:2.13.0",
  "-Ywarn-dead-code",
  "-language:postfixOps"
)

lazy val libraries = new {
  val akkaHttpVersion = "10.1.12"
  val akkaVersion     = "2.6.5"
  val logBackVersion  = "1.2.3"
  val slickVersion    = "3.3.2"
  val mssqlVersion    = "8.2.2.jre8"
  val jwtVersion      = "4.2.0"
  val scalaReflectVersion = "2.13.2"
  val rabbitVersion = "5.9.0"
  val akkaHttp       = "com.typesafe.akka"        %% "akka-http"                % akkaHttpVersion
  val akkaActor      ="com.typesafe.akka"         %% "akka-actor-typed"         % akkaVersion
  val akkaStream     = "com.typesafe.akka"        %% "akka-stream"              % akkaVersion
  val logBack        = "ch.qos.logback"           % "logback-classic"           % logBackVersion
  val sprayJson      = "com.typesafe.akka"        %% "akka-http-spray-json"     % akkaHttpVersion
  val slick          = "com.typesafe.slick"       %% "slick"                    % slickVersion
  val slickHikaricp  = "com.typesafe.slick"       %% "slick-hikaricp"           % slickVersion
  val mssql          = "com.microsoft.sqlserver"   % "mssql-jdbc"               % mssqlVersion
  val scalaReflect   = "org.scala-lang"            % "scala-reflect"            % scalaReflectVersion
  val jwt            = "com.pauldijou"            %% "jwt-spray-json"           % jwtVersion
  val rabbit         = "com.rabbitmq"              % "amqp-client"              % rabbitVersion
}

lazy val librariesTest = new {
  val scalatestVersion   = "3.1.2"
  val junitVersion       = "0.11"
  val scalaCheckVersion  = "1.14.0"
  val scalatestOrg       = "org.scalatest"
  val junitCom           = "com.novocode"
  val scalaCheckOrg      = "org.scalacheck"
  val testFXVersion      ="4.0.13-alpha"
  val testFXOrg          = "org.testfx"
  val junitParamsVersion = "1.1.1"
  val juntPl             = "pl.pragmatists"
  val scalatest          = "org.scalatest" % "scalatest_2.13"   % scalatestVersion % Test
  val junitInterface     = "com.novocode"   % "junit-interface" % junitVersion % Test
  val junit              = "com.novocode"   % "junit-interface" % junitVersion % Test
  val scalaCheck         = "org.scalacheck" %% "scalacheck"     % scalaCheckVersion % Test
  val testFXTest         ="org.testfx" % "testfx-junit" % testFXVersion % Test
  val testFXCore         = "org.testfx" % "testfx-core" % testFXVersion % Test
  val monocle            ="org.testfx" % "openjfx-monocle" % "1.8.0_20" % Test
  val junitParams        = "pl.pragmatists" % "JUnitParams" % junitParamsVersion % Test

}

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  scalacOptions ++= compilerOptions,
  excludeDependencies ++= Seq(librariesTest.scalatestOrg,librariesTest.junitCom,librariesTest.scalaCheckOrg, librariesTest.testFXOrg, librariesTest.juntPl)
)
