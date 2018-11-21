package triggernz.reactnative
package demo

import java.net.URI

import japgolly.scalajs.react._
import org.scalajs.dom.ext.Color

import scala.scalajs.js.annotation._
import japgolly.scalajs.react.vdom.PackageBase._
import components.builtin._
import cats.effect.IO
import apis.builtin.{AlertIos, PermissionsAndroid}
import org.scalajs.dom.Position
import org.scalajs.dom.raw.PositionError
import scalaz.{-\/, \/, \/-}
import triggernz.reactnative.core.ContT._
import triggernz.reactnative.core.Platform
import triggernz.reactnative.core.Platform.RunningPlatform
import triggernz.reactnative.demo.PosError.RawError
import triggernz.reactnative.external.vectoricons.Icon

import scalajs.js
import triggernz.reactnative.components.dimensions.DimValue._
import triggernz.reactnative.external.geolocationservice.GeolocationService

@js.native
@JSImport("./images/scala.jpeg", JSImport.Namespace)
object scalaImage extends js.Object

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
        Text(Text.Props())(s.text.fold("fetching")(s => s)),
        Image(Image.Props(Image.Source.FromRequire(scalaImage))),
        Image(Image.Props(Image.Source.FromUri(new URI("https://s3-ap-southeast-2.amazonaws.com/profile.ppets/1395/large/203331042656ca0dd2014403.76919395-profile-1395.gif"), 345.px, 350.px)))

      )
    }

    def startGps = watchLocation.map(\/.fromEither) {
      case -\/(RawError(e)) => println(s"error: ${e.message}"); $.modState(_.copy(position = None))
      case -\/(PosError.NoPermission) => println(s"No location permissions!"); $.modState(_.copy(position = None))
      case \/-(p) => $.modState(_.copy(position = Some(p)))
    }

    def fetchJson: Callback = {
      import hammock._
      import hammock.fetch.Interpreter

      implicit val interpretexr = Interpreter[IO](Interpreter.BrowserFetch)

      AsyncE.fromIO(Hammock
        .request(Method.GET, uri"https://jsonplaceholder.typicode.com/todos/1", Map.empty)
        .exec[IO]) {
          case Right(r) => $.modState(_.copy(text = Some(r.entity.content.toString)))
          case Left(f) => $.modState(_.copy(text = Some(f.getMessage)))
        }
    }
  }

  def watchLocation: AsyncE[PosError, Position] = {
    import cats.syntax.either._
    for {
      p <- PermissionsAndroid.request(PermissionsAndroid.Permission.AccessFineLocation)
      loc <-
        if (p == PermissionsAndroid.Result.Granted)
          GeolocationService().watchPosition().map(_.leftMap(PosError.RawError)).voidR
        else
          Async.point(Left(PosError.NoPermission))
    } yield loc
  }


  val scalaNativeApp = ScalaComponent.builder[Unit]("App")
    .initialState(State(true, None, None))
    .renderBackend[Backend]
    .componentDidMount {c =>
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

sealed trait PosError
object PosError {
  case object NoPermission extends PosError
  case class RawError(e: PositionError) extends PosError
}