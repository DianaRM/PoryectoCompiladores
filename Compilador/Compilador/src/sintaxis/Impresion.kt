package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Impresion(var ex: Expresion?) : Sentencia() {
    override fun toString(): String {
        return "Impresion [ex=$ex]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Impresion")
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
        val raiz = TreeItem("Impresion")
        raiz.children.add(ex!!.getArbolVisual())
        return raiz
    }

    override val javaCode: String
        get() {
            println()
            return "System.out.println( " + ex!!.javaCode + " );"
        }

}