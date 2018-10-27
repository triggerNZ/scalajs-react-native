package triggernz.reactnative.components.builtin


sealed trait FontWeight {
  val toJs: String
}

object FontWeight {

  case object Normal extends FontWeight {
    val toJs = "normal"
  }

  case object Bold extends FontWeight {
    val toJs = "bold"
  }

}
