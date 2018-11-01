package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Children, JsComponent}
import japgolly.scalajs.react.vdom.VdomNode
import org.scalajs.dom.ext.Color

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Text {
  @JSImport("react-native", "Text")
  @js.native
  object RawComponent extends js.Object

  val Js = JsComponent[JsProps, Children.Varargs, Null](RawComponent)
  val Component = Js.cmapCtorProps[Props](_.toJs)

  case class Props(style: Style) {
    def toJs: JsProps = {
      val p = new js.Object().asInstanceOf[JsProps]
      p.style = style.toJs
      p
    }
  }

  @js.native
  trait JsProps extends js.Object {
    var style: JsStyle = js.native
  }

  case class Style(color: Color = Color.Black, fontWeight: FontWeight = FontWeight.Normal, fontSize: Int = 10) {
    def toJs: JsStyle = {
      val s = new js.Object().asInstanceOf[JsStyle]
      s.color = color.toString()
      s.fontWeight = fontWeight.toJs
      s.fontSize = fontSize
      s
    }
  }

  @js.native
  trait JsStyle extends js.Object {
    var color: String
    var fontWeight: String
    var fontSize: Int
  }
  def apply(p: Props)(children: VdomNode*) = Component(p)(children: _*)
}