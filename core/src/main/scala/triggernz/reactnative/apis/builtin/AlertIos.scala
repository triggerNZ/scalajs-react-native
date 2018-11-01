package triggernz.reactnative.apis.builtin

import triggernz.reactnative.core.PlatformIO.IosIO

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import js.JSConverters._

object AlertIos {
  def alert(title: String, message: Option[String] = None): IosIO[Unit] =
    IosIO {
      Raw.alert(title, message.orUndefined)
    }
  @JSImport("react-native", "AlertIOS")
  @js.native
  private object Raw extends js.Object {
    //TODO there is more here. Fill in
    def alert(title: String, message: js.UndefOr[String]): Unit = js.native
  }
}
