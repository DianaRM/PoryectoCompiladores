package sintaxis

import javafx.scene.control.TreeItem
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class Retorno(private val categoriaRetorno: CategoriaRetorno?) : Sentencia() {
    override fun toString(): String {
        return "Retorno [categoriaRetorno=$categoriaRetorno]"
    }

    override val arbolVisual: DefaultMutableTreeNode
        get() = categoriaRetorno!!.arbolVisual

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        categoriaRetorno!!.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        categoriaRetorno?.analizarSemantica(tablaSimbolos, erroresSemanticos!!, ambito)
    }

    override fun getArbolVisual(): TreeItem<String>? {
        return categoriaRetorno!!.getArbolVisual()
    }

    override val javaCode: String
        get() = "return " + categoriaRetorno!!.javaCode + ";"

}