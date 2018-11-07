package triggernz.reactnative.components.builtin

//import java.net.URI

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react._
import triggernz.reactnative.components.builtin.Image.Source.FromRequire
object Image {
  sealed trait Source
  object Source {
    case class FromRequire(o: js.Object) extends Source
  }
//  case class Size(width: Int, height: Int)
  case class Props(source: Source) {
    def toJs: js.Object = {
      val src = source match {
        case FromRequire(o) => o
      }
      js.Dynamic.literal(source = src)
    }

  }

  type JsProps = js.Object

  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)
  val Component = Js.cmapCtorProps[Props](_.toJs)


  @JSImport("react-native", "Image")
  @js.native
  object RawComponent extends js.Object

  def apply(p: Props) = Component(p)
}
