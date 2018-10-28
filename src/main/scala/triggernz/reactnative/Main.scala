package triggernz.reactnative

import japgolly.scalajs.react._
import org.scalajs.dom.ext.Color

import scala.scalajs.js.annotation._
import japgolly.scalajs.react.vdom.PackageBase._
import components.builtin._
import apis.builtin.{Fetch, Geolocation}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.JSON

object Main {
  case class State(spinning: Boolean, position: Option[org.scalajs.dom.Position], text: Option[String]) {
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
        Text(Text.Props(Text.Style()))(s.position.fold("No location found") {pos => s"Location is ${pos.coords.latitude}, ${pos.coords.longitude}"}),
        Text(Text.Props(Text.Style()))(s.text.fold("fetching")(s => s))
      )
    }

    def startGps: Callback =
      Geolocation.watchPosition(pos => $.modState(_.copy(position = Some(pos)))).ret(())

    def fetchJson: Callback =
      Fetch.fetch("https://jsonplaceholder.typicode.com/todos/1").map {
        f => f.flatMap(_.json().toFuture).map(JSON.stringify(_)).onComplete {
          case Success(r) => $.modState(_.copy(text = Some(r.toString))).runNow()
          case Failure(f) => $.modState(_.copy(text = Some(f.getMessage))).runNow()
        }
      }
  }

  val scalaNativeApp = ScalaComponent.builder[Unit]("App")
    .initialState(State(true, None, None))
    .renderBackend[Backend]
    .componentDidMount(c => c.backend.startGps >> c.backend.fetchJson)
    .build


  @JSExportTopLevel("App")
  val app =
    scalaNativeApp
      .toJsComponent
      .raw

}

