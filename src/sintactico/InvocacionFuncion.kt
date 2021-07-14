package sintactico

import javafx.scene.control.TreeItem
import lexico.Token
import java.util.ArrayList

class InvocacionFuncion(var identificador: Token, var listaArgumentos: ArrayList<Argumento>) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Invocacion Funcion")
        raiz.children.add(TreeItem(identificador.lexema))
        val argumens = TreeItem("Argumentos")
        raiz.children.add(argumens)
        for (a in listaArgumentos) {
            argumens.children.add(a.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "InvocacionFuncion [iden=$identificador, listaArgumento=$listaArgumentos]"
    }

}