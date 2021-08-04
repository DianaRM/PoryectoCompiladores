package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Ciclo(var expresionLogica: ExpresionLogica?, var listaSentencias: ArrayList<Sentencia>?) : Sentencia() {
    override fun toString(): String {
        return "Ciclo [expresionLogica=$expresionLogica, listaSentencias=$listaSentencias]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Ciclo")
            raiz.add(expresionLogica!!.arbolVisual)
            val sentences = DefaultMutableTreeNode("Sentencias")
            raiz.add(sentences)
            for (s in listaSentencias!!) {
                sentences.add(s.arbolVisual)
            }
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        for (s in listaSentencias!!) {
            s.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (expresionLogica != null) {
            expresionLogica!!.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
        if (listaSentencias != null) {
            for (i in listaSentencias!!.indices) {
                listaSentencias!![i].analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
            }
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Ciclo")
        raiz.children.add(expresionLogica!!.getArbolVisual())
        val sentences = TreeItem("Sentencias")
        raiz.children.add(sentences)
        for (s in listaSentencias!!) {
            sentences.children.add(s.getArbolVisual())
        }
        return raiz
    }

    override val javaCode: String
        get() {
            var sentenc = ""
            if (listaSentencias != null) {
                for (s in listaSentencias!!) {
                    sentenc += """
                        ${s.javaCode}
                        
                        """.trimIndent()
                }
            }
            return """while ( ${expresionLogica!!.javaCode} ) { 
$sentenc
 }"""
        }

}