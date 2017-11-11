import javax.swing.GroupLayout.Alignment

import datastruct.{Announce, LivePage}

import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.{Insets, Orientation, Pos}
import scalafx.scene.{Cursor, Scene}
import scalafx.scene.image.ImageView
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane
import scalafx.Includes._
import git.GitHandler
import ui.{AddEventDialog, EditEventDialog, LoginDialog}

import scalafx.collections.ObservableBuffer
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color

object SimpleApp extends JFXApp {

  val opt: Option[GitHandler] = login()

  @scala.annotation.tailrec
  def login(attemptFailed: Boolean = false): Option[GitHandler] = {
    LoginDialog.promptForLogin(stage, attemptFailed) match {
      case Some(res) =>
        val repo = new GitHandler(res)
        if (repo.verifyCredential()) {
          Some(repo)
        } else {
          login(attemptFailed = true)
        }
      case None =>
        None
    }
  }

  opt match {
    case None =>
      Platform.exit()

    case Some(repository) =>

      repository.loadRepo()
      val data: LivePage = repository.loadFile()

      stage = new PrimaryStage {
        title = "LauzHack Live Edit"
        scene = new Scene {
          root = new BorderPane {
            padding = Insets(25)
            top = new ImageView("logo.jpg")

            def updateData(newData: LivePage) = {
              list.items = ObservableBuffer(data.announces)
              repository.commitAndPushFile(data)
            }

            // Create a button to add new announcement
            val publishButton = new Button {
              margin = Insets(10)
              alignment = Pos.Center
              text = "Add a new announcement"
              onAction = AddEventDialog.create(stage, data, updateData _)
            }
            val list = new ListView[Announce] {
              orientation = Orientation.Vertical
              items = ObservableBuffer(data.announces)
              cellFactory = {
                p => {
                  val cell = new ListCell[Announce]
                  cell.textFill = Color.Black
                  cell.cursor = Cursor.Hand
                  cell.item.onChange { (_, _, entry) =>
                    if (entry == null) {
                      cell.text = ""
                    } else {
                      cell.text = entry.trimmedTxt()
                    }
                  }
                  cell.onMouseClicked = { me: MouseEvent =>
                    if (!cell.isEmpty) {
                      println("Hello")
                      EditEventDialog.create(stage, cell.item.value, data, { newData =>
                        items = ObservableBuffer(data.announces)
                        repository.commitAndPushFile(data)
                      })
                    }
                  }
                  cell
                }
              }
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
  }
}
