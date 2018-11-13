package triggernz.reactnative.apis.builtin

import japgolly.scalajs.react.{Callback, CallbackKleisli, CallbackTo}
import org.scalajs.dom._
import triggernz.reactnative.core.ContT.{AsyncE, AsyncRE}

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

  def watchPosition(opts: Options = Options()): AsyncRE[PositionError, Position, WatchId] = AsyncRE { handler =>
    val k = CallbackKleisli(handler)
    CallbackTo (raw.watchPosition(
      k.contramap[Position](Right(_)).toJsFn,
      k.contramap[PositionError](Left(_)).toJsFn,
      opts.toJs)
    )
  }

  def clearWatch(id: WatchId): Callback = Callback {
    raw.clearWatch(id)
  }

  def getCurrentPosition(opts: Options = Options()): AsyncE[PositionError, Position] = AsyncE { handler =>
    val k = CallbackKleisli(handler)
    Callback(raw.getCurrentPosition(
      k.contramap[Position](Right(_)).toJsFn,
      k.contramap[PositionError](Left(_)).toJsFn,
      opts.toJs))
  }
}
