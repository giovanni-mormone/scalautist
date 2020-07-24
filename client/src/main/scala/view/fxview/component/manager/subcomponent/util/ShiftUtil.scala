package view.fxview.component.manager.subcomponent.util

object ShiftUtil {

  private val NONE: String = "None"
  private val ERROR_ID: Int = -1
  val N_SHIFT = SHIFT_STRING_MAP.size

  private val SHIFT_STRING_MAP: Map[Int, String] = Map(1 -> "2-6", 2 -> "6-10", 3 -> "10-14",
                                                        4 -> "14-18", 5 -> "18-22", 6 -> "22-2")
  private val SHIFT_INT_MAP: Map[String, Int] = Map("2-6" -> 1 , "6-10" -> 2, "10-14" -> 3 ,
                                                    "14-18" -> 4, "18-22" -> 5, "22-2" -> 6)

  def getShiftId(key: String): Int =
    SHIFT_INT_MAP.getOrElse(key, ERROR_ID)

  def getShiftName(key: Int): String =
    SHIFT_STRING_MAP.getOrElse(key, NONE)

}
