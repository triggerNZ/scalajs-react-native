package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.component.Js.{RawMounted, UnmountedWithRawType}
import japgolly.scalajs.react.{Children, JsComponent}
import japgolly.scalajs.react.vdom.VdomNode
import org.scalajs.dom.ext.Color
import triggernz.reactnative.components.dimensions.DimValue
import triggernz.reactnative.flex.{AlignItems, Direction, Flex, JustifyContent}

import scala.scalajs.js
import scala.scalajs.js.{UndefOr, |}
import scala.scalajs.js.annotation.JSImport

object View {
  @JSImport("react-native", "View")
  @js.native
  object RawComponent extends js.Object

  val Js = JsComponent[JsProps, Children.Varargs, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)

  @js.native
  trait JsProps extends js.Object {
    var style : UndefOr[JsStyle]
  }

  @js.native
  trait JsStyle extends js.Object {
    var height: UndefOr[Int | String]
    var width: UndefOr[Int | String]
    var backgroundColor: UndefOr[String]
    var flex: UndefOr[Int]
    var flexDirection: UndefOr[String]
    var padding: UndefOr[Int | String]
    var margin: UndefOr[Int | String]
    var justifyContent: UndefOr[String]
    var alignItems: UndefOr[String]
    var borderColor: UndefOr[String]
    var borderWidth: UndefOr[Int]
  }


  case class Props(style: Option[Props.Style] = None) {
    def toJs: JsProps = {
      val jso = new js.Object().asInstanceOf[JsProps]
      style.foreach { s =>
        jso.style = s.toJs
      }
      jso
    }
  }

  object Props {
    case class Border(width: Int, color: Color)

    case class Style(
                      width: Option[DimValue] = None,
                      height: Option[DimValue] = None,
                      backgroundColor: Option[Color] = None,
                      flex: Option[Flex] = None,
                      flexDirection: Option[Direction] = None,
                      padding: Option[DimValue] = None,
                      margin: Option[DimValue] = None,
                      justifyContent: Option[JustifyContent] = None,
                      alignItems: Option[AlignItems] = None,
                      border: Option[Border] = None
                    ) {
      def toJs: JsStyle = {
        val jss = new js.Object().asInstanceOf[JsStyle]
        height.foreach(h => jss.height = h.toReactNativeStyle)
        width.foreach(w => jss.width = w.toReactNativeStyle)
        backgroundColor.foreach(c => jss.backgroundColor = c.toString)
        flex.foreach(f => jss.flex = f.n)
        flexDirection.foreach(fd => jss.flexDirection = fd.str)
        padding.foreach(p => jss.padding = p.toReactNativeStyle)
        margin.foreach(m => jss.margin = m.toReactNativeStyle)
        justifyContent.foreach(jc => jss.justifyContent = jc.str)
        alignItems.foreach(ai => jss.alignItems = ai.str)
        border.foreach { b =>
          jss.borderWidth = b.width
          jss.borderColor = b.color.toString()
        }
        jss
      }
    }
  }

  def apply(children: VdomNode*): UnmountedWithRawType[JsProps, Null, RawMounted[JsProps, Null]] =
    apply(Props())(children: _*)

  def apply(p: Props)(children: VdomNode*): UnmountedWithRawType[JsProps, Null, RawMounted[JsProps, Null]] =
    Component(p)(children: _*)
}