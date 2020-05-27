package controller

import view.fxview.mainview.ChangePasswordView

/**
 * A change password controller for a view of type [[view.fxview.mainview.ChangePasswordView]]
 */
trait ChangePasswordController extends AbstractController[ChangePasswordView]{
  /**
   * Tries to change the password of a user in the system. Gets the params submitted by a [[view.fxview.mainview.ChangePasswordView]]
   * * and tells to the view to change accordingly.
   *
   * @param password
   *                The new password of the user.
   * @param userID
   *               The id of the user whose password should be changed.
   *
   */
  def changePassword(password: String, userID: Int)
}

/**
 * Companion object of [[controller.ChangePasswordController]]
 */
object ChangePasswordController{
  private val instance = new ChangePasswordControllerImpl

  def apply(): ChangePasswordController = instance

  private class ChangePasswordControllerImpl extends ChangePasswordController{
    override def changePassword(password: String, userID: Int): Unit = password match{
      case x if x.length > 8 => {println("OK!", x); myView.back}
      case _ => println("ERROR")
    }
  }
}
