package compras;

import compras.model.Compra;
import compras.model.DetalleCompra;
import compras.model.DetalleCompraReporte;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import productos.model.Producto;
import proveedores.model.Proveedor;
import proveedores.model.ProveedorCodNom;
import proveedores.model.ProveedoresDAO;
import reportes.Reporte;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by user on 30/06/2017.
 */
public class ComprasController implements Initializable {

    @FXML
    private GridPane rootPos;

    @FXML
    private TableView<Producto> tblDatos;

    @FXML
    private TableColumn<Producto, Integer> colNoProducto;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Float> colPrecioCompra;

    @FXML
    private TableColumn<Producto, Integer> colStock;

    @FXML
    private TableColumn<Producto, String> colFechaVencimiento;

    @FXML
    private Label longObservaciones;

    @FXML
    private TextArea txtObservaciones;

    @FXML
    private Label longCondiciones;

    @FXML
    private TextArea txtCondiciones;

    @FXML
    private Label lblNoProductos;

    @FXML
    private Label lblTotal;

    @FXML
    private Button btnComprar;

    @FXML
    private CheckBox checkImprimir;

    @FXML
    private CheckBox checkGuardar;

    @FXML
    private ComboBox<ProveedorCodNom> comboProveedor;

    @FXML
    private TextField txtEmpleado;

    @FXML
    private ListView<DetalleCompra> listaCompra;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblNoCompra;

    @FXML
    private TextField txtNoFactura;

    @FXML
    private TextField txtSerie;

    @FXML
    private DatePicker txtFechaEntrega;

    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    CompraDAO compraDAO = new CompraDAO();
    EmpleadoDAO empleadoDAO =  new EmpleadoDAO();
    Reporte reporte = new Reporte();

    public static ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
    public static ObservableList<DetalleCompra> listaDetalleCompra = FXCollections.observableArrayList();
    public static ObservableList<Long> listaCodigos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        llenarComboProveedores();
        convertComboProveedoresToString();
        matchDataWithTable();
        initDatosEncabezado();
        configListenerToComboProveedores();
        configClickListenerTableView();
        listaCompra.setItems(listaDetalleCompra);
        configCellFactoryListView();
        configClickListenerListView();
        convertDataPicker();
        initializeLabelsCounter();
        configListChangeListener();
        btnComprar.setOnAction((event -> {
            comprar();
        }));
        initDatosEncabezado();
        listaDetalleCompra.addListener(new ListChangeListener<DetalleCompra>() {
            @Override
            public void onChanged(Change<? extends DetalleCompra> c) {
                if(listaDetalleCompra.size() > 0){
                    comboProveedor.setDisable(true);
                }else{
                    comboProveedor.setDisable(false);
                }
            }
        });
        configFiltrado();
    }

    private void initDatosEncabezado(){
        lblNoCompra.setText(compraDAO.getUltimoIdCompra()+1+"");
        txtNoFactura.setText("");
        txtSerie.setText("");
        txtEmpleado.setText(Sesion.getmUsuarioActual().getNick());

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

        txtCondiciones.setText("");
        txtObservaciones.setText("");
        listaProductos.clear();
        listaDetalleCompra.clear();
    }

    private void llenarComboProveedores(){
        ObservableList<ProveedorCodNom> listaProveedores = FXCollections.observableArrayList(proveedoresDAO.getProveedoresCodNom());
        comboProveedor.setItems(listaProveedores);
    }

    private void convertComboProveedoresToString(){
        comboProveedor.setCellFactory(new Callback<ListView<ProveedorCodNom>, ListCell<ProveedorCodNom>>() {
            @Override
            public ListCell<ProveedorCodNom> call(ListView<ProveedorCodNom> param) {
                ListCell<ProveedorCodNom> cell = new ListCell<ProveedorCodNom>(){
                    @Override
                    protected void updateItem(ProveedorCodNom item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForProveedorCodNom(item));
                        }
                    }
                };
                return cell;
            }
        });

        comboProveedor.setButtonCell(new ListCell<ProveedorCodNom>() {
            @Override
            protected void updateItem(ProveedorCodNom item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForProveedorCodNom(item));
                }
            }
        });

        comboProveedor.setConverter(new StringConverter<ProveedorCodNom>() {
            private Map<String,ProveedorCodNom> map = new HashMap<String, ProveedorCodNom>();
            @Override
            public String toString(ProveedorCodNom object) {
                if(object != null){
                    String str = getStringForProveedorCodNom(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public ProveedorCodNom fromString(String string) {
                if(!map.containsKey(string)){
                    comboProveedor.setValue(null);
                    comboProveedor.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private String getStringForProveedorCodNom(ProveedorCodNom pcn){
        return pcn.getCodigo() + "\t" + pcn.getNombre();
    }

    private void configListenerToComboProveedores(){
        comboProveedor.valueProperty().addListener(new ChangeListener<ProveedorCodNom>() {
            @Override
            public void changed(ObservableValue<? extends ProveedorCodNom> observable, ProveedorCodNom oldValue, ProveedorCodNom newValue) {
                llenarTabla(String.valueOf(newValue.getCodigo()));
            }
        });
    }

    private void llenarTabla(String idProveedor){
        listaProductos.setAll(compraDAO.getProductosXProveedor(idProveedor));
    }

    private void matchDataWithTable(){
        colNoProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombre"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Float>("precioCompra"));
        colStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("stock"));
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory<Producto, String>("fechaVencimiento"));
        colNoProducto.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.35));
        colPrecioCompra.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));
        colStock.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colFechaVencimiento.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.25));
        tblDatos.setItems(listaProductos);
    }

    private void configCellFactoryListView(){
        listaCompra.setCellFactory(new Callback<ListView<DetalleCompra>, ListCell<DetalleCompra>>() {
            @Override
            public ListCell<DetalleCompra> call(ListView<DetalleCompra> param) {
                return new CeldaCompras();
            }
        });
    }

    private void configClickListenerTableView(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                    addItemToList(tblDatos.getSelectionModel().getSelectedItem());
                }
            }
        }));
    }

    private void addItemToList(Producto producto){
        if(listaCodigos.contains(producto.getCodigo())){
            for(int i = 0; i<listaDetalleCompra.size(); i++){
                DetalleCompra dc = listaDetalleCompra.get(i);
                if(dc.getIdProducto() == producto.getCodigo()){
                    dc.setCantidad(dc.getCantidad()+1);
                    dc.setSubTotal(dc.getCantidad()*dc.getPrecioCompra());
                    listaDetalleCompra.set(i,dc);
                    break;
                }
            }
        }else{
            listaCodigos.add(producto.getCodigo());
            DetalleCompra dc = new DetalleCompra(
                    0,
                    Integer.parseInt(lblNoCompra.getText()),
                    producto.getCodigo(),
                    producto.getNombre(),
                    producto.getPrecioCompra(),
                    1,
                    producto.getPrecioCompra(),
                    "");
            listaDetalleCompra.add(dc);
        }
    }

    private void configClickListenerListView(){
        listaCompra.setOnMouseClicked((event -> {
            if(event.getClickCount() == 2){
                if(listaDetalleCompra.size() > 0){
                    if(listaCompra.getSelectionModel().getSelectedItem() != null){
                        listaCodigos.remove(listaCodigos.indexOf(
                                listaCompra.getSelectionModel().getSelectedItem().getIdProducto()));
                        listaDetalleCompra.remove(listaCompra.getSelectionModel().getSelectedItem());
                        listaCompra.refresh();
                    }
                }
            }
        }));
    }

    private void initializeLabelsCounter(){
        List<TextContador> listaLabels = new ArrayList<>();
        listaLabels.add(new TextContador(txtCondiciones,100,longCondiciones));
        listaLabels.add(new TextContador(txtObservaciones,100,longObservaciones));
        Util.addListenerToLabels(listaLabels);
    }

    private void convertDataPicker(){
        String pattern = "yyyy-MM-dd";
        txtFechaEntrega.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void configListChangeListener(){
        listaDetalleCompra.addListener(new ListChangeListener<DetalleCompra>() {
            @Override
            public void onChanged(Change<? extends DetalleCompra> c) {
                calcularTotales();
            }
        });
    }

    private void calcularTotales(){
        lblNoProductos.setText(listaDetalleCompra.size()+"");
        float total = 0;
        for(DetalleCompra dc: listaDetalleCompra){
            total+= dc.getSubTotal();
        }
        lblTotal.setText(getDosDecimales(total)+"");
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    private void comprar(){
        List<DetalleCompraReporte> listaDetallesReporte = new ArrayList<>();
        int noProducto = 1;
        if(listaDetalleCompra.size() > 0){
            if(!compraDAO.agregarModificarEliminarCompra(crearCompra(),"new")){
                for(int i = 0; i < listaDetalleCompra.size(); i++){
                    DetalleCompra dc = listaDetalleCompra.get(i);
                    dc.setFechaVencimiento(getFechaHoy());
                    listaDetallesReporte.add(new DetalleCompraReporte(String.valueOf(noProducto),
                            dc.getNomProducto(),String.valueOf(dc.getPrecioCompra()),
                            String.valueOf(dc.getCantidad()),String.valueOf(dc.getSubTotal())));
                    noProducto++;
                    compraDAO.agregarModificarEliminarDetalleCompra(dc,"new");
                }
                Compra compra = crearCompra();
                ArrayList<Proveedor> listaProveedores = new ArrayList<Proveedor>(proveedoresDAO.getProveedores());
                Proveedor auxProveedor = null;
                for(Proveedor proveedor : listaProveedores){
                    if(proveedor.getCodigo() == compra.getIdProveedor()){
                        auxProveedor = proveedor;
                        break;
                    }
                }
                String telefonoProveedor = String.valueOf(auxProveedor.getTelefono());
                String direccionProveedor = String.valueOf(auxProveedor.getDireccion());
                generarReporte(listaDetallesReporte,String.valueOf(compra.getNumCompra()),
                        compra.getFechaEntrega(),compra.getNomProveedor(),telefonoProveedor,
                        direccionProveedor,lblTotal.getText(),checkGuardar.isSelected(),checkImprimir.isSelected(),
                        compra.getObservaciones(),compra.getCondiciones());
                initDatosEncabezado();
                Util.makeSuccess("Compra Realizada","Se relizó la compra con éxito");
            }
        }
    }

    private void generarReporte(List<DetalleCompraReporte> lista, String noCompra, String fechaEntrega,
                                String nomProveedor, String telProveedor, String direccionProveedor,
                                String total, boolean guardar, boolean imprimir, String observaciones,
                                String condiciones){
        reporte.reporteCompras(lista,noCompra,fechaEntrega,nomProveedor,telProveedor,direccionProveedor,
                total,guardar,imprimir,observaciones,condiciones);
    }

    private String getFechaHoy(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return df.format(date);
    }

    private Compra crearCompra(){
        String fechaEntrega = "";
        if(txtFechaEntrega.getValue() != null){
            fechaEntrega = txtFechaEntrega.getValue().toString();
        }
        return new Compra(Integer.parseInt(lblNoCompra.getText()),
                txtNoFactura.getText(),
                txtSerie.getText(),
                comboProveedor.getSelectionModel().getSelectedItem().getCodigo(),
                comboProveedor.getSelectionModel().getSelectedItem().getNombre(),
                "",
                fechaEntrega,
                Sesion.getmUsuarioActual().getCodigoEmpleado(),
                txtEmpleado.getText(),
                txtObservaciones.getText(),
                txtCondiciones.getText(),
                0,
                "COMPRADO");


    }

    private void configFiltrado(){
        FilteredList<Producto> filteredList = new FilteredList<Producto>(listaProductos, p -> true);
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
                }
                return false; // Does not match.
            });
        });
        SortedList<Producto> sortedList = new SortedList<Producto>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

}
