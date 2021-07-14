package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class IncrementoDecremento(private val identificador: Token?, private val oPincremento: Token) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("IncrementoDecremento")
        raiz.children.add(TreeItem(identificador!!.lexema))
        raiz.children.add(TreeItem(oPincremento.lexema))
        return raiz
    }

}