package view.fxview.component.driver.subcomponent.util

import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoShift
import javafx.scene.control.{Accordion, Label, TitledPane}
import javafx.scene.layout.VBox

object Days {
  private val days = List((1,"monday"),(2,"tuesday"),(3,"wednesday"),(4,"thursday"),(5,"friday"),(6,"saturday"),(7,"sunday"))
  def createAccordion(resourceBundle: ResourceBundle,shiftAccordion: Accordion,shift:InfoShift):Unit={
    val result = shift.shiftDay.groupBy(_.idGiorno)
    val finalResult = days.filter(dayIn=>result.contains(dayIn._1)).map(day=>day._2->result(day._1))
    finalResult.foreach(day=>{
      val firstTitled = new TitledPane
      firstTitled.setText(resourceBundle.getString(day._1))
      val content = new VBox
      day._2.foreach(result=>content.getChildren.add(new Label(result.orario)))
      firstTitled.setContent(content)
      shiftAccordion.getPanes.add(firstTitled)
    })

  }
}
