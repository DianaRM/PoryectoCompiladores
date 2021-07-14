package sintactico

import javafx.scene.control.TreeItem
import lexico.Token
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode
import kotlin.collections.ArrayList

class Funcion(var nombre: Token, var parametros: ArrayList<Parametro>?, var tipoRetorno: Token?,
              var listaSentencias: ArrayList<Sentencia>?) {
    override fun toString(): String {
        return ("Funcion [tipoRetorno=" + tipoRetorno + ", nombre=" + nombre + ", parametros=" + parametros
                + ", listaSentencias=" + listaSentencias + "]")
    }

    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Funcion ${nombre.lexema}")
        if (tipoRetorno != null) {
            raiz.children.add(TreeItem("${nombre.lexema}: ${tipoRetorno!!.lexema}"))
        } else {
            raiz.children.add(TreeItem(nombre.lexema))
        }
        if (parametros != null) {
            if (parametros!!.isNotEmpty()) {
                val params = TreeItem("Parametros")
                raiz.children.add(params)
                for (parametro in parametros!!) {
                    params.children.add(parametro.getArbolVisual())
                }
            }
        }
        val sentencias = TreeItem("Sentencias")
        raiz.children.add(sentencias)
        for (sentencia in listaSentencias!!) {
            print("Sentencia: $sentencia")
            sentencias.children.add(sentencia.getArbolVisual())
        }
        return raiz
    }

    fun getTipoParametros(): ArrayList<String?> {
        val tipoParametros = ArrayList<String?>()
        for (i in parametros!!.indices) {
            val tipo = parametros!![i].tipoDato.lexema
            tipoParametros.add(tipo)
        }
        return tipoParametros
    }

}
