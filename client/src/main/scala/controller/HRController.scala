package controller

import java.sql.Date

import caseclass.CaseClassDB.Persona
import model.entity.HumanResourceModel
import view.fxview.mainview.HumanResourceView

/**
 * @author Francesco Cassano
 *
 */
trait HumanResourceController extends AbstractController[HumanResourceView] {

  def recruit(persona:Persona): Unit

  def fires(ids:Set[Int]): Unit

  def getAllPersona(): Unit //TODO quando i dati arrivano li faccio disegnare

  def illness(idPersona: Int, startDate: Date, endDate: Date): Unit

  def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit

  def passwordRecovery(user: Int): Unit //TODO quando i dati arrivano li faccio disegnare
}

object HumanResourceController {
  private val instance = new HumanResourceControllerImpl()
  private val model = HumanResourceModel()

  def apply(): HumanResourceController = instance

  private class HumanResourceControllerImpl extends HumanResourceController {

    override def recruit(persona: Persona): Unit = model.recruit(persona)

    override def fires(ids: Set[Int]): Unit = model.fires(ids)

    override def getAllPersona(): Unit = model.getAllPersone()

    override def illness(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.illnessPeriod(idPersona, startDate,endDate)

    override def holiday(idPersona: Int, startDate: Date, endDate: Date): Unit =
      model.holidays(idPersona, startDate, endDate)

    override def passwordRecovery(user: Int): Unit = model.passwordRecovery(user)
  }
}
