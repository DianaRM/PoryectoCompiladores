package semantica

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper
import sintaxis.Expresion
import sintaxis.UnidadDeCompilacion
import java.util.*
import kotlin.collections.ArrayList


/**
 * Clase que representa un Analizador Semántico del compilador
 * @author Esthefania Lemus - Diana Ramirez - Cristian Bonilla
 */
class AnalizadorSemantico(uc: UnidadDeCompilacion) {
    var erroresSemanticos: ArrayList<String?>? = ArrayList<String?>()
    private var tablaSimbolos: TablaSimbolos = TablaSimbolos(erroresSemanticos)
    private val uc: UnidadDeCompilacion
    fun llenarTablaSimbolos() {
        uc.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos)
    }

    fun analizarSemantica() {
        uc.analizarSemantica(tablaSimbolos, erroresSemanticos)
    }

    fun getTablaSimbolos(): TablaSimbolos {
        return tablaSimbolos
    }

    fun setTablaSimbolos(tablaSimbolos: TablaSimbolos) {
        this.tablaSimbolos = tablaSimbolos
    }

    init {
        erroresSemanticos = ArrayList()
        tablaSimbolos = TablaSimbolos(erroresSemanticos)
        this.uc = uc
    }
}