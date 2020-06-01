package controller

/**
 * @author Giovanni Mormone.
 *
 * Utilities for the controller; It is now used to save the userId and username of
 * a logged user, to make it sharable between controllers.
 *
 */
private[controller] object Utils {
  var userId:Int = _
  var username:String = _
}
