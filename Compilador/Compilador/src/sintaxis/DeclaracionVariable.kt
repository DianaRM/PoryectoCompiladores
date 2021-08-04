package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class DeclaracionVariable(private val tipoDato: Token, private val identificador: Token) : Sentencia() {
    override fun toString(): String {
        return "DeclaracionVariable [tipoDato=$tipoDato, identificador=$identificador]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() = DefaultMutableTreeNode(identificador.lexema + " " + tipoDato.lexema)

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        tablaSimbolos!!.guardarSimboloVariable(identificador.lexema, tipoDato.lexema, identificador.fila,
                identificador.columna, ambito!!, null)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {}
    override fun getArbolVisual(): TreeItem<String>? {
        return TreeItem(identificador.lexema + " " + tipoDato.lexema)
    }

    override val javaCode: String
        get() = tipoDato.traducirReservada() + " " + identificador.traducirIdentificador() + ";"

}