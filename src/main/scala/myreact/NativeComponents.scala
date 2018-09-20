package myreact

import org.scalajs.dom.ext.Color

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.VdomNode


object NativeComponents {
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

  sealed trait FontWeight {
    val toJs: String
  }

  object FontWeight {
    case object Normal extends FontWeight { val toJs = "normal" }
    case object Bold extends FontWeight { val toJs = "bold" }
  }
}
