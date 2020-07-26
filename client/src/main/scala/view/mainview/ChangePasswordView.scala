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

  def errorChange():Unit

  def okChange():Unit
}