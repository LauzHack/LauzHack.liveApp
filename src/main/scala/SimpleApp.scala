import datastruct.Announce

import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.{Cursor, Scene}
import scalafx.scene.image.ImageView
import scalafx.scene.control._
import scalafx.scene.control.Alert._
import scalafx.scene.layout.{Background, BackgroundFill, BorderPane}
import scalafx.Includes._
import scalafx.event.ActionEvent
import datastruct.UIComponents._
import git.GitHandler
import ui.LoginDialog

import scalafx.collections.ObservableBuffer
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

object SimpleApp extends JFXApp {

  val repository: GitHandler = login()

  @scala.annotation.tailrec
  def login(attemptFailed: Boolean = false): GitHandler = {
    LoginDialog.promptForLogin(stage, attemptFailed) match {
      case Some(res) =>
        val repo = new GitHandler(res)
        if (repo.verifyCredential()) {
          repo
        } else {
          login(attemptFailed = true)
        }
      case None =>
        login(attemptFailed = true)
    }
  }

  repository.loadRepo()
  val data = repository.loadFile()

  stage = new PrimaryStage {
    title = "LauzHack Live Edit"
    scene = new Scene {
      root = new BorderPane {
        padding = Insets(25)
        top = new ImageView("logo.jpg")

        // Create a button to add new announcement
        val publishButton = new Button{
          text = "Add a new announcement"
          onAction = addEventPopUp
        }
        val list = new ListView[Announce] {
          orientation = Orientation.Vertical
          cellFactory = {
            p => {
              val cell = new ListCell[Announce]
              cell.textFill = Color.Black
              cell.cursor = Cursor.Hand
              cell.item.onChange { (_, _, entry) =>
                cell.text = entry.text
              }
              cell.onMouseClicked = { me: MouseEvent => cell.item.value) }
              cell
            }
          }
          items = ObservableBuffer(data.announces)
        }
        center = list
        bottom = publishButton
      }
    }
  }

  // Quit the app properly.
  stage.onCloseRequest = { _ =>
    println("Bye bye <3")
    repository.discard()
    Platform.exit()
  }


  val addEventPopUp = (event: ActionEvent) => {
    val alert = new Alert(AlertType.Information) {
      title = "Add a new event"
      headerText = "Look, an Information Dialog"
      contentText = "I have a great message for you!"
      buttonTypes = Seq(ButtonType.Cancel, AddButton)
    }
    val result = alert.showAndWait()

    result match {
      case Some(AddButton) => 
      case _ =>
        throw new IllegalArgumentException("I don't know were you pressed this button")
    }
  }
}
