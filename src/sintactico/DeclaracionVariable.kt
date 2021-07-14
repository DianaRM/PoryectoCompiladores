package sintactico

import javafx.scene.control.TreeItem
import lexico.Token

class DeclaracionVariable(private val tipoDato: Token, private val identificador: Token) : Sentencia() {
    override fun getArbolVisual(): TreeItem<String> {
        return TreeItem(identificador.lexema + " " + tipoDato.lexema)
    }

    override fun toString(): String {
        return "DeclaracionVariable [tipoDato=$tipoDato, identificador=$identificador]"
    }
}