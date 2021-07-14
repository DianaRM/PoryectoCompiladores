package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class ExpresionCadena(private val cadena: Token, private val zCadena: ZCadena?) {
    override fun toString(): String {
        return "ExpresionCadena [cadena=$cadena, zCadena=$zCadena]"
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = TreeItem("Expresion Cadena")
        raiz.children.add(TreeItem(cadena.lexema))
        if (zCadena != null) {
            raiz.children.add(zCadena.getArbolVisual())
        }
        return raiz
    }

}