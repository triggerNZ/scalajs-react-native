package triggernz.reactnative.apis.builtin

import japgolly.scalajs.react.{Callback, CallbackTo}
import org.scalajs.dom._
import scalajs.js

object Geolocation {
  case class Options() {
    def toJs: PositionOptions = {
      val po = (new js.Object).asInstanceOf[PositionOptions]
      po
    }
  }

  type WatchId = Int
  private val raw = window.navigator.geolocation

  def watchPosition(
                     success: Position => Callback,
                     error: PositionError => Callback = Function.const(Callback.empty),
                     opts: Options = Options()): CallbackTo[WatchId] = CallbackTo {
    raw.watchPosition(p => success(p).runNow(), e => error(e).runNow(), opts.toJs)
  }

  def clearWatch(id: WatchId): Callback = Callback {
    raw.clearWatch(id)
  }

  def getCurrentPosition(
                     success: Position => Callback,
                     error: PositionError => Callback = Function.const(Callback.empty),
                     opts: Options = Options()): Callback = Callback {
    raw.getCurrentPosition(p => success(p).runNow(), e => error(e).runNow(), opts.toJs)
  }
}
