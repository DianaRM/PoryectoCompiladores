package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class Funcion(var nombre: Token, var parametros: ArrayList<Parametro>?, var tipoRetorno: Token?,
              var listaSentencias: ArrayList<Sentencia>?) {
    override fun toString(): String {
        return ("Funcion [tipoRetorno=" + tipoRetorno + ", nombre=" + nombre + ", parametros=" + parametros
                + ", listaSentencias=" + listaSentencias + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("Funci�n")
            raiz.add(DefaultMutableTreeNode("Nombre: " + nombre.lexema))
            if (tipoRetorno != null) {
                raiz.add(DefaultMutableTreeNode("Tipo de retorno: " + tipoRetorno!!.lexema))
            }
            val params = DefaultMutableTreeNode("Par�metros")
            raiz.add(params)
            for (parametro in parametros!!) {
                params.add(parametro.arbolVisual)
            }
            val sentencias = DefaultMutableTreeNode("Sentencias")
            raiz.add(sentencias)
            for (sentencia in listaSentencias!!) {
                sentencias.add(sentencia.arbolVisual)
            }
            return raiz
        }
    val tipoParametros: ArrayList<String?>
        get() {
            val tipoParametros = ArrayList<String?>()
            for (i in parametros!!.indices) {
                val tipo = parametros!![i].tipoDato.lexema
                tipoParametros.add(tipo)
            }
            return tipoParametros
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {

        var cont:Int = 0
        if (tablaSimbolos != null) {
            for(s in tablaSimbolos.listaSimbolos!!){
                if(s.nombre == nombre.lexema) cont+=1
            }
        }

        if(cont > 1){
            erroresSemanticos!!.add("Hay mas de una función con el mismo nombre: " + nombre.lexema)
        }else {

            for (s in listaSentencias!!) {
                s.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
            }
            if (parametros!!.size > 0) {
                for (p in parametros!!) {
                    if (tablaSimbolos != null) {
                        p.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
                    }
                }
            }
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, erroresSemanticos: ArrayList<String?>?) {
        val tiposParametros = ArrayList<String>()
        for (i in parametros!!.indices) {
            tiposParametros.add(parametros!![i].tipoDato.lexema)
        }

        var ambito: Simbolo? = null
        for (s in tablaSimbolos.listaSimbolos) {
            if (s.nombre == nombre.lexema) {
                if (s.tipoParametros == tiposParametros) {
                    ambito = s
                    break
                }
            }
        }
        for (i in listaSentencias!!.indices) {
            listaSentencias!![i].analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        }
    }

    fun getArbolVisual(): TreeItem<String>? {
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

    val javaCode: String?
        get() {
            var codigo: String? = ""
            var param = ""
            var sentenc = ""
            if (nombre.traducirIdentificador() == "main") {
                codigo += "\t public static void main(String[]args) { \n"
                for (s in listaSentencias!!) {
                    sentenc += """
                    ${s.javaCode}
                    
                    """.trimIndent()
                }
                codigo += sentenc
                codigo += "\n }"
                return codigo
            }
            if (parametros != null) {
                for (i in parametros!!.indices) {
                    param += "" + parametros!![i].javaCode + ","
                }
            }
            if (listaSentencias != null) {
                for (s in listaSentencias!!) {
                    sentenc += """
                    ${s.javaCode}
                    
                    """.trimIndent()
                }
            }
            if (parametros!!.size > 0) {
                param = param.substring(0, param.length - 1)
            }
            codigo += """	 public static ${tipoRetorno!!.traducirReservada()} ${nombre.traducirIdentificador()}( $param ) { 
"""
            codigo += sentenc
            codigo += "\n }"
            return codigo
        }
}