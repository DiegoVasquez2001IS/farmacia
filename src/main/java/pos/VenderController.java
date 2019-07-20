package pos;

import clientes.model.Cliente;
import clientes.model.ClienteDAO;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pago.PagoDAO;
import pago.model.Pago;
import pos.model.OrdenPedido;
import pos.model.PasarDatosVenta;
import pos.model.ProductosListaCompras;
import pos.model.VenderDAO;
import sesion.Sesion;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 27/05/2017.
 */
public class VenderController implements Initializable, PasarDatosVenta {

    @FXML
    private GridPane rootVentas;

    @FXML
    private ComboBox<Cliente> comboClientes;

    @FXML
    private TextField txtEmpleado;

    @FXML
    private ComboBox<Pago> comboTipoPago;

    @FXML
    private Label lblNoProductos;

    @FXML
    private TextField txtNoProductos;

    @FXML
    private Label lblSubtotal;

    @FXML
    private TextField txtSubtotal;

    @FXML
    private Label lblDescuento;

    @FXML
    private TextField txtDescuento;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtTotal;

    @FXML
    private Button btnVender;

    private VenderDAO venderDAO = new VenderDAO();

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Pago> listaPago = FXCollections.observableArrayList();

    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private PagoDAO pagoDAO = new PagoDAO();

    private int mUltimoId = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mUltimoId = venderDAO.getUltimoIdOrden();

        btnVender.setOnAction((event -> {
            venderDAO.AgregarModificarOrderPedido(crearOrden(),"new");
            for(int i = 0; i<PosController.listaCompras.size(); i++){
                ProductosListaCompras plc = PosController.listaCompras.get(i);
                venderDAO.AgregarModificarDetalleOrderPedido(plc,crearOrden(),"new");
            }
            PosController.listaCompras.clear();
            PosController.listaCodigos.clear();
            close();
        }));


        llenarListas();
        //configFiltrado();
    }

    /**
     * COMBO TIPO PAGO NO TIENE ERRORES REVISAR ESO
     */


    private void configFiltrado(){
        FilteredList<Cliente> filteredListCliente = new FilteredList<Cliente>(listaClientes, c -> true);
        comboClientes.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredListCliente.setPredicate(cliente -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filtro = newValue.toLowerCase();
                if(cliente.getApellidos().toLowerCase().contains(filtro)){
                    return true;
                }else{
                    return false;
                }
            });
        }));
        comboClientes.setItems(filteredListCliente);


        FilteredList<Pago> filteredListPago = new FilteredList<Pago>(listaPago, p -> true);
        comboTipoPago.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredListPago.setPredicate(pago -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String filtro = newValue.toLowerCase();
                if(pago.getPago().toLowerCase().contains(filtro)){
                    return true;
                }else if(String.valueOf(pago.getIdPago()).toLowerCase().contains(filtro)){
                    return true;
                }else{
                    return false;
                }
            });
        }));
        comboTipoPago.setItems(filteredListPago);
    }

    private void llenarListas(){
        listaClientes.addAll(clienteDAO.getClientes());
        comboClientes.setItems(listaClientes);
        comboClientes.getSelectionModel().select(0);

        Empleado actual = empleadoDAO.getEmpleado(String.valueOf(Sesion.getmUsuarioActual().getCodigoEmpleado()));
        txtEmpleado.setText(actual.getNombre() + " " + actual.getApellidos());

        listaPago.addAll(pagoDAO.getPagos());
        comboTipoPago.setItems(listaPago);
        comboTipoPago.getSelectionModel().select(0);
    }

    private OrdenPedido crearOrden(){
        return new OrdenPedido(mUltimoId+1,comboClientes.getSelectionModel().getSelectedItem().getCodigo(),
                Sesion.getmUsuarioActual().getCodigoEmpleado(),
                comboTipoPago.getSelectionModel().getSelectedItem().getIdPago());
    }

    private void close(){
        Stage thisStage =  (Stage) rootVentas.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void pasar(String noProductos, String subTotal, String descuento, String total) {
        txtNoProductos.setText(noProductos);
        txtSubtotal.setText(subTotal);
        txtDescuento.setText(descuento);
        txtTotal.setText(total);
    }
}
