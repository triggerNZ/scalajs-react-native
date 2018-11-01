package triggernz.reactnative.apis.builtin

import triggernz.reactnative.core.PlatformIO.AndroidIO

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

sealed abstract class PermissionAndroid
//TODO: map this part out fully
object PermissionsAndroid {
  def request(permission: String): AndroidIO[Unit] = AndroidIO(Raw.request(permission))


  @JSImport("react-native", "PermissionsAndroid")
  @js.native
  private object Raw extends js.Object {
    def request(permission: String): Unit = js.native
  }
}
