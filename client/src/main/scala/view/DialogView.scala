package view

/**
 * @author Giovanni Mormone.
 *
 * Extension of [[view.BaseView]]. Adds the functionality to show a dialog with a message.
 */
trait DialogView extends BaseView{
  /**
   * Shows the provided message to the user.
   * @param message
   */
  def showMessage(message:String): Unit

  def showMessageFromKey(message:String):Unit

  def alertMessage(message:String):Boolean
}