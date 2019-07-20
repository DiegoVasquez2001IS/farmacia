package compras;

import compras.model.Compra;
import compras.model.DetalleCompra;
import javafx.collections.FXCollections;
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
import util.Util;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by user on 3/07/2017.
 */
public class IngresoController implements Initializable{

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

    public static ObservableList<Compra> listaCompras = FXCollections.observableArrayList();
    public static ObservableList<DetalleCompra> listaDetalleCompra = FXCollections.observableArrayList();

    CompraDAO compraDAO = new CompraDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDataToList();
        matchDataWithTable();
        listaCompra.setItems(listaDetalleCompra);
        configCellFactoryListView();
        configTableViewClick();
        convertComboProveedoresToString();
        convertDataPicker();
        btnComprar.setOnAction((event -> {
            ingresar();
            listaDetalleCompra.clear();
            addDataToList();
        }));
        configFiltrado();
    }

    private void addDataToList(){
        listaCompras.setAll(compraDAO.getCompras());
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

    private void configCellFactoryListView(){
        listaCompra.setCellFactory(new Callback<ListView<DetalleCompra>, ListCell<DetalleCompra>>() {
            @Override
            public ListCell<DetalleCompra> call(ListView<DetalleCompra> param) {
                return new CeldaIngreso();
            }
        });
    }



    private void fillListView(String idCompra){
        listaDetalleCompra.setAll(compraDAO.getDetalleXIdCompr(idCompra));
    }


    private void configTableViewClick(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                    if(!tblDatos.getSelectionModel().getSelectedItem().getEstatus().equals("INGRESADO")){
                        btnComprar.setDisable(false);
                        llenarDatosEncabedado(tblDatos.getSelectionModel().getSelectedItem());
                        fillListView(tblDatos.getSelectionModel().getSelectedItem().getNumCompra()+"");
                        llenarTotales();
                    }else {
                        btnComprar.setDisable(true);
                        fillListView(tblDatos.getSelectionModel().getSelectedItem().getNumCompra()+"");
                        llenarDatosEncabedado(tblDatos.getSelectionModel().getSelectedItem());
                        llenarTotales();
                    }
                }
            }
        }));
    }

    private void llenarDatosEncabedado(Compra c){
        System.out.println("COMPRA NO. FACTURA: " +  c.getNumFactura());
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

    private void ingresar(){
        if(tblDatos.getSelectionModel().getSelectedItem() != null){
            if(tblDatos.getSelectionModel().getSelectedItem().getEstatus().equals("PEDIDO")){
                compraDAO.agregarModificarEliminarCompra(getCompra(),"actualizar");
                for(DetalleCompra dc : listaDetalleCompra){
                    compraDAO.agregarModificarEliminarDetalleCompra(dc,"actualizar");
                }
                compraDAO.ingresarProducto(getCompra().getNumCompra());
                Util.makeSuccess("Compra Ingresada","La compra " + getCompra().getNumCompra() +
                        " ha sido ingresada exitósamente");
            }
        }
    }

    private Compra getCompra(){
        return new Compra(
                Integer.parseInt(lblNoCompra.getText()),
                txtNoFactura.getText(),
                txtSerie.getText(),
                comboProveedor.getSelectionModel().getSelectedItem().getCodigo(),
                comboProveedor.getSelectionModel().getSelectedItem().getNombre(),
                tblDatos.getSelectionModel().getSelectedItem().getFechaCompra(),
                txtFechaEntrega.getValue().toString(),
                tblDatos.getSelectionModel().getSelectedItem().getIdEmpleado(),
                tblDatos.getSelectionModel().getSelectedItem().getNomEmpleado(),
                txtObservaciones.getText(),
                txtCondiciones.getText(),
                Float.parseFloat(lblTotal.getText()),
                tblDatos.getSelectionModel().getSelectedItem().getEstatus());
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
                } else if (compra.getFechaCompra().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (compra.getNomProveedor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (compra.getNomEmpleado().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (compra.getFechaEntrega().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(compra.getEstatus().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<Compra> sortedList = new SortedList<Compra>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }


}
