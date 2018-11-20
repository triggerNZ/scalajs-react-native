package triggernz.reactnative.components.builtin

//import java.net.URI

import java.net.URI

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react._
import triggernz.reactnative.components.dimensions.DimValue

import scala.scalajs.js.{|}
object Image {
  sealed trait Source
  object Source {
    case class FromRequire(o: js.Object) extends Source
    case class FromUri(uri: URI, width: DimValue, height: DimValue) extends Source
  }

  case class Props(source: Source) {
    def toJs: js.Object = {
      val src = source match {
        case Source.FromRequire(src) =>
          val o = new js.Object().asInstanceOf[JsProps]
          o.source = src.asInstanceOf[JsSource] // this is a lie
          o
        case Source.FromUri(uri, w, h) =>
          val o = new js.Object().asInstanceOf[JsProps]
          o.source = new js.Object().asInstanceOf[JsSource]
          o.source.uri = uri.toASCIIString

          o.style = new js.Object().asInstanceOf[JsStyle]
          o.style.height = h.toReactNativeStyle
          o.style.width = w.toReactNativeStyle
          o
      }
      src
    }
  }

  @js.native
  trait JsProps extends js.Object{
    var source: JsSource
    var style: JsStyle
  }
  @js.native
  trait JsSource extends js.Object {
    var uri: String
  }

  @js.native
  trait JsStyle extends js.Object {
    var width: Int | String
    var height: Int | String
  }

  val Js = JsComponent[js.Object, Children.None, Null](RawComponent)
  val Component = Js.cmapCtorProps[Props](_.toJs)


  @JSImport("react-native", "Image")
  @js.native
  object RawComponent extends js.Object

  def apply(p: Props) = Component(p)
}
