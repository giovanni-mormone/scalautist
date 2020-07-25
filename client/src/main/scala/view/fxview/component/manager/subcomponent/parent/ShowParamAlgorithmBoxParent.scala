package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.{AlgorithmExecute, InfoAlgorithm}

trait ShowParamAlgorithmBoxParent {

  def run(info: AlgorithmExecute): Unit

  def saveParam(param: InfoAlgorithm)

  def resetParams(): Unit
}
