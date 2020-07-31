name := "scalautist"

version := "0.1"
ThisBuild /scalaVersion := "2.13.2"
ThisBuild /crossPaths := false
ThisBuild /Test / parallelExecution := false

lazy val client = project.settings(
  coverageEnabled  := true,
  mainClass := Some("MainClient"),
  name := "scalautist-client-scala",
  libraryDependencies ++= Seq(
    libraries.akkaHttp,
    libraries.akkaActor,
    libraries.akkaStream,
    libraries.sprayJson,
    libraries.controlsfx,
    librariesTest.scalatest,
    librariesTest.scalaCheck,
    librariesTest.testFXTest,
    librariesTest.junit,
    librariesTest.monocle,
    librariesTest.testFXCore,
    librariesTest.junitParams,
    libraries.slick,
    libraries.mssql,
    libraries.slickHikaricp
  ),
  scalacOptions ++= compilerOptions,
  assemblySettings,

).dependsOn(utils,event)

lazy val server = project.enablePlugins(JavaAppPackaging).
enablePlugins(DockerPlugin).settings(
  coverageEnabled  := true,
  dockerBaseImage       := "openjdk:jre",
  dockerExposedPorts := Seq(8080),
  mainClass  in Compile := Some("servermodel.MainServer"),
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
    libraries.javax,
    libraries.swaggerAkkaHttp,
    libraries.swaggerScala ,
    libraries.swaggerEnum,
    libraries.jacksonModule,
    libraries.iterator,
    libraries.coreSwagger,
    libraries.coreAnnotations,
    libraries.modelSwagger,
    libraries.jaxrs2Swagger,
    libraries.megard,
    libraries.bcryp,
    librariesTest.scalatest,
    librariesTest.scalaCheck,
    librariesTest.junit,
    librariesTest.testKitHttp,
    librariesTest.testKitStream
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
  "-language:postfixOps",
  "-Wunused:nowarn",
  "-language:implicitConversions",
  "-feature"
)

lazy val libraries = new {
  val akkaHttpVersion = "10.1.12"
  val akkaVersion     = "2.6.5"
  val logBackVersion  = "1.2.3"
  val slickVersion    = "3.3.2"
  val mssqlVersion    = "8.2.2.jre8"
  val jwtVersion      = "4.2.0"
  val scalaReflectVersion = "2.13.2"
  val rabbitVersion  = "5.9.0"
  val swaggerVersion ="2.1.1"
  val jacksonVersion = "2.11.0"
  val javaxVersion   ="2.0.1"
  val swaggerAkkaHttpVersion="2.1.0"
  val swaggerEnumeratunVersion = "2.0.0"
  val iteratorVersion = "1.7.1"
  val megarVersion    = "0.4.3"
  val controlsfxVersion= "8.40.14"
  val bcrypVersion    ="0.3m"
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
  val javax          ="javax.ws.rs"               % "javax.ws.rs-api"           % javaxVersion
  val swaggerAkkaHttp="com.github.swagger-akka-http" %% "swagger-akka-http"     % swaggerAkkaHttpVersion
  val swaggerScala   ="com.github.swagger-akka-http" %% "swagger-scala-module"  % swaggerVersion
  val swaggerEnum    ="com.github.swagger-akka-http" %% "swagger-enumeratum-module" % swaggerEnumeratunVersion
  val jacksonModule  = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
  val iterator       ="pl.iterators" %% "kebs-spray-json"                       % iteratorVersion
  val coreSwagger    = "io.swagger.core.v3" % "swagger-core"                    % swaggerVersion
  val coreAnnotations ="io.swagger.core.v3" % "swagger-annotations"              % swaggerVersion
  val modelSwagger   ="io.swagger.core.v3" % "swagger-models"                   % swaggerVersion
  val jaxrs2Swagger  ="io.swagger.core.v3" % "swagger-jaxrs2"                   % swaggerVersion
  val megard         = "ch.megard"                %% "akka-http-cors"           % megarVersion
  val controlsfx     = "org.controlsfx"             % "controlsfx"              % controlsfxVersion
  val bcryp          ="org.mindrot"               %  "jbcrypt"                  % bcrypVersion
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
  val akkaStream         = "akka-stream-testkit"
  val akkaHttp           = "akka-http-testkit"
  val juntPl             = "pl.pragmatists"
  val scalatest          = "org.scalatest" % "scalatest_2.13"   % scalatestVersion % Test
  val junitInterface     = "com.novocode"   % "junit-interface" % junitVersion % Test
  val junit              = "com.novocode"   % "junit-interface" % junitVersion % Test
  val scalaCheck         = "org.scalacheck" %% "scalacheck"     % scalaCheckVersion % Test
  val testFXTest         ="org.testfx" % "testfx-junit" % testFXVersion % Test
  val testFXCore         = "org.testfx" % "testfx-core" % testFXVersion % Test
  val monocle            ="org.testfx" % "openjfx-monocle" % "1.8.0_20" % Test
  val junitParams        = "pl.pragmatists" % "JUnitParams" % junitParamsVersion % Test
  val testKitStream      ="com.typesafe.akka" %% "akka-stream-testkit" % libraries.akkaVersion % Test
  val testKitHttp        ="com.typesafe.akka" %% "akka-http-testkit" % libraries.akkaHttpVersion % Test

}

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("application.conf")  => MergeStrategy.concat
    case PathList("reference.conf")    => MergeStrategy.concat
    case x                             => MergeStrategy.first
  },
  scalacOptions ++= compilerOptions,
  cleanFiles += baseDirectory.value / "temp",
  coverageEnabled  := false,
  test in assembly := {},
    excludeDependencies ++= Seq(
    librariesTest.scalatestOrg,
    librariesTest.junitCom,
    librariesTest.scalaCheckOrg,
    librariesTest.testFXOrg,
    librariesTest.juntPl,
    librariesTest.akkaStream,
    librariesTest.akkaHttp,
    librariesTest.juntPl,
  )
)