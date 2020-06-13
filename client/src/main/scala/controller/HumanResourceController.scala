package controller

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie, Response}
import messagecodes.StatusCodes
import model.entity.HumanResourceModel
import view.fxview.mainview.HumanResourceView

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

/**
 * @author Francesco Cassano
 *
 * A HumanResource controller for a view of type [[view.fxview.mainview.HumanResourceView]]
 */
trait HumanResourceController extends AbstractController[HumanResourceView] {



  /**
   * Absence saves a new absence on the db
   *
   * @param absence
   *                instance of [[caseclass.CaseClassDB.Assenza]]
   */
  def saveAbsence(absence: Assenza): Unit

  /**
   * Recruit saves a new employee on the db
   *
   * @param persona
   *                instance of the employee to save. It's [[caseclass.CaseClassHttpMessage.Assumi]] instance
   */
  def recruit(persona: Assumi): Unit

  /**
   * Fires deletes one or more employee from the db
   *
   * @param ids
   *            Set of integer that represent employees' ids
   */
  def fires(ids:Set[Int]): Unit

  /**
   * illness saves on the db an employee's absence for a period of time
   *
   * @param assenza
   *                instance of [[caseclass.CaseClassDB.Assenza]]
   */
  def illness(assenza: Assenza): Unit

  /**
   * Holiday saves on the db an employee's absence for a period of time
   *
   * @param assenza
   *                instance of [[caseclass.CaseClassDB.Assenza]]
   */
  def holiday(assenza: Assenza): Unit

  /**
   * PasswordRecovery asks the system for create new credential for a user
   * @param user
   *             User's id
   */
  def passwordRecovery(user: Int): Unit

  /**
   * save a new zone into db
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to save
   */
  def saveZona(zone: Zona): Unit

  /**
   * update selected zone
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to update
   */
  def updateZona(zone: Zona): Unit

  /**
   * delete selected zone
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]] to delete
   */
  def deleteZona(zone: Zona): Unit

  /**
   * insert terminal into db
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to save
   */
  def saveTerminal(terminal: Terminale): Unit

  /**
   * update selected terminal
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to update
   */
  def updateTerminal(terminal: Terminale): Unit

  /**
   * delete selected terminal
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]] to delete
   */
  def deleteTerminal(terminal: Terminale): Unit

  /**
   * getRecruitData method retrieves all data needed to recruit employee
   *
   */
  def dataToRecruit(): Unit

  /**
   * Return all terminals in a zone
   *
   * @param zona
   *             The zone of interest
   */
  def getTerminals(zona: Zona): Unit

  /**
   * getAllPersona asks model for the employees list
   *
   * @param callingView
   *                    String that define which box the view must show
   */
  def dataToFireAndIll(callingView: String): Unit

  /**
   * asks model for the employees list and their holiday
   *
   */
  def dataToHoliday(): Unit

  /**
   * getZonaData method retrieves all data needed to draw zona view
   *
   */
  def dataToZone(): Unit

  /**
   * getTerminalData method retrieves all data needed to draw zona view
   *
   */
  def dataToTerminal(): Unit

  /**
   * draw the terminal modal to manage it
   *
   * @param terminalId
   *                   terminal id to manage
   */
  def terminalModalData(terminalId: Int): Unit

  /**
   * Verify if a driver have absence within year
   * @param item case class that identifies driver and his holiday in a year
   * @param isMalattia represent if absence is for illness or holiday
   */
  def absencePerson(item: Ferie,isMalattia:Boolean): Unit
}

/**
 * @author Francesco Cassano
 *
 * Companion object of [[controller.HumanResourceController]] [Singleton]
 *
 */
object HumanResourceController {
  private val instance = new HumanResourceControllerImpl()
  private val model = HumanResourceModel()

  def apply(): HumanResourceController = instance

  val UNKNOWN = "Unknown"

  /**
   * HumanResourceController implementation
   */
  private class HumanResourceControllerImpl extends HumanResourceController {

    //////////////////////////////////////////////////////////////////////////////// general method

    private def responseValutation[A](result: Try[Response[A]],
                                      successA: A => Unit,
                                      failurA: String => Unit,
                                      messageOnModal: Boolean = false,
                                      showSuccess: Boolean = true): Unit = {
      result match {
        case Success(response) if response.statusCode == StatusCodes.SUCCES_CODE =>
          if(showSuccess)
            showResult(messageOnModal, "success")
          if(response.payload.isDefined)
            successA(response.payload.get)
        case _ => notSuccessCodes[A](result, failurA, messageOnModal)
      }
    }

    private def notSuccessCodes[A](result: Try[Response[A]], failurA: String => Unit, messageOnModal: Boolean = false): Unit =
      result match {
        case Success(response) if response.statusCode == StatusCodes.NOT_FOUND =>
          failureAction("NotFound", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.BAD_REQUEST =>
          failureAction("BadRequest", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE1 =>
          failureAction("Error1", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE2 =>
          failureAction("Error2", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE3 =>
          failureAction("Error3", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE4 =>
          failureAction("Error4", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE5 =>
          failureAction("Error5", failurA, messageOnModal)
        case Success(response) if response.statusCode == StatusCodes.ERROR_CODE6 =>
          failureAction("Error6", failurA, messageOnModal)
        case Failure(_) =>
          failureAction(UNKNOWN, failurA, messageOnModal)
        case _ => failureAction(UNKNOWN, failurA, messageOnModal)
      }

    private def failureAction[A](message: String = UNKNOWN, failurA: String => Unit, messageOnModal: Boolean): Unit = {
      showResult(messageOnModal, message)
      failurA(message)
    }

    private def showResult[A](messageOnModal: Boolean, message: String): Unit = {
      if (messageOnModal)
        myView.result(message)
      else
        myView.dialog(message)
    }

    //////////////////////////////////////////////////////////////////////////////// system -> db

    override def recruit(persona: Assumi): Unit =
      model.recruit(persona).onComplete(result =>
        responseValutation[Login](result,
          login => showResult(messageOnModal = false, login.user + ": " + login.password), _ => None, showSuccess = false))

    override def fires(ids: Set[Int]): Unit = {
      //println(ids)
      val future: Future[Response[Int]] =
          if(ids.size > 1)
            model.firesAll(ids)
          else
            model.fires(ids.head)

      future.onComplete(result => responseValutation[Int](result, _ => None, _ => None))
    }

    override def illness(assenza: Assenza): Unit =
      model.illnessPeriod(assenza).onComplete(result => responseValutation[Int](result, _ => None, _ => None))

    override def holiday(assenza: Assenza): Unit =
      model.holidays(assenza).onComplete(result => responseValutation[Int](result, _ => None, _ => None))

    override def saveZona(zone: Zona): Unit =
      model.setZona(zone).onComplete(result => responseValutation[Zona](result, _ => None, _ => None))

    override def updateZona(zone: Zona): Unit =
      model.updateZona(zone)
        .onComplete(result => responseValutation[Int](result, _ => None, _ => None, messageOnModal = true))

    override def deleteZona(zone: Zona): Unit =
      model.deleteZona(zone.idZone.head)
        .onComplete(result => responseValutation[Zona](result, _ => None, _ => None, messageOnModal = true))

    override def saveTerminal(terminal: Terminale): Unit =
      model.createTerminale(terminal).onComplete(result => responseValutation[Terminale](result, _ => None, _ => None))

    override def updateTerminal(terminal: Terminale): Unit =
      model.updateTerminale(terminal)
        .onComplete(result => responseValutation[Int](result, _ => None, _ => None, messageOnModal = true))

    override def deleteTerminal(terminal: Terminale): Unit =
      model.deleteTerminale(terminal.idTerminale.head)
        .onComplete(result => responseValutation[Int](result, _ => None, _ => None, messageOnModal = true))

    override def saveAbsence(absence: Assenza): Unit = {
      if(absence.malattia)
        model.illnessPeriod(absence).onComplete{result => sendMessageModal(result)}
      else
        model.holidays(absence).onComplete{result => sendMessageModal(result,isMalattia = false)}
    }

    private def sendMessageModal(t:Try[Response[Int]], isMalattia:Boolean=true):Unit = (t,isMalattia) match {
      case (Failure(_),true)  =>  myView.result("errore-malattie")
      case (Failure(_),false) => myView.result("Error assignando vacaciones")
      case (Success(value),true)  =>myView.result("Malattia Inserite Correttamente")
      case (Success(value),false)  =>myView.result("Ferie Assegnate Correttamente")
      case (Success(_),_)  => myView.result("utente no encontrado")
    }

    //////////////////////////////////////////////////////////////////////////////// db -> system

    override def dataToFireAndIll(callingView: String): Unit =
      model.getAllPersone.onComplete(employees =>
            responseValutation[List[Persona]](employees,
              employeeList => myView.drawEmployeeView(employeeList, callingView),
              _ => None,
              showSuccess = false)
      )

    override def dataToHoliday(): Unit =
      model.getHolidayByPerson.onComplete(employees =>
        responseValutation[List[Ferie]](employees,
          employeeList => myView.drawHolidayView(employeeList),
          _ => None,
          showSuccess = false)
      )

    override def dataToRecruit(): Unit = {
      case class recruitData(zoneL: Response[List[Zona]], contractL: Response[List[Contratto]], shiftL: Response[List[Turno]])
      val future: Future[recruitData] = for {
          turns <- getTurni
          contracts <- getContratti
          zones <- getZone
        } yield recruitData(zones, contracts, turns)
      future.onComplete {
        case Success(data) if data.contractL.statusCode == StatusCodes.SUCCES_CODE &&
            data.shiftL.statusCode == StatusCodes.SUCCES_CODE &&
            data.zoneL.statusCode == StatusCodes.SUCCES_CODE =>
          myView.drawRecruit(data.zoneL.payload.get, data.contractL.payload.get, data.shiftL.payload.get)
        case Success(data) if data.zoneL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.zoneL), _ => None)
        case Success(data) if data.shiftL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.shiftL), _ => None)
        case Success(data) if data.zoneL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.zoneL), _ => None)
        case _ => showResult(messageOnModal = false, UNKNOWN)
      }
    }

