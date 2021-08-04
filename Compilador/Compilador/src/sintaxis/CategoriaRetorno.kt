package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class CategoriaRetorno {
    private var expresion: Expresion? = null
    private var nulo: Token? = null
    private var invoFuncion: InvocacionFuncion? = null

    constructor(expresion: Expresion?) : super() {
        this.expresion = expresion
    }

    constructor(nulo: Token?) : super() {
        this.nulo = nulo
    }

    constructor(invoFuncion: InvocacionFuncion?) : super() {
        this.invoFuncion = invoFuncion
    }

    override fun toString(): String {
        return "CategoriaRetorno [expresion=$expresion, nulo=$nulo, invoFuncion=$invoFuncion]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Categoria Retorno")
            if (expresion != null) {
                raiz.add(expresion!!.arbolVisual)
            } else if (nulo != null) {
                raiz.add(DefaultMutableTreeNode(nulo!!.lexema))
            } else {
                raiz.add(invoFuncion!!.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresion != null) {
            expresion!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invoFuncion != null) {
            invoFuncion!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>, ambito: Simbolo?) {
        if (expresion != null) {
            expresion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            if (ambito != null) {
                if (ambito.tipo != "Void") {
                    if (expresion!!.obtenerTipo() != ambito.tipo) {
                        erroresSemanticos.add("El tipo de retorno no corresponde al tipo de la funcion " + ambito.nombre)
                    }
                }else{
                    erroresSemanticos.add("La función es de tipo Void, no debería haber ningún retorno")
                }
            }else{
                erroresSemanticos.add("El ambito es nulo en la función con expresion: " + expresion)
            }
        }
        if (invoFuncion != null) {
            invoFuncion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Retorno")
        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        } else if (nulo != null) {
            raiz.children.add(TreeItem(nulo!!.lexema))
        } else {
            raiz.children.add(invoFuncion!!.getArbolVisual())
        }
        return raiz
    }

    val javaCode: String?
        get() {
            if (nulo != null) {
                return "null"
            } else if (expresion != null) {
                return expresion!!.javaCode
            }
            return invoFuncion!!.javaCode
        }
}