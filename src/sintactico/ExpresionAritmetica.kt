package sintactico

import javafx.scene.control.TreeItem
import lexico.Categoria
import lexico.Token

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
    fun getArbolVisual(): TreeItem<String>{
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

    fun obtenerTipo(): String {
        var tipo = "Entero"
        if (numero != null) {
            if (numero!!.categoria === Categoria.DECIMAL) {
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

}