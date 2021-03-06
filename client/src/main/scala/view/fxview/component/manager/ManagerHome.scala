package view.fxview.component.manager

import java.net.URL
import java.sql.Date
import java.util.ResourceBundle

import caseclass.CaseClassDB.{Regola, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{InfoAbsenceOnDay, InfoAlgorithm, InfoReplacement, ResultAlgorithm, _}
import javafx.fxml.FXML
import javafx.scene.control.{Accordion, Button, Label}
import javafx.scene.layout.{BorderPane, Pane, VBox}
import org.controlsfx.control.PopOver
import view.fxview.NotificationHelper.NotificationParameters
import view.fxview.component.manager.subcomponent.GroupParamsBox.Group
import view.fxview.component.manager.subcomponent._
import view.fxview.component.manager.subcomponent.parent.ManagerHomeParent
import view.fxview.component.manager.subcomponent.util.ParamsForAlgorithm
import view.fxview.component.{AbstractComponent, Component}
import view.fxview.util.ResourceBundleUtil._
import view.fxview.{FXHelperFactory, NotificationHelper}

/**
 * @author Fabian Aspee Encina, Giovanni Mormone, Francesco Cassano
 *         trait of methods that allow user to do desired operations.
 */
trait ManagerHome extends Component[ManagerHomeParent]{

  /**
   * Method that draw the panel that allow to show chosen params and go on
   * @param info instance of [[AlgorithmExecute]], it contains information that allows the algorithm to work
   * @param name optional of name used to save params
   * @param terminals list of [[Terminale]] that contains all terminals information
   * @param rules list of [[Regola]] that contains all rules information
   */
  def drawShowParams(info: AlgorithmExecute, name: Option[String], terminals: List[Terminale], rules: List[Regola]): Unit
  /**
   * method that draw notification in view manager
   * @param str message of the notification
   * @param tag tag of message
   */
  def drawNotifica(str: String,tag:Long): Unit

  /**
   * Draws a representation of the result on the view.
   *
   * @param resultList
   *                   The List of results to draw.
   * @param dateList
   *                 The days of the results to draw.
   */
  def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit

  /**
   * draws a result selection view.
   * @param terminal
   *                 The list of terminals present in the db.
   */
  def drawResultTerminal(terminal: List[Terminale]): Unit

  /**
   * Method used to draw the panel that allow to choose params for run assignment algorithm
   *
   * @param terminals List of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawChooseParams(terminals: List[Terminale]): Unit

  /**
   * used to redraw the richiesta panel.
   */
  def reDrawRichiesta(): Unit

  /**
   * method that send all shift that existing in system and allow draw this
   *
   * @param listShift list with all shift in the system
   */
  def drawShiftRichiesta(listShift: List[Turno]): Unit

  /**
   * method that send all terminal that existing in system and allow draw this
   *
   * @param terminal list with all terminal existing in system
   */
  def drawRichiesta(terminal: List[Terminale]): Unit

  /**
   * Method used to draw the list of turns that needs a replacement
   *
   * @param absences
   * The list of turns that needs a replacement
   */
  def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit

  /**
   * Method used to draw the list of people avalaible for the turn that needs a replacement
   *
   * @param replacement
   * The people avalaible for the turn that needs a replacement
   */
  def drawManageReplacement(replacement: List[InfoReplacement]): Unit
  /**
   * method that show loading icon
   */
  def loadingReplacements(): Unit
  /**
   * method that remove loading icon
   */
  def stopLoadingReplacements(): Unit

  /**
   * method that draw the view for choosing the parameters of the week
   *
   * @param params insatance of [[ParamsForAlgorithm]] contains info to show (if a parameter has been loaded)
   * @param rules list of rules to show
   */
  def drawWeekParams(params: ParamsForAlgorithm, rules: List[Regola]): Unit

  /**
   * Load params chosen in modal and save information to use in the rest of session
   * @param param instance of [[InfoAlgorithm]] that contains information chosen until that view
   */
  def drawLoadedParam(param: InfoAlgorithm): Unit

  /**
   * Draws the view for choosing of group
   * @param params instance of [[ParamsForAlgorithm]] that contains information chosen until that view
   * @param rule list of [[Regola]] to show
   */
  def drawGroupsParam(params: ParamsForAlgorithm, rule: List[Regola]): Unit

  /**
   * Notify the new group to the view that manages the groups
   * @param group instance of new [[Group]]
   */
  def updateGroup(group: Group): Unit

  /**
   * draw the panel that recap chosen parameters
   * @param info instance of [[AlgorithmExecute]] that contains all information chosen
   * @param name optional of name of parameter
   */
  def showParamAlgorithm(info: AlgorithmExecute, name: Option[String]): Unit

  /** Initialize zona Manager view before show
   *
   * @param zones
   * List of [[caseclass.CaseClassDB.Zona]]
   */
  def drawZona(zones: List[Zona]): Unit

  /**
   * Initialize Terminal Manager view before show
   *
   * @param zones
   * List of [[caseclass.CaseClassDB.Zona]]
   * @param terminals
   * List of [[caseclass.CaseClassDB.Terminale]]
   */
  def drawTerminal(zones: List[Zona], terminals: List[Terminale]): Unit

  /**
   * method that show loading icon
   */
  def loadingResult(): Unit

  /**
   * method that remove loading icon
   */
  def stopLoadingResult(): Unit
}

object ManagerHome{

  def apply(userName: String, userId:String): ManagerHome = new ManagerHomeFX(userName,userId)

  private class ManagerHomeFX(userName: String, userId:String) extends AbstractComponent[ManagerHomeParent]("manager/BaseManager")
    with ManagerHome{
    @FXML
    var nameLabel: Label = _
    @FXML
    var baseManager: BorderPane = _
    @FXML
    var notificationButton: Button = _
    @FXML
    var generateTurnsButton: Button = _
    @FXML
    var manageAbsenceButton: Button = _
    @FXML
    var printResultButton: Button = _
    @FXML
    var manageZoneButton: Button = _
    @FXML
    var manageTerminalButton: Button = _
    @FXML
    var richiestaButton: Button = _
    @FXML
    var idLabel: Label = _
    @FXML
    var popover: PopOver = _
    @FXML
    var vBoxPopover:VBox = _

    var fillHolesView: FillHolesBox = _
    var managerRichiestaBoxView:ManagerRichiestaBox = _
    var chooseParamsBox: ChooseParamsBox = _
    var selectResultBox:SelectResultBox = _
    var terminalView: TerminalBox = _
    var zonaView: ZonaBox = _
    var gruopParamBox: GroupParamsBox = _
    var showParam: ShowParamAlgorithmBox = _

    override def initialize(location: URL, resources: ResourceBundle): Unit = {
      nameLabel.setText(resources.getResource("username-label"))
      idLabel.setText(resources.getResource("id-label"))
      notificationButton.setText(resources.getResource("notification-button"))
      generateTurnsButton.setText(resources.getResource("generate-turns-button"))
      manageAbsenceButton.setText(resources.getResource("manage-absence-button"))
      printResultButton.setText(resources.getResource("print-result-button"))
      manageZoneButton.setText(resources.getResource("manage-zone-button"))
      manageTerminalButton.setText(resources.getResource("manage-terminal-button"))
      richiestaButton.setText(resources.getResource("richiesta-button"))
      manageAbsenceButton.setOnAction(_ => parent.drawAbsencePanel())
      richiestaButton.setOnAction(_ => parent.drawRichiestaPanel())
      generateTurnsButton.setOnAction(_ => parent.drawParamsPanel())
      printResultButton.setOnAction(_=> parent.drawResultPanel())
      notificationButton.setOnAction(_=>showPopover())
      manageZoneButton.setOnAction(_ => parent.drawZonePanel())
      manageTerminalButton.setOnAction(_ => parent.drawTerminalPanel())
      nameLabel.setText(resources.println("username-label",userName))
      idLabel.setText(resources.println("id-label",userId))
    }

    private def showPopover(): Unit ={
      if(vBoxPopover.getChildren.isEmpty){
        popover.setContentNode(vBoxPopover)
      }
      popover.show(notificationButton)
    }

    override def drawManageAbsence(absences: List[InfoAbsenceOnDay]): Unit = {
      fillHolesView = FillHolesBox()
      baseManager.setCenter(fillHolesView.setParent(parent).pane)
      fillHolesView.drawAbsenceList(absences)
    }

    override def drawManageReplacement(replacement: List[InfoReplacement]): Unit = {
      fillHolesView.endLoading()
      fillHolesView.drawSubstituteList(replacement)
    }

    override def loadingReplacements(): Unit =
      fillHolesView.startLoading()

    override def startLoading(): Unit = {
      baseManager.setCenter(FXHelperFactory.loadingBox)
    }

    override def stopLoadingReplacements(): Unit =
      fillHolesView.endLoading()

    override def loadingResult(): Unit =
      selectResultBox.startLoading()

    override def stopLoadingResult(): Unit =
      selectResultBox.endLoading()

    override def drawRichiesta(terminal: List[Terminale]): Unit = {
      managerRichiestaBoxView = ManagerRichiestaBox(terminal)
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def reDrawRichiesta(): Unit = {
      managerRichiestaBoxView.reDrawRichiesta()
      baseManager.setCenter(managerRichiestaBoxView.setParent(parent).pane)
    }

    override def drawShiftRichiesta(listShift: List[Turno]): Unit = {
      managerRichiestaBoxView.drawShiftRequest(listShift)
    }

    override def drawChooseParams(terminals: List[Terminale]): Unit = {
      chooseParamsBox = ChooseParamsBox(terminals)
      baseManager.setCenter(chooseParamsBox.setParent(parent).pane)
    }

    override def drawWeekParams(params: ParamsForAlgorithm, rules: List[Regola]): Unit = {
      val box = ChangeSettimanaRichiesta(params, rules)
      baseManager.setCenter(box.setParent(parent).pane)
    }

    override def drawLoadedParam(param: InfoAlgorithm): Unit =
      chooseParamsBox.loadParam(param)

    override def drawGroupsParam(params: ParamsForAlgorithm, rule: List[Regola]): Unit = {
      gruopParamBox = GroupParamsBox(params, rule)
      baseManager.setCenter(gruopParamBox.setParent(parent).pane)
    }

    override def drawResultTerminal(terminal: List[Terminale]): Unit = {
      selectResultBox = SelectResultBox(terminal)
      baseManager.setCenter(selectResultBox.setParent(parent).pane)
    }

    override def drawResult(resultList: List[ResultAlgorithm], dateList: List[Date]): Unit = selectResultBox.createResult(resultList,dateList)

    private def consumeNotification(tag:Long): Unit ={
    }

    override def drawNotifica(str: String,tag:Long): Unit = {
      NotificationHelper.drawNotifica(str,tag, NotificationParameters(popover,vBoxPopover,consumeNotification))
    }

    override def updateGroup(group: Group): Unit =
      gruopParamBox.updateGroup(group)

    override def showParamAlgorithm(info: AlgorithmExecute, name: Option[String]): Unit = {

    }

    override def drawShowParams(info: AlgorithmExecute, name: Option[String], terminals: List[Terminale], rules: List[Regola]): Unit = {
      showParam = ShowParamAlgorithmBox(info, name, rules, terminals)
      baseManager.setCenter(showParam.setParent(parent).pane)
    }

    override def drawZona(zones: List[Zona]): Unit =
      baseManager.setCenter(zonaBox(zones))

    override def drawTerminal(zones: List[Zona], terminals: List[Terminale]): Unit =
      baseManager.setCenter(terminalBox(zones, terminals))

    private def zonaBox(zones: List[Zona]): Pane = {
      zonaView = ZonaBox(zones)
      zonaView.setParent(parent)
      zonaView.pane
    }

    private def terminalBox(zones: List[Zona], terminals: List[Terminale]): Pane = {
      terminalView = TerminalBox(zones, terminals)
      terminalView.setParent(parent)
      terminalView.pane
    }

  }


}
