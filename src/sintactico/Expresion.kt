package sintactico

import javafx.scene.control.TreeItem

class Expresion : Sentencia {
    private var expresionLogica: ExpresionLogica? = null
    private var expresionAritmetica: ExpresionAritmetica? = null
    private var expresionRelacional: ExpresionRelacional? = null
    private var expresionCadena: ExpresionCadena? = null

    constructor(expresionAritmetica: ExpresionAritmetica?) : super() {
        this.expresionAritmetica = expresionAritmetica
    }

    constructor(expresionLogica: ExpresionLogica?) : super() {
        this.expresionLogica = expresionLogica
    }

    constructor(expresionRelacional: ExpresionRelacional?) : super() {
        this.expresionRelacional = expresionRelacional
    }

    constructor(expresionCadena: ExpresionCadena?) : super() {
        this.expresionCadena = expresionCadena
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Expresion")
        if (expresionLogica != null) {
            raiz.children.add(expresionLogica!!.getArbolVisual())
        } else if (expresionRelacional != null) {
            raiz.children.add(expresionRelacional!!.getArbolVisual())
        } else if (expresionAritmetica != null) {
            raiz.children.add(expresionAritmetica!!.getArbolVisual())
        } else {
            raiz.children.add(expresionCadena!!.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return ("Expresion [expresionLogica=" + expresionLogica + ", expresionAritmetica=" + expresionAritmetica
                + ", expresionRelacional=" + expresionRelacional + ", expresionCadena=" + expresionCadena + "]")
    }

    fun obtenerTipo(): String {
        if (expresionLogica != null) {
            return "Bool"
        } else if (expresionRelacional != null) {
            return "Bool"
        } else if (expresionAritmetica != null) {
            return expresionAritmetica!!.obtenerTipo()
        }
        return "String"
    }

}