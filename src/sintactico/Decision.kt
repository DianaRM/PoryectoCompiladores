package sintactico

import javafx.scene.control.TreeItem
import java.util.ArrayList

class Decision : Sentencia {
    private var expresionLogica: ExpresionLogica?
    private var listaSentencias1: ArrayList<Sentencia>?
    private var listaSentencias2: ArrayList<Sentencia>? = null

    constructor(expresionLogica: ExpresionLogica?, listaSentencias1: ArrayList<Sentencia>?) : super() {
        this.expresionLogica = expresionLogica
        this.listaSentencias1 = listaSentencias1
    }

    constructor(expresionLogica: ExpresionLogica?, listaSentencias1: ArrayList<Sentencia>?,
                listaSentencias2: ArrayList<Sentencia>?) : super() {
        this.expresionLogica = expresionLogica
        this.listaSentencias1 = listaSentencias1
        this.listaSentencias2 = listaSentencias2
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Decisión")
        raiz.children.add(expresionLogica!!.getArbolVisual())
        val raizTrue = TreeItem("Sentencias True")
        for (sentencia in listaSentencias1!!) {
            raizTrue.children.add(sentencia.getArbolVisual())
        }
        raiz.children.add(raizTrue)
        if (listaSentencias2 != null) {
            val raizFalse = TreeItem("Sentencias False")
            for (sentencia in listaSentencias2!!) {
                raizFalse.children.add(sentencia.getArbolVisual())
            }
            raiz.children.add(raizFalse)
        }
        return raiz
    }

    override fun toString(): String {
        return ("Decision [expresionLogica=" + expresionLogica + ", listaSentencias1=" + listaSentencias1
                + ", listaSentencias2=" + listaSentencias2 + "]")
    }
}