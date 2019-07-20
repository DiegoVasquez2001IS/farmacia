package compras;

import compras.model.Compra;
import compras.model.DetalleCompra;
import compras.model.DetalleCompraReporte;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import proveedores.model.Proveedor;
import proveedores.model.ProveedorCodNom;
import proveedores.model.ProveedoresDAO;
import reportes.Reporte;
import sesion.Sesion;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by user on 13/07/2017.
 */
public class ModificarCompraController implements Initializable{

    @FXML
    private GridPane rootPos;

    @FXML
    private TableView<Compra> tblDatos;

    @FXML
    private TableColumn<Compra, Integer> colNoCompra;

    @FXML
    private TableColumn<Compra, String> colFechaCompra;

    @FXML
    private TableColumn<Compra, String> colFechaEntrega;

    @FXML
    private TableColumn<Compra, Float> colTotal;

    @FXML
    private TableColumn<Compra, String> colEstatus;

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

    @FXML
    private Button btnModificar;

    public static ObservableList<Compra> listaCompras = FXCollections.observableArrayList();
    public static ObservableList<DetalleCompra> listaDetalleCompra = FXCollections.observableArrayList();
    public static ObservableList<Long> listaCodigos = FXCollections.observableArrayList();

    CompraDAO compraDAO = new CompraDAO();
    Reporte reporte = new Reporte();
    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    EmpleadoDAO empleadoDAO =  new EmpleadoDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDataToList();
        matchDataWithTable();
        configClickTable();
        convertComboProveedoresToString();
        convertDataPicker();
        configCellFactoryListView();
        listaDetalleCompra.addListener(new ListChangeListener<DetalleCompra>() {
            @Override
            public void onChanged(Change<? extends DetalleCompra> c) {
                calcularTotales();
            }
        });

        btnModificar.setOnAction((event -> {
            if(tblDatos.getSelectionModel().getSelectedItem() != null){
                abrirVentanaProductos();
            }
        }));
        configClickListenerListView();
        btnComprar.setOnAction((event -> {
            comprar();
        }));

    }

    private void addDataToList(){
        listaCompras.setAll(compraDAO.getCompras());
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

    private void matchDataWithTable(){
        colNoCompra.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("numCompra"));
        colFechaCompra.setCellValueFactory(new PropertyValueFactory<Compra, String>("fechaCompra"));
        colFechaEntrega.setCellValueFactory(new PropertyValueFactory<Compra, String>("fechaEntrega"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Float>("total"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<Compra, String>("estatus"));
        colNoCompra.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colFechaCompra.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.25));
        colFechaEntrega.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.22));
        colTotal.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.15));
        colEstatus.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));
        tblDatos.setItems(listaCompras);
    }

    private void comprar(){
        List<DetalleCompraReporte> listaDetallesReporte = new ArrayList<>();
        int noProducto = 1;
        if(listaDetalleCompra.size() > 0){
            if(!compraDAO.agregarModificarEliminarCompra(crearCompra(),"actualizar")){
                compraDAO.eliminarDetallesXCompra(lblNoCompra.getText());
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
        listaDetalleCompra.clear();
    }

    private void generarReporte(List<DetalleCompraReporte> lista, String noCompra, String fechaEntrega,
                                String nomProveedor, String telProveedor, String direccionProveedor,
                                String total, boolean guardar, boolean imprimir, String observaciones,
                                String condiciones){
        //reporte.reporteCompras(lista,noCompra,fechaEntrega,nomProveedor,telProveedor,direccionProveedor,
                //total,guardar,imprimir,observaciones,condiciones);
        reporte.reporteModificacionCompras(lista,noCompra,fechaEntrega,nomProveedor,
                telProveedor,direccionProveedor,total,guardar,imprimir,observaciones,condiciones);
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

    private void fillListView(String idCompra){
        listaDetalleCompra.setAll(compraDAO.getDetalleXIdCompr(idCompra));
        listaCodigos.clear();
        for(DetalleCompra dc : listaDetalleCompra){
            listaCodigos.add(dc.getIdProducto());
        }
    }

    private void configCellFactoryListView(){
        listaCompra.setCellFactory(new Callback<ListView<DetalleCompra>, ListCell<DetalleCompra>>() {
            @Override
            public ListCell<DetalleCompra> call(ListView<DetalleCompra> param) {
                return new CeldaModificarCompra();
            }
        });
        listaCompra.setItems(listaDetalleCompra);
    }

    private void configClickTable(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                    if(!tblDatos.getSelectionModel().getSelectedItem().getEstatus().equals("INGRESADO")){
                        btnComprar.setDisable(false);
                        fillListView(tblDatos.getSelectionModel().getSelectedItem().getNumCompra()+"");
                        llenarDatosEncabedado(tblDatos.getSelectionModel().getSelectedItem());
                        llenarTotales();
                    }else {
                        btnComprar.setDisable(true);
                        listaDetalleCompra.clear();
                        limpiar();
                    }
                }
            }
        }));
    }

    private void limpiar(){
        lblNoCompra.setText("");
        txtNoFactura.setText("");
        txtFechaEntrega.setValue(null);
        txtSerie.setText("");
        comboProveedor.setItems(null);
        txtCondiciones.setText("");
        txtObservaciones.setText("");
        txtEmpleado.setText("");
    }

    private void abrirVentanaProductos(){
        tblDatos.setDisable(true);
        AgregarProductoCompraController mController;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("compras/agregar_producto_compras.fxml"));
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
        mController.pasarId(comboProveedor.getSelectionModel().getSelectedItem().getCodigo()+"");
        mController.pasarNoCompra(Integer.parseInt(lblNoCompra.getText()));
        stage.setOnHiding((event -> {
            AgregarProductoCompraController.listaProductos.clear();
            tblDatos.setDisable(false);
        }));
        stage.show();
    }

    private void llenarDatosEncabedado(Compra c){
        lblNoCompra.setText(c.getNumCompra()+"");
        txtNoFactura.setText(c.getNumFactura());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(c.getFechaEntrega(),dateTimeFormatter);
        txtFechaEntrega.setValue(localDate);
        txtSerie.setText(c.getSerie());
        ObservableList<ProveedorCodNom> prov = FXCollections.observableArrayList();
        prov.setAll(new ProveedorCodNom(c.getIdProveedor(),c.getNomProveedor()));
        comboProveedor.setItems(prov);
        comboProveedor.getSelectionModel().select(0);
        txtEmpleado.setText(c.getNomEmpleado());
        txtCondiciones.setText(c.getCondiciones());
        txtObservaciones.setText(c.getObservaciones());
    }

    private void calcularTotales(){
        lblNoProductos.setText(listaDetalleCompra.size()+"");
        float total = 0;
        for(DetalleCompra dc: listaDetalleCompra){
            total+= dc.getSubTotal();
        }
        lblTotal.setText(getDosDecimales(total)+"");
    }


    private void llenarTotales(){
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

    public void convertDataPicker(){
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
}
