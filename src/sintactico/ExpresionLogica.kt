package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class ExpresionLogica(private val expresionLogica: ExpresionLogica?, private val opLogicoUnario: Token?,
                      private val expresionRelacional: ExpresionRelacional?, private val invocacionFuncion: Sentencia?, private val bool: Token?, private val identificador: Token?,
                      private val zlogica: ZLogica?) {
    override fun toString(): String {
        return ("ExpresionLogica [expresionLogica=" + expresionLogica + ", opLogicoUnario=" + opLogicoUnario
                + ", expresionRelacional=" + expresionRelacional + ", invocacionFuncion=" + invocacionFuncion
                + ", bool=" + bool + ", identificador=" + identificador + ", zlogica=" + zlogica + "]")
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = TreeItem("ExpresionLogica")
        if (opLogicoUnario != null) {
            raiz.children.add(TreeItem(opLogicoUnario.lexema))
            if (expresionLogica != null) {
                raiz.children.add(expresionLogica.getArbolVisual())
                if (zlogica != null) {
                    raiz.children.add(zlogica.getArbolVisual())
                }
                return raiz
            }
        }
        if (expresionLogica != null) {
            raiz.children.add(expresionLogica.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (bool != null) {
            raiz.children.add(TreeItem(bool.lexema))
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (expresionRelacional != null) {
            raiz.children.add(expresionRelacional.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (invocacionFuncion != null) {
            raiz.children.add(invocacionFuncion.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (identificador != null) {
            raiz.children.add(TreeItem(identificador.lexema))
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        return raiz
    }

}