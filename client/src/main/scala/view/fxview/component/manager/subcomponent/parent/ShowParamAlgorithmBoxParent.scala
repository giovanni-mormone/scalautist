package view.fxview.component.manager.subcomponent.parent

import caseclass.CaseClassHttpMessage.AlgorithmExecute

trait ShowParamAlgorithmBoxParent {

  def run(info: AlgorithmExecute, name: Option[String]): Unit

  def resetParams(): Unit
}
