package datastruct

case class LivePage(prefix: String, suffix: String, announces: List[Announce]) {
  def add(announce: Announce): LivePage = {
    LivePage(prefix, suffix, announce :: announces)
  }

  def remove(announce: Announce): LivePage = {
    LivePage(prefix, suffix, announces.filterNot(_ == announce))
  }

  def updateAnnounce(oldA: Announce, newA: Announce): LivePage = {
    LivePage(prefix, suffix, announces.map{ case `oldA` => newA; case anyOther => anyOther })
  }
}

case class Announce(text: String) {
  def toString(level: Priority): String = {
    s"<$level>$text</$level>"
  }

  override def toString: String = toString(Normal)

  def trimmedTxt(): String = {
    val trimmed = text.take(80)
    if (trimmed == text) {
      trimmed
    } else {
      trimmed + "..."
    }
  }

}

object Announce {
  val TextLengthDisplayed: Int = 80
}

sealed trait Priority {
}
case object VeryHigh extends Priority {
override def toString(): String = "h2"
}
case object High extends Priority {
  override def toString(): String = "h3"
}
case object Normal extends Priority {
  override def toString(): String = "h4"
}


object Announcement {
  val pattern = """<h\d>([^<]*)<\/h\d>""".r.unanchored

  def apply(line: String): Option[Announce] = {
    val res: Option[Announce] = line match {
      case pattern(txt) =>
        Some(Announce(txt))
      case a =>
        None
    }
    res
  }
}
