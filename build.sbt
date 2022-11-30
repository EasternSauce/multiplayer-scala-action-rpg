ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "networking_test"
  )

resolvers += "clojars" at "https://clojars.org/repo/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "jitpack" at "https://jitpack.io"


libraryDependencies += "kryonet" % "kryonet" % "2.21-2"

libraryDependencies += "com.badlogicgames.gdx" % "gdx"                % "1.11.0"
libraryDependencies += "com.badlogicgames.gdx" % "gdx-box2d"          % "1.11.0"
libraryDependencies += "com.badlogicgames.gdx" % "gdx-backend-lwjgl3" % "1.11.0"
libraryDependencies += "com.badlogicgames.gdx" % "gdx-freetype"       % "1.11.0"

libraryDependencies += "com.badlogicgames.gdx" % "gdx-platform"          % "1.11.0" classifier "natives-desktop"
libraryDependencies += "com.badlogicgames.gdx" % "gdx-box2d-platform"    % "1.11.0" classifier "natives-desktop"
libraryDependencies += "com.badlogicgames.gdx" % "gdx-freetype-platform" % "1.11.0" classifier "natives-desktop"

libraryDependencies += "com.badlogicgames.gdx" % "gdx-backend-android" % "1.11.0"
libraryDependencies += "com.twitter" %% "chill" % "0.10.0"

libraryDependencies += "space.earlygrey" % "shapedrawer" % "2.4.0"

libraryDependencies += "com.softwaremill.quicklens" %% "quicklens" % "1.9.0"
