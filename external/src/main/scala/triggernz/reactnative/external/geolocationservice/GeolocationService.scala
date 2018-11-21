package triggernz.reactnative.external.geolocationservice

import org.scalajs.dom.raw.{Geolocation => RawGeolocation}
import triggernz.reactnative.apis.builtin.Geolocation

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object GeolocationService {
  @JSImport("react-native-geolocation-service", JSImport.Default)
  @js.native
  object Raw extends RawGeolocation

  def apply(): Geolocation = Geolocation(Raw)
}
