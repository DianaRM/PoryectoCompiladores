package controladores

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import lexico.AnalizadorLexico
import lexico.Error
import lexico.Token
import sintactico.AnalizadorSintactico
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

    }

    @FXML
    fun analizar(event: ActionEvent?) {
        if(areaCodigo.text.isNotEmpty()) {
            tblLexico.items.clear()
            tblSintactico.items.clear()
            tblTokens.items.clear()

            val lexico = AnalizadorLexico(areaCodigo.text)
            lexico.analizar()
            val listaErroresLexico = lexico.listaDeErrores
            val lexicoTokens = lexico.listaTokens
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

           // val sintactico = AnalizadorSintactico(tokens)


        }
    }

}

