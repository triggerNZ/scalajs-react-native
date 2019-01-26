package triggernz.reactnative.shoutem.components

import japgolly.scalajs.react.{Children, JsComponent}
import scala.scalajs.js

trait TypographyComponent {
  val RawComponent: js.Object

  type JsProps = triggernz.reactnative.components.builtin.Text.JsProps
  type Props = triggernz.reactnative.components.builtin.Text.Props


  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)
}
