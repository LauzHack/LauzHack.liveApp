package ui

import datastruct.{Announce, LivePage}
import ui.UIComponents.{DeleteButton, EditButton}

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.control.{ButtonType, Dialog, Label, TextField}
import scalafx.scene.layout.GridPane
import scalafx.stage.Stage

object EditEventDialog {

  def create(stage: Stage, announce: Announce, data: LivePage, editData: LivePage => Unit): Unit = {

    val dialog = new Dialog[LogInfo]() {
      initOwner(stage)
      title = "Edit Announcement"
    }
  println("built")
    // Set the buttons.
    dialog.dialogPane().buttonTypes = Seq(EditButton, DeleteButton, ButtonType.Cancel)

    // Create the username and password labels and fields.
    val announcement = new TextField() {
      promptText = "Announce"
      text = announce.text

    }

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)
      add(new Label("Announcement text:"), 0, 0)
      add(announcement, 1, 0)
    }

    dialog.dialogPane().content = grid

    // Request focus on the username field by default.
    Platform.runLater(announcement.requestFocus())

    val result = dialog.showAndWait()

    result match {
      case Some(EditButton) =>
        editData(data.updateAnnounce(announce, Announce(announcement.text.value)))
      case Some(DeleteButton) =>
        editData(data.remove(announce))
      case _ =>
    }
  }

}
