package triggernz.reactnative.components.dimensions

import scala.scalajs.js.|

sealed trait DimValue {
  def toReactNativeStyle: String | Int
}

object DimValue {
  final case class Px(px: Int) extends DimValue {
    override def toReactNativeStyle: String | Int = px
  }

  final case class Percent(pct: Int) extends DimValue {
    override def toReactNativeStyle: String | Int = pct + "%"
  }

  implicit class DimIntExt(val e: Int) extends AnyVal {
    def px: Px = Px(e)
    def pct: Percent = Percent(e)
  }
}