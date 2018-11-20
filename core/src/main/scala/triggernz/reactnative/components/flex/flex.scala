package triggernz.reactnative.flex

sealed abstract class Direction(val str: String)
object Direction {
  val prop = "flex-direction"
  case object Column extends Direction("column")
  case object ColumnReverse extends Direction("column-reverse")
  case object Row extends Direction("row")
  case object RowReverse extends Direction("row-reverse")
}

case class Flex(n: Int)
object Flex {
  val prop = "flex"
  def enable = Flex(1)
}

sealed abstract class JustifyContent(val str: String)
object JustifyContent {
  val prop = "justify-content"

  case object Start extends JustifyContent("flex-start")
  case object End extends JustifyContent("flex-start")
  case object Center extends JustifyContent("center")
  case object SpaceBetween extends JustifyContent("space-between")
  case object SpaceAround extends JustifyContent("space-around")
}

sealed abstract class AlignItems(val str: String)
object AlignItems {
  val prop = "align-items"

  case object Start extends AlignItems("flex-start")
  case object End extends AlignItems("flex-end")
  case object Center extends AlignItems("center")
  case object Stretch extends AlignItems("stretch")
}

//TODO: Use the same type as AlignItems?
sealed abstract class AlignSelf(val str: String)
object AlignSelf {
  val prop = "align-self"
  case object Start extends AlignSelf("flex-start")
  case object End extends AlignSelf("flex-end")
  case object Center extends AlignSelf("center")
  case object Stretch extends AlignSelf("stretch")
}

sealed abstract class Wrap(val str: String)
object Wrap {
  val prop = "wrap"

  case object On extends Wrap("wrap")
  case object Off extends Wrap("nowrap")
}

sealed abstract class AlignContent(val str: String)
object AlignContent {
  object Start extends AlignContent("flex-start")
  object Center extends AlignContent("center")
  object End extends AlignContent("flex-end")
  object Stretch extends AlignContent("stretch")
  object SpaceBetween extends AlignContent("space-between")
  object SpaceAround extends AlignContent("space-around")
}

sealed abstract class Position(val str: String)
object Position {
  object Relative extends Position("relative")
  object Absolute extends Position("absolute")
}

case class ZIndex(z: Int)