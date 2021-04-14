package controladores

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import lexico.AnalizadorLexico
import lexico.Token
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.util.*
import javafx.collections.ObservableList




class InicioController : Initializable {

    @FXML
    lateinit var txtCodigo:TextArea
    @FXML
    lateinit var tlbTablaTokens: TableView<Token>

    @FXML
    lateinit var lstLexema: TableColumn<Token,String>
    @FXML
    lateinit var lstCategoria: TableColumn<Token,String>
    @FXML
    lateinit var lstFila: TableColumn<Token,Int>
    @FXML
    lateinit var lstColumna: TableColumn<Token,Int>

    @FXML
    lateinit var cmbAutomatas: ComboBox<String>
    @FXML
    lateinit var imgImagenes: ImageView

    @FXML
    private lateinit var txtAutomata: Label


    override fun initialize(location: URL?, resources: ResourceBundle?) {

        lstLexema.cellValueFactory = PropertyValueFactory("lexema")
        lstCategoria.cellValueFactory = PropertyValueFactory("categoria")
        lstFila.cellValueFactory = PropertyValueFactory("fila")
        lstColumna.cellValueFactory = PropertyValueFactory("columna")

        cmbAutomatas.items.add("Real")
        cmbAutomatas.items.add("Cadena")
        cmbAutomatas.items.add("Caracter")
        cmbAutomatas.items.add("Comentario de Bloque")
        cmbAutomatas.items.add("Comentario de Linea")
        cmbAutomatas.items.add("Corchete")
        cmbAutomatas.items.add("Dos Puntos")
        cmbAutomatas.items.add("Entero")
        cmbAutomatas.items.add("Hexadecimal")
        cmbAutomatas.items.add("Identificador")
        cmbAutomatas.items.add("Llaves")
        cmbAutomatas.items.add("Operador Aritmetico")
        cmbAutomatas.items.add("Operador de Asignacion")
        cmbAutomatas.items.add("Incremento Decremento")
        cmbAutomatas.items.add("Operador Logico")
        cmbAutomatas.items.add("Operador Relacional")
        cmbAutomatas.items.add("Parentesis")
        cmbAutomatas.items.add("Punto")
        cmbAutomatas.items.add("Separador")
        cmbAutomatas.items.add("Terminal")

    }

    @FXML
    fun btnAnalizarCodigo(e: ActionEvent) {
        if(txtCodigo.text.length > 0){
            val lexico = AnalizadorLexico(txtCodigo.text)
            lexico.analizar()
            print (lexico.listaTokens)
            tlbTablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)
        }
    }

    fun aceptar(actionEvent: ActionEvent) {



        var valor = cmbAutomatas.value
        txtAutomata.text = valor
        var f: File = File("src/images/" + valor + ".png")

        val stream: InputStream = FileInputStream(f.absolutePath)
        //Creating the image view
        //Creating the image view
        //Setting image to the image view
        //Setting image to the image view
        imgImagenes.image = Image(stream)
        //Setting the image view parameters
        //Setting the image view parameters

    }

}