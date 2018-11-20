package triggernz.reactnative.components.builtin

import japgolly.scalajs.react.{Children, JsComponent}
import japgolly.scalajs.react.vdom.VdomNode
import org.scalajs.dom.ext.Color
import triggernz.reactnative.components.dimensions.DimValue
import triggernz.reactnative.flex.{Direction, Flex}

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
  }


  case class Props(style: Option[Props.Style] = None) {
    def toJs: JsProps = {
      val jso = new js.Object().asInstanceOf[JsProps]
      style.foreach { s =>
        val jss = new js.Object().asInstanceOf[JsStyle]
        s.height.foreach(h => jss.height = h.toReactNativeStyle)
        s.width.foreach(w => jss.width = w.toReactNativeStyle)
        s.backgroundColor.foreach(c => jss.backgroundColor = c.toString)
        s.flex.foreach(f => jss.flex = f.n)
        s.flexDirection.foreach(fd => jss.flexDirection = fd.str)
        s.padding.foreach(p => jss.padding = p.toReactNativeStyle)
        s.margin.foreach(m => jss.margin = m.toReactNativeStyle)
        jso.style = jss
      }
      jso
    }
  }

  object Props {
    case class Style(
                      width: Option[DimValue] = None,
                      height: Option[DimValue] = None,
                      backgroundColor: Option[Color] = None,
                      flex: Option[Flex] = None,
                      flexDirection: Option[Direction] = None,
                      padding: Option[DimValue] = None,
                      margin: Option[DimValue] = None
                    )
  }

  def apply(p: Props)(children: VdomNode*) = Component(p)(children: _*)
}