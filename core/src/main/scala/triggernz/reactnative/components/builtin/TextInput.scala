package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Children, JsComponent}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object TextInput {
  @JSImport("react-native", "View")
  @js.native
  object RawComponent extends js.Object

  @js.native
  trait JsProps extends js.Object {
    var allowFontScaling: Boolean
  }

  case class Props(allowFontScaling: Boolean = true) {
    def toJs: JsProps = {
      val j = (new js.Object).asInstanceOf[JsProps]
      j.allowFontScaling = true
      j
    }
  }

  val Js = JsComponent[JsProps, Children.Varargs, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)
}
