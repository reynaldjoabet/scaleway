import Dependencies._

ThisBuild / scalaVersion := "3.3.8"
ThisBuild / version := "0.1.0-SNAPSHOT"

Global / onChangedBuildSource := ReloadOnSourceChanges

lazy val root = (project in file("."))
  .settings(
    name := "scaleway",
    ThisBuild / scalacOptions ++= Seq(
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
    ),
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
  .dependsOn(
    `scaleway-autoscaling-codegen` % "compile->compile;test->test",
    `scaleway-containers-codegen` % "compile->compile;test->test",
    `scaleway-iam-codegen` % "compile->compile;test->test",
    `scaleway-ipam-codegen` % "compile->compile;test->test",
    `scaleway-key-manager-codegen` % "compile->compile;test->test",
    `scaleway-mongodb-codegen` % "compile->compile;test->test",
    `scaleway-secret-manager-codegen` % "compile->compile;test->test",
    `scaleway-vpc-codegen` % "compile->compile;test->test",
    `scaleway-vpc-gw-codegen` % "compile->compile;test->test",
    `scaleway-instance-codegen` % "compile->compile;test->test",
    `scaleway-kubernetes-codegen` % "compile->compile;test->test",
    `scaleway-kafka-codegen` % "compile->compile;test->test",
    `scaleway-redis-codegen` % "compile->compile;test->test",
    `scaleway-serverless-databases-codegen` % "compile->compile;test->test",
    `scaleway-postgre-mysql-codegen` % "compile->compile;test->test",
    `scaleway-lb-codegen` % "compile->compile;test->test",
    `scaleway-s2s-vpn-codegen` % "compile->compile;test->test"
  )
  .enablePlugins(BuildInfoPlugin)
  .aggregate(
    `scaleway-autoscaling-codegen`,
    `scaleway-containers-codegen`,
    `scaleway-iam-codegen`,
    `scaleway-ipam-codegen`,
    `scaleway-key-manager-codegen`,
    `scaleway-mongodb-codegen`,
    `scaleway-secret-manager-codegen`,
    `scaleway-vpc-codegen`,
    `scaleway-vpc-gw-codegen`,
    `scaleway-instance-codegen`,
    `scaleway-kubernetes-codegen`,
    `scaleway-kafka-codegen`,
    `scaleway-redis-codegen`,
    `scaleway-serverless-databases-codegen`,
    `scaleway-postgre-mysql-codegen`,
    `scaleway-lb-codegen`,
    `scaleway-s2s-vpn-codegen`
  )

val commonSettings = Seq(
  scalacOptions --= Seq(
    "-no-indent",
    "-Wunused:all",
    "-Wvalue-discard",
    "-Wnonunit-statement"
  ),
  openApiModelNamePrefix := "",
  openApiModelNameSuffix := "",
  openApiGenerateMetadata := SettingDisabled,
  // Use the module-local config.json
  openApiConfigFile := (baseDirectory.value / "config.json").getPath,

  openApiIgnoreFileOverride := (baseDirectory.value / ".openapi-generator-ignore").getPath,

  // Regenerated into src/main/scala on every compile via the sourceGenerator
  // below, not manually via a standalone `generate` invocation.
  openApiOutputDir := ((Compile / baseDirectory).value / "src/main/scala").getAbsolutePath,
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
  // openApiOutputDir *is* src/main/scala, so the generator above already globs
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

lazy val `scaleway-autoscaling-codegen` =
  (project in file("modules/scaleway-autoscaling-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-autoscaling-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.autoscaling.yml").getPath,
      openApiApiPackage := "scaleway.autoscaling.api",
      openApiModelPackage := "scaleway.autoscaling.models",
      openApiInvokerPackage := "scaleway.autoscaling"
    )

lazy val `scaleway-containers-codegen` =
  (project in file("modules/scaleway-containers-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-containers-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.containers.yml").getPath,
      openApiApiPackage := "scaleway.containers.api",
      openApiModelPackage := "scaleway.containers.models",
      openApiInvokerPackage := "scaleway.containers"
    )

lazy val `scaleway-iam-codegen` =
  (project in file("modules/scaleway-iam-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-iam-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.iam.yml").getPath,
      openApiApiPackage := "scaleway.iam.api",
      openApiModelPackage := "scaleway.iam.models",
      openApiInvokerPackage := "scaleway.iam"
    )

lazy val `scaleway-ipam-codegen` =
  (project in file("modules/scaleway-ipam-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-ipam-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.ipam.yml").getPath,
      openApiApiPackage := "scaleway.ipam.api",
      openApiModelPackage := "scaleway.ipam.models",
      openApiInvokerPackage := "scaleway.ipam"
    )

lazy val `scaleway-key-manager-codegen` =
  (project in file("modules/scaleway-key-manager-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-key-manager-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.key_manager.yml").getPath,
      openApiApiPackage := "scaleway.keymanager.api",
      openApiModelPackage := "scaleway.keymanager.models",
      openApiInvokerPackage := "scaleway.keymanager"
    )
lazy val `scaleway-mongodb-codegen` =
  (project in file("modules/scaleway-mongodb-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-mongodb-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.mongodb.yml").getPath,
      openApiApiPackage := "scaleway.mongodb.api",
      openApiModelPackage := "scaleway.mongodb.models",
      openApiInvokerPackage := "scaleway.mongodb"
    )

lazy val `scaleway-secret-manager-codegen` =
  (project in file("modules/scaleway-secret-manager-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-secret-manager-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.secret_manager.yml").getPath,
      openApiApiPackage := "scaleway.secretmanager.api",
      openApiModelPackage := "scaleway.secretmanager.models",
      openApiInvokerPackage := "scaleway.secretmanager"
    )

lazy val `scaleway-vpc-codegen` =
  (project in file("modules/scaleway-vpc-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-vpc-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc.yml").getPath,
      openApiApiPackage := "scaleway.vpc.api",
      openApiModelPackage := "scaleway.vpc.models",
      openApiInvokerPackage := "scaleway.vpc"
    )

lazy val `scaleway-vpc-gw-codegen` =
  (project in file("modules/scaleway-vpc-gw-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-vpc-gw-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc_gw.yml").getPath,
      openApiApiPackage := "scaleway.vpcgw.api",
      openApiModelPackage := "scaleway.vpcgw.models",
      openApiInvokerPackage := "scaleway.vpcgw"
    )

lazy val `scaleway-instance-codegen` =
  (project in file("modules/scaleway-instance-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-instance-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.instance.yml").getPath,
      openApiApiPackage := "scaleway.instance.api",
      openApiModelPackage := "scaleway.instance.models",
      openApiInvokerPackage := "scaleway.instance"
    )

lazy val `scaleway-kubernetes-codegen` =
  (project in file("modules/scaleway-kubernetes-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-kubernetes-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.kubernetes.yml").getPath,
      openApiApiPackage := "scaleway.kubernetes.api",
      openApiModelPackage := "scaleway.kubernetes.models",
      openApiInvokerPackage := "scaleway.kubernetes"
    )

lazy val `scaleway-kafka-codegen` =
  (project in file("modules/scaleway-kafka-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-kafka-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.kafka.yml").getPath,
      openApiApiPackage := "scaleway.kafka.api",
      openApiModelPackage := "scaleway.kafka.models",
      openApiInvokerPackage := "scaleway.kafka"
    )

lazy val `scaleway-redis-codegen` =
  (project in file("modules/scaleway-redis-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-redis-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.redis.yml").getPath,
      openApiApiPackage := "scaleway.redis.api",
      openApiModelPackage := "scaleway.redis.models",
      openApiInvokerPackage := "scaleway.redis"
    )

lazy val `scaleway-serverless-databases-codegen` =
  (project in file("modules/scaleway-serverless-databases-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-serverless-databases-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.serverless_databases.yml").getPath,
      openApiApiPackage := "scaleway.serverlessdatabases.api",
      openApiModelPackage := "scaleway.serverlessdatabases.models",
      openApiInvokerPackage := "scaleway.serverlessdatabases"
    )

lazy val `scaleway-postgre-mysql-codegen` =
  (project in file("modules/scaleway-postgre-mysql-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-postgre-mysql-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.postgre_mysql.yml").getPath,
      openApiApiPackage := "scaleway.postgremysql.api",
      openApiModelPackage := "scaleway.postgremysql.models",
      openApiInvokerPackage := "scaleway.postgremysql"
    )

lazy val `scaleway-lb-codegen` =
  (project in file("modules/scaleway-lb-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-lb-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.lb.zoned.yml").getPath,
      openApiApiPackage := "scaleway.lb.api",
      openApiModelPackage := "scaleway.lb.models",
      openApiInvokerPackage := "scaleway.lb"
    )

lazy val `scaleway-s2s-vpn-codegen` =
  (project in file("modules/scaleway-s2s-vpn-codegen"))
    .enablePlugins(OpenApiGeneratorPlugin)
    .settings(commonSettings *)
    .settings(
      name := "scaleway-s2s-vpn-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.s2s_vpn.yml").getPath,
      openApiApiPackage := "scaleway.s2svpn.api",
      openApiModelPackage := "scaleway.s2svpn.models",
      openApiInvokerPackage := "scaleway.s2svpn"
    )
