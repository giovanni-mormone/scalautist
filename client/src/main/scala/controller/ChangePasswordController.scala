package controller

import messagecodes.StatusCodes
import model.entity.PersonaModel
import regularexpressionutilities.PasswordHelper
import view.mainview.ChangePasswordView

import scala.util.Success

/**
 * @author Giovanni Mormone.
 *
 * A change password controller for a view of type [[view.mainview.ChangePasswordView]]
 *
 */
trait ChangePasswordController extends AbstractController[ChangePasswordView]{
  /**
   * Tries to change the password of a user in the system. Gets the params submitted by a [[view.mainview.ChangePasswordView]]
   * * and tells to the view to change accordingly.
   *
   * @param oldPassword
   *                The old password of the user.
   * @param newPassword
   *                The new password of the user.
   *
   */
  def changePassword(oldPassword: String, newPassword:String): Unit
}

/**
 * @author Giovanni Mormone.
 *
 * Companion object of [[controller.ChangePasswordController]]
 *
 */
object ChangePasswordController{
  private val instance = new ChangePasswordControllerImpl
  private val myModel = PersonaModel()

  def apply(): ChangePasswordController = instance

  private class ChangePasswordControllerImpl extends ChangePasswordController{
    override def changePassword(oldPassword: String, newPassword: String): Unit = newPassword match{
      case x if PasswordHelper.passwordRegex().matches(x) =>
        myModel.changePassword(Utils.userId.getOrElse(0),oldPassword,newPassword).onComplete{
          case Success(value) if value.statusCode == StatusCodes.NOT_FOUND => myView.errorChange()
          case Success(_) => myView.okChange()
          case _ => myView.errorChange()
        }
      case _ =>  myView.errorChange()
    }
  }
}
