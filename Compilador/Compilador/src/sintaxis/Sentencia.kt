package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

abstract class Sentencia {

    abstract val arbolVisual: DefaultMutableTreeNode?

    abstract fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?)

    abstract fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?)

    abstract fun getArbolVisual(): TreeItem<String>?

    abstract val javaCode: String?
}