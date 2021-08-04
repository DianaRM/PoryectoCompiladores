package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Arreglo(var tipoDato: Token, var identificador: Token, var corchetes: ArrayList<Corchetes>,
              var argumentos: ArrayList<Argumento>?) : Sentencia() {
    override fun toString(): String {
        return ("Arreglo [tipoDato=" + tipoDato + ", identificador=" + identificador + ", corchetes=" + corchetes
                + ", argumentos=" + argumentos + "]")
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            var aux = ""
            val raiz = DefaultMutableTreeNode("Arreglo")
            raiz.add(DefaultMutableTreeNode(identificador.lexema + " " + tipoDato.lexema))
            val corche = DefaultMutableTreeNode("Corchetes")
            raiz.add(corche)
            for (c in corchetes) {
                aux += c.arbolVisual
            }
            corche.add(DefaultMutableTreeNode(aux))
            val argumens = DefaultMutableTreeNode("Argumentos")
            raiz.add(argumens)
            if (argumentos != null) {
                for (a in argumentos!!) {
                    argumens.add(a.arbolVisual)
                }
            }
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        tablaSimbolos!!.guardarSimboloVariable(identificador.lexema, tipoDato.lexema, 0, 0, ambito!!, null)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        for (i in argumentos!!.indices) {
            val ex = argumentos!![i].ex
            ex.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
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

    override val javaCode: String
        get() = ""

}