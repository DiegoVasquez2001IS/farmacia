package factura;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 4/05/2017.
 */
public class DetalleFactura implements Initializable{

    @FXML
    private GridPane rootClientes;

    @FXML
    private TextField txtCodigo;

    @FXML
    private ComboBox<String> cbEmpleado;

    @FXML
    private ComboBox<String> cbCliente;

    @FXML
    private ComboBox<String> cbPago;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
