package view.mainviewoperations

trait HumanResourceOperations {
  //home operations
  def openRescruit(): Unit
  def openFire(): Unit
  def openSick(): Unit
  def openHoliday(): Unit
  def openChangePassword(): Unit
  def openZona(): Unit
  def openTerminal(): Unit
}

object HumanResourceOperations {

  def apply(): HumanResourceOperations = new HumanResourceOperationsImpl()

  private class HumanResourceOperationsImpl extends HumanResourceOperations {
    override def openRescruit(): Unit = ???

    override def openFire(): Unit = ???

    override def openSick(): Unit = ???

    override def openHoliday(): Unit = ???

    override def openChangePassword(): Unit = ???

    override def openZona(): Unit = ???

    override def openTerminal(): Unit = ???
  }
}