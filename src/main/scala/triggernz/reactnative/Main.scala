package triggernz.reactnative

import japgolly.scalajs.react._
import org.scalajs.dom.ext.Color

import scala.scalajs.js.annotation._
import japgolly.scalajs.react.vdom.PackageBase._
import components.builtin._
import apis.builtin.Geolocation

object Main {
  case class State(spinning: Boolean, position: Option[org.scalajs.dom.Position]) {
    def toggleSpin: State = copy(spinning = !spinning)
  }

  class Backend($: BackendScope[Unit, State]) {
    def render(s: State) = {
      val title = if (s.spinning) "Stop spinning" else "Start spinning"
      val colour = if (s.spinning) Color.Blue else Color.Red

      View(View.Props())(
        Button(Button.Props(title, colour, $.modState(_.toggleSpin))),
        ActivityIndicator(ActivityIndicator.Props(animating = s.spinning, size = ActivityIndicator.Size.Large)),
        Text(Text.Props(Text.Style(color = Color.Green, fontSize = 34)))("I am green, and big", Text(Text.Props(Text.Style(color = Color.Blue, fontWeight = FontWeight.Bold)))(" but I am nested and bold and blue")),
        Text(Text.Props(Text.Style()))(s.position.fold("No location found") {pos => s"Location is ${pos.coords.latitude}, ${pos.coords.longitude}"})
      )
    }

    def startGps: Callback =
      Geolocation.watchPosition(pos => $.modState(_.copy(position = Some(pos)))).ret(())

    def stopGps = Callback {
    }
  }

  val scalaNativeApp = ScalaComponent.builder[Unit]("App")
    .initialState(State(true, None))
    .renderBackend[Backend]
    .componentDidMount(_.backend.startGps)
    .componentWillUnmount(_.backend.stopGps)
    .build


  @JSExportTopLevel("App")
  val app =
    scalaNativeApp
      .toJsComponent
      .raw

}

