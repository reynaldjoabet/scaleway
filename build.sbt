import Dependencies._

ThisBuild / scalaVersion := "3.3.8"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-no-indent",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Wunused:all",
  "-Wvalue-discard",
  "-Wnonunit-statement",
  "-Ykind-projector",
  "-Xmax-inlines:64"
)

Global / onChangedBuildSource := ReloadOnSourceChanges

val commonSettings = Seq(
  scalacOptions := Seq.empty,
  openApiModelNamePrefix := "",
  openApiModelNameSuffix := "",
  openApiGenerateMetadata := SettingDisabled,
  // Use the module-local config.json
  openApiConfigFile := (baseDirectory.value / "config.json").getPath,
  // Single shared ignore file at modules/, one level above each module dir
  openApiIgnoreFileOverride := (baseDirectory.value.getParentFile / ".openapi-generator-ignore").getPath,
  openApiOutputDir := (baseDirectory.value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests := SettingDisabled,
  // Scaleway specs do not pass the generator's validator
  openApiValidateSpec := SettingDisabled,
  generate := Def.uncached {
    openApiGenerate.value
  },
  // Wired in as a sourceGenerator, NOT as `compile.dependsOn(generate)`.
  // sbt collects `sources` by globbing src/main/scala in a task separate from
  // `compile`, and dependsOn only sequences generate ahead of `compile` --
  // not ahead of that glob. So on a clean checkout the glob would run first,
  // find nothing, and the module would compile 0 sources, leaving its
  // api/models off the classpath and failing every downstream import that
  // depends on it -- and locally you'd never notice, since the previous run's
  // files are still on disk and the glob always finds those. A sourceGenerator
  // feeds `sources` directly, so sbt has to run it first.
  //
  // No separate glob needed: generate is typed Seq[File] (see
  // Dependencies.scala), so its own return value -- the exact file list
  // openApiGenerate just wrote -- IS what sourceGenerators needs.
  Compile / sourceGenerators += generate.taskValue,
  // openApiOutputDir *is* src/main/scala, so the generator above already covers
  // everything sbt would otherwise pick up as unmanaged sources. Dropping the
  // unmanaged dir makes the generator the single source of truth instead of
  // having sbt separately glob a directory that's empty on a clean checkout.
  Compile / unmanagedSourceDirectories := Seq.empty,
  libraryDependencies ++= Seq(
    sttpJsoniter,
    jsoniter,
    jsoniterMacros,
    jsoniterCirce
  )
)

def scalewayModule(id: String, spec: String, pkg: String): Project =
  Project(s"scaleway-$id-codegen", file(s"modules/scaleway-$id-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := s"scaleway-$id-codegen",
      openApiInputSpec := (baseDirectory.value / spec).getPath,
      openApiApiPackage := s"scaleway.$pkg.api",
      openApiModelPackage := s"scaleway.$pkg.models",
      openApiInvokerPackage := s"scaleway.$pkg"
    )

lazy val autoscaling = scalewayModule("autoscaling", "scaleway.autoscaling.yml", "autoscaling")
lazy val containers = scalewayModule("containers", "scaleway.containers.yml", "containers")
lazy val iam = scalewayModule("iam", "scaleway.iam.yml", "iam")
lazy val ipam = scalewayModule("ipam", "scaleway.ipam.yml", "ipam")
lazy val keyManager = scalewayModule("key-manager", "scaleway.key_manager.yml", "keymanager")
lazy val mongodb = scalewayModule("mongodb", "scaleway.mongodb.yml", "mongodb")
lazy val secretManager = scalewayModule("secret-manager", "scaleway.secret_manager.yml", "secretmanager")
lazy val vpc = scalewayModule("vpc", "scaleway.vpc.yml", "vpc")
lazy val vpcGw = scalewayModule("vpc-gw", "scaleway.vpc_gw.yml", "vpcgw")
lazy val instance = scalewayModule("instance", "scaleway.instance.yml", "instance")
lazy val kubernetes = scalewayModule("kubernetes", "scaleway.kubernetes.yml", "kubernetes")
lazy val kafka = scalewayModule("kafka", "scaleway.kafka.yml", "kafka")
lazy val redis = scalewayModule("redis", "scaleway.redis.yml", "redis")
lazy val serverlessDatabases =
  scalewayModule("serverless-databases", "scaleway.serverless_databases.yml", "serverlessdatabases")
lazy val postgreMysql = scalewayModule("postgre-mysql", "scaleway.postgre_mysql.yml", "postgremysql")
lazy val lb = scalewayModule("lb", "scaleway.lb.zoned.yml", "lb")
lazy val s2sVpn = scalewayModule("s2s-vpn", "scaleway.s2s_vpn.yml", "s2svpn")

lazy val modules: Seq[Project] = Seq(
  autoscaling,
  containers,
  iam,
  ipam,
  keyManager,
  mongodb,
  secretManager,
  vpc,
  vpcGw,
  instance,
  kubernetes,
  kafka,
  redis,
  serverlessDatabases,
  postgreMysql,
  lb,
  s2sVpn
)

lazy val root = (project in file("."))
  .settings(
    name := "scaleway",
    libraryDependencies ++= Seq(
      sttpCore,
      http4sBackend,
      http4sDsl,
      emberServer,
      fs2,
      chimney,
      emberClient,
      catsEffect,
      pureconfig,
      slf4j,
      scribe,
      scribeSlf4j,
      scribeCats,
      munit
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "scaleway"
  )
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(modules.map(_ % "compile->compile;test->test") *)
  .aggregate(modules.map(m => LocalProject(m.id)) *)
