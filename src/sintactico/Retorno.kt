package sintactico

import javafx.scene.control.TreeItem

class Retorno(private val categoriaRetorno: CategoriaRetorno?) : Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        return categoriaRetorno!!.getArbolVisual()
    }

    override fun toString(): String {
        return "Retorno [categoriaRetorno=$categoriaRetorno]"
    }

}