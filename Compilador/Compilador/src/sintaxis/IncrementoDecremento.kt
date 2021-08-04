package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class IncrementoDecremento(private val identificador: Token?, private val oPincremento: Token) : Sentencia() {
    override val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("IncrementoDecremento")
            raiz.add(DefaultMutableTreeNode(identificador!!.lexema))
            raiz.add(DefaultMutableTreeNode(oPincremento.lexema))
            return raiz
        }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {}
    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        if (ambito != null) {
            identificador?.analizarSemantica(tablaSimbolos!!, erroresSemanticos!!, ambito)
        }
    }

    override fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("IncrementoDecremento")
        raiz.children.add(TreeItem(identificador!!.lexema))
        raiz.children.add(TreeItem(oPincremento.lexema))
        return raiz
    }

    override val javaCode: String
        get() = identificador!!.traducirIdentificador() + oPincremento.lexema + ";"

}