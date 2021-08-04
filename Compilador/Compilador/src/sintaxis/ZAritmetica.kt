package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class ZAritmetica(var opAritmetico: Token, var expAritmetica: ExpresionAritmetica?, var zAritmetica: ZAritmetica?) {
    override fun toString(): String {
        return ("ZAritmetica [opAritmetico=" + opAritmetico + ", expAritmetica=" + expAritmetica + ", zAritmetica="
                + zAritmetica + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ZAritemtica")
            raiz.add(DefaultMutableTreeNode(opAritmetico.lexema))
            raiz.add(expAritmetica!!.arbolVisual)
            if (zAritmetica != null) {
                raiz.add(zAritmetica!!.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expAritmetica != null) {
            expAritmetica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zAritmetica != null) {
            zAritmetica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expAritmetica != null) {
            expAritmetica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zAritmetica != null) {
            zAritmetica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun obtenerTipo(): String {
        var tipo = "Entero"
        if (expAritmetica != null) {
            tipo = expAritmetica!!.obtenerTipo()
            if (tipo == "Real") {
                return tipo
            }
        }
        if (zAritmetica != null) {
            tipo = zAritmetica!!.obtenerTipo()
            if (tipo == "Real") {
                return tipo
            }
        }
        return tipo
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ZAritemtica")
        raiz.children.add(TreeItem(opAritmetico.lexema))
        raiz.children.add(expAritmetica!!.getArbolVisual())
        if (zAritmetica != null) {
            raiz.children.add(zAritmetica!!.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            codigo += opAritmetico.lexema
            codigo += "( " + expAritmetica!!.javaCode + " )"
            if (zAritmetica != null) {
                codigo += zAritmetica!!.javaCode
            }
            return codigo
        }

}