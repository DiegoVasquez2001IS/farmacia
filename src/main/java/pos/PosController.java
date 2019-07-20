package pos;

import clientes.model.Cliente;
import clientes.model.ClienteDAO;
import com.sun.javafx.scene.control.skin.FXVK;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jdk.internal.org.objectweb.asm.Handle;
import org.controlsfx.control.PopOver;
import pago.PagoDAO;
import pago.model.Pago;
import pos.model.*;
import reportes.Reporte;
import sesion.Sesion;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by user on 7/02/2017.
 */
public class PosController implements Initializable{

    @FXML
    private GridPane rootPos;

    @FXML
    private TableView<Pos> tblDatos;

    @FXML
    private TableColumn<Pos, String> colNombre;

    @FXML
    private TableColumn<Pos, Float> colPrecioVenta;

    @FXML
    private TableColumn<Pos, Integer> colStock;

    @FXML
    private TableColumn<Pos, String> colProveedor;

    @FXML
    private TableColumn<Pos, String> colPresentacion;

    @FXML
    private ListView<ProductosListaCompras> listaCompra;

    @FXML
    private Label lblNoProductos;

    @FXML
    private Label lblSubTotal;

    @FXML
    private TextField txtDescuento;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnComprar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtRecibido;

    @FXML
    private TextField txtCambio;

    @FXML
    private ComboBox<Cliente> comboCliente;

    @FXML
    private ComboBox<Pago> comboTipoPago;

    @FXML
    private TextField txtEmpleado;

    @FXML
    private Label lblNoOrden;

    @FXML
    private Label lblFecha;

    @FXML
    private CheckBox checkImprimir;

    @FXML
    private CheckBox checkGuardar;

    @FXML
    private TextField txtDescuentoTotal;

    @FXML
    private Button btnNuevoCliente;


    private ClienteDAO clienteDAO = new ClienteDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private PagoDAO pagoDAO = new PagoDAO();

    private int mUltimoId = 0;

    private VenderDAO venderDAO = new VenderDAO();

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Pago> listaPago = FXCollections.observableArrayList();

    private PosDAO posDAO = new PosDAO();

    private ObservableList<Pos> listaProductos = FXCollections.observableArrayList();

    public static ObservableList<ProductosListaCompras> listaCompras = FXCollections.observableArrayList();

