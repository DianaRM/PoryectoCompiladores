package controladores

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.cell.PropertyValueFactory
import lexico.Token
import java.awt.event.ActionEvent
import java.net.URL
import java.util.*
import javax.swing.table.TableColumn

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
    fun analizarCodigo (e:ActionEvent){
        if(codigo.text.lenght > 0){
            val lexico = AnalizadorLexico(codigo.text)
            lexico.analizar()
            print (lexico.listaTokens)
            tablaTokens.items = FXCollections.observableArrayList(lexico.listaTokens)

        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        TODO("Not yet implemented")

        lstLexema.cellValueFactory = PropertyValueFactory("lexema")
        lstCategoria.cellValueFactory = PropertyValueFactory("categoria")
        lstFila.cellValueFactory = PropertyValueFactory("fila")
        lstColumna.cellValueFactory = PropertyValueFactory("columna")
    }
}