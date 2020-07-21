package view.fxview.component.manager.subcomponent

import java.net.URL
import java.sql.Date
import java.time.LocalDate
import java.util.ResourceBundle

import caseclass.CaseClassDB.Regola
import javafx.fxml.FXML
import javafx.scene.control.{Button, ComboBox}
import org.controlsfx.control.CheckComboBox
import view.fxview.component.manager.subcomponent.parent.ChangeSettimanaRichiestaParent
import view.fxview.component.manager.subcomponent.util.ParamsForAlgoritm
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._

trait ChangeSettimanaRichiesta extends Component[ChangeSettimanaRichiestaParent] {

}

object ChangeSettimanaRichiesta{

  def apply(params: ParamsForAlgoritm, rules: List[Regola]): ChangeSettimanaRichiesta =
    new ChangeSettimanaRichiestaFX(params, rules)

  private class ChangeSettimanaRichiestaFX(params: ParamsForAlgoritm, rules: List[Regola])
  extends AbstractComponent[ChangeSettimanaRichiestaParent](path = "manager/subcomponent/ParamSettimana")
  with ChangeSettimanaRichiesta{

    @FXML
    var run: Button = _
    @FXML
    var reset: Button = _
    @FXML
    var weekS: CheckComboBox[String] = _
    @FXML
    var ruleN: ComboBox[String] = _
    @FXML
    var ruleS: ComboBox[String] = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      initButton()
      initCombo()
    }

    private def initButton(): Unit = {
      run.setText(resources.getResource("runtxt"))
      run.setOnAction(_ => parent.groupParam(params))

      reset.setText(resources.getResource("resettxt"))
      reset.setOnAction(_ => parent.resetWeekParams())
    }

    private def initCombo(): Unit = {
      val list = calcolateWeeks()
      list.foreach(week => weekS.getItems.add(week.toString))

      rules.foreach(rule => {
        ruleN.getItems.add(rule.nomeRegola)
        ruleS.getItems.add(rule.nomeRegola)
      })
    }

    private def calcolateWeeks(): List[Int] = {
      val DECEMBER: Int = 12
      val LAST_DAY: Int = 28
      val FIRST_WEEK: Int = 1
      var res: List[Int] = List.empty

      val initWeek = weekNum(params.dateI)
      val endWeek = weekNum(params.dateF)

      if (initWeek <= endWeek)
        for(i <- initWeek to endWeek)
          res = res :+ i
      else {
        val lastWeek = weekNum(LocalDate.of(params.dateI.getYear, DECEMBER, LAST_DAY))
        for (i <- initWeek to lastWeek)
          res = res :+ i
        for (i <- FIRST_WEEK to endWeek)
          res = res :+ i
      }
      res
    }

    private def weekNum(localDate: LocalDate): Int = {
      import java.util.Calendar
      val cal = Calendar.getInstance
      cal.setTime(Date.valueOf(localDate))
      cal.get(Calendar.WEEK_OF_YEAR)
    }
  }
}
