package triggernz.reactnative.apis.builtin

import japgolly.scalajs.react.{Callback, CallbackKleisli, CallbackTo}
import org.scalajs.dom._
import triggernz.reactnative.core.ContT.{AsyncE, AsyncRE}

import scalajs.js

class Geolocation(raw: org.scalajs.dom.raw.Geolocation) {
  import Geolocation._

  def watchPosition(opts: Options = Options(true, 5000)): AsyncRE[PositionError, Position, WatchId] = AsyncRE { handler =>
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

  def getCurrentPosition(opts: Options = Options(true, 5000)): AsyncE[PositionError, Position] = AsyncE { handler =>
    val k = CallbackKleisli(handler)
    Callback(raw.getCurrentPosition(
      k.contramap[Position](Right(_)).toJsFn,
      k.contramap[PositionError](Left(_)).toJsFn,
      opts.toJs))
  }
}

object Geolocation {
  def apply(): Geolocation = apply(window.navigator.geolocation)
  def apply(raw: org.scalajs.dom.raw.Geolocation): Geolocation = new Geolocation(raw)

  type WatchId = Int

  case class Options(enableHighAccuracy: Boolean, maximumAge: Int) {
    def toJs: PositionOptions = {
      val po = (new js.Object).asInstanceOf[PositionOptions]
      po.enableHighAccuracy = enableHighAccuracy
      po.maximumAge = maximumAge
      po
    }
  }
}
