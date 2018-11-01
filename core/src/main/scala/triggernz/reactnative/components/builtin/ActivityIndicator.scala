package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Children, JsComponent}
import org.scalajs.dom.ext.Color

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport


object ActivityIndicator {
  @JSImport("react-native", "ActivityIndicator")
  @js.native
  object RawComponent extends js.Object

  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)


  case class Props(animating: Boolean = true, color: Color = Color(100, 100, 100), size: Size = Size.Small) {
    def toJs: JsProps = {
      val p = (new js.Object).asInstanceOf[JsProps]
      p.color = color.toString
      p.size = size.toJs
      p.animating = animating
      p
    }
  }

  @js.native
  trait JsProps extends js.Object{
    var size: String = js.native
    var color: String = js.native
    var animating: Boolean = js.native
  }

  sealed trait Size {
    val toJs: String
  }
  object Size {
    case object Small extends Size {val toJs = "small"}
    case object Large extends Size {val toJs = "large"}
  }

  def apply(p: Props) = Component(p)
}