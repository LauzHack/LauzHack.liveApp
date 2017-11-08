package datastruct

import scalafx.scene.paint.Color

case class LivePage(prefix: String, suffix: String, announces: List[Announce]) {
  def +:(announce: Announce): LivePage = {
    LivePage(prefix, suffix, announce :: announces)
  }
}

case class Announce(level: Priority, text: String) {
  override def toString(): String = {
    s"<$level>$text</$level>"
  }

  def colorCodedLevel(): Color = {
    level.color
  }

  def modifyText(newText: String): Announce = {
    Announce(level, newText)
  }
}

sealed trait Priority {
  def color(): Color
}
case object VeryHigh extends Priority {
  override def toString(): String = "h2"
  override def color() = Color.OrangeRed
}
case object High extends Priority {
  override def toString(): String = "h3"
  override def color() = Color.LightGoldrenrodYellow
}
case object Normal extends Priority {
  override def toString(): String = "h4"
  override def color() = Color.ForestGreen
}

object Priority {
  def strToPriority(str: String): Priority = str match {
    case "h2" => VeryHigh
    case "h3" => High
    case "h4" => Normal
    case "h1" | _ => VeryHigh
  }
}

object Announce {
  val pattern = """<(h\d)>([^<]*)<\/h\d>""".r.unanchored

  def apply(line: String): Option[Announce] = {
    val res = line match {
      case pattern(prio, txt) =>
        Some(Announce(Priority.strToPriority(prio), txt))
      case a =>
        None
    }
    res
  }
}
