package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class Parametro(var tipoDato: Token, var identificador: Token) {
    override fun toString(): String {
        return "Parametro [tipoDato=$tipoDato, identificador=$identificador]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() = DefaultMutableTreeNode(identificador.lexema + " " + tipoDato.lexema)

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        tablaSimbolos.guardarSimboloVariable(identificador.lexema, tipoDato.lexema, identificador.fila,
                identificador.columna, ambito!!, null)
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("${identificador.lexema}" )
        raiz.children.add(TreeItem("${tipoDato.lexema}"))
        return raiz
    }

    val javaCode: String
        get() {
            val tipo = tipoDato.traducirReservada()
            val iden = identificador.traducirIdentificador()
            return "$tipo $iden"
        }
}