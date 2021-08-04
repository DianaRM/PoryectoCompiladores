package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ZCadena(var ex: Expresion?, var zCadena: ZCadena?) {
    override fun toString(): String {
        return "ZCadena [ex=$ex, zCadena=$zCadena]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ZCadena")
            raiz.add(ex!!.arbolVisual)
            if (zCadena != null) {
                raiz.add(zCadena!!.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (ex != null) {
            ex!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zCadena != null) {
            zCadena!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (ex != null) {
            ex!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zCadena != null) {
            zCadena!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ZCadena")
        raiz.children.add(ex!!.getArbolVisual())
        if (zCadena != null) {
            raiz.children.add(zCadena!!.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            codigo += ex!!.javaCode
            if (zCadena != null) {
                codigo += zCadena!!.javaCode
            }
            return codigo
        }
}