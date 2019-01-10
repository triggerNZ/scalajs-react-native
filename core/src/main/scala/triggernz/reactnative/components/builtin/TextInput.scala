package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.component.Js.{RawMounted, UnmountedWithRawType}
import japgolly.scalajs.react.{Callback, CallbackKleisli, Children, JsComponent}
import triggernz.reactnative.components.builtin.View.Props.Style

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object TextInput {
  @JSImport("react-native", "TextInput")
  @js.native
  object RawComponent extends js.Object

  @js.native
  trait JsProps extends js.Object {
    var allowFontScaling: Boolean
    var autoCapitalize: String
    var onChangeText: js.UndefOr[js.Function1[String, Unit]]
    var secureTextEntry: Boolean
    var style: js.UndefOr[View.JsStyle]
    var value: String
  }

  case class Props(
                    allowFontScaling: Boolean = true,
                    autoCapitalize: AutoCapitalize = AutoCapitalize.None,
                    onChangeText: Option[String => Callback] = None,
                    secureTextEntry: Boolean = false,
                    style: Option[Style] = None,
                    value: String = "") {
    def toJs: JsProps = {
      val j = (new js.Object).asInstanceOf[JsProps]
      j.allowFontScaling = true
      j.autoCapitalize = autoCapitalize.str
      onChangeText.foreach(f => j.onChangeText = CallbackKleisli(f).toJsFn)
      j.secureTextEntry = secureTextEntry
      style.foreach(s => j.style = s.toJs)
      j.value = value
      j
    }
  }

  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)

  sealed abstract class AutoCapitalize(val str: String)
  object AutoCapitalize {
    case object None extends AutoCapitalize("none")
    case object Sentences extends AutoCapitalize("sentences")
    case object Words extends AutoCapitalize("words")
    case object Characters extends AutoCapitalize("characters")
  }

  def apply(): UnmountedWithRawType[JsProps, Null, RawMounted[JsProps, Null]] =
    apply(Props())

  def apply(props: Props): UnmountedWithRawType[JsProps, Null, RawMounted[JsProps, Null]] =
    Component(props)
}
