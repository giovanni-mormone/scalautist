package view

/**
 * Basic definition of view functionalities.
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
 * Extension of [[BaseView]]. Adds the functionality to go back to the previous view.
 */
trait GoBackView extends BaseView{

  /**
   * Goes back to the previous view, if present;
   * does nothing otherwise.
   */
  def back(): Unit
}
