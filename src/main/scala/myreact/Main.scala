package myreact

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import scala.scalajs.js
import scala.scalajs.js.annotation._

object Main {
  // Say this is the Scala component you want to share
  val scalaNativeComponent = ScalaComponent.builder[String]("")
    .render_P(_ =>  ActivityIndicatorComponent())
    .build

  // This will be the props object used from JS-land
  @js.native
  trait JsProps extends js.Object {
    val name: String
  }

  @JSExportTopLevel("ScalaNativeComponent")
  val eportedComponent =
    scalaNativeComponent
      .cmapCtorProps[JsProps](_.name) // Change props from JS to Scala
      .toJsComponent // Create a new, real JS component
      .raw // Leave the nice Scala wrappers behind and obtain the underlying JS value


  @JSImport("react-native", "ActivityIndicator")
  @js.native
  object ActivityIndicatorJs extends js.Object

  val ActivityIndicatorComponent = JsComponent[Null, Children.None, Null](ActivityIndicatorJs)

}