package triggernz.reactnative.apis.builtin

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

sealed abstract class PermissionAndroid
//TODO: map this part out properly
object PermissionsAndroid {
  @JSImport("react-native", "PermissionsAndroid")
  @js.native
  object Raw extends js.Object {
    def request(permission: String): Unit = js.native
  }
}
