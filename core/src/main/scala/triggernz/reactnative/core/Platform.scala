package triggernz.reactnative.core

import japgolly.scalajs.react._
import triggernz.reactnative.core.Platform.RunningPlatform

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|
import scala.util.Try

sealed trait Platform {
  import Platform._
  def select[A](onIos: => A, onAndroid: => A): A = this match {
    case Ios => onIos
    case Android => onAndroid
  }

  def onIos(block: => Unit) = select(block, ())
  def onAndroid(block: => Unit) = select((), block)
}
object Platform {

  case object Ios extends Platform
  case object Android extends Platform



  @JSImport("react-native", "Platform")
  @js.native
  object Raw extends js.Object {
    val Version: String | Int = js.native
    val OS: String = js.native
  }

  lazy val get: Option[RunningPlatform] = Try {
    Raw.OS match {
      case "ios" => Ios
      case "android" => Android
    }
  }.toOption.map(new RunningPlatform(_))

  class RunningPlatform private[Platform] (val p: Platform)
}

class PlatformIO[P <: Platform, A]private (private val platform: P)(private val thunk: () => A) {
  def toCallback(running: RunningPlatform, onOtherPlatforms: CallbackTo[A]): CallbackTo[A] =
    if (running.p == platform)
      CallbackTo.lift(thunk)
    else
      onOtherPlatforms

  def flatMap[B](fn: A => PlatformIO[P, B]) = new PlatformIO[P, B](platform)(fn(thunk()).thunk)
  def map[B](fn: A => B) = new PlatformIO[P, B](platform)(() => fn(thunk()))
}

class PlatformIoUnitExt[P <: Platform](val self: PlatformIO[P, Unit]) extends AnyVal {
  def toCallbackOrEmpty(running: RunningPlatform) = self.toCallback(running, Callback.empty)
}

object PlatformIO {
  type AndroidIO[A] = PlatformIO[Platform.Android.type, A]
  type IosIO[A] = PlatformIO[Platform.Ios.type, A]

  def AndroidIO[A](thunk: => A) = new PlatformIO(Platform.Android)(() => thunk)
  def IosIO[A](thunk: => A) = new PlatformIO(Platform.Ios)(() => thunk)

  implicit def ioUnitExt[P <: Platform](self: PlatformIO[P, Unit]) = new PlatformIoUnitExt[P](self)
}
