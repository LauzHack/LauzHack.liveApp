package ui

import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.GridPane
import scalafx.stage.Stage
import scalafx.Includes._

case class LogInfo(username: String, password: String)
object LoginDialog {

  def promptForLogin(stage: Stage, hasFailed: Boolean): Option[LogInfo] = {
    // Create the custom dialog.
    val dialog = new Dialog[LogInfo]() {
      initOwner(stage)
      title = "LauzHack Live page"
      headerText = "Login to LauzHack Live github project"
    }

    // Set the button types.
    val loginButtonType = new ButtonType("Login", ButtonData.OKDone)
    dialog.dialogPane().buttonTypes = Seq(loginButtonType, ButtonType.Cancel)

    // Create the username and password labels and fields.
    val username = new TextField() {
      promptText = "GitHub Username"
    }
    val password = new PasswordField() {
      promptText = "GitHub Password"
    }

    val grid = new GridPane() {
      hgap = 10
      vgap = 10
      padding = Insets(20, 100, 10, 10)
      if (hasFailed) {
        add(new Label("Failed login"), 0, 0)
      }
      add(new Label("Username:"), 0, 1)
      add(username, 1, 1)
      add(new Label("Password:"), 0, 2)
      add(password, 1, 2)
    }

    // Enable/Disable login button depending on whether a username was
    // entered.
    val loginButton = dialog.dialogPane().lookupButton(loginButtonType)
    loginButton.disable = true

    // Do some validation (disable when username is empty).
    username.text.onChange { (_, _, newValue) =>
      loginButton.disable = newValue.trim().isEmpty
    }

    dialog.dialogPane().content = grid

    // Request focus on the username field by default.
    Platform.runLater(username.requestFocus())

    // When the login button is clicked, convert the result to
    // a username-password-pair.
    dialog.resultConverter = dialogButton =>
      if (dialogButton == loginButtonType)
        LogInfo(username.text(), password.text())
      else
        null

    val result = dialog.showAndWait()

    result match {
      case x: Some[LogInfo] => x
      case None => None
    }
  }
}
