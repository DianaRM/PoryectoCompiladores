package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class ZAritmetica(var opAritmetico: Token, var expAritmetica: ExpresionAritmetica?, var zAritmetica: ZAritmetica?) {
    override fun toString(): String {
        return ("ZAritmetica [opAritmetico=" + opAritmetico + ", expAritmetica=" + expAritmetica + ", zAritmetica="
                + zAritmetica + "]")
    }

    fun getArbolVisual(): TreeItem<String>{
        val raiz = TreeItem("ZAritemtica")
        raiz.children.add(TreeItem(opAritmetico.lexema))
        raiz.children.add(expAritmetica!!.getArbolVisual())
        if (zAritmetica != null) {
            raiz.children.add(zAritmetica!!.getArbolVisual())
        }
        return raiz
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

}