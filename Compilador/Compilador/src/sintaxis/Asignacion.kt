package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Asignacion(var identificador: Token, var operadorAsignacion: Token, var expresion: Expresion?) : Sentencia() {
    override fun toString(): String {
        return ("Asignacion [identificador=" + identificador + ", operadorAsignacion=" + operadorAsignacion
                + ", exprecion=" + expresion + "]")
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Asignacion")
            raiz.add(DefaultMutableTreeNode(identificador.lexema))
            raiz.add(DefaultMutableTreeNode(operadorAsignacion.lexema))
            raiz.add(expresion!!.arbolVisual)
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {}
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {

        val s = tablaSimbolos?.buscarSimboloVariable(identificador.lexema, ambito, identificador.fila,
                identificador.columna)
        if (s == null) {
            erroresSemanticos!!.add("La variable " + identificador.lexema + " no esta declarada")
        } else {
            if (expresion != null) {
                if (s.tipo != expresion!!.obtenerTipo()) {
                    erroresSemanticos!!.add("El tipo de la expresión no corresponde al tipo de dato de la variable "
                            + identificador.lexema)
                }
            }
        }
        if (expresion != null) {
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Asignacion")
        raiz.children.add(TreeItem(identificador.lexema))
        raiz.children.add(TreeItem(operadorAsignacion.lexema))
        raiz.children.add(expresion!!.getArbolVisual())
        return raiz
    }

    override val javaCode: String
        get() = identificador.traducirIdentificador() + " " + operadorAsignacion.lexema + expresion!!.javaCode

}