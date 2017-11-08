package git

import scala.sys.process._
import java.io.{File, PrintWriter}

import datastruct._
import ui.LogInfo

class GitHandler(logInfo: LogInfo) {
  import GitHandler._

  val LogInfo(user, pass) = logInfo
  val repo = s"https://$user:$pass@github.com/LauzHack/Lauzhacklivepage.github.io"
  val testRepo = s"https://$user:$pass@github.com/tOverney/AreYouLauzHackBot"

  /**
    * Query a private repository where @LauzHackBot has access so that
    * @return whether credentials works or not.
    */
  def verifyCredential(): Boolean = {
    // make sure that we start from scratch.
    "rm -rf cloned".!

    val exitCode = s"git clone $testRepo cloned".!
    if (exitCode == 0) {
      "rm -rf cloned".!
      true
    } else {
      false
    }
  }

  def loadRepo(): Boolean = {
    val exitCode = if ("mkdir cloned".! == 1) {
      "cd cloned; git pull; cd ..".!
    } else {
      s"git clone $repo cloned".!
    }

    exitCode == 0
  }

  def loadFile(): LivePage = {
    val rawPage = scala.io.Source.fromFile(filePath).mkString
    val Array(prefix, tail) = rawPage.split(startingMarker)
    val Array(lines, suffix) = tail.split(endingMarker)

    val announces = lines.split('\n').toList.flatMap( line => Announce(line))

    LivePage(prefix, suffix, announces)
  }

  def commitAndPushFile(page: LivePage) = {
    // Update file
    val f2 = new File("tmp/tmp.txt") // Temporary File
    val finalFile = new File(filePath)
    val w = new PrintWriter(f2)
    (page.prefix ++ List(startingMarker) ++
        page.announces.map(_.toString) ++ List(endingMarker)
        ).foreach(w.println)
    w.close()
    f2.renameTo(finalFile)

    val currDir = new File("cloned")
    // commit
    sys.process.Process(Seq("git","commit","-am","Update the live view"), currDir).!!
  }

  def discard(): Unit = {
    "rm -rf cloned".!
  }
}

object GitHandler {
  val filePath = "cloned/live.html"
  val startingMarker = "<!-- INSERT ITEMS HERE -->"
  val endingMarker = "<!-- STOP INSERTING THERE -->"
}
