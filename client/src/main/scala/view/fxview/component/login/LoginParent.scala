package view.fxview.component.login


/**
 * @author Giovanni Mormone.
 *
 *         A LoginParent is the container of a [[view.fxview.component.login.LoginBox]] [[view.fxview.component.Component]]
 *
 */
trait LoginParent{
  /**
   * Method called to submit user credentials. It should be called by a [[view.fxview.component.login.LoginBox]]
   *
   * @param username
   *                 The username of the user.
   * @param password
   *                 The password of the user.
   */
  def login(username: String, password: String): Unit
}
