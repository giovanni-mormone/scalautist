package view.fxview.component.manager.subcomponent.parent

trait FillHolesParent {

  /**
   * Method used by a [[view.fxview.component.manager.subcomponent.TerminalAndTurnsBox]] to tell it's parent
   * the absence to substitute selected by the user
   * @param idRisultato
   *                    The id of the result to substitute
   * @param idTerminale
   *                    The id of the terminal
   * @param idTurno
   *                The id of the Turn
   */
  def absenceSelected(idRisultato: Int, idTerminale: Int, idTurno: Int): Unit

  /**
   * Method used by a [[view.fxview.component.manager.subcomponent.ReplacementsBox]] to tell it's parent
   * the substitute selected by the user
   *
   * @param idRisultato
   *                    The turn that need a replacement
   * @param idPersona
   *                  The person that will fill the hole
   */
  def replacementSelected(idRisultato: Int, idPersona: Int)

}
