package myreact

import japgolly.scalajs.react._
import org.scalajs.dom.ext.Color

import scala.scalajs.js.annotation._
import NativeComponents._

import japgolly.scalajs.react.vdom.PackageBase._


object Main {
  class Backend($: BackendScope[Unit, Boolean]) {
    def render(s: Boolean) = {
      val title = if (s) "Stop spinning" else "Start spinning"
      val colour = if (s) Color.Blue else Color.Red

      View(View.Props())(
        Button(Button.Props(title, colour, $.modState(!_))),
        ActivityIndicator(ActivityIndicator.Props(animating = s, size = ActivityIndicator.Size.Large)),
        Text(Text.Props(Text.Style(color = Color.Green, fontSize = 14)))("I am green, and big", Text(Text.Props(Text.Style(color = Color.Blue, fontWeight = FontWeight.Bold)))(" but I am nested and bold and blue"))
      )
    }

  }

  val scalaNativeApp = ScalaComponent.builder[Unit]("App")
    .initialState(true)
    .renderBackend[Backend]
    .build


  @JSExportTopLevel("App")
  val app =
    scalaNativeApp
      .toJsComponent
      .raw

}

