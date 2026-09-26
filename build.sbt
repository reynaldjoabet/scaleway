import Dependencies._

ThisBuild / scalaVersion := "3.9.0"
ThisBuild / version      := "0.1.0-SNAPSHOT"

ThisBuild / crossScalaVersions := Seq("3.3.8", "3.9.0")

ThisBuild / scalacOptions := Seq(
  "-encoding",
  "UTF-8",
  "-no-indent",
  "-deprecation",
  "-feature",
  "-unchecked",
  // "-Werror",
  // "-Wunused:all",
  "-Wvalue-discard",
  "-Wnonunit-statement",
  "-language:strictEquality",
  "-Xcheck-macros",
  "-Xmax-inlines:64"
)

Global / onChangedBuildSource := ReloadOnSourceChanges

val generatedScalacOptions = Seq(
  "-encoding",
  "UTF-8",
  "-java-output-version:17",
  "-Xmax-inlines:64"
)

val commonSettings = Seq(
  scalacOptions           := generatedScalacOptions,
  openApiModelNamePrefix  := "",
  openApiModelNameSuffix  := "",
  openApiGenerateMetadata := SettingDisabled,
  // Use the module-local config.json
  openApiConfigFile := (baseDirectory.value / "config.json").getPath,
  // Single shared ignore file at modules/, one level above each module dir
  openApiIgnoreFileOverride := (baseDirectory.value.getParentFile / ".openapi-generator-ignore").getPath,
  openApiOutputDir          := (baseDirectory.value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests   := SettingDisabled,
  // Scaleway specs do not pass the generator's validator
  openApiValidateSpec := SettingDisabled,
  // WHY: sbt 2 caches task results, and a cached result must be serializable.
  // java.io.File is not -- sbt accepts only xsbti.{HashedVirtualFileRef,
  // VirtualFileRef, VirtualFile} -- so a plain `generate := openApiGenerate.value`
  // fails the reload outright with "java.io.File and Path are not valid output
  // types for a cached task".
  //
  // HOW: Def.uncached opts this one task out of the cache. (Annotating the key
  // @transient is the other way out.) Opting out is what we want regardless: the
  // generator re-runs every build, so the sources on disk always match the spec.
  generate := Def.uncached {
    openApiGenerate.value
  },
  // WHY: sbt learns about sources two ways -- by listing
  // unmanagedSourceDirectories, and by running sourceGenerators, each of which
  // returns the files it wrote. `sources` is those two lists concatenated.
  //
  // `compile.dependsOn(generate)` would promise only "generate finishes before
  // compile starts". The directory listing is a SEPARATE input to compile, so
  // nothing orders it after generate and sbt may take it while generate is still
  // writing. A listing taken too early finds nothing: the module compiles 0
  // sources into an empty jar, and every downstream import of its api/models
  // fails.
  //
  // HOW: as a sourceGenerator there is no race to lose. The task's return value
  // IS half of `sources`, so sbt cannot assemble `sources` without running it
  // first -- ordering falls out of the data dependency rather than being
  // asserted. And because `generate` is typed Seq[File] (see
  // Dependencies.scala), the paths come straight from the generator; nothing
  // has to be found by listing a directory.
  //
  // The generated sources happen to be committed today, so the listing would in
  // practice always find them. This wiring is what keeps that from being
  // load-bearing.
  Compile / sourceGenerators += generate.taskValue,
  // WHY: openApiOutputDir *is* src/main/scala, so the directory listing would
  // report exactly the files the generator already returned, and each source
  // would land in `sources` twice -- it is a plain ++, sbt does not dedupe.
  //
  // HOW: emptying the directory list removes that supplier, leaving the
  // generator as the single source of truth.
  Compile / unmanagedSourceDirectories := Seq.empty,
  libraryDependencies                 ++= Seq(
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
      name                  := s"scaleway-$id-codegen",
      openApiInputSpec      := (baseDirectory.value / spec).getPath,
      openApiApiPackage     := s"scaleway.$pkg.api",
      openApiModelPackage   := s"scaleway.$pkg.models",
      openApiInvokerPackage := s"scaleway.$pkg"
    )

lazy val autoscaling = scalewayModule("autoscaling", "scaleway.autoscaling.yml", "autoscaling")
lazy val containers  = scalewayModule("containers", "scaleway.containers.yml", "containers")
lazy val iam         = scalewayModule("iam", "scaleway.iam.yml", "iam")
lazy val ipam        = scalewayModule("ipam", "scaleway.ipam.yml", "ipam")
lazy val keyManager  = scalewayModule("key-manager", "scaleway.key_manager.yml", "keymanager")
lazy val mongodb     = scalewayModule("mongodb", "scaleway.mongodb.yml", "mongodb")

lazy val secretManager =
  scalewayModule("secret-manager", "scaleway.secret_manager.yml", "secretmanager")

lazy val vpc        = scalewayModule("vpc", "scaleway.vpc.yml", "vpc")
lazy val vpcGw      = scalewayModule("vpc-gw", "scaleway.vpc_gw.yml", "vpcgw")
lazy val instance   = scalewayModule("instance", "scaleway.instance.yml", "instance")
lazy val kubernetes = scalewayModule("kubernetes", "scaleway.kubernetes.yml", "kubernetes")
lazy val kafka      = scalewayModule("kafka", "scaleway.kafka.yml", "kafka")
lazy val redis      = scalewayModule("redis", "scaleway.redis.yml", "redis")

lazy val serverlessDatabases =
  scalewayModule("serverless-databases", "scaleway.serverless_databases.yml", "serverlessdatabases")

lazy val postgreMysql =
  scalewayModule("postgre-mysql", "scaleway.postgre_mysql.yml", "postgremysql")

lazy val lb     = scalewayModule("lb", "scaleway.lb.zoned.yml", "lb")
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
    name                 := "scaleway",
    semanticdbEnabled    := true,
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
    buildInfoKeys    := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "scaleway"
  )
  .enablePlugins(BuildInfoPlugin)
  .dependsOn(modules.map(_ % "compile->compile;test->test") *)
  .aggregate(modules.map(m => LocalProject(m.id)) *)
