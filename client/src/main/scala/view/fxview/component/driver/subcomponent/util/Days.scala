package view.fxview.component.driver.subcomponent.util

import java.util.ResourceBundle

import caseclass.CaseClassDB.Turno
import javafx.scene.layout.VBox
import javafx.scene.control.{Accordion, Label, TitledPane}

object Days {
  val days = List("monday","tuesday","wednesday","thursday","friday","saturday","sunday")
  def createAccordion(resourceBundle: ResourceBundle,shiftAccordion: Accordion,shift:List[Turno]):Unit={
    val result = days zip shift
    result.foreach(day=>{
      val firstTitled = new TitledPane
      firstTitled.setText(resourceBundle.getString(day._1))
      val content = new VBox
      content.getChildren.add(new Label(day._2.nomeTurno))
      content.getChildren.add(new Label(day._2.fasciaOraria))
      firstTitled.setContent(content)
      shiftAccordion.getPanes.add(firstTitled)
    })

  }
}
