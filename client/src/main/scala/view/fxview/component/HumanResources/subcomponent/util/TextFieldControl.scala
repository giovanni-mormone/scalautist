package view.fxview.component.HumanResources.subcomponent.util

import javafx.scene.control.TextField
import regularexpressionutilities.Checker

object TextFieldControl {

  val MAX_CHARS: Int = 20

  def controlNewChar(component: TextField, checker: Checker, word: String, old: String, maxChar: Int = MAX_CHARS): Unit =
      if (!word.isEmpty && !checker.checkRegex.matches(s"${word.last}") || word.length > maxChar)
        component.setText(old)
}
