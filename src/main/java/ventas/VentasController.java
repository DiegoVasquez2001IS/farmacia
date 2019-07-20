package ventas;

import clientes.model.Cliente;
import clientes.model.ClienteDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import pago.PagoDAO;
import pago.model.Pago;
import pos.AgregarProductosController;
import pos.model.ProductoReporte;
import pos.model.VenderDAO;
import reportes.Reporte;
import sesion.Sesion;
import ventas.anulacion.PasarNoFactura;
import ventas.model.DetalleOPedido;
import ventas.model.OrdenPedido;
import ventas.model.OrdenPedidoDAO;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by user on 23/06/2017.
 */
public class VentasController implements Initializable {

    @FXML
    private GridPane rootPos;

    @FXML
    private TableView<OrdenPedido> tblDatos;

    @FXML
    private TableColumn<OrdenPedido, Integer> colNoOrden;

    @FXML
    private TableColumn<OrdenPedido, Float> colDescuento;

    @FXML
    private TableColumn<OrdenPedido, Float> colTotal;

    @FXML
    private TableColumn<OrdenPedido, String> colEstatus;

    @FXML
    private TableColumn<OrdenPedido, String> colMotivoAnulacion;

    @FXML
    private Label lblNoProductos;

    @FXML
    private Label lblSubTotal;

    @FXML
    private TextField txtDescuento;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtRecibido;

    @FXML
    private TextField txtCambio;

    @FXML
    private CheckBox checkImprimir;

    @FXML
    private CheckBox checkGuardar;

    @FXML
    private ComboBox<Cliente> comboCliente;

    @FXML
    private ComboBox<Pago> comboTipoPago;

    @FXML
    private TextField txtEmpleado;

    @FXML
    private ListView<DetalleOPedido> listaCompra;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblNoOrden;

    @FXML
    private Label lblFecha;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnAnular;

    @FXML
    private Button btnAgregar;

    @FXML
    private TextField txtDescuentoTotal;


    ClienteDAO clienteDAO = new ClienteDAO();
    PagoDAO pagoDAO = new PagoDAO();
    OrdenPedidoDAO ordenPedidoDAO = new OrdenPedidoDAO();
    private VenderDAO venderDAO = new VenderDAO();

    Reporte reporte = new Reporte();

    public static ObservableList<OrdenPedido> listaOrdenes = FXCollections.observableArrayList();
    public static ObservableList<DetalleOPedido> listaDetalles = FXCollections.observableArrayList();
    public static ObservableList<Long> listaCodigos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        limpiar();
        configWidthsForColumnsInTable();
        matchDataWithTable();
        configFiltrado();
        configClickTable();
        configListView();
        listaDetalles.addListener(new ListChangeListener<DetalleOPedido>() {
            @Override
            public void onChanged(Change<? extends DetalleOPedido> c) {
                lblNoProductos.setText(calcularNoProductos() + "");
                lblSubTotal.setText(getDosDecimales(calcularSubtotal()) + "");
                boolean hayDescuento = false;
                for (DetalleOPedido dop : listaDetalles) {
                    if (dop.getDescuento() > 0) {
                        hayDescuento = true;
                    }
                }
                if (hayDescuento) {
                    txtDescuento.setText(getDosDecimales(calcularTotalDescuento()) + "");
                    txtDescuentoTotal.setText("0");
                    txtDescuentoTotal.setDisable(true);
                    lblTotal.setText(getDosDecimales(calcularTotal()) + "");
                } else {
                    txtDescuentoTotal.setDisable(false);
                    //txtDescuento.setText(Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText()) / 100) + "");
                    txtDescuento.setText(tblDatos.getSelectionModel().getSelectedItem().getDescuentoCliente()+"");
                    float porcentajeDescuento = (Float.parseFloat(txtDescuento.getText()) * 100) /
                            Float .parseFloat(lblSubTotal.getText());
                    txtDescuentoTotal.setText(porcentajeDescuento+"");
                    float total = Float.parseFloat(lblSubTotal.getText()) -
                            (Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText()) / 100));
                    lblTotal.setText(total + "");
                }
                listaCodigos.clear();
                for (DetalleOPedido dop : listaDetalles) {
                    listaCodigos.add(dop.getIdProducto());
                }
            }
        });

        txtDescuentoTotal.setOnKeyPressed((event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtDescuento.setText(Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText()) / 100) + "");
                float total = Float.parseFloat(lblSubTotal.getText()) -
                        (Float.parseFloat(lblSubTotal.getText()) * (Float.parseFloat(txtDescuentoTotal.getText()) / 100));
                lblTotal.setText(total + "");
            }
        }));

        btnModificar.setOnAction((event -> {
            verReporte();
            modificar();
        }));

        configCambio();

        btnAgregar.setOnAction((event -> {
            if(tblDatos.getSelectionModel().getSelectedItem() != null){
                abrirVentanaProductos();
            }
        }));

        configClickListenerListView();

        /*btnCierre.setOnAction((event -> {
            ObservableList<DetalleOPedido> listaDetalles = FXCollections.observableArrayList(
                    ordenPedidoDAO.getDetallesXOrdenPedido(1));
            ObservableList<DetalleOPedidoReporte> listaDetallesReporte = FXCollections.observableArrayList();
            listaDetallesReporte.clear();
            DetalleOPedidoReporte dopr = null;
            for (DetalleOPedido dop : listaDetalles) {
                dopr = new DetalleOPedidoReporte(dop.getNumDetalle() + "", dop.getIdProducto() + "",
                        dop.getNomPro(), dop.getCantidad() + "", dop.getPrecioVenta() + "",
                        dop.getDescuento() + "", dop.getImporte() + "");
                listaDetallesReporte.add(dopr);
            }
            for (DetalleOPedidoReporte rep : listaDetallesReporte) {
                System.out.println("DETALLE---- + " + rep.getNomProducto());
            }
            reporte.reporteCierreDia(listaDetallesReporte, false);
        }));*/
    }

    private void limpiar(){
        lblNoOrden.setText("");
        lblFecha.setText("");
        lblSubTotal.setText("");
        lblNoProductos.setText("");
        lblTotal.setText("");
        comboTipoPago.setItems(null);
        comboCliente.setItems(null);

        listaDetalles.clear();
    }
    private void addDataToTable(){
        listaOrdenes.setAll(ordenPedidoDAO.getOrdenesPedido());
    }

    private void abrirVentanaProductos(){
        tblDatos.setDisable(true);
        AgregarProductosController mController;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pos/agregar_producto.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();
        Scene posScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(posScene);
        stage.setTitle("Agregar Producto");
        mController.pasarNoCompra(Integer.parseInt(lblNoOrden.getText()));
        stage.setOnHiding((event -> {
            AgregarProductosController.listaProductos.clear();
            tblDatos.setDisable(false);
        }));
        stage.show();
    }

    private void matchDataWithTable(){
        addDataToTable();
        colNoOrden.setCellValueFactory(new PropertyValueFactory<OrdenPedido, Integer>("numOrdenPedido"));;
        colDescuento.setCellValueFactory(new PropertyValueFactory<OrdenPedido, Float>("descuentoCliente"));
        colTotal.setCellValueFactory(new PropertyValueFactory<OrdenPedido, Float>("total"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<OrdenPedido, String>("estatus"));
        colMotivoAnulacion.setCellValueFactory(new PropertyValueFactory<OrdenPedido, String>("motivoAnulacion"));
        //tblDatos.setItems(listaOrdenes);
        configTextWrapperToTable();
        convertComboClienteToString();
        convertComboTipoPagoToString();
    }

    private void configTextWrapperToTable(){
        colMotivoAnulacion.setCellFactory(new Callback<TableColumn<OrdenPedido, String>, TableCell<OrdenPedido, String>>() {
            @Override
            public TableCell<OrdenPedido, String> call(TableColumn<OrdenPedido, String> param) {
                TableCell<OrdenPedido,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colMotivoAnulacion.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });
    }

    private void configWidthsForColumnsInTable(){
        colNoOrden.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colDescuento.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colTotal.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colEstatus.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colMotivoAnulacion.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.40));
    }

    private void configClickTable(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if (tblDatos.getSelectionModel().getSelectedItem() != null){
                    llenarDatosEncabezado(tblDatos.getSelectionModel().getSelectedItem());
                    llenarListaDetalle(tblDatos.getSelectionModel().getSelectedItem());
                    llenarDatosFinalesFactura();
                    txtDescuento.setText(tblDatos.getSelectionModel().getSelectedItem().getDescuentoCliente()+"");
                    float porcentajeDescuento = (Float.parseFloat(txtDescuento.getText()) * 100) /
                            Float .parseFloat(lblSubTotal.getText());
                    txtDescuentoTotal.setText(porcentajeDescuento+"");
                    habilitarDesabilitarBotones(lblFecha.getText(),tblDatos.getSelectionModel().getSelectedItem().getEstatus());
                    btnAnular.setOnAction((event1 -> {
                        abrirAnulacion(tblDatos.getSelectionModel().getSelectedItem().getNumOrdenPedido());
                    }));
                }
            }
        }));
    }

    private void llenarDatosEncabezado(OrdenPedido ordenPedido){
        lblNoOrden.setText(ordenPedido.getNumOrdenPedido()+"");
        lblFecha.setText(ordenPedido.getFecha());
        ObservableList<Cliente> cliente = FXCollections.observableArrayList();
        cliente.add(clienteDAO.getClienteXId(ordenPedido.getIdCliente()));
        comboCliente.setItems(cliente);
        comboCliente.getSelectionModel().select(0);
        ObservableList<Pago> pago = FXCollections.observableArrayList();
        pago.add(pagoDAO.getPagoXId(ordenPedido.getCodTipoPago()));
        comboTipoPago.setItems(pago);
        comboTipoPago.getSelectionModel().select(0);
        txtEmpleado.setText(ordenPedido.getNomEmpleado());
    }

    private void convertComboClienteToString(){
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
        return cliente.getCelular() + "\t" +cliente.getNombre() + " " + cliente.getApellidos();
    }

    private void convertComboTipoPagoToString(){
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
                    comboTipoPago.setValue(null);
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

    private void llenarListaDetalle(OrdenPedido ordenPedido){
        listaDetalles.clear();
        Collection<DetalleOPedido> listaAux = ordenPedidoDAO.getDetallesXOrdenPedido(ordenPedido.getNumOrdenPedido());
        float descuentoTotal = 0;
        for(DetalleOPedido dop : listaAux){
            listaCodigos.clear();
            dop.setImporte(getDosDecimales(dop.getImporte()-dop.getDescuento()));
            descuentoTotal+= dop.getDescuento();
            float descuento = (dop.getDescuento() * 100) / (dop.getPrecioVenta() * dop.getCantidad());
            dop.setDescuento(getDosDecimales(descuento));
            listaDetalles.add(dop);
            listaCodigos.add(dop.getIdProducto());
        }
        txtDescuento.setText(descuentoTotal+"");
        //listaDetalles.setAll(ordenPedidoDAO.getDetallesXOrdenPedido(ordenPedido.getNumOrdenPedido()));
    }

    private void configListView(){
        listaCompra.setCellFactory(new Callback<ListView<DetalleOPedido>, ListCell<DetalleOPedido>>() {
            @Override
            public ListCell<DetalleOPedido> call(ListView<DetalleOPedido> param) {
                return new CeldaVentas();
            }
        });
        listaCompra.setItems(listaDetalles);
    }


    private void configClickListenerListView(){
        listaCompra.setOnMouseClicked((event -> {
            if(event.getClickCount() == 2){
                if(listaDetalles.size() > 0){
                    if(listaCompra.getSelectionModel().getSelectedItem() != null){
                        listaCodigos.remove(listaCodigos.indexOf(
                                listaCompra.getSelectionModel().getSelectedItem().getIdProducto()));
                        listaDetalles.remove(listaCompra.getSelectionModel().getSelectedItem());
                        listaCompra.refresh();
                    }
                }
            }
        }));
    }

    private void llenarDatosFinalesFactura(){
        lblNoProductos.setText(listaDetalles.size()+"");
        lblSubTotal.setText(calcularSubtotal()+"");
    }

    private void habilitarDesabilitarBotones(String fecha,String estatus){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date hoy = new Date();
        Date fechaVenta = null;
        try {
            fechaVenta = dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(estatus.equals("VENDIDO")) {
            if (dateFormat.format(fechaVenta).equals(dateFormat.format(hoy))) {
                btnModificar.setDisable(false);
                btnAgregar.setDisable(false);
                btnAnular.setDisable(true);
            } else {
                btnAgregar.setDisable(true);
                btnModificar.setDisable(true);
                btnAnular.setDisable(false);
            }
        }else{
            btnModificar.setDisable(true);
            btnAnular.setDisable(true);
            btnAgregar.setDisable(true);
        }
    }

    private void abrirAnulacion(int numeroOrden){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ventas/anulacion.fxml"));
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Anular Orden de Pedido");
        stage.setScene(new Scene(parent));
        PasarNoFactura controller = loader.getController();
        controller.pasarNoFactura(numeroOrden);
        stage.setOnHiding((event -> {
            addDataToTable();
        }));
        stage.show();
    }

    private int calcularNoProductos() {
        return listaDetalles.size();
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    private float calcularSubtotal(){
        float subtotal = 0;
        for(int i=0; i<listaDetalles.size(); i++){
            DetalleOPedido dop = listaDetalles.get(i);
            subtotal+= (dop.getPrecioVenta() * dop.getCantidad());
        }
        return subtotal;
    }

    private float calcularTotalDescuento(){
        float descuentoTotal = 0;
        for(DetalleOPedido dop : listaDetalles){
            descuentoTotal+= (dop.getCantidad()*dop.getPrecioVenta()*(dop.getDescuento()/100));
        }
        return descuentoTotal;
    }

    private float calcularTotal(){
        float total = 0f;
        for(int i = 0; i<listaDetalles.size(); i++){
            DetalleOPedido dop = listaDetalles.get(i);
            total += dop.getImporte();
        }
        return total;
    }

    private void modificar(){
        if(!venderDAO.AgregarModificarOrderPedido(crearOrden(),"actualizar")){
            venderDAO.eliminarDetallesXOrden(crearOrden().getIdOrdenPedido()+"");
            for(int i = 0; i < listaDetalles.size(); i++){
                DetalleOPedido dop = listaDetalles.get(i);
                dop.setNumDetalle(0);
                if(dop.getCantidad() > 0){
                    venderDAO.AgregarModificarDetalleOrderPedido(dop,crearOrden(),"new");
                    venderDAO.descuentoPorProducto(venderDAO.getUltimoIdDetalle(),listaDetalles.get(i).getDescuento());
                }
            }

            if(txtDescuentoTotal.getText().matches("\\d+.?\\d*")){
                venderDAO.descuentoTotalVenta(crearOrden().getIdOrdenPedido(),Float.parseFloat(txtDescuentoTotal.getText()));
            }

            addDataToTable();
            txtRecibido.setText("");
            txtCambio.setText("");
            txtDescuentoTotal.setText("0");
        }
        limpiar();
    }

    private pos.model.OrdenPedido crearOrden(){
        return new pos.model.OrdenPedido(
                Integer.parseInt(lblNoOrden.getText()),
                comboCliente.getSelectionModel().getSelectedItem().getCodigo(),
                Sesion.getmUsuarioActual().getCodigoEmpleado(),
                comboTipoPago.getSelectionModel().getSelectedItem().getIdPago());
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

    private void verReporte(){
        List<ProductoReporte> listaReporte = new ArrayList<>();
        for(int i = 0; i<listaDetalles.size(); i++){
            listaReporte.add(new ProductoReporte(String.valueOf(listaDetalles.get(i).getCantidad()),
                    listaDetalles.get(i).getNomPro(),
                    String.valueOf(listaDetalles.get(i).getPrecioVenta()),
                    "% " + String.valueOf(listaDetalles.get(i).getDescuento()),
                    String.valueOf(listaDetalles.get(i).getImporte())));
        }
        Reporte reporte = new Reporte();
        /*reporte.pdfReporte(listaReporte,
                comboCliente.getSelectionModel().getSelectedItem().getNombre() +
                        " " + comboCliente.getSelectionModel().getSelectedItem().getApellidos(),
                comboCliente.getSelectionModel().getSelectedItem().getNit(),
                comboCliente.getSelectionModel().getSelectedItem().getDireccion(),
                String.valueOf(comboCliente.getSelectionModel().getSelectedItem().getCelular()),
                String.valueOf(lblNoOrden.getText()),checkImprimir.isSelected(),checkGuardar.isSelected(),
                lblSubTotal.getText(),txtDescuento.getText(),lblTotal.getText());*/

        reporte.reporteModificacionVenta(listaReporte,
                comboCliente.getSelectionModel().getSelectedItem().getNombre() +
                        " " + comboCliente.getSelectionModel().getSelectedItem().getApellidos(),
                comboCliente.getSelectionModel().getSelectedItem().getNit(),
                comboCliente.getSelectionModel().getSelectedItem().getDireccion(),
                String.valueOf(comboCliente.getSelectionModel().getSelectedItem().getCelular()),
                String.valueOf(lblNoOrden.getText()),checkImprimir.isSelected(),checkGuardar.isSelected(),
                lblSubTotal.getText(),txtDescuento.getText(),lblTotal.getText(),lblNoOrden.getText());
    }

    private void configFiltrado(){
        FilteredList<OrdenPedido> filteredList = new FilteredList<OrdenPedido>(listaOrdenes, o -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(ordenPedido -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(ordenPedido.getNumOrdenPedido()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if(ordenPedido.getNomCliente().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(ordenPedido.getFecha().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(ordenPedido.getNomEmpleado().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<OrdenPedido> sortedList = new SortedList<OrdenPedido>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }
}
