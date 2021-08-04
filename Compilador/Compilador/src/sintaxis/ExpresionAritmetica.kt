package sintaxis

import javafx.scene.control.TreeItem
import lexico.Categoria
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ExpresionAritmetica {
    private var numero: Token? = null
    private var expresionAritmetica: ExpresionAritmetica? = null
    private var identificador: Token? = null
    private var invocacionFuncion: InvocacionFuncion? = null
    private var zAritmetica: ZAritmetica? = null

    constructor(numero: Token?, zAritmetica: ZAritmetica?) : super() {
        this.numero = numero
        this.zAritmetica = zAritmetica
    }

    constructor(zAritmetica: ZAritmetica?, expresionAritmetica: ExpresionAritmetica?) : super() {
        this.zAritmetica = zAritmetica
        this.expresionAritmetica = expresionAritmetica
    }

    constructor(zAritmetica: ZAritmetica?, identificador: Token?) : super() {
        this.zAritmetica = zAritmetica
        this.identificador = identificador
    }

    constructor(zAritmetica: ZAritmetica?, invocacionFuncion: InvocacionFuncion?) : super() {
        this.zAritmetica = zAritmetica
        this.invocacionFuncion = invocacionFuncion
    }

    override fun toString(): String {
        return ("ExpresionAritmetica [numero=" + numero + ", expresionAritmetica=" + expresionAritmetica
                + ", identificador=" + identificador + ", invocacionFuncion=" + invocacionFuncion + ", zAritmetica="
                + zAritmetica + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Expresion Aritmetica")
            if (numero != null) {
                raiz.add(DefaultMutableTreeNode(numero!!.lexema))
                if (zAritmetica != null) {
                    raiz.add(zAritmetica!!.arbolVisual)
                }
                return raiz
            }
            if (expresionAritmetica != null) {
                raiz.add(expresionAritmetica!!.arbolVisual)
                if (zAritmetica != null) {
                    raiz.add(zAritmetica!!.arbolVisual)
                }
                return raiz
            }
            if (identificador != null) {
                raiz.add(DefaultMutableTreeNode(identificador!!.lexema))
                if (zAritmetica != null) {
                    raiz.add(zAritmetica!!.arbolVisual)
                }
                return raiz
            }
            if (invocacionFuncion != null) {
                raiz.add(invocacionFuncion!!.arbolVisual)
                if (zAritmetica != null) {
                    raiz.add(zAritmetica!!.arbolVisual)
                }
                return raiz
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionAritmetica != null) {
            expresionAritmetica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (invocacionFuncion != null) {
            invocacionFuncion!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zAritmetica != null) {
            zAritmetica!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionAritmetica != null) {
            expresionAritmetica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (identificador != null) {
            if (ambito != null) {
                identificador!!.analizarSemantica(tablaSimbolos!!, erroresSemanticos!!, ambito)
            }
        }
        if (invocacionFuncion != null) {
            invocacionFuncion!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (zAritmetica != null) {
            zAritmetica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun obtenerTipo(): String {
        var tipo = "Entero"
        if (numero != null) {
            if (numero!!.getCategoria() === Categoria.REAL) {
                return "Real"
            }
        }
        if (expresionAritmetica != null) {
            tipo = expresionAritmetica!!.obtenerTipo()
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
        val raiz = TreeItem("Expresion Aritmetica")
        if (numero != null) {
            raiz.children.add(TreeItem(numero!!.lexema))
            if (zAritmetica != null) {
                raiz.children.add(zAritmetica!!.getArbolVisual())
            }
            return raiz
        }
        if (expresionAritmetica != null) {
            raiz.children.add(expresionAritmetica!!.getArbolVisual())
            if (zAritmetica != null) {
                raiz.children.add(zAritmetica!!.getArbolVisual())
            }
            return raiz
        }
        if (identificador != null) {
            raiz.children.add(TreeItem(identificador!!.lexema))
            if (zAritmetica != null) {
                raiz.children.add(zAritmetica!!.getArbolVisual())
            }
            return raiz
        }
        if (invocacionFuncion != null) {
            raiz.children.add(invocacionFuncion!!.getArbolVisual())
            if (zAritmetica != null) {
                raiz.children.add(zAritmetica!!.getArbolVisual())
            }
            return raiz
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            if (numero != null) {
                codigo += "( " + numero!!.lexema + " )"
                if (zAritmetica != null) {
                    codigo += zAritmetica!!.javaCode
                }
                return codigo
            }
            if (expresionAritmetica != null) {
                codigo += "( " + expresionAritmetica!!.javaCode + " )"
                if (zAritmetica != null) {
                    codigo += zAritmetica!!.javaCode
                }
                return codigo
            }
            if (identificador != null) {
                codigo += "( " + identificador!!.traducirIdentificador() + " )"
                if (zAritmetica != null) {
                    codigo += zAritmetica!!.javaCode
                }
                return codigo
            }
            if (invocacionFuncion != null) {
                codigo += "( " + invocacionFuncion!!.javaCode + " )"
                if (zAritmetica != null) {
                    codigo += zAritmetica!!.javaCode
                }
                return codigo
            }
            return codigo
        }
}