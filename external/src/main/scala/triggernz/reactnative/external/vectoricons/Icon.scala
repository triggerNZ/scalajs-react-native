package triggernz.reactnative.external.vectoricons

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import japgolly.scalajs.react.{Children, JsComponent}
import org.scalajs.dom.ext.Color

object Icon {
  case class Props(name: String, size: Int = 12, color: Option[Color] = None) {
    def toJs: JsProps = {
      val jso = new js.Object().asInstanceOf[JsProps]
      color.foreach(c => jso.color = c.toString())
      jso.name = name
      jso.size = size
      jso
    }
  }

  @js.native
  trait JsProps extends js.Object{
    var name: String
    var size: Int
    var color: String
  }

  @JSImport("react-native-vector-icons/FontAwesome", JSImport.Default)
  @js.native
  object RawComponent extends js.Object


  val Js = JsComponent[JsProps, Children.None, Null](RawComponent)

  val Component = Js.cmapCtorProps[Props](_.toJs)

  def apply(props: Props) = Component(props)
}
