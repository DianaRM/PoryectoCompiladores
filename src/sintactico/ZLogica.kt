package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class ZLogica(private val opLogicoBinario: Token, private val expreLogica: ExpresionLogica?, private val zLogica: ZLogica?) {
    override fun toString(): String {
        return ("ZLogica [opLogicoBinario=" + opLogicoBinario + ", expreLogica=" + expreLogica + ", zLogica=" + zLogica
                + "]")
    }

    fun getArbolVisual():TreeItem<String>{
        val raiz = TreeItem("ZLogica")
        raiz.children.add(TreeItem(opLogicoBinario.lexema))
        raiz.children.add(expreLogica!!.getArbolVisual())
        if (zLogica != null) {
            raiz.children.add(zLogica.getArbolVisual())
        }
        return raiz
    }
}