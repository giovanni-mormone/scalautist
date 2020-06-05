package controller

import java.sql.Date

import caseclass.CaseClassDB.{Contratto, Persona, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import model.entity.HumanResourceModel
import view.fxview.mainview.HumanResourceView
import model.utils.ModelUtils.id
import utils.UserType
import view.fxview.component.HumanResources.subcomponent.util.EmployeeView

import scala.concurrent.Future

/**
 * @author Francesco Cassano
 *
 * A HumanResource controller for a view of type [[view.fxview.mainview.HumanResourceView]]
 */
trait HumanResourceController extends AbstractController[HumanResourceView] {

  /**
   * Recruit saves a new employee on the db
   *
   * @param persona
   *                instance of the employee to save. It's [[caseclass.CaseClassHttpMessage.Assumi]] instance //todo
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
   * @param idPersona
   *                  It's the employee's id
   * @param startDate
   *                  The date the desease starts
   * @param endDate
   *                The date the desease ends
   */
  def illness(idPersona: Int, startDate: Date, endDate: Date): Unit

  /**
   * Holiday saves on the db an employee's absence for a period of time
   *
   * @param idPersona
   *                  It's the employee's id
   * @param startDate
   *                  The date the desease starts
   * @param endDate
   *                The date the desease ends
   */
  def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit

  /**
   * PasswordRecovery asks the system for create new credential for a user
   * @param user
   *             User's id
   */
  def passwordRecovery(user: Int): Unit //TODO quando i dati arrivano li faccio disegnare

  def saveZona(zone: Zona): Unit

  /**
   * getRecruitData method retrieves all data needed to recruit employee
   *
   */
  def getRecruitData: Unit

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
  def getAllPersona(callingView: String): Unit //TODO quando i dati arrivano li faccio disegnare

  /**
   * getZonaData method retrieves all data needed to draw zona view
   *
   */
  def getZonaData(): Unit

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
      println(persona)
      //model.recruit(persona)

    override def fires(ids: Set[Int]): Unit = {
      println(ids)
      /*if(ids.size > 1)
        model.firesAll(ids)
      else
        model.fires(ids.head)*/
      //getAllPersona(EmployeeView.fire)
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

    override def illness(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.illnessPeriod(idPersona, startDate,endDate)

    override def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.holidays(idPersona, startDate, endDate)

    override def passwordRecovery(user: Int): Unit =
       model.passwordRecovery(user)

    def getZone: Future[Option[List[Zona]]] = model.getAllZone

    def getTurni: Future[Option[List[Turno]]] = model.getAllShift

    def getContratti: Future[Option[List[Contratto]]] = model.getAllContract

    override def getRecruitData: Unit = {
      /*val future: Future[(List[Zona], List[Contratto], List[Turno])] = for{
          turns <- getTurni
          contracts <- getContratti
          zones <- getZone
        } yield (zones.head, contracts.head, turns.head)
      future.onComplete(data => myView.drawRecruit(data.get._1, data.get._2, data.get._3))
       */
      val turni = List(Turno("bho","0-6",Some(1)), Turno("bho","6-12",Some(2)),
        Turno("bho","12-18",Some(3)), Turno("bho","18-0",Some(4)))
      val contratti = List(Contratto("Full-Time-5x2", true,Some(1)), Contratto("Part-Time-5x2", true,Some(2)),
        Contratto("Part-Time-6x1", false,Some(3)), Contratto("Full-Time-6x1", true,Some(4)))
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

    override def saveZona(zone: Zona): Unit = {
      //model.newZona(zone).onComplete(_ => getZonaData)
      println(zone)
    }
  }
}
