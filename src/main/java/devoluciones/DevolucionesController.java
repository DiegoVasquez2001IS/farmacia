package devoluciones;

import compras.CompraDAO;
import compras.model.Compra;
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
import proveedores.model.ProveedorCodNom;
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
 * Created by user on 3/07/2017.
 */
public class DevolucionesController implements Initializable {

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
    private Label longMotivo;

    @FXML
    private TextArea txtMotivo;

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
    private ListView<DetalleDevolucion> listaCompra;

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

    public static ObservableList<Compra> listaCompras = FXCollections.observableArrayList();
    public static ObservableList<DetalleDevolucion> listaDetalleDevolucion = FXCollections.observableArrayList();

    CompraDAO compraDAO = new CompraDAO();
    DevolucionesDAO devolucionesDAO = new DevolucionesDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToTable();
        matchDataWithTable();
        configTableViewClick();
        convertComboProveedoresToString();
        convertDataPicker();
        configCellFactoryListView();
        configFiltrado();
        configListChangeListener();
        initializeLabelsCounter();
        btnComprar.setOnAction((event -> {
            devolver();
        }));
    }

    private void setDataToTable(){
        listaCompras.setAll(compraDAO.getComprasIngresadas());
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

    private void configTableViewClick(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                    llenarDatosEncabedado(tblDatos.getSelectionModel().getSelectedItem());
                    txtMotivo.setText(devolucionesDAO.getMotivoXIdCompra(
                            tblDatos.getSelectionModel().getSelectedItem().getNumCompra()+""));
                    fillListView(tblDatos.getSelectionModel().getSelectedItem().getNumCompra()+"");
                    calcularTotales();
                }
            }
        }));
    }

    private void fillListView(String idCompra){
        ObservableList<DetalleDevolucion> listaDetalleCompras = FXCollections.observableArrayList();
        listaDetalleCompras.setAll(devolucionesDAO.getDetalleDevolucionXIdCompra(idCompra));
        listaDetalleDevolucion.clear();
        for(int i = 0; i< listaDetalleCompras.size(); i++){
            DetalleDevolucion detalleCompra = listaDetalleCompras.get(i);
            DetalleDevolucion aux = null;
            if(devolucionesDAO.getDetalleDevueltosXIdCompra(idCompra,detalleCompra.getIdProducto()+"") != null){
                aux = devolucionesDAO.getDetalleDevueltosXIdCompra(idCompra,detalleCompra.getIdProducto()+"");
            }
            if(aux != null){
                detalleCompra.setIdDetalle(aux.getIdDetalle());
                detalleCompra.setStockDevuelto(aux.getStockDevuelto());
                detalleCompra.setTotalDevuelto(aux.getTotalDevuelto());
            }
            listaDetalleDevolucion.add(detalleCompra);
        }
        //listaDetalleDevolucion.setAll(devolucionesDAO.getDetalleDevolucionXIdCompra(idCompra));
    }

    private void configCellFactoryListView(){
        listaCompra.setCellFactory(new Callback<ListView<DetalleDevolucion>, ListCell<DetalleDevolucion>>() {
            @Override
            public ListCell<DetalleDevolucion> call(ListView<DetalleDevolucion> param) {
                return new CeldaDevoluciones();
            }
        });
        listaCompra.setItems(listaDetalleDevolucion);
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

    private void configFiltrado(){
        FilteredList<Compra> filteredList = new FilteredList<Compra>(listaCompras, c -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(compra -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(compra.getNumCompra()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (compra.getNomEmpleado().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (compra.getNomProveedor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(compra.getFechaCompra().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (compra.getNumFactura()!=null && compra.getNumFactura().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (compra.getSerie()!= null && compra.getSerie().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<Compra> sortedList = new SortedList<Compra>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void calcularTotales(){
        float total = 0;
        for(DetalleDevolucion dd: DevolucionesController.listaDetalleDevolucion){
            total += dd.getTotalDevuelto();
        }
        lblTotal.setText(total+"");
        lblNoProductos.setText(DevolucionesController.listaDetalleDevolucion.size()+"");
    }

    private void configListChangeListener(){
        listaDetalleDevolucion.addListener(new ListChangeListener<DetalleDevolucion>() {
            @Override
            public void onChanged(Change<? extends DetalleDevolucion> c) {
                calcularTotales();
            }
        });
    }

    private void initializeLabelsCounter(){
        List<TextContador> labelsList = new ArrayList<>();
        labelsList.add(new TextContador(txtMotivo,50,longMotivo));
        Util.addListenerToLabels(labelsList);
    }

    private void limpiar(){
        lblNoProductos.setText("");
        lblTotal.setText("");
        lblNoCompra.setText("");
        comboProveedor.setItems(null);
        txtNoFactura.setText("");
        txtSerie.setText("");
        listaDetalleDevolucion.clear();
        txtMotivo.clear();
        txtObservaciones.clear();
        txtCondiciones.clear();

    }

    private void devolver(){
        if(!txtMotivo.getText().equals("")){
            Devolucion devolucion = getDevulocion();
            if(Float.parseFloat(lblTotal.getText()) > 0){
                if(!devolucionesDAO.agregarModificarEliminarDevolucion(devolucion,"new")){
                    for(DetalleDevolucion dd : listaDetalleDevolucion){
                        System.out.println("codigo devolucion: " + dd.getIdDetalle());
                        if(dd.getTotalDevuelto() > 0){
                            if(dd.getIdDetalle() == 0){
                                devolucionesDAO.agregarModificarEliminarDetalleDevolucion(dd,"new");
                            }/*else{
                                System.out.println(dd.getIdDetalle()+"----ID DETALLE");
                                System.out.println(dd.getStockDevuelto()+"----STOCK DEVUELTO");
                                devolucionesDAO.agregarModificarEliminarDetalleDevolucion(dd,"actualizar");
                            }*/
                        }
                    }
                }
            }else{
                Util.makeAdvertencia("No ha devuelto nada","Debe devolver por lo menos un producto");
            }
        }else{
            Util.makeAdvertencia("Debe llenar el motivo","La devolución debe tener un motivo");
        }
        limpiar();
    }

    private Devolucion getDevulocion(){
        int idCompra = tblDatos.getSelectionModel().getSelectedItem().getNumCompra();
        return new Devolucion(0,idCompra, Sesion.getmUsuarioActual().getNick(),
                tblDatos.getSelectionModel().getSelectedItem().getNomProveedor(),txtMotivo.getText(),
                getFechaHoy(),getDosDecimales(Float.parseFloat(lblTotal.getText())));
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    private String getFechaHoy(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return df.format(date);
    }

}
