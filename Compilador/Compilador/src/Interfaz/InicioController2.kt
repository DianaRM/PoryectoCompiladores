package Interfaz

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import lexico.AnalizadorLexico
import lexico.Error
import lexico.Token
import semantica.AnalizadorSemantico
import semantica.Simbolo
import sintaxis.AnalizadorSintactico
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.net.URL
import java.util.*


class InicioController2: Initializable {
    @FXML
    private var btnAnalizar: Button? = null

    @FXML
    lateinit var areaCodigo: TextArea

    @FXML
    lateinit var tblTokens: TableView<Token>

    @FXML
    lateinit var colLexema: TableColumn<Token,String>

    @FXML
    lateinit var colCategoria: TableColumn<Token,String>

    @FXML
    lateinit var colFila: TableColumn<Token,Int>

    @FXML
    lateinit var colColumna: TableColumn<Token,Int>

    @FXML
    lateinit var tblLexico: TableView<Error>

    @FXML
    lateinit var colMensajeL: TableColumn<Error,String>

    @FXML
    lateinit var colFila2: TableColumn<Error,Int>

    @FXML
    lateinit var colColumna2: TableColumn<Error,Int>

    @FXML
    lateinit var tblSintactico: TableView<Error>

    @FXML
    lateinit var colMensajeS: TableColumn<Error,String>

    @FXML
    lateinit var colFila3: TableColumn<Error,Int>

    @FXML
    lateinit var colColumna3: TableColumn<Error,Int>

    @FXML
    lateinit var txtAreaMensaje: TextArea

    @FXML
    lateinit var tblSimbolos: TableView<Simbolo>

    @FXML
    lateinit var colNombreSemantico: TableColumn<Simbolo, String>

    @FXML
    lateinit var colTipoSemantico: TableColumn<Simbolo, String>

    @FXML
    lateinit var colFilaSemantico: TableColumn<Simbolo, Int>

    @FXML
    lateinit var colColumnaSemantico: TableColumn<Simbolo, Int>

    @FXML
    lateinit var colAmbitoSemantico: TableColumn<Simbolo, String>

    @FXML
    lateinit var colExpresionSemantico: TableColumn<Simbolo, String>

    @FXML
    private var tree: TreeView<String> = TreeView()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        colLexema.cellValueFactory = PropertyValueFactory("lexema")
        colCategoria.cellValueFactory = PropertyValueFactory("categoria")
        colFila.cellValueFactory = PropertyValueFactory("fila")
        colColumna.cellValueFactory = PropertyValueFactory("columna")

        colMensajeL.cellValueFactory = PropertyValueFactory("mensaje")
        colColumna2.cellValueFactory = PropertyValueFactory("columna")
        colFila2.cellValueFactory = PropertyValueFactory("fila")

        colMensajeS.cellValueFactory = PropertyValueFactory("mensaje")
        colColumna3.cellValueFactory = PropertyValueFactory("columna")
        colFila3.cellValueFactory = PropertyValueFactory("fila")

        colNombreSemantico.cellValueFactory = PropertyValueFactory("nombre")
        colTipoSemantico.cellValueFactory = PropertyValueFactory("tipo")
        colFilaSemantico.cellValueFactory = PropertyValueFactory("fila")
        colColumnaSemantico.cellValueFactory = PropertyValueFactory("columna")
        colAmbitoSemantico.cellValueFactory = PropertyValueFactory("ambito")
        colExpresionSemantico.cellValueFactory = PropertyValueFactory("expresion")
    }

    @FXML
    fun analizar(event: ActionEvent?) {
        if(areaCodigo.text.isNotEmpty()) {
            tblLexico.items.clear()
            tblSintactico.items.clear()
            tblSimbolos.items.clear()
            tblTokens.items.clear()

            val lexico = AnalizadorLexico(areaCodigo.text)
            lexico.analizar()
            val listaErroresLexico = lexico.listaDeErrores
            val lexicoTokens = lexico.getListaTokens()
            for(error in listaErroresLexico){
                tblLexico.items.add(error)
            }
            for(token in lexicoTokens){
                tblTokens.items.add(token)
            }
            val sintactico = AnalizadorSintactico(lexicoTokens)
            val unidadCompilacion =sintactico.esUnidadDeCompilacion()
            if (unidadCompilacion != null) {
                tree.root = unidadCompilacion.getArbolVisual()
            }
            val listaErroresSintactico = sintactico.listaErrores
            for (error in listaErroresSintactico){
                tblSintactico.items.add(error)
            }

            val semantico = AnalizadorSemantico(unidadCompilacion!!)
            semantico.llenarTablaSimbolos()
            val simbolos = semantico.getTablaSimbolos().listaSimbolos

            for(simbolo in simbolos){
                tblSimbolos.items.add(simbolo)
            }

            semantico.analizarSemantica()

            val listaErroresSemantico = semantico.erroresSemanticos
            println("LISTA DE ERRORES SEMANTICOS")
            println(listaErroresSemantico)
            var mensaje = ""
            for(error in listaErroresSemantico!!){
                mensaje += error + "\n"
            }

            txtAreaMensaje.text = mensaje;

           // val sintactico = AnalizadorSintactico(tokens)

            var codigo: String? = ""
            if (listaErroresLexico!!.isEmpty() && listaErroresSintactico!!.isEmpty() && listaErroresSemantico!!.isEmpty()) {
                codigo = unidadCompilacion.javaCode
                try {
                    crearArchivo(codigo)

                    //Invocar el compilador de Java
                    val p = Runtime.getRuntime().exec("javac src/Principal.java")
                    p.waitFor()

                    //Se ejecuta el .class
                    Runtime.getRuntime().exec("java Principal", null, File("src"))
                } catch (e2: Exception) {
                    e2.printStackTrace()
                }
            }
            println(codigo)


        }
    }

    @Throws(Exception::class)
    fun crearArchivo(codigo: String?) {
        val fw = FileWriter("Principal.java")
        val bw = BufferedWriter(fw)
        bw.write(codigo)
        bw.flush()
        bw.close()
    }

}

