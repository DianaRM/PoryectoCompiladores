package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class ZRelacional(var opRelacional: Token, var expresionRelacional: ExpresionRelacional?, var zRelacional: ZRelacional?) {
    override fun toString(): String {
        return ("ZRelacional [opRelacional=" + opRelacional + ", expresionRelacional=" + expresionRelacional
                + ", zRelacional=" + zRelacional + "]")
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = TreeItem("ZRelacional")
        raiz.children.add(TreeItem(opRelacional.lexema))
        raiz.children.add(expresionRelacional!!.getArbolVisual())
        if (zRelacional != null) {
            raiz.children.add(zRelacional!!.getArbolVisual())
        }
        return raiz
    }
}