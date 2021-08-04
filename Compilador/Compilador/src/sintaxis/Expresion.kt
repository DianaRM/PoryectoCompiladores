package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

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

    override fun toString(): String {
        return ("Expresion [expresionLogica=" + expresionLogica + ", expresionAritmetica=" + expresionAritmetica
                + ", expresionRelacional=" + expresionRelacional + ", expresionCadena=" + expresionCadena + "]")
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Expresion")
            if (expresionLogica != null) {
                raiz.add(expresionLogica!!.arbolVisual)
            } else if (expresionRelacional != null) {
                raiz.add(expresionRelacional!!.arbolVisual)
            } else if (expresionAritmetica != null) {
                raiz.add(expresionAritmetica!!.arbolVisual)
            } else {
                raiz.add(expresionCadena!!.arbolVisual)
            }
            return raiz
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

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionAritmetica != null) {
            expresionAritmetica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        } else if (expresionLogica != null) {
            expresionLogica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        } else if (expresionRelacional != null) {
            expresionRelacional!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        } else {
            expresionCadena!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionLogica != null) {
            expresionLogica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        } else if (expresionRelacional != null) {
            expresionRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        } else if (expresionAritmetica != null) {
            expresionAritmetica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        } else if (expresionCadena != null) {
            expresionCadena!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
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

    override val javaCode: String
        get() {
            var codigo = ""
            if (expresionLogica != null) {
                codigo += expresionLogica!!.javaCode
            } else if (expresionRelacional != null) {
                codigo += expresionRelacional!!.javaCode
            } else if (expresionAritmetica != null) {
                codigo += expresionAritmetica!!.javaCode
            } else if (expresionCadena != null) {
                codigo += expresionCadena!!.javaCode
            }
            return codigo
        }
}