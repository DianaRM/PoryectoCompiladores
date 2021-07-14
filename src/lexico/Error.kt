package lexico

class Error(var fila: Int, var columna: Int, var mensaje: String) {

    override fun toString(): String {
        return "Error [fila=$fila, columna=$columna, mensaje=$mensaje]"
    }
}