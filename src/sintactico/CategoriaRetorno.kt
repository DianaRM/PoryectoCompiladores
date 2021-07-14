package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class CategoriaRetorno {
    private var expresion: Expresion? = null
    private var nulo: Token? = null
    private var invoFuncion: InvocacionFuncion? = null

    constructor(expresion: Expresion?) : super() {
        this.expresion = expresion
    }

    constructor(nulo: Token?) : super() {
        this.nulo = nulo
    }

    constructor(invoFuncion: InvocacionFuncion?) : super() {
        this.invoFuncion = invoFuncion
    }

    override fun toString(): String {
        return "CategoriaRetorno [expresion=$expresion, nulo=$nulo, invoFuncion=$invoFuncion]"
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = TreeItem("Retorno")
        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        } else if (nulo != null) {
            raiz.children.add(TreeItem(nulo!!.lexema))
        } else {
            raiz.children.add(invoFuncion!!.getArbolVisual())
        }
        return raiz
    }
}