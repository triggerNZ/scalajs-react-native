package triggernz.reactnative.apis.builtin

import triggernz.reactnative.core.ContT.{ Async}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

sealed abstract class PermissionAndroid
//TODO: map this part out fully
object PermissionsAndroid {
  def request(permission: Permission): Async[Result] =
    Async.fromPromise(Raw.request(permission.identifier)).map(Result.fromString) // We accept partiality here because we trust the react-native docs.

  sealed trait Result
  object Result {
    case object Granted extends Result
    case object Denied extends Result
    case object NeverAskAgain extends Result

    val fromString: PartialFunction[String, Result] = {
      case "granted" => Granted
      case "denied" => Denied
      case "never_ask_again" => NeverAskAgain
    }
  }

  sealed abstract class Permission(val identifier: String)
  object Permission {
    case object AccessFineLocation extends Permission("android.permission.ACCESS_FINE_LOCATION")
  }

  @JSImport("react-native", "PermissionsAndroid")
  @js.native
  private object Raw extends js.Object {
    def request(permission: String): js.Promise[String] = js.native
    val RESULTS: Results = js.native
  }

  @js.native
  trait Results extends js.Object {
    def GRANTED: String
    def DENIED: String
    def NEVER_ASK_AGAIN: String
  }
}
