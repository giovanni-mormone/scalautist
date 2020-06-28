package view

/**
 * @author Giovanni Mormone.
 *
 * Basic definition of view functionalities.
 *
 */
trait BaseView {

  /**
   * Shows the view. If alredy shown does nothing
   */
  def show(): Unit

  /**
   * Hides the view. If alredy hidden does nothing
   */
  def hide(): Unit

  /**
   * Closes the view.
   */
  def close(): Unit

}

/**
 * @author Giovanni Mormone.
 *
 * Extension of [[BaseView]]. Adds the functionality to show a dialog with a message.
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

/**
 * @author Giovanni Mormone.
 *
 * Extension of [[DialogView]]. Adds the functionality to go back to the previous view.
 *
 * */
trait GoBackView extends DialogView {

  /**
   * Goes back to the previous view, if present;
   * does nothing otherwise.
   */
  def back(): Unit
}
