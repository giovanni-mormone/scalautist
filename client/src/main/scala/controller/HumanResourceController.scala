package controller

import java.sql.Date

import caseclass.CaseClassDB
import caseclass.CaseClassDB.{Contratto, Terminale, Turno, Zona}
import caseclass.CaseClassHttpMessage.Assumi
import model.entity.HumanResourceModel
import view.fxview.mainview.HumanResourceView
import model.utils.ModelUtils.id

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
   * getAllPersona asks model for the employees list
   */
  def getAllPersona: Unit //TODO quando i dati arrivano li faccio disegnare

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

  /**
   * getData method recovery all data that are requied to recruit employee
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

    override def recruit(persona: Assumi): Unit = model.recruit(persona)

    override def fires(ids: Set[Int]): Unit =
      if(ids.size > 1)
        model.firesAll(ids)
      else
        model.fires(ids.head)

    override def getAllPersona: Unit = model.getAllPersone

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
  }
}
