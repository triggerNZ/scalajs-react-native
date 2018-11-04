package triggernz.reactnative
package demo

import japgolly.scalajs.react._
import org.scalajs.dom.ext.Color

import scala.scalajs.js.annotation._
import japgolly.scalajs.react.vdom.PackageBase._
import components.builtin._
import apis.builtin.Geolocation
import cats.effect.IO

import apis.builtin.{AlertIos, Geolocation, PermissionsAndroid}
import triggernz.reactnative.core.Platform
import triggernz.reactnative.core.Platform.RunningPlatform
import triggernz.reactnative.external.vectoricons.Icon


import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class Main(platform: RunningPlatform) {
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
        Text(Text.Props(Text.Style()))(s.text.fold("fetching")(s => s)),
        Text(Text.Props(Text.Style()))(s"Platform is ${platform}"),
        Button(Button.Props("Show IOS alert", Color.Red, AlertIos.alert("Hello, World", None).toCallbackOrEmpty(platform))),
        Icon(Icon.Props("rocket", size = 30, color = Some(Color("#900")))),
        Text(Text.Props())(s.text.fold("fetching")(s => s))
      )
    }

    def startGps: Callback =
      Geolocation.watchPosition(pos => $.modState(_.copy(position = Some(pos)))).ret(())

    def requestPermissions: Callback =
      PermissionsAndroid.request("android.permission.ACCESS_FINE_LOCATION").toCallback(platform, Callback.empty)

    def fetchJson: Callback = Callback {
      import hammock._
      import hammock.fetch.Interpreter

      implicit val interpretexr = Interpreter[IO](Interpreter.BrowserFetch)

      Hammock
        .request(Method.GET, uri"https://jsonplaceholder.typicode.com/todos/1", Map.empty)
        .exec[IO].unsafeToFuture().onComplete {
          case Success(r) => $.modState(_.copy(text = Some(r.entity.content.toString))).runNow()
          case Failure(f) => $.modState(_.copy(text = Some(f.getMessage))).runNow()
        }
    }
  }

  val scalaNativeApp = ScalaComponent.builder[Unit]("App")
    .initialState(State(true, None, None))
    .renderBackend[Backend]
    .componentDidMount {c =>
      c.backend.requestPermissions >>
        c.backend.startGps >>
        c.backend.fetchJson
    }
    .build




}

object Main {
  val platform = Platform.get.getOrElse(sys.error("Failed reading platform"))
  val main = new Main(platform)

  @JSExportTopLevel("App")
  val app =
    main.scalaNativeApp
      .toJsComponent
      .raw
}