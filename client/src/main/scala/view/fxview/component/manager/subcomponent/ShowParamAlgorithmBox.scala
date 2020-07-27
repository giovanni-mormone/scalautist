package view.fxview.component.manager.subcomponent

import java.net.URL
import java.util.ResourceBundle

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, Regola, Terminale, ZonaTerminale}
import caseclass.CaseClassHttpMessage.{AlgorithmExecute, InfoAlgorithm}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.layout.VBox
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.component.manager.subcomponent.parent.ShowParamAlgorithmBoxParent
import view.fxview.component.manager.subcomponent.util.ShiftUtil
import view.fxview.util.ResourceBundleUtil._

trait ShowParamAlgorithmBox  extends Component[ShowParamAlgorithmBoxParent] {

}

object ShowParamAlgorithmBox {

  def apply(info: AlgorithmExecute, name: Option[String], rules: List[Regola], terminal: List[Terminale]): ShowParamAlgorithmBox = {

    new ShowParamAlgorithmBoxFX(info, name, rules, terminal)
  }

  private class ShowParamAlgorithmBoxFX (info: AlgorithmExecute, name: Option[String], rules: List[Regola], terminalList: List[Terminale])
    extends AbstractComponent[ShowParamAlgorithmBoxParent](path = "manager/subcomponent/ShowParamsBox")
    with ShowParamAlgorithmBox {

    @FXML
    var title: Label = _
    @FXML
    var initialInfo: VBox = _
    @FXML
    var titleInitial: Label = _
    @FXML
    var nomeTitle: Label = _
    @FXML
    var nome: Label = _
    @FXML
    var sabatoTitle: Label = _
    @FXML
    var sabato: Label = _
    @FXML
    var terminal: VBox = _
    @FXML
    var initTitleTerminal: Label = _
    @FXML
    var titleTerminal: Label = _
    @FXML
    var gruppi: VBox = _
    @FXML
    var titleGruppi: Label = _
    @FXML
    var idGruppo: Label = _
    @FXML
    var data: Label = _
    @FXML
    var normalWeek: VBox = _
    @FXML
    var titleWeek: Label = _
    @FXML
    var day: Label = _
    @FXML
    var shift: Label = _
    @FXML
    var quantity: Label = _
    @FXML
    var ruleW: Label = _
    @FXML
    var specialWeek: VBox = _
    @FXML
    var titleWeekS: Label = _
    @FXML
    var dayS: Label = _
    @FXML
    var shiftS: Label = _
    @FXML
    var quantityS: Label = _
    @FXML
    var ruleWS: Label = _
    @FXML
    var senzaInfo1: Label = _
    @FXML
    var senzaInfo2: Label = _
    @FXML
    var senzaInfo3: Label = _
    @FXML
    var senzaInfo4: Label = _
    @FXML
    var run: Button = _
    @FXML
    var reset: Button = _
    private var dayInWeek:List[String] = _
    private val NONE: String = "NONE"

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      super.initialize(location, resources)
      dayInWeek =  List(resources.getResource("monday"),
        resources.getResource("tuesday"),
        resources.getResource("wednesday"),
        resources.getResource("thursday"),
        resources.getResource("friday"),
        resources.getResource("saturday"))
      setTitle()
      setTitleInformation()
      setBaseInfo()
      setTerminalInfo()
      setGroupInfo()
      setNormalWeekInfo()
      setSpecialWeekInfo()
      initButton()
    }
    def setTitle():Unit={
      title.setText(resources.getResource("title"))
      titleInitial.setText(resources.getResource("initial-title"))
      initTitleTerminal.setText(resources.getResource("terminal-title"))
      titleGruppi.setText(resources.getResource("group-title"))
      titleWeek.setText(resources.getResource("normal-week-title"))
      titleWeekS.setText(resources.getResource("special-week-title"))
      run.setText(resources.getResource("run"))
      reset.setText(resources.getResource("reset"))
    }
    def setTitleInformation():Unit={
      nomeTitle.setText(resources.getResource("title-name"))
      sabatoTitle.setText(resources.getResource("title-saturday"))
      titleTerminal.setText(resources.getResource("sub-terminal-title"))
      idGruppo.setText(resources.getResource("id-group"))
      data.setText(resources.getResource("date"))
      day.setText(resources.getResource("day"))
      shift.setText(resources.getResource("shift"))
      quantity.setText(resources.getResource("quantity"))
      ruleW.setText(resources.getResource("rule"))
      dayS.setText(resources.getResource("day"))
      shiftS.setText(resources.getResource("shift"))
      quantityS.setText(resources.getResource("quantity"))
      ruleWS.setText(resources.getResource("rule"))
    }
    def setBaseInfo(): Unit ={
      name match {
        case Some(name) => nome.setText(name)
        case None =>nome.setText(resources.getResource("without-name"))
      }
      if(info.regolaTreSabato) sabato.setText(resources.getResource("saturday-active"))
      else  sabato.setText(resources.getResource("saturday-not-active"))

    }
    def setTerminalInfo(): Unit ={
      terminalList.filter(id => info.idTerminal.exists(id.idTerminale.contains)) match {
        case terminale if terminale.nonEmpty=>
          val label = TerminalLabelBox(terminale.map(_.nomeTerminale))
          terminal.getChildren.add(label.setParent(parent).pane)
        case Nil =>senzaInfo4.setText(resources.getResource("unknown"))
          terminal.getChildren.add(senzaInfo4)
      }
    }
    def setGroupInfo(): Unit ={
      info.gruppo.toList.flatten.zipWithIndex match {
        case ::(head, next) => (head::next).foreach(gruppo=>{
          gruppo._1.date.foreach(grup=>{
            val group = InfoGroup(resources.println("name-group",gruppo._2.toString),grup.toString)
            gruppi.getChildren.add(group.setParent(parent).pane)
          })
        })
        case Nil =>senzaInfo1.setText(resources.getResource("unknown"))
          gruppi.getChildren.add(senzaInfo1)
      }

    }
    def setNormalWeekInfo(): Unit ={
      info.settimanaNormale.toList.flatten match {
        case ::(head, next) => (head::next).reverse.foreach(settimanaN=>{
          rules.collect{
            case  regola if regola.idRegola.contains(settimanaN.regola)=>
              val settimana= NormalAndSpecialWeekInfo(dayInWeek(settimanaN.idDay-1),ShiftUtil.getShiftName(settimanaN.turnoId),settimanaN.quantita.toString,regola.nomeRegola)
              normalWeek.getChildren.add(settimana.setParent(parent).pane)
          }
        })
        case Nil =>senzaInfo2.setText(resources.getResource("unknown"))
          normalWeek.getChildren.add(senzaInfo2)
      }
    }
    def setSpecialWeekInfo(): Unit ={
      info.settimanaSpeciale.toList.flatten match {
        case ::(head, next) => (head::next).reverse.foreach(settimanaS=>{
          rules.collect {
            case regola if regola.idRegola.contains(settimanaS.regola) =>
              val settimana = NormalAndSpecialWeekInfo(dayInWeek(settimanaS.idDay-1), ShiftUtil.getShiftName(settimanaS.turnoId), settimanaS.quantita.toString, regola.nomeRegola)
              specialWeek.getChildren.add(settimana.setParent(parent).pane)
          }
        })
        case Nil =>senzaInfo3.setText(resources.getResource("unknown"))
          specialWeek.getChildren.add(senzaInfo3)
      }
    }
    def initButton(): Unit = {
      reset.setOnAction(_ => parent.resetParams())
      run.setOnAction(_ => {
        name.fold()(name => {
          parent.saveParam( InfoAlgorithm(
            Parametro(info.regolaTreSabato, name),
            info.idTerminal.collect{
              case id if terminalList.exists(_.idTerminale.contains(id)) =>
                terminalList.find(_.idTerminale.contains(id))
                  .fold(ZonaTerminale(0,0))(term => ZonaTerminale(term.idZona, term.idTerminale.getOrElse(0)))
            },
            getGiorniInSettimana
          ))
        })
        println(info)
        parent.run(info)
      })
    }
    private def getGiorniInSettimana: Option[List[GiornoInSettimana]] = {
      val weekDays = info.settimanaNormale.map(nWeek => nWeek.map(day =>
        GiornoInSettimana(day.idDay, day.turnoId, day.regola, day.quantita)))
      if(weekDays.isDefined)
        if(weekDays.getOrElse(List.empty).isEmpty)
          return None
      weekDays
    }
  }
}
