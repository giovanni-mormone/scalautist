package controller


import caseclass.CaseClassDB.{Assenza, Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.{Assumi, Ferie, Id, Response}
import model.entity.HumanResourceModel
import model.utilsmodel.ModelUtils.id
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
  def getRecruitData(): Unit

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
  def getAllPersona(callingView: String): Unit
  def getAllPersona(): Unit

  /**
   * getZonaData method retrieves all data needed to draw zona view
   *
   */
  def getZonaData(): Unit

  /**
   * getTerminalData method retrieves all data needed to draw zona view
   *
   */
  def getTerminalData(): Unit
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
      model.recruit(persona)
      //println(persona)

    override def fires(ids: Set[Int]): Unit = {
      /*println(ids)*/
      if(ids.size > 1)
        model.firesAll(ids)
      else
        model.fires(ids.head)

      getAllPersona(EmployeeView.fire)
    }

    override def getAllPersona(callingView: String): Unit = {
       /*model.getAllPersone.onComplete(employees =>
              myView.drawEmployeeView(employees.get.head, callingView))*/
      val perosne = List(Persona("azer","baijan","123", None, 3, false, "gne", Some(2), matricola = Some(14)),
        Persona("ajeje","brazorf","123", None, 3, false, "gne", Some(2), matricola = Some(16)),
        Persona("samir","kebab","123", None, 3, false, "gne", Some(2), matricola = Some(18)),
        Persona("mangiapane","atradimento","123", None, 3, false, "gne", Some(2), matricola = Some(32)),
      )
      myView.drawEmployeeView(perosne, callingView)
    }

    override def getAllPersona(): Unit =
      myView.drawHolidayView(List(Ferie(1,"Fabain Andres",20)))
    /*model.getHolidayByPerson.onComplete {
      case Failure(exception) => myView.drawHolidayView(List(Ferie(1,"Fabain Andres",20)))
      case Success(value) => myView.drawHolidayView(List(Ferie(1,"Fabain Andres",20)))
    }*/

    override def illness(assenza: Assenza): Unit =
      model.illnessPeriod(assenza)

    override def holiday(assenza: Assenza): Unit =
      model.holidays(assenza)

    override def passwordRecovery(user: Int): Unit =
       model.passwordRecovery(user)

    def getZone: Future[Response[List[Zona]]] = model.getAllZone

    def getTurni: Future[Response[List[Turno]]] = model.getAllShift

    def getContratti: Future[Response[List[Contratto]]] = model.getAllContract

    override def getRecruitData(): Unit = {
      /*val future: Future[(List[Zona], List[Contratto], List[Turno])] = for{
          turns <- getTurni
          contracts <- getContratti
          zones <- getZone
        } yield (zones.head, contracts.head, turns.head)
      future.onComplete(data => myView.drawRecruit(data.get._1, data.get._2, data.get._3))*/

      val turni = List(Turno("bho","0-6",true,Some(1)), Turno("bho","6-12",true,Some(2)),
        Turno("bho","12-18",true,Some(3)), Turno("bho","18-0",true,Some(4)))
      val contratti = List(
        Contratto("Full-Time-5x2", turnoFisso = true, Some(1)),
        Contratto("Part-Time-5x2", turnoFisso = true, Some(2)),
        Contratto("Part-Time-6x1", turnoFisso = false,Some(3)),
        Contratto("Full-Time-6x1", turnoFisso = true, Some(4))
      )
      val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      myView.drawRecruit(zone, contratti, turni)
    }

    override def getTerminals(zona: Zona): Unit = {
     // model.getTerminalByZone(zona.idZone.head).onComplete(terminals => myView.drawTerminal(terminals.get.head))
      val terminale = List(Terminale("minestra", 3, Some(18)), Terminale("bistecca", 3, Some(81)),
        Terminale("occhio", 10, Some(108)), Terminale("lingua", 10, Some(180)), Terminale("maschera", 10, Some(8)))
      myView.drawTerminal(terminale.filter(terminale => terminale.idZona == zona.idZone.head))
    }

    override def getZonaData(): Unit = {
       //getZone.onComplete(zones => myView.drawZonaView(zones.get.head))
      val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      myView.drawZonaView(zone)
    }

    override def getTerminalData(): Unit = {
      //chiamata al model simile al recruit
      val zone = List(Zona("ciao", Some(3)), Zona("stronzo", Some(10)))
      val terminale = List(Terminale("minestra", 3, Some(18)), Terminale("bistecca", 3, Some(81)),
        Terminale("occhio", 10, Some(108)), Terminale("lingua", 10, Some(180)), Terminale("maschera", 10, Some(8)))
      myView.drawTerminaleView(zone, terminale)
    }

    override def saveZona(zone: Zona): Unit = {
       //model.setZona(zone).onComplete(_ => getZonaData())
       println(zone)
    }

    override def updateZona(zone: Zona): Unit =
      //model.updateZona(zone).onComplete(_ => myView.showMessage("Completato"))
      println(zone + "-> update")

    override def deleteZona(zone: Zona): Unit =
      //model.deleteZona(zone).onComplete(_ => myView.showMessage("Completato"))
      println(zone + "-> delete")

    override def saveTerminal(terminal: Terminale): Unit = {
      //model.newTerminale(terminale).onComplete(_ => getTerminalData())
      println(terminal)
    }

    override def updateTerminal(terminal: Terminale): Unit =
      //model.updateTerminal(terminal).onComplete(_ => myView.showMessage("Completato"))
      println(terminal + "-> update")

    override def deleteTerminal(terminal: Terminale): Unit =
      //model.deleteTerminal(terminal).onComplete(_ => myView.showMessage("Completato"))
      println(terminal + "-> delete")
      
    override def saveAbsence(absence: Assenza): Unit = {
       if(absence.malattia)
         model.illnessPeriod(absence).onComplete{result => sendMessageModal(result)}
       else
         model.holidays(absence).onComplete{result => sendMessageModal(result,isMalattia = false)}
    }
    private def sendMessageModal(t:Try[Response[Id]], isMalattia:Boolean=true):Unit = (t,isMalattia) match {
      case (Failure(_),true)  if -1== -1=>  myView.result("errore-malattie")
      case (Failure(_),false) => myView.result("Error assignando vacaciones")
      case (Success(value),true)  =>myView.result("malattia asignada correctamente")
      case (Success(value),false)  =>myView.result("vacaciones asignada correctamente")
      case (Success(_),_)  => myView.result("utente no encontrado")
    }


  }
}
