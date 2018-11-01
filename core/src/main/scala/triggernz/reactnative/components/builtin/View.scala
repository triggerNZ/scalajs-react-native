package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Children, JsComponent}
import japgolly.scalajs.react.vdom.VdomNode

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object View {
  @JSImport("react-native", "View")
  @js.native
  object RawComponent extends js.Object

  val Js = JsComponent[JsProps, Children.Varargs, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)

  @js.native
  trait JsProps extends js.Object {
  }

  case class Props() {
    def toJs: JsProps = {
      new js.Object().asInstanceOf[JsProps]
    }
  }

  def apply(p: Props)(children: VdomNode*) = Component(p)(children: _*)
}