package sintactico

import javafx.scene.control.TreeItem
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class UnidadDeCompilacion(private val listaFunciones: ArrayList<Funcion>) {
    override fun toString(): String {
        return "UnidadDeCompilacion [listaFunciones=$listaFunciones]"
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Unidad de Compilacion")

        for(function in listaFunciones){
            raiz.children.add(function.getArbolVisual())
        }
        return  raiz
    }

}