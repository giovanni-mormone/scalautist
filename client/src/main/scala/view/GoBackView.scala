package view

/**
* @author Giovanni Mormone.
 *
* Extension of [[view.DialogView]]. Adds the functionality to go back to the previous view.
*
* */
trait GoBackView extends DialogView {

  /**
   * Goes back to the previous view, if present;
   * does nothing otherwise.
   */
  def back(): Unit
}