package view

/**
 * @author Giovanni Mormone, Fabian Aspee Encina
 *
 * Extension of [[view.BaseView]]. Adds the functionality to show a dialog with a message.
 */
trait DialogView extends BaseView{
  /**
   * Shows the provided message to the user.
   * @param message
   */
  def showMessage(message:String): Unit

  /**
   * Shows a message loaded from a resource bundle to the user.
   * @param key
   */
  def showMessageFromKey(key:String):Unit

  /**
   *
   * @param message
   * @return
   */
  def alertMessage(message:String):Boolean
}