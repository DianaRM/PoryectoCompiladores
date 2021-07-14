package sintactico

import javafx.scene.control.TreeItem
import lexico.Token
import java.util.ArrayList

class Arreglo(var tipoDato: Token, var identificador: Token, var corchetes: ArrayList<Corchetes>,
              var argumentos: ArrayList<Argumento>?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        var aux = ""
        val raiz = TreeItem("Arreglo")
        raiz.children.add(TreeItem(identificador.lexema + " " + tipoDato.lexema))
        val corche = TreeItem("Corchetes")
        raiz.children.add(corche)
        for (c in corchetes) {
            aux += c.arbolVisual
        }
        corche.children.add(TreeItem(aux))
        val argumens = TreeItem("Argumentos")
        raiz.children.add(argumens)
        if (argumentos != null) {
            for (a in argumentos!!) {
                argumens.children.add(a.getArbolVisual())
            }
        }
        return raiz
    }

    override fun toString(): String {
        return ("Arreglo [tipoDato=" + tipoDato + ", identificador=" + identificador + ", corchetes=" + corchetes
                + ", argumentos=" + argumentos + "]")
    }

}