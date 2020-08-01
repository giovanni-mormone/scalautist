package view.fxview.component.driver.subcomponent.util

import java.util.ResourceBundle

import caseclass.CaseClassHttpMessage.InfoShift
import javafx.scene.control.{Accordion, Label, TitledPane}
import javafx.scene.layout.VBox

object Days {
  private val days = List((1,"monday"),(2,"tuesday"),(3,"wednesday"),(4,"thursday"),(5,"friday"),(6,"saturday"),(7,"sunday"))
  def createAccordion(resourceBundle: ResourceBundle,shiftAccordion: Accordion,shift:InfoShift):Unit={

    val result = shift.shiftDay.map(_.idGiorno).distinct.map(value=> value -> shift.shiftDay.filter(day=>day.idGiorno==value))
    val finalResult= result.map(day=>days.filter(value=>value._1==day._1).head._2->result.filter(_._1 == day._1).flatMap(value => value._2))

    finalResult.foreach(day=>{
      val firstTitled = new TitledPane
      firstTitled.setId(day._1)
      firstTitled.setText(resourceBundle.getString(day._1))
      val content = new VBox
      val label = new Label("Orario")
      content.getChildren.add(label)
      day._2.foreach(result=>{
        val label = new Label("    -" + result.orario)
        label.setId(result.orario+result.idGiorno)
        content.getChildren.add(label)
      })
      firstTitled.setContent(content)
      shiftAccordion.getPanes.add(firstTitled)
    })

  }
}
