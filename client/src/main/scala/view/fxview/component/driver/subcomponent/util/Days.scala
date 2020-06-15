package view.fxview.component.driver.subcomponent.util

import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.scene.layout.VBox
import javafx.scene.control.{Accordion, TitledPane}

object Days {
  val days = List("monday","tuesday","wednesday","thursday","friday","saturday","sunday")
  def createAccordion(resourceBundle: ResourceBundle,shiftAccordion: Accordion,shift:List[Turno]):Unit={
    days.foreach(day=>{
      val firstTitled = new TitledPane
      firstTitled.setText(resourceBundle.getString(day))
      val firstContent = new VBox

    })

  }
}
