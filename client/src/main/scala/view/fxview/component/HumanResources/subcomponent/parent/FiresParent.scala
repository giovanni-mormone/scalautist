package view.fxview.component.HumanResources.subcomponent.parent

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by fire views to make requests to controller
 */
trait FiresParent {

  /**
   * If fire button is clicked the controller is asked to delete the instance of persona
   *
   * @param employees
   *                  Sequence of employees Ids to delete
   */
  def fireClicked(employees: Seq[Int])

}
