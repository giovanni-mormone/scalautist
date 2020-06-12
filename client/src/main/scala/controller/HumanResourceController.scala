package controller

import caseclass.CaseClassDB._
import caseclass.CaseClassHttpMessage.{Assumi, Ferie, Response}
import messagecodes.StatusCodes
import model.entity.HumanResourceModel
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView
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
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]]
   */
  def saveZona(zone: Zona): Unit

  /**
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]]
   */
  def updateZona(zone: Zona): Unit

  /**
   *
   * @param zone
   *             instance of [[caseclass.CaseClassDB.Zona]]
   */
  def deleteZona(zone: Zona): Unit

  /**
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]]
   */
  def saveTerminal(terminal: Terminale): Unit

  /**
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]]
   */
  def updateTerminal(terminal: Terminale): Unit

  /**
   *
   * @param terminal
   *                  instance of [[caseclass.CaseClassDB.Terminale]]
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
   */
  def dataToFireAndIll(callingView: String): Unit
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
   *
   * @param terminalId
   */
  def terminalModalData(terminalId: Int): Unit
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

  /**
   * HumanResourceController implementation
   */
  private class HumanResourceControllerImpl extends HumanResourceController {

    override def recruit(persona: Assumi): Unit =
      model.recruit(persona).onComplete {
        case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
          val login: Login = result.payload.get
          myView.dialog("new credential:\n user: " + login.user + "\n psw: " + login.password)
          dataToRecruit()
        case Failure(_) => myView.dialog("error")
        case _ => myView.message("errore sconosciuto")
      }

    override def fires(ids: Set[Int]): Unit = {
      println(ids)
      val future: Future[Response[Int]] =
          if(ids.size > 1)
            model.firesAll(ids)
          else
            model.fires(ids.head)

      future.onComplete {
        case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
          myView.dialog("operazione completata")
          dataToFireAndIll(EmployeeView.fire)
        case Failure(_) =>
          myView.dialog("ritenta!")
          dataToFireAndIll(EmployeeView.fire)
        case _ => myView.dialog("errore sconosciuto")
      }
    }

    override def dataToFireAndIll(callingView: String): Unit = {
       model.getAllPersone.onComplete(employees =>
              myView.drawEmployeeView(employees.get.payload.head, callingView))
      /*val perosne = List(Persona("azer","baijan","123", None, 3, false, "gne", Some(2), matricola = Some(14)),
        Persona("ajeje","brazorf","123", None, 3, false, "gne", Some(2), matricola = Some(16)),
        Persona("samir","kebab","123", None, 3, false, "gne", Some(2), matricola = Some(18)),
        Persona("mangiapane","atradimento","123", None, 3, false, "gne", Some(2), matricola = Some(32)),
      )
      myView.drawEmployeeView(perosne, callingView)*/
    }

    override def dataToHoliday(): Unit =
      //myView.drawHolidayView(List(Ferie(1,"Fabain Andres",20)))
      model.getHolidayByPerson.onComplete {
        case Failure(exception) => myView.dialog("error")
        case Success(value) => myView.drawHolidayView(value.payload.head)
      }

    override def illness(assenza: Assenza): Unit =
      model.illnessPeriod(assenza).onComplete {
        case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
          myView.dialog("operazione completata")
          dataToFireAndIll(EmployeeView.ill)
        case Failure(_) =>
          myView.dialog("ritenta!")
          dataToFireAndIll(EmployeeView.ill)
        case _ => myView.dialog("errore sconosciuto")
      }

    override def holiday(assenza: Assenza): Unit =
      model.holidays(assenza).onComplete {
        case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
          myView.dialog("operazione completata")
          dataToHoliday()
        case Failure(_) =>
          myView.dialog("ritenta!")
          dataToHoliday()
        case _ => myView.dialog("errore sconosciuto")
      }

    override def passwordRecovery(user: Int): Unit =
       model.passwordRecovery(user)

    private def getZone: Future[Response[List[Zona]]] =
      model.getAllZone

    private def getTurni: Future[Response[List[Turno]]] =
      model.getAllShift

    private def getContratti: Future[Response[List[Contratto]]] =
      model.getAllContract

    override def dataToRecruit(): Unit = {
      val future: Future[(List[Zona], List[Contratto], List[Turno])] = for{
          turns <- getTurni
          contracts <- getContratti
          zones <- getZone
        } yield (zones.payload.head, contracts.payload.head, turns.payload.head)
      future.onComplete(data => myView.drawRecruit(data.get._1, data.get._2, data.get._3))

      /*val turni = List(Turno("bho","0-6",true,Some(1)), Turno("bho","6-12",true,Some(2)),
        Turno("bho","12-18",true,Some(3)), Turno("bho","18-0",true,Some(4)))
      val contratti = List(
        Contratto("Full-Time-5x2", turnoFisso = true, partTime = true, 1),
        Contratto("Part-Time-5x2", turnoFisso = true,partTime = true, 2),
        Contratto("Part-Time-6x1", turnoFisso = false,partTime = false,3),
        Contratto("Full-Time-6x1", turnoFisso = true, partTime = false,4)
      )
      val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      myView.drawRecruit(zone, contratti, turni)*/
    }

    override def getTerminals(zona: Zona): Unit = {
      model.getTerminalByZone(zona.idZone.head).onComplete(terminals => myView.drawTerminal(terminals.get.payload.head))
      /*val terminale = List(Terminale("minestra", 3, Some(18)), Terminale("bistecca", 3, Some(81)),
        Terminale("occhio", 10, Some(108)), Terminale("lingua", 10, Some(180)), Terminale("maschera", 10, Some(8)))
      myView.drawTerminal(terminale.filter(terminale => terminale.idZona == zona.idZone.head))*/
    }

    override def dataToZone(): Unit = {
       getZone.onComplete(zones => myView.drawZonaView(zones.get.payload.head))
      /*val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      myView.drawZonaView(zone)*/
    }

    override def dataToTerminal(): Unit = {
      val future: Future[(List[Zona], List[Terminale])] = for{
        terminals <- model.getAllTerminale
        zones <- getZone
      } yield (zones.payload.head, terminals.payload.head)
      future.onComplete(data => myView.drawTerminaleView(data.get._1, data.get._2))
      /*val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      val terminale = List(Terminale("minestra", 3, Some(18)), Terminale("bistecca", 3, Some(81)),
        Terminale("occhio", 10, Some(108)), Terminale("lingua", 10, Some(180)), Terminale("maschera", 10, Some(8)))
      myView.drawTerminaleView(zone, terminale)*/
    }

    override def saveZona(zone: Zona): Unit =
       model.setZona(zone).onComplete {
         case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
           myView.dialog("operazione completata")
           dataToZone()
         case Failure(_) =>
           myView.dialog("ritenta!")
           dataToZone()
         case _ => myView.dialog("errore sconosciuto")
       }

    override def updateZona(zone: Zona): Unit =
      model.updateZona(zone).onComplete(_ => myView.result("Completato"))

    override def deleteZona(zone: Zona): Unit =
      model.deleteZona(zone.idZone.head).onComplete(_ => myView.result("Completato"))

    override def saveTerminal(terminal: Terminale): Unit =
      model.createTerminale(terminal).onComplete {
        case Success(result) if result.statusCode == StatusCodes.SUCCES_CODE =>
          myView.dialog("operazione completata")
          dataToTerminal()
        case Failure(_) =>
          myView.dialog("ritenta!")
          dataToTerminal()
        case _ => myView.dialog("errore sconosciuto")
      }

    override def updateTerminal(terminal: Terminale): Unit =
      model.updateTerminale(terminal).onComplete(_ => myView.result("Completato"))

    override def deleteTerminal(terminal: Terminale): Unit =
      model.deleteTerminale(terminal.idTerminale.head).onComplete(_ => myView.result("Completato"))

    override def saveAbsence(absence: Assenza): Unit = {
       if(absence.malattia)
         model.illnessPeriod(absence).onComplete{result => sendMessageModal(result)}
       else
         model.holidays(absence).onComplete{result => sendMessageModal(result,isMalattia = false)}
    }

    private def sendMessageModal(t:Try[Response[Int]], isMalattia:Boolean=true):Unit = (t,isMalattia) match {
      case (Failure(_),true)  =>  myView.result("errore-malattie")
      case (Failure(_),false) => myView.result("Error assignando vacaciones")
      case (Success(value),true)  =>myView.result("malattia asignada correctamente")
      case (Success(value),false)  =>myView.result("vacaciones asignada correctamente")
      case (Success(_),_)  => myView.result("utente no encontrado")
    }

    override def terminalModalData(terminalId: Int): Unit = {
      val future: Future[(List[Zona], Terminale)] = for{
        zones <- getZone
        terminal <- model.getTerminale(terminalId)
      } yield (zones.payload.head, terminal.payload.head)
      future.onComplete(data => myView.openTerminalModal(data.get._1, data.get._2))
    }

  }
}
