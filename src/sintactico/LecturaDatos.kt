package sintactico

import javafx.scene.control.TreeItem

class LecturaDatos(var ex: Expresion?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Lectura")
        raiz.children.add(ex!!.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return "LecturaDatos [ex=$ex]"
    }
}