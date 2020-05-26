package view.fxview.component

import javafx.fxml.Initializable
import javafx.scene.layout.Pane
import view.fxview.loader.FXLoader

/**
 * Generic component of the view. It should be inside some [[view.BaseView]]
 *
 * @tparam A
 *           The parent/container of the component.
 */
trait Component[A] {

  /**
   *  Gets the pane of the component.
   * @return
   *        The pane where the component is contained, loaded from fxml.
   */
  def pane(): Pane

  /**
   * Setter for the parent of this [[view.fxview.component.Component]].
   * @param parent
   *               The parent to set.
   */
  def setParent(parent:A)
}

/**
 *Abstract implementation of the [[view.fxview.component.Component]] trait, provides the basic functionalities of the
 * component, such as loading a fxml file and set an fx controller
 *
 * @param path
 *             The path of the fxml to load.
 * @tparam A
 *           The parent/container of the component.
 */
abstract class AbstractComponent[A](val path:String) extends Component[A] with Initializable{
  /**
   * The parent of the component
   */
  protected var parent:A = _
  private val loadedPane:Pane = FXLoader.loadComponent(this,path)

  override def pane(): Pane = {
    loadedPane
  }

  override def setParent(observer: A): Unit =
    this.parent = observer
}