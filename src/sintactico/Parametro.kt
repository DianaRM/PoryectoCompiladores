package sintactico

import javafx.scene.control.TreeItem
import lexico.Token
import javax.swing.tree.DefaultMutableTreeNode

class Parametro(var tipoDato: Token, var identificador: Token) {
    override fun toString(): String {
        return "Parametro [tipoDato=$tipoDato, identificador=$identificador]"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("${identificador.lexema}" )
        raiz.children.add(TreeItem("${tipoDato.lexema}"))
        return raiz
    }
    val arbolVisual: DefaultMutableTreeNode
        get() = DefaultMutableTreeNode(identificador.lexema + " " + tipoDato.lexema)

}