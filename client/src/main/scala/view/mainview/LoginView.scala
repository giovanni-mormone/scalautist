package view.mainview

import view.BaseView


/**
 * @author Giovanni Mormone.
 *
 * A view to manage login functionalities.
 * It extends [[view.BaseView]]
 *
 */
trait LoginView extends BaseView{
  /**
   * show a message in the modal like a pop-up
   *
   * @param message
   *                String of message to show
   */
  def result(message: String): Unit

  /**
   * Method that shows a message of error in case of a bad login(e.g. wrong username or password)
   */
  def badLogin():Unit

  /**
   * Method called to notify a login by a user not validated. it opens a
   * [[view.fxview.mainview.ChangePasswordView]].
   *
   */
  def firstUserAccess(): Unit

  /**
   * Method called to notify a login by a conducente user; it opens a
   * [[view.mainview.DriverView]].
   *
   */
  def driverAccess(): Unit
  /**
   * Method called to notify a login by a human resource user; it opens a
   * [[view.mainview.HumanResourceView]].
   *
   */
  def humanResourcesAccess(userName: String, userId:String): Unit
  /**
   * Method called to notify a login by a manager user; it opens a
   * [[view.fxview.mainview.ManagerViewFX]].
   *
   */
  def managerAccess(userName: String, userId:String): Unit


}
