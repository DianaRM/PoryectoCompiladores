package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class Asignacion(var identificador: Token, var operadorAsignacion: Token, var expresion: Expresion?) : Sentencia() {
    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Asignacion")
        raiz.children.add(TreeItem(identificador.lexema))
        raiz.children.add(TreeItem(operadorAsignacion.lexema))
        raiz.children.add(expresion!!.getArbolVisual())
        return raiz
    }

    override fun toString(): String {
        return ("Asignacion [identificador=" + identificador + ", operadorAsignacion=" + operadorAsignacion
                + ", exprecion=" + expresion + "]")
    }

}