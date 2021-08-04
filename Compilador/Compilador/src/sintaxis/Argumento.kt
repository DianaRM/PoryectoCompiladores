package sintaxis

import javafx.scene.control.TreeItem
import javax.swing.tree.DefaultMutableTreeNode

class Argumento(var ex: Expresion) {
    override fun toString(): String {
        return "Argumento [ex=$ex]"
    }

    val arbolVisual: DefaultMutableTreeNode?
        get() = ex.arbolVisual

    fun obtenerTipo(): String {
        return ex.obtenerTipo()
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = ex.getArbolVisual()
        return raiz
    }

    val javaCode: String?
        get() = ex.javaCode
}