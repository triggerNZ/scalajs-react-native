package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Callback, Children, JsComponent}
import org.scalajs.dom.ext.Color

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object Button {
  @JSImport("react-native", "Button")
  @js.native
  object RawComponent extends js.Object

  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)

  @js.native
  trait JsProps extends js.Object{
    var title: String = js.native
    var color: String = js.native
    var onPress: js.Function0[Unit] = js.native
  }

  case class Props(title: String, color: Color, onPress: Callback) {
    def toJs: JsProps = {
      val p = (new js.Object).asInstanceOf[JsProps]
      p.title = title
      p.color = color.toString()
      p.onPress = onPress.toJsFn
      p
    }
  }

  def apply(p: Props) = Component(p)
}