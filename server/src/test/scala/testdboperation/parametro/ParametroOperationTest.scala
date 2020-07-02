package testdboperation.parametro

import caseclass.CaseClassDB.{GiornoInSettimana, Parametro, ZonaTerminale}
import caseclass.CaseClassHttpMessage.InfoAlgorithm

object ParametroOperationTest {
  val parametro:Parametro = Parametro(treSabato = true,"First Run")
  val parametroWithoutName:Parametro = Parametro(treSabato = true,"")
  val zonaTerminale:List[ZonaTerminale] = List(ZonaTerminale(1,1),ZonaTerminale(1,2))
  val giornoInSettimana:Option[List[GiornoInSettimana]] = Option(List(GiornoInSettimana(1,1,1),GiornoInSettimana(1,2,2),GiornoInSettimana(1,3,3)))
  val infoAlgorithm: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,giornoInSettimana)
  val infoAlgorithmWithoutGiornoInSettimana: InfoAlgorithm = InfoAlgorithm(parametro,zonaTerminale,None)
  val infoAlgorithmWithoutZonaTerminale: InfoAlgorithm = InfoAlgorithm(parametro,List.empty,None)
  val infoAlgorithmWithoutNameParameters: InfoAlgorithm = InfoAlgorithm(parametroWithoutName,zonaTerminale,None)
  val idParametersNotExist=10
  val idParameter=1
}
