package datastruct

class LivePage(val prefix: String, val suffix: String, var announces: List[Announce]) {
  def add(announce: Announce): LivePage = {
    announces = announce :: announces
    this
  }

  def remove(announce: Announce): LivePage = {
    announces = announces.filterNot(_ == announce)
    this
  }

  def updateAnnounce(oldA: Announce, newA: Announce): LivePage = {
    announces = announces.map{ case `oldA` => newA; case anyOther => anyOther }
    this
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
