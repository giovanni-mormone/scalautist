package view.fxview.component.driver.subcomponent.parent

trait DriverHomeParent extends HomeBoxParent with SalaryBoxParent with ShiftBoxParent{
    def drawHomePanel():Unit
    def drawTurnoPanel():Unit
    def drawStipendioPanel():Unit
}
