package sintaxis

import javafx.scene.control.TreeItem
import semantica.TablaSimbolos
import java.util.ArrayList
import javax.swing.tree.DefaultMutableTreeNode

class UnidadDeCompilacion(private val listaFunciones: ArrayList<Funcion>) {
    override fun toString(): String {
        return "UnidadDeCompilacion [listaFunciones=$listaFunciones]"
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Unidad de compilación")
            for (funcion in listaFunciones) {
                raiz.add(funcion.arbolVisual)
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String?>?) {
        if (listaFunciones.size > 0) {
            for (f in listaFunciones) {
                tablaSimbolos.guardarSimboloFuncion(f.nombre.lexema, f.tipoRetorno!!.lexema, f.tipoParametros)
                f.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, tablaSimbolos.buscarSimboloFuncion(f.nombre.lexema, f.tipoParametros))
            }
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?) {
        if (listaFunciones.size > 0) {
            for (i in listaFunciones.indices) {
                listaFunciones[i].analizarSemantica(tablaSimbolos!!, erroresSemanticos)
            }
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("Unidad de Compilacion")

        for(function in listaFunciones){
            raiz.children.add(function.getArbolVisual())
        }
        return  raiz
    }

    val javaCode: String?
        get() {
            var codigo: String? = ""
            codigo += "public class UnidadDeCompilacion { \n"
            if (listaFunciones.size > 0) {
                for (i in listaFunciones.indices) {
                    codigo += listaFunciones[i].javaCode
                }
            }
            codigo += "\n }"
            return codigo
        }

}