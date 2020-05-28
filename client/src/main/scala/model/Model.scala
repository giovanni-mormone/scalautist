package model

/**
 * Generic definition of model
 */
trait Model {

  protected val dispatcher = ModelDispatcher()

  protected implicit val system = dispatcher.system

  protected def getURI(request: String) = dispatcher.address + "/" + request

  //protected def composePostRequest(request: String)
}
