package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

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

    fun getArbolVisual():TreeItem<String>{
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

}