package sintactico

import javafx.scene.control.TreeItem
import java.util.ArrayList

class Ciclo(var expresionLogica: ExpresionLogica?, var listaSentencias: ArrayList<Sentencia>?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Ciclo")
        raiz.children.add(expresionLogica!!.getArbolVisual())
        val sentences = TreeItem("Sentencias")
        raiz.children.add(sentences)
        for (s in listaSentencias!!) {
            sentences.children.add(s.getArbolVisual())
        }
        return raiz
    }

    override fun toString(): String {
        return "Ciclo [expresionLogica=$expresionLogica, listaSentencias=$listaSentencias]"
    }

}