package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ExpresionRelacional {
    private var expresionAritmeticaIzquierda: ExpresionAritmetica? = null
    private var opRelacional: Token? = null
    private var expresionAritmeticaDerecha: ExpresionAritmetica? = null
    private var invoMetodoIzquierda: InvocacionFuncion? = null
    private var invoMetodoDerecha: InvocacionFuncion? = null
    private var expresionRelacional: ExpresionRelacional? = null
    private var identificador: Token? = null
    private var zRelacional: ZRelacional? = null

    constructor(invoMetodoIzquierda: InvocacionFuncion?, invoMetodoDerecha: InvocacionFuncion?, opRelacional: Token?,
                zRelacional: ZRelacional?) : super() {
        this.invoMetodoIzquierda = invoMetodoIzquierda
        this.invoMetodoDerecha = invoMetodoDerecha
        this.opRelacional = opRelacional
        this.zRelacional = zRelacional
    }

    constructor(expresionRelacional: ExpresionRelacional?, zRelacional: ZRelacional?) : super() {
        this.expresionRelacional = expresionRelacional
        this.zRelacional = zRelacional
    }

    constructor(identificador: Token?, zRelacional: ZRelacional?) : super() {
        this.identificador = identificador
        this.zRelacional = zRelacional
    }

    constructor(opRelacional: Token?, expresionAritmeticaIzquierda: ExpresionAritmetica?,
                expresionAritmeticaDerecha: ExpresionAritmetica?, zRelacional: ZRelacional?) : super() {
        this.opRelacional = opRelacional
        this.expresionAritmeticaIzquierda = expresionAritmeticaIzquierda
        this.expresionAritmeticaDerecha = expresionAritmeticaDerecha
        this.zRelacional = zRelacional
    }

    override fun toString(): String {
        return ("ExpresionRelacional [expresionAritmeticaIzquierda=" + expresionAritmeticaIzquierda + ", opRelacional="
                + opRelacional + ", expresionAritmeticaDerecha=" + expresionAritmeticaDerecha + ", invoMetodoIzquierda="
                + invoMetodoIzquierda + ", invoMetodoDerecha=" + invoMetodoDerecha + ", expresionRelacional="
                + expresionRelacional + ", identificador=" + identificador + ", zRelacional=" + zRelacional + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ExpresionRelacional")
            if (expresionRelacional != null) {
                raiz.add(expresionRelacional!!.arbolVisual)
                if (zRelacional != null) {
                    raiz.add(zRelacional!!.arbolVisual)
                }
                return raiz
            }
            if (identificador != null) {
                raiz.add(DefaultMutableTreeNode(identificador!!.lexema))
                if (zRelacional != null) {
                    raiz.add(zRelacional!!.arbolVisual)
                }
                return raiz
            }
            if (expresionAritmeticaIzquierda != null) {
                raiz.add(expresionAritmeticaIzquierda!!.arbolVisual)
                raiz.add(DefaultMutableTreeNode(opRelacional!!.lexema))
                raiz.add(expresionAritmeticaDerecha!!.arbolVisual)
                if (zRelacional != null) {
                    raiz.add(zRelacional!!.arbolVisual)
                }
                return raiz
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionAritmeticaIzquierda != null) {
            expresionAritmeticaIzquierda!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (expresionAritmeticaDerecha != null) {
            expresionAritmeticaDerecha!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (expresionRelacional != null) {
            expresionAritmeticaDerecha!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invoMetodoIzquierda != null) {
            invoMetodoIzquierda!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invoMetodoDerecha != null) {
            invoMetodoDerecha!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zRelacional != null) {
            zRelacional!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionAritmeticaIzquierda != null) {
            expresionAritmeticaIzquierda!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (expresionAritmeticaDerecha != null) {
            expresionAritmeticaDerecha!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (expresionRelacional != null) {
            expresionRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invoMetodoIzquierda != null) {
            invoMetodoIzquierda!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invoMetodoDerecha != null) {
            invoMetodoDerecha!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (identificador != null) {
            if (ambito != null) {
                identificador!!.analizarSemantica(tablaSimbolos!!, erroresSemanticos!!, ambito)
            }
        }
        if (zRelacional != null) {
            zRelacional!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ExpresionRelacional")
        if (expresionRelacional != null) {
            raiz.children.add(expresionRelacional!!.getArbolVisual())
            if (zRelacional != null) {
                raiz.children.add(zRelacional!!.getArbolVisual())
            }
            return raiz
        }
        if (identificador != null) {
            raiz.children.add(TreeItem(identificador!!.lexema))
            if (zRelacional != null) {
                raiz.children.add(zRelacional!!.getArbolVisual())
            }
            return raiz
        }
        if (expresionAritmeticaIzquierda != null) {
            raiz.children.add(expresionAritmeticaIzquierda!!.getArbolVisual())
            raiz.children.add(TreeItem(opRelacional!!.lexema))
            raiz.children.add(expresionAritmeticaDerecha!!.getArbolVisual())
            if (zRelacional != null) {
                raiz.children.add(zRelacional!!.getArbolVisual())
            }
            return raiz
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            if (expresionRelacional != null) {
                codigo += "( " + expresionRelacional!!.javaCode + " )"
                if (zRelacional != null) {
                    codigo += zRelacional!!.javaCode
                }
                return codigo
            }
            if (identificador != null) {
                codigo += "( " + identificador!!.traducirIdentificador() + " )"
                if (zRelacional != null) {
                    codigo += zRelacional!!.javaCode
                }
                return codigo
            }
            if (expresionAritmeticaIzquierda != null) {
                codigo += "( " + expresionAritmeticaIzquierda!!.javaCode + " )"
                codigo += opRelacional!!.lexema
                codigo += "( " + expresionAritmeticaDerecha!!.javaCode + " )"
                codigo += ")"
                if (zRelacional != null) {
                    codigo += zRelacional!!.javaCode
                }
                return codigo
            }
            return codigo
        }
}