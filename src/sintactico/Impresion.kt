package sintactico

import javafx.scene.control.TreeItem

class Impresion(var ex: Expresion?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Impresion")
        raiz.children.add(ex!!.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "Impresion [ex=$ex]"
    }
}