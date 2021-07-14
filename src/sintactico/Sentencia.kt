package sintactico

import javafx.scene.control.TreeItem

abstract class Sentencia {
    /**
     * obtiene un arbol visual de la representacion de los tokens en sus respectivas estructuras sintacticas
     * @return
     */
    abstract fun getArbolVisual(): TreeItem<String>
}