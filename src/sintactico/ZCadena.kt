package sintactico

import javafx.scene.control.TreeItem

class ZCadena(var ex: Expresion?, var zCadena: ZCadena?) {
    override fun toString(): String {
        return "ZCadena [ex=$ex, zCadena=$zCadena]"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("ZCadena")
        raiz.children.add(ex!!.getArbolVisual())
        if (zCadena != null) {
            raiz.children.add(zCadena!!.getArbolVisual())
        }
        return raiz
    }
}