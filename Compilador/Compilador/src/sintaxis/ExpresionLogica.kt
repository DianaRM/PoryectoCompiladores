package sintaxis

import javafx.scene.control.TreeItem
import lexico.Token
import semantica.Simbolo
import semantica.TablaSimbolos
import java.util.*
import javax.swing.tree.DefaultMutableTreeNode

class ExpresionLogica(private val expresionLogica: ExpresionLogica?, private val opLogicoUnario: Token?,
                      private val expresionRelacional: ExpresionRelacional?, private val invocacionFuncion: Sentencia?, private val bool: Token?, private val identificador: Token?,
                      private val zlogica: ZLogica?) {
    override fun toString(): String {
        return ("ExpresionLogica [expresionLogica=" + expresionLogica + ", opLogicoUnario=" + opLogicoUnario
                + ", expresionRelacional=" + expresionRelacional + ", invocacionFuncion=" + invocacionFuncion
                + ", bool=" + bool + ", identificador=" + identificador + ", zlogica=" + zlogica + "]")
    }

    val arbolVisual: DefaultMutableTreeNode
        get() {
            val raiz = DefaultMutableTreeNode("ExpresionLogica")
            if (opLogicoUnario != null) {
                raiz.add(DefaultMutableTreeNode(opLogicoUnario.lexema))
                if (expresionLogica != null) {
                    raiz.add(expresionLogica.arbolVisual)
                    if (zlogica != null) {
                        raiz.add(zlogica.arbolVisual)
                    }
                    return raiz
                }
            }
            if (expresionLogica != null) {
                raiz.add(expresionLogica.arbolVisual)
                if (zlogica != null) {
                    raiz.add(zlogica.arbolVisual)
                }
                return raiz
            }
            if (bool != null) {
                raiz.add(DefaultMutableTreeNode(bool.lexema))
                if (zlogica != null) {
                    raiz.add(zlogica.arbolVisual)
                }
                return raiz
            }
            if (expresionRelacional != null) {
                raiz.add(expresionRelacional.arbolVisual)
                if (zlogica != null) {
                    raiz.add(zlogica.arbolVisual)
                }
                return raiz
            }
            if (invocacionFuncion != null) {
                raiz.add(invocacionFuncion.arbolVisual)
                if (zlogica != null) {
                    raiz.add(zlogica.arbolVisual)
                }
                return raiz
            }
            if (identificador != null) {
                raiz.add(DefaultMutableTreeNode(identificador.lexema))
                if (zlogica != null) {
                    raiz.add(zlogica.arbolVisual)
                }
                return raiz
            }
            return raiz
        }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        expresionLogica?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        expresionRelacional?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        zlogica?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
        invocacionFuncion?.llenarTablaSimbolos(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos?, erroresSemanticos: ArrayList<String?>?, ambito: Simbolo?) {
        expresionLogica?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        expresionRelacional?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        invocacionFuncion?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
        if (ambito != null) {
            identificador?.analizarSemantica(tablaSimbolos!!, erroresSemanticos!!, ambito)
        }
        zlogica?.analizarSemantica(tablaSimbolos, erroresSemanticos, ambito)
    }

    fun getArbolVisual(): TreeItem<String>? {
        val raiz = TreeItem("ExpresionLogica")
        if (opLogicoUnario != null) {
            raiz.children.add(TreeItem(opLogicoUnario.lexema))
            if (expresionLogica != null) {
                raiz.children.add(expresionLogica.getArbolVisual())
                if (zlogica != null) {
                    raiz.children.add(zlogica.getArbolVisual())
                }
                return raiz
            }
        }
        if (expresionLogica != null) {
            raiz.children.add(expresionLogica.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (bool != null) {
            raiz.children.add(TreeItem(bool.lexema))
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (expresionRelacional != null) {
            raiz.children.add(expresionRelacional.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (invocacionFuncion != null) {
            raiz.children.add(invocacionFuncion.getArbolVisual())
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        if (identificador != null) {
            raiz.children.add(TreeItem(identificador.lexema))
            if (zlogica != null) {
                raiz.children.add(zlogica.getArbolVisual())
            }
            return raiz
        }
        return raiz
    }

    val javaCode: String
        get() {
            var codigo = ""
            if (opLogicoUnario != null) {
                codigo += "!"
                if (expresionLogica != null) {
                    codigo += "( " + expresionLogica.javaCode + " )"
                    if (zlogica != null) {
                        codigo += zlogica.javaCode
                    }
                    return codigo
                }
            }
            if (expresionLogica != null) {
                codigo += "(" + expresionLogica.javaCode + ")"
                if (zlogica != null) {
                    codigo += zlogica.javaCode
                }
                return codigo
            }
            if (bool != null) {
                codigo += bool.traducirReservada()
                if (zlogica != null) {
                    codigo += zlogica.javaCode
                }
                return codigo
            }
            if (expresionRelacional != null) {
                codigo += "(" + expresionRelacional.javaCode + ")"
                if (zlogica != null) {
                    codigo += zlogica.javaCode
                }
                return codigo
            }
            if (invocacionFuncion != null) {
                codigo += "(" + invocacionFuncion.javaCode + ")"
                if (zlogica != null) {
                    codigo += zlogica.javaCode
                }
                return codigo
            }
            if (identificador != null) {
                codigo += "( " + identificador.traducirIdentificador() + " )"
                if (zlogica != null) {
                    codigo += zlogica.javaCode
                }
                return codigo
            }
            return codigo
        }
}