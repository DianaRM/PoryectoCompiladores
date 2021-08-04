package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class LecturaDatos(var ex: Expresion?) : Sentencia() {
    override fun toString(): String {
        return "LecturaDatos [ex=$ex]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Lectura")
            raiz.add(ex!!.arbolVisual)
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (ex != null) {
            ex!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (ex != null) {
            ex!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Lectura")
        raiz.children.add(ex!!.getArbolVisual())
        return raiz
    }

    override val javaCode: String
        get() = "JOptionPane.showInputDialog(" + ex!!.javaCode + ");"

}