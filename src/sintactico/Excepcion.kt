package sintactico

import javafx.scene.control.TreeItem

class Excepcion(var expresionCadena: ExpresionCadena?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Excepcion")
        raiz.children.add(expresionCadena!!.getArbolVisual())
        return raiz
    }

}