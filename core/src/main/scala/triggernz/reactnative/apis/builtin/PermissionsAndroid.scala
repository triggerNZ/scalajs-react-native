package triggernz.reactnative.apis.builtin

import triggernz.reactnative.core.ContT.{Async}

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

sealed abstract class PermissionAndroid
//TODO: map this part out fully
object PermissionsAndroid {
  def request(permission: String): Async[String] =
    Async.fromPromise(Raw.request(permission))


  lazy val RESULTS = Raw.RESULTS

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
