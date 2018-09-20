name := "scalajs-react-native"

enablePlugins(ScalaJSPlugin)

enablePlugins(ScalaJSBundlerPlugin)

npmDependencies in Compile ++= Seq(
    "react" -> "16.5.1",
    //"react-dom" -> "16.5.1",
    "react-native" -> "0.57.0")


libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.3.1-SNAPSHOT"

