ThisBuild / scalaVersion := "3.3.1"

ThisBuild / tlBaseVersion := "0.1"

ThisBuild / organization := "com.scalawithcats"
ThisBuild / organizationName := "Scala with Cats"

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision

Global / onChangedBuildSource := ReloadOnSourceChanges

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.10.0",
    "org.typelevel" %% "cats-effect" % "3.5.2",
    "org.scalameta" %% "munit" % "0.7.29" % Test,
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  )
)

// Run this command (build) to do everything involved in building the project
commands += Command.command("build") { state =>
  "clean" ::
    "compile" ::
    "test" ::
    "scalafixAll" ::
    "scalafmtAll" ::
    "headerCreateAll" ::
    "githubWorkflowGenerate" ::
    "dependencyUpdates" ::
    "reload plugins; dependencyUpdates; reload return" ::
    state
}
lazy val root = project.in(file(".")).aggregate(core, benchmarks)

lazy val core = project.in(file("core")).settings(commonSettings)

lazy val benchmarks = project
  .in(file("benchmarks"))
  .settings(
    javaOptions ++= Seq(
      "-XX:+UnlockExperimentalVMOptions",
      "-XX:+UnlockDiagnosticVMOptions",
      // Disable C1 compiler, making logs easier to read
      "-XX:-TieredCompilation"
      // Print all assembly
      // You'll need to install hsdis, available from https://chriswhocodes.com/hsdis/
      // "-XX:+PrintAssembly",
      // "-XX:+DebugNonSafepoints",
      // "-Xlog:class+load=info",
      // The log file produced by this can be viewed with JITWatch: https://github.com/AdoptOpenJDK/jitwatch
      // "-XX:+LogCompilation",
      // Print assembly only for specific methods
      // "-XX:CompileCommand=print,arithmetic/DirectThreaded$Expression.eval*",
      // "-XX:CompileCommand=print,arithmetic/DirectThreaded$StackMachine.eval*",
      // "-XX:CompileCommand=print,arithmetic/DirectThreaded$StackMachine.trampoline*",
      // "-XX:CompileCommand=print,arithmetic/DirectThreaded$StackMachine$Op.apply*"
    )
  )
  .enablePlugins(JmhPlugin)
  .dependsOn(core)
