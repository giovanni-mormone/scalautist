package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.{AlgorithmExecute, InfoAlgorithm}

/**
 * @author Francesco Cassano
 *
 * It is the interface of the methods used by terminal modal to make requests to controller
 */
trait ShowParamAlgorithmBoxParent {

  /**
   * run method start the sequence of operation to run the algorithm
   * @param info instance of [[AlgorithmExecute]] that contains information to run algorithm
   */
  def run(info: AlgorithmExecute): Unit

  /**
   * Allows to save main params
   * @param param instance of [[InfoAlgorithm]] that contains information to save algorithm
   */
  def saveParam(param: InfoAlgorithm)

  /**
   * Reset data chosen
   */
  def resetParams(): Unit
}
