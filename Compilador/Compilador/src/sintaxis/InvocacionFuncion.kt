package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class InvocacionFuncion(var identificador: Token, var listaArgumentos: ArrayList<Argumento>) : Sentencia() {
    override fun toString(): String {
        return "InvocacionFuncion [iden=$identificador, listaArgumento=$listaArgumentos]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Invocacion Funcion")
            raiz.add(DefaultMutableTreeNode(identificador.lexema))
            val argumens = DefaultMutableTreeNode("Argumentos")
            raiz.add(argumens)
            for (a in listaArgumentos) {
                argumens.add(a.arbolVisual)
            }
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {}
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        val tiposParametros = ArrayList<String?>()
        for (i in listaArgumentos.indices) {
            tiposParametros.add(listaArgumentos[i].obtenerTipo())
        }
        val s = tablaSimbolos!!.buscarSimboloFuncion(identificador.lexema, tiposParametros)
        if (s == null) {
            erroresSemanticos!!.add("La funcion " + identificador.lexema + " no existe")
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Invocacion Funcion")
        raiz.children.add(TreeItem(identificador.lexema))
        val argumens = TreeItem("Argumentos")
        raiz.children.add(argumens)
        for (a in listaArgumentos) {
            argumens.children.add(a.getArbolVisual())
        }
        return raiz
    }

    override val javaCode: String
        get() {
            var arg = ""
            for (a in listaArgumentos) {
                arg += a.javaCode + ","
            }
            if (listaArgumentos.size > 0) {
                arg = arg.substring(0, arg.length - 1)
            }
            return identificador.traducirIdentificador() + "(" + arg + ");"
        }

}