package view.fxview.component.driver.subcomponent.parent

trait DriverHomeParent extends HomeBoxParent with SalaryBoxParent with ShiftBoxParent{
    def drawHomePanel():Unit
    def drawShiftPanel():Unit
    def drawSalaryPanel():Unit
}