    override def dataToZone(): Unit =
       getZone.onComplete(zones =>
         responseValutation[List[Zona]](zones,
           zone => myView.drawZonaView(zone),
           _ => None,
           showSuccess = false)
       )

    override def dataToTerminal(): Unit = {
      case class TerminalData(zoneL: Response[List[Zona]], terminalL: Response[List[Terminale]])
      val future: Future[TerminalData] = for {
        terminals <- model.getAllTerminale
        zones <- getZone
      } yield TerminalData(zones, terminals)
      future.onComplete {
        case Success(data) if data.terminalL.statusCode == StatusCodes.SUCCES_CODE &&
            data.zoneL.statusCode == StatusCodes.SUCCES_CODE =>
          myView.drawTerminaleView(data.zoneL.payload.head, data.terminalL.payload.head)
        case Success(data) if data.terminalL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.terminalL), _ => None)
        case Success(data) if data.zoneL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.zoneL), _ => None)
        case _ => showResult(messageOnModal = false, UNKNOWN)
      }
    }

    override def getTerminals(zona: Zona): Unit =
      model.getTerminalByZone(zona.idZone.head).onComplete(terminals =>
        responseValutation[List[Terminale]](terminals,
          terminal => myView.drawTerminal(terminal),
          _ => None,
          showSuccess = false)
      )

    override def terminalModalData(terminalId: Int): Unit = {
      case class terminalMData(zoneL: Response[List[Zona]], terminalS: Response[Terminale])
      val future: Future[terminalMData] = for{
        zones <- getZone
        terminal <- model.getTerminale(terminalId)
      } yield terminalMData(zones, terminal)
      future.onComplete {
        case Success(data) if data.terminalS.statusCode == StatusCodes.SUCCES_CODE &&
          data.zoneL.statusCode == StatusCodes.SUCCES_CODE =>
          myView.openTerminalModal(data.zoneL.payload.head, data.terminalS.payload.head)
        case Success(data) if data.terminalS.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.terminalS), _ => None)
        case Success(data) if data.zoneL.statusCode != StatusCodes.SUCCES_CODE =>
          notSuccessCodes(Try(data.zoneL), _ => None)
        case _ => showResult(messageOnModal = false, UNKNOWN)
      }
    }

    override def absencePerson(item:Ferie,isMalattia:Boolean): Unit =
      model.getAbsenceInYearForPerson(item.idPersona)
      .onComplete {
        case Failure(exception) =>   myView.result("errore-malattie")
        case Success(value) =>value.payload.foreach(assenza=>myView.drawModalAbsenceHoliday(item,isMalattia,assenza))
      }
    override def passwordRecovery(user: Int): Unit =
      model.passwordRecovery(user)

    private def getZone: Future[Response[List[Zona]]] =
      model.getAllZone

    private def getTurni: Future[Response[List[Turno]]] =
      model.getAllShift

    private def getContratti: Future[Response[List[Contratto]]] =
      model.getAllContract

  }
}
