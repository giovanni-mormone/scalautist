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

