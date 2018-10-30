package triggernz.reactnative.core

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|
import scala.util.Try

sealed trait Platform {
  import Platform._
  def select[A](onIos: => A, onAndroid: => A): A = this match {
    case _: Ios => onIos
    case _: Android => onAndroid
  }

  def onIos(block: => Unit) = select(block, ())
  def onAndroid(block: => Unit) = select((), block)
}
object Platform {

  case class Ios(version: String) extends Platform

  case class Android(version: Int) extends Platform

  @JSImport("react-native", "Platform")
  @js.native
  object Raw extends js.Object {
    val Version: String | Int = js.native
    val OS: String = js.native
  }

  lazy val get: Option[Platform] = Try {
    Raw.OS match {
      case "ios" => Ios(Raw.Version.asInstanceOf[String])
      case "android" => Android(Raw.Version.asInstanceOf[Int])
    }
  }.toOption
}