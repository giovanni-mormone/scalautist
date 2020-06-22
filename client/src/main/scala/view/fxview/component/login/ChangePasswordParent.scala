package view.fxview.component.login


/**
 * @author Giovanni Mormone.
 *
 * A ChangePasswordParent is the container of a [[view.fxview.component.login.ChangePasswordBox]]
 *
 */
trait ChangePasswordParent{
  /**
   * Called to submit the new password chosen by the user. It should be called by a
   * @param oldPassword
   *                    The oldPassword submitted by the user.
   * @param newPassword
   *                    The newPassword submitted by the user.
   */
  def changePass(oldPassword:String, newPassword: String)
}
