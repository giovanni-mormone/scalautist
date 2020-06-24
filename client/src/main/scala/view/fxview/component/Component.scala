package view.fxview.component

import javafx.fxml.Initializable
import javafx.scene.layout.Pane
import view.fxview.FXHelperFactory
import view.fxview.loader.FXLoader

/**
 * @author Giovanni Mormone.
 *
 * Generic component of the view. It should be inside some [[view.BaseView]]
 *
 * @tparam A
 *           The parent/container of the component.
 *
 */
trait Component[A] {

  /**
   * Gets the pane of the component.
   * @return
   *        The pane where the component is contained, loaded from fxml.
   */
  val pane: Pane

  /**
   * Setter for the parent of this [[view.fxview.component.Component]].
   * @param parent
   *               The parent to set.
   */
  def setParent(parent:A):Component[A]

  /**
   * Disables the component, making it not interactive.
   */
  def disable(): Unit

  /**
   * Enables the component, making it interactive
   */
  def enable(): Unit

  def startLoading(): Unit

  def endLoading(): Unit
}

/**
 * @author Giovanni Mormone.
 *
 * Abstract implementation of the [[view.fxview.component.Component]] trait, provides the basic functionalities of the
 * component, such as loading a fxml file and set an fx controller
 *
 * @param path
 *             The path of the fxml to load.
 * @tparam A
 *           The parent/container of the component.
 *
 */
abstract class AbstractComponent[A](val path:String) extends Component[A] with Initializable{
  /**
   * The parent of the component
   */
  protected var parent:A = _

  val pane:Pane = {
    FXLoader.loadComponent(this,path)
  }

  override def setParent(parent: A): Component[A] = {
    this.parent = parent
    this
  }

  override def disable(): Unit =
    pane.setDisable(true)

  override def enable(): Unit =
    pane.setDisable(false)

  override def startLoading(): Unit = {
    pane.getChildren.add(FXHelperFactory.loadingBox)
    disable()
  }

  override def endLoading(): Unit = {
    pane.getChildren.remove(FXHelperFactory.loadingBox)
    enable()
  }
}

