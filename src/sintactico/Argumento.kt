package sintactico

import javafx.scene.control.TreeItem
import javax.swing.tree.DefaultMutableTreeNode

class Argumento(var ex: Expresion) {
    override fun toString(): String {
        return "Argumento [ex=$ex]"
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = ex.getArbolVisual()
        return raiz
    }

    fun obtenerTipo(): String {
        return ex.obtenerTipo()
    }

}