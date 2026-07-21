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
      sttpJsoniter,
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
      jsoniter,
      jsoniterMacros,
      jsoniterCirce,
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
    `scaleway-vpc-gw-codegen` % "compile->compile;test->test"
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
    `scaleway-vpc-gw-codegen`
  )

val commonSettings = Seq(
  // Generated code uses indentation syntax and is not held to our lint flags
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

  // Generated sources are committed under src/main/scala; regenerate with the `generate` task
  openApiOutputDir := ((Compile / baseDirectory).value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests := SettingDisabled,
  // Scaleway specs do not pass the generator's validator
  openApiValidateSpec := SettingDisabled,

  // Regenerate the client from the spec: clear the previous output so
  // renamed/removed files don't linger, then run the generator. The
  // sbt/project scaffolding it would otherwise emit alongside the sources
  // is suppressed via the module-local .openapi-generator-ignore instead
  // of being generated and then deleted.
  //
  // Must stay uncached. sbt 2 caches `:=` task results by default, but the
  // cache key is built from the task's `.value` inputs, and nothing here
  // hashes the OpenAPI spec's *contents* -- sbt's own file-input keys
  // (allInputFiles / changedInputFiles) are @transient, i.e. deliberately
  // excluded from cache input. A cached task would therefore keep serving a
  // stale client whenever the spec changed, so we always regenerate.
  generate := Def.uncached {
    Def
      .sequential(
        Def.task {
          val packageDir = openApiInvokerPackage.value.split('.').foldLeft(file(openApiOutputDir.value))(_ / _)
          IO.delete(packageDir)
        },
        openApiGenerate
      )
      .value
  },
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
      openApiInputSpec := (baseDirectory.value / "scaleway.autoscaling.v1alpha1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.containers.v1beta1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.iam.v1alpha1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.ipam.v1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.key_manager.v1alpha1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.mongodb.v1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.secret_manager.v1beta1.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc.v2.Api.yml").getPath,
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
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc_gw.v2.Api.yml").getPath,
      openApiApiPackage := "scaleway.vpcgw.api",
      openApiModelPackage := "scaleway.vpcgw.models",
      openApiInvokerPackage := "scaleway.vpcgw"
    )
