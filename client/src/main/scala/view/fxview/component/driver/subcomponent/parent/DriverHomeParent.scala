package view.fxview.component.driver.subcomponent.parent

trait DriverHomeParent extends HomeBoxParent with SalaryBoxParent with ShiftBoxParent{
    /**
     * Draws the home panel of the driver
     */
    def drawHomePanel():Unit

    /**
     * draws the shift panel of the driver
     */
    def drawShiftPanel():Unit

    /**
     * draws the salary panel of the driver
     */
    def drawSalaryPanel():Unit
}
