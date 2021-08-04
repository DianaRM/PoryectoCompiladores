package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ZLogica(private val opLogicoBinario: Token, private val expreLogica: ExpresionLogica?, private val zLogica: ZLogica?) {
    override fun toString(): String {
        return ("ZLogica [opLogicoBinario=" + opLogicoBinario + ", expreLogica=" + expreLogica + ", zLogica=" + zLogica
                + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ZLogica")
            raiz.add(DefaultMutableTreeNode(opLogicoBinario.lexema))
            raiz.add(expreLogica!!.arbolVisual)
            if (zLogica != null) {
                raiz.add(zLogica.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        expreLogica?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        zLogica?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        expreLogica?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        zLogica?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ZLogica")
        raiz.children.add(TreeItem(opLogicoBinario.lexema))
        raiz.children.add(expreLogica!!.getArbolVisual())
        if (zLogica != null) {
            raiz.children.add(zLogica.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            codigo += opLogicoBinario.lexema
            codigo += "( " + expreLogica!!.javaCode + " )"
            if (zLogica != null) {
                codigo += zLogica.javaCode
            }
            return codigo
        }
}