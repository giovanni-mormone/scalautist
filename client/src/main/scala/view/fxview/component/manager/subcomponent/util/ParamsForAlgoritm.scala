package view.fxview.component.manager.subcomponent.util

import java.time.LocalDate

import caseclass.CaseClassDB.{GiornoInSettimana, Terminale}
import caseclass.CaseClassHttpMessage.SettimanaS

case class ParamsForAlgoritm(dateI: LocalDate, dateF: LocalDate, terminals: List[Terminale],
                             roleS: Boolean = false,
                             name: Option[String] = None,
                             requestN: Option[List[GiornoInSettimana]] = None,
                             requestS: Option[List[SettimanaS]] = None)
