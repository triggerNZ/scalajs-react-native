package triggernz.reactnative.apis.builtin

import japgolly.scalajs.react.{CallbackTo}
import org.scalajs.dom.experimental.Response

import scala.concurrent.Future


object Fetch {
  private val inner = org.scalajs.dom.experimental.Fetch

  def fetch(url: String) = CallbackTo[Future[Response]] {
    inner.fetch(url).toFuture
  }
}
