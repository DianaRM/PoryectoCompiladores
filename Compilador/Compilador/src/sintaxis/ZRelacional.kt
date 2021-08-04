package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ZRelacional(var opRelacional: Token, var expresionRelacional: ExpresionRelacional?, var zRelacional: ZRelacional?) {
    override fun toString(): String {
        return ("ZRelacional [opRelacional=" + opRelacional + ", expresionRelacional=" + expresionRelacional
                + ", zRelacional=" + zRelacional + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ZRelacional")
            raiz.add(DefaultMutableTreeNode(opRelacional.lexema))
            raiz.add(expresionRelacional!!.arbolVisual)
            if (zRelacional != null) {
                raiz.add(zRelacional!!.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionRelacional != null) {
            expresionRelacional!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zRelacional != null) {
            zRelacional!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionRelacional != null) {
            expresionRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zRelacional != null) {
            zRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ZRelacional")
        raiz.children.add(TreeItem(opRelacional.lexema))
        raiz.children.add(expresionRelacional!!.getArbolVisual())
        if (zRelacional != null) {
            raiz.children.add(zRelacional!!.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            codigo += opRelacional.lexema
            codigo += "( " + expresionRelacional!!.javaCode + " )"
            if (zRelacional != null) {
                codigo += zRelacional!!.javaCode
            }
            return codigo
        }
}