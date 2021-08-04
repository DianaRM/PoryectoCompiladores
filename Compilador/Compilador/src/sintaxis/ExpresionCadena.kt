package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ExpresionCadena(private val cadena: Token, private val zCadena: ZCadena?) {
    override fun toString(): String {
        return "ExpresionCadena [cadena=$cadena, zCadena=$zCadena]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Expresion Cadena")
            raiz.add(DefaultMutableTreeNode(cadena.lexema))
            if (zCadena != null) {
                raiz.add(zCadena.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        zCadena?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        zCadena?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Expresion Cadena")
        raiz.children.add(TreeItem(cadena.lexema))
        if (zCadena != null) {
            raiz.children.add(zCadena.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            codigo += cadena.lexema.replace('~', '"')
            if (zCadena != null) {
                codigo += zCadena.javaCode
            }
            return codigo
        }
}