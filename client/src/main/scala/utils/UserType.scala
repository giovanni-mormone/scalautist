package utils

/**
 * @author Francesco Cassano
 *
 * Generalize User type
 */
object UserType {

  val manager: String = "Manager di sistema"
  val humanResource: String = "Risorse umane"
  val driver: String = "Autista"

  private val managerCode: Int = 1
  private val humanResourceCode: Int = 2
  private val driverCode: Int = 3

  val getCode: String => Int = {
    case code if code.equals(manager) => managerCode
    case code if code.equals(humanResource) => humanResourceCode
    case code if code.equals(driver) => driverCode
  }

  val getUser: Int => String = {
    case code if code == managerCode => manager
    case code if code == humanResourceCode => humanResource
    case code if code == driverCode => driver
  }

  val getUserType: List[String] = List(manager, humanResource, driver)

}
