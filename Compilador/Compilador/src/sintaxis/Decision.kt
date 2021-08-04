package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

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

    override fun toString(): String {
        return ("Decision [expresionLogica=" + expresionLogica + ", listaSentencias1=" + listaSentencias1
                + ", listaSentencias2=" + listaSentencias2 + "]")
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Decisión")
            raiz.add(expresionLogica!!.arbolVisual)
            val raizTrue = DefaultMutableTreeNode("Sentencias True")
            for (sentencia in listaSentencias1!!) {
                raizTrue.add(sentencia.arbolVisual)
            }
            raiz.add(raizTrue)
            if (listaSentencias2 != null) {
                val raizFalse = DefaultMutableTreeNode("Sentencias False")
                for (sentencia in listaSentencias2!!) {
                    raizFalse.add(sentencia.arbolVisual)
                }
                raiz.add(raizFalse)
            }
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        for (s in listaSentencias1!!) {
            s.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        for (s in listaSentencias2!!) {
            s.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionLogica != null) {
            expresionLogica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (listaSentencias1 != null) {
            for (i in listaSentencias1!!.indices) {
                listaSentencias1!![i].analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            }
        }
        if (listaSentencias2 != null) {
            for (i in listaSentencias2!!.indices) {
                listaSentencias2!![i].analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            }
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
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

    override val javaCode: String
        get() {
            var deci = ""
            var sentenc = ""
            var sentenc2 = ""
            for (s in listaSentencias1!!) {
                sentenc += """
                    ${s.javaCode}
                    
                    """.trimIndent()
            }
            deci += """if ( ${expresionLogica!!.javaCode} ) { 
$sentenc
 }"""
            if (listaSentencias2!!.size > 0) {
                for (s in listaSentencias2!!) {
                    sentenc2 += """
                        ${s.javaCode}
                        
                        """.trimIndent()
                }
            }
            deci += " else { \n$sentenc2\n }"
            return deci
        }
}