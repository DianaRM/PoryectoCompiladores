package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Excepcion(var expresionCadena: ExpresionCadena?) : Sentencia() {
    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Excepcion")
            raiz.add(expresionCadena!!.arbolVisual)
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {}
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionCadena != null) {
            expresionCadena!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Excepcion")
        raiz.children.add(expresionCadena!!.getArbolVisual())
        return raiz
    }

    override val javaCode: String
        get() = ""

}