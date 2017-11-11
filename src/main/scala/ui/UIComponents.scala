package ui

import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.ButtonType

object UIComponents {
  val AddButton = new ButtonType("Create", ButtonData.OKDone)
  val LoginButton = new ButtonType("Login", ButtonData.OKDone)
  val EditButton = new ButtonType("Save", ButtonData.OKDone)
  val DeleteButton = new ButtonType("Delete", ButtonData.No)
}