    public static ArrayList<Long> listaCodigos = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
        configFiltrado();
        configTextWrapperForTableView();
        showPopUpForItem();
        llenarCombos();
        initLabelsFactura();
        configCellFactoriesCliente();
        configCellFactoriesPago();
        configCambio();
        listaCompra.setItems(listaCompras);
        configClickListViewAndTableView();
        listaCompra.setCellFactory(new Callback<ListView<ProductosListaCompras>, ListCell<ProductosListaCompras>>() {
            @Override
            public ListCell<ProductosListaCompras> call(ListView<ProductosListaCompras> param) {
                PosCell posCell = new PosCell();
                return posCell;
            }
        });
        listaCompras.addListener(new ListChangeListener<ProductosListaCompras>() {
            @Override
            public void onChanged(Change<? extends ProductosListaCompras> c) {
                lblNoProductos.setText(calcularNoProductos()+"");
                lblSubTotal.setText(getDosDecimales(calcularSubtotal())+"");
                boolean hayDescuento = false;
                for(ProductosListaCompras plc : listaCompras){
                    if(plc.getDescuento() > 0){
                        hayDescuento = true;
                    }
                }
                if(hayDescuento){
                    txtDescuento.setText(getDosDecimales(calcularTotalDescuento())+"");
                    txtDescuentoTotal.setText("0");
                    txtDescuentoTotal.setDisable(true);
                    lblTotal.setText(getDosDecimales(calcularTotal())+"");
                }else{
                    txtDescuentoTotal.setDisable(false);
                    txtDescuento.setText(Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText())/100)+"");
                    float total = Float.parseFloat(lblSubTotal.getText()) -
                            (Float.parseFloat(lblSubTotal.getText())*(Float.parseFloat(txtDescuentoTotal.getText())/100));
                    lblTotal.setText(total+"");
                }
            }
        });
        btnComprar.setOnAction((event -> {
            if(listaCompras.size() > 0){
                verReporte();
                vender();
                initLabelsFactura();
            }
        }));

        txtDescuentoTotal.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                txtDescuento.setText(Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText())/100)+"");
                float total = Float.parseFloat(lblSubTotal.getText()) -
                        (Float.parseFloat(lblSubTotal.getText())*(Float.parseFloat(txtDescuentoTotal.getText())/100));
                lblTotal.setText(getDosDecimales(total)+"");
            }
        }));

        btnNuevoCliente.setOnAction((event -> {
            abrirNuevoCliente();
        }));
    }

    private void verReporte(){
        List<ProductoReporte> listaReporte = new ArrayList<>();
        for(int i = 0; i<listaCompras.size(); i++){
            listaReporte.add(new ProductoReporte(String.valueOf(listaCompras.get(i).getCantidad()),
                    listaCompras.get(i).getNombre(),
                    String.valueOf(listaCompras.get(i).getPrecio()),
                    "%" + String.valueOf(listaCompras.get(i).getDescuento()),
                    String.valueOf(listaCompras.get(i).getSubtotal())));
        }
        Reporte reporte = new Reporte();
        reporte.pdfReporte(listaReporte,comboCliente.getSelectionModel().getSelectedItem().getNombre() +
                " " + comboCliente.getSelectionModel().getSelectedItem().getApellidos(),
                comboCliente.getSelectionModel().getSelectedItem().getNit(),
                comboCliente.getSelectionModel().getSelectedItem().getDireccion(),
                String.valueOf(comboCliente.getSelectionModel().getSelectedItem().getCelular()),
                String.valueOf(lblNoOrden.getText()),checkImprimir.isSelected(),checkGuardar.isSelected(),
                lblSubTotal.getText(),txtDescuento.getText(),lblTotal.getText());
    }

    private void vender(){
        if(!venderDAO.AgregarModificarOrderPedido(crearOrden(),"new")){
            for(int i = 0; i < listaCompras.size(); i++){
                ProductosListaCompras plc = listaCompras.get(i);
                venderDAO.AgregarModificarDetalleOrderPedido(plc,crearOrden(),"new");
                if(listaCompras.get(i).getDescuento() > 0){
                    venderDAO.descuentoPorProducto(venderDAO.getUltimoIdDetalle(),listaCompras.get(i).getDescuento());
                }
            }
            if(txtDescuentoTotal.getText().length()>0){
                if(txtDescuentoTotal.getText().matches("\\d+.?\\d*")){
                    venderDAO.descuentoTotalVenta(venderDAO.getUltimoIdOrden(),Float.parseFloat(txtDescuentoTotal.getText()));
                }
            }
            PosController.listaCompras.clear();
            PosController.listaCodigos.clear();
            addDataToTable();
            txtRecibido.setText("");
            txtCambio.setText("");
            txtDescuentoTotal.setText("0");
        }
    }

    private void configCambio(){
        txtRecibido.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(validarCantidad(txtRecibido.getText())){
                    txtCambio.setText(String.valueOf(getDosDecimales(Float.parseFloat(txtRecibido.getText()) - Float.parseFloat(lblTotal.getText()))));
                }
            }
        }));
    }

    private boolean validarCantidad(String numero){
        if (!numero.isEmpty()){
            if (!numero.matches("\\d*\\.?\\d*")) {
                txtRecibido.setText(numero.replaceAll("[^\\d]", ""));
            }else {
                if(Float.parseFloat(numero) <= 0){
                    txtRecibido.setText("1");
                }else{
                    return true;
                }
            }
        }
        return false;
    }

    private void configCellFactoriesCliente(){
        comboCliente.setCellFactory(new Callback<ListView<Cliente>, ListCell<Cliente>>() {
            @Override
            public ListCell<Cliente> call(ListView<Cliente> param) {
                ListCell<Cliente> listCell = new ListCell<Cliente>(){
                    @Override
                    protected void updateItem(Cliente item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText("");
                        }else {
                            setText(getStringForCliente(item));
                        }
                    }
                };
                return listCell;
            }
        });
        comboCliente.setButtonCell(new ListCell<Cliente>(){
            @Override
            protected void updateItem(Cliente item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText("");
                }else{
                    setText(getStringForCliente(item));
                }
            }
        });
        comboCliente.setConverter(new StringConverter<Cliente>() {
            private Map<String,Cliente> map = new HashMap<String, Cliente>();
            @Override
            public String toString(Cliente object) {
                if(object != null){
                    String str = getStringForCliente(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }
            @Override
            public Cliente fromString(String string) {
                if(!map.containsKey(string)){
                    comboCliente.setValue(null);
                    comboCliente.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private String getStringForCliente(Cliente cliente){
        return cliente.getCelular() + "\t" + cliente.getApellidos() + " " + cliente.getNombre();
    }

    private void abrirNuevoCliente(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("clientes/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Nuevo Cliente");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                llenarCombos();
            }
        });
        stage.show();
    }

    private void configCellFactoriesPago(){
        comboTipoPago.setCellFactory(new Callback<ListView<Pago>, ListCell<Pago>>() {
            @Override
            public ListCell<Pago> call(ListView<Pago> param) {
                ListCell<Pago> cell = new ListCell<Pago>(){
                    @Override
                    protected void updateItem(Pago item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForPago(item));
                        }
                    }
                };
                return cell;
            }
        });

        comboTipoPago.setButtonCell(new ListCell<Pago>() {
            @Override
            protected void updateItem(Pago item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForPago(item));
                }
            }
        });

        comboTipoPago.setConverter(new StringConverter<Pago>() {
            private Map<String,Pago> map = new HashMap<String, Pago>();
            @Override
            public String toString(Pago object) {
                if(object != null){
                    String str = getStringForPago(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public Pago fromString(String string) {
                if(!map.containsKey(string)){
                    comboTipoPago.setValue(null); // acá se tenía comboEmpleados "ojo" por el error de filtrado
                    comboTipoPago.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private String getStringForPago(Pago pago){
        return pago.getPago();
    }

    private OrdenPedido crearOrden(){
        return new OrdenPedido(
                Integer.parseInt(lblNoOrden.getText()),
                comboCliente.getSelectionModel().getSelectedItem().getCodigo(),
                Sesion.getmUsuarioActual().getCodigoEmpleado(),
                comboTipoPago.getSelectionModel().getSelectedItem().getIdPago());
    }

    private void showPopUpForItem(){
        tblDatos.setRowFactory(new Callback<TableView<Pos>, TableRow<Pos>>() {

            @Override
            public TableRow<Pos> call(TableView<Pos> param) {

                TableRow<Pos> tableRow = new TableRow<>();
                PopOver popOver = new PopOver();

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pos/popup.fxml"));
                tableRow.setOnMouseEntered((MouseEvent event) -> {
                    popOver.setDetachable(true);
                    popOver.setDetached(true);
                    popOver.setCornerRadius(4);
                    try {
                        popOver.setContentNode(loader.load());
                        PopController popController = (PopController) loader.getController();
                        if(popController != null) {
                            popController.setCodigo(String.valueOf(tableRow.getItem().getCodigo()));
                            popController.setVencimiento(tableRow.getItem().getFechaVencimiento());
                            popController.setDolencia(tableRow.getItem().getDolencia());
                            popController.setColocacion(tableRow.getItem().getColocacion());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    popOver.show(rootPos,event.getScreenX()+20,event.getScreenY());

                });
                tableRow.setOnMouseExited((event -> {
                    popOver.hide();
                    loader.setRoot(null);
                    loader.setController(null);

                }));
                return tableRow;
            }
        });

    }

    private void configTextWrapperForTableView(){
        colNombre.setCellFactory(new Callback<TableColumn<Pos, String>, TableCell<Pos, String>>() {
            @Override
            public TableCell<Pos, String> call(TableColumn<Pos, String> param) {
                TableCell<Pos,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colNombre.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.40));
        colPrecioVenta.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colStock.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colPresentacion.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));
        colProveedor.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));

    }


    private void configClickListViewAndTableView(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                    if(tblDatos.getSelectionModel().getSelectedItem().getStock() > 0) {
                        Pos pos = tblDatos.getSelectionModel().getSelectedItem();
                        if (listaCodigos.contains(pos.getCodigo())) { // item existente
                            // tienes que ser los mismo datos que la lista
                            for (int i = 0; i < listaCompras.size(); i++) {
                                ProductosListaCompras plc = listaCompras.get(i);
                                if (plc.getCodigo() == pos.getCodigo()) {
                                    if (plc.getCantidad() < plc.getStock()) {
                                        plc.setCantidad(plc.getCantidad() + 1);
                                    }
                                    float aux = plc.getPrecio() * plc.getCantidad();
                                    float subTotal = 0f;
                                    if (plc.getDescuento() > 0f) {
                                        subTotal = aux - (aux * (plc.getDescuento() / 100));
                                    } else {
                                        subTotal = aux;
                                    }
                                    plc.setSubtotal(getDosDecimales(subTotal));
                                    listaCompras.set(i, plc);
                                    txtBuscar.clear();
                                    break;
                                }
                            }
                        } else { // item nuevo
                            listaCodigos.add(pos.getCodigo());
                            listaCompras.add(new ProductosListaCompras(pos.getCodigo(), pos.getNombre(),
                                    pos.getPreVenta(), pos.getStock(), 1, 0f, pos.getPreVenta()));
                            txtBuscar.clear();
                        }
                    }
                }
            }
        }));
        listaCompra.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                if(listaCompras.size() > 0){
                    if(listaCompra.getSelectionModel().getSelectedItem() != null){
                        listaCodigos.remove(listaCodigos.indexOf(listaCompra.getSelectionModel().getSelectedItem().getCodigo()));
                        listaCompras.remove(listaCompra.getSelectionModel().getSelectedItem());
                        listaCompra.refresh();
                    }
                }
            }
        });
    }

    private void initLabelsFactura(){
        lblNoOrden.setText(venderDAO.getUltimoIdOrden()+1+"");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        lblFecha.setText(dateFormat.format(date));

        Empleado empleado = null;
        for (Empleado aux : empleadoDAO.getEmpleados()){
            if(aux.getCodigo() == Sesion.getmUsuarioActual().getCodigoEmpleado()){
                empleado = aux;
                break;
            }
        }
        if(empleado != null){
            txtEmpleado.setText(empleado.getNombre() + " " +empleado.getApellidos());
        }
    }

    private void llenarCombos(){
        listaClientes.setAll(clienteDAO.getClientes());
        comboCliente.setItems(listaClientes);
        comboCliente.getSelectionModel().select(0);

        listaPago.setAll(pagoDAO.getPagos());
        comboTipoPago.setItems(listaPago);
        comboTipoPago.getSelectionModel().select(0);
    }

    private int calcularNoProductos() {
        return listaCompras.size();
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    private float calcularSubtotal(){
        float subtotal = 0;
        for(int i=0; i<listaCompras.size(); i++){
            ProductosListaCompras plc = listaCompras.get(i);
            subtotal+= (plc.getPrecio() * plc.getCantidad());
        }
        return subtotal;
    }

    private float calcularTotalDescuento(){
        return calcularSubtotal() - calcularTotal();
    }

    private float calcularTotal(){
        float total = 0f;
        for(int i = 0; i<listaCompras.size(); i++){
            ProductosListaCompras plc = listaCompras.get(i);
            total += plc.getSubtotal();
        }
        return total;
    }

    private void configFiltrado() {
        FilteredList<Pos> filteredList = new FilteredList<Pos>(listaProductos, p -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(producto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(producto.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (producto.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (producto.getColocacion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (producto.getDolencia().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (producto.getProveedor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (producto.getPresentacion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });

        });
        SortedList<Pos> sortedList = new SortedList<Pos>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
        txtBuscar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event ) {
               if(event.getCode() == KeyCode.ENTER) {
                   tblDatos.getSelectionModel().selectFirst();
                   if(tblDatos.getSelectionModel().getSelectedItem() != null){
                       if(tblDatos.getSelectionModel().getSelectedItem().getStock() > 0) {
                           Pos pos = tblDatos.getSelectionModel().getSelectedItem();
                           if (listaCodigos.contains(pos.getCodigo())) { // item existente
                               // tienes que ser los mismo datos que la lista
                               for (int i = 0; i < listaCompras.size(); i++) {
                                   ProductosListaCompras plc = listaCompras.get(i);
                                   if (plc.getCodigo() == pos.getCodigo()) {
                                       if (plc.getCantidad() < plc.getStock()) {
                                           plc.setCantidad(plc.getCantidad() + 1);
                                       }
                                       float aux = plc.getPrecio() * plc.getCantidad();
                                       float subTotal = 0f;
                                       if (plc.getDescuento() > 0f) {
                                           subTotal = aux - (aux * (plc.getDescuento() / 100));
                                       } else {
                                           subTotal = aux;
                                       }
                                       plc.setSubtotal(getDosDecimales(subTotal));
                                       listaCompras.set(i, plc);
                                       txtBuscar.clear();
                                       break;
                                   }
                               }
                           } else { // item nuevo
                               listaCodigos.add(pos.getCodigo());
                               listaCompras.add(new ProductosListaCompras(pos.getCodigo(), pos.getNombre(),
                                       pos.getPreVenta(), pos.getStock(), 1, 0f, pos.getPreVenta()));
                               txtBuscar.clear();
                           }
                       }
                       tblDatos.refresh();
                   }

               }
            }
        });
    }

    private void addDataToTable(){
        listaProductos.setAll(posDAO.getProductos());
    }

    private void matchDataWithTable(){
        addDataToTable();
        colNombre.setCellValueFactory(new PropertyValueFactory<Pos, String>("nombre"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<Pos, Float>("preVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<Pos, Integer>("stock"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<Pos, String>("proveedor"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<Pos, String>("presentacion"));
    }

}
