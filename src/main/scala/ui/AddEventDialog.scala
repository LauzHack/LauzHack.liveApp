package ui

import UIComponents.AddButton
import datastruct.{Announce, LivePage}

import scalafx.event.ActionEvent
import scalafx.geometry.Insets
import scalafx.scene.control._
import scalafx.scene.layout.GridPane
import scalafx.stage.Stage
import scalafx.Includes._
import scalafx.application.Platform

object AddEventDialog {


  def create(stage: Stage, data: LivePage, editData: LivePage => Unit) = (_: ActionEvent) => {

    val dialog = new Dialog[LogInfo]() {
      initOwner(stage)
      title = "Add Announcement"
    }

    // Set the buttons.
    dialog.dialogPane().buttonTypes = Seq(AddButton, ButtonType.Cancel)

    // Create the username and password labels and fields.
    val announcement = new TextField() {
      promptText = "Announce"
    }

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)
      add(new Label("Announcement text:"), 0, 0)
      add(announcement, 1, 0)
    }

    // Enable/Disable login button depending on whether a username was
    // entered.
    val addButton = dialog.dialogPane().lookupButton(AddButton)
    addButton.disable = true

    // Do some validation (disable when username is empty).
    announcement.text.onChange { (_, _, newValue) =>
      addButton.disable = newValue.trim().isEmpty
    }

    dialog.dialogPane().content = grid

    // Request focus on the username field by default.
    Platform.runLater(announcement.requestFocus())

    val result = dialog.showAndWait()

    result match {
      case Some(AddButton) =>
        editData(data.add(Announce(announcement.text.value)))
      case _ =>
    }
  }
}
