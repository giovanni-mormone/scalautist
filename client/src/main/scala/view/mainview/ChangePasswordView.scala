package view.mainview

import view.GoBackView

/**
 * @author Giovanni Mormone.
 *
 * A view to manage the change of the password functionality.
 * It should be [[view.GoBackView]] to return to the previous view
 * once the password is changed.
 */
trait ChangePasswordView extends GoBackView{

  /**
   * Tells to the view that an error as occurred during the attempt to change the password
   */
  def errorChange():Unit

  /**
   * Tells to the view that the password was successfully updated
   */
  def okChange():Unit
}