import Dependencies._

ThisBuild / scalaVersion := "3.3.8"
ThisBuild / version := "0.1.0-SNAPSHOT"

Global / onChangedBuildSource := ReloadOnSourceChanges

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
)

lazy val root = (project in file("."))
  .settings(
    name := "scaleway",
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
  // .dependsOn(`scaleway-codegen` % "compile->compile")
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

  // Generated sources are committed under src/main/scala; regenerate with the `generate` task
  openApiOutputDir := ((Compile / baseDirectory).value / "src/main/scala").getAbsolutePath,
  openApiGenerateModelTests := SettingDisabled,
  openApiGenerateApiTests := SettingDisabled,
  // Scaleway specs do not pass the generator's validator
  openApiValidateSpec := SettingDisabled,

  // Regenerate the client from the spec: clear the previous output so
  // renamed/removed files don't linger, run the generator, then strip
  // the sbt/project scaffolding it emits alongside the sources
  generate := Def
    .sequential(
      Def.task {
        val packageDir = openApiInvokerPackage.value.split('.').foldLeft(file(openApiOutputDir.value))(_ / _)
        IO.delete(packageDir)
      },
      openApiGenerate,
      Def.task {
        val outputDir = file(openApiOutputDir.value)
        val scaffolding = Seq(
          outputDir / "build.sbt",
          outputDir / "project",
          outputDir / "README.md",
          outputDir / ".scalafmt.conf",
          outputDir / ".openapi-generator",
          outputDir / ".openapi-generator-ignore",
          outputDir / ".gitignore"
        )
        IO.delete(scaffolding)
      }
    )
    .value,
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
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
    .settings(commonSettings: _*)
    .settings(
      name := "scaleway-vpc-gw-codegen",
      openApiInputSpec := (baseDirectory.value / "scaleway.vpc_gw.v2.Api.yml").getPath,
      openApiApiPackage := "scaleway.vpcgw.api",
      openApiModelPackage := "scaleway.vpcgw.models",
      openApiInvokerPackage := "scaleway.vpcgw"
    )
