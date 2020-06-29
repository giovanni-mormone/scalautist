package utils

import java.sql.Date

object TransferObject {

  /**
   * Needed data for create request
   *
   * @param date
   *             start request date
   * @param dateF
   *              end request date
   * @param info
   *             number of driver needed in each shift of each day
   * @param idTerminal
   *                   list of terminal ids
   */
  final case class InfoRichiesta(date: Date, dateF: Date, info: List[(Int,List[(Int,Int)])], idTerminal:List[Int])
}
