package productos;

import colocaciones.model.Colocacion;
import colocaciones.model.ColocacionesDAO;
import dolencias.model.Dolencia;
import dolencias.model.DolenciasDAO;
import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import permisos.PermisosDAO;
import presentaciones.model.Presentacion;
import presentaciones.model.PresentacionesDAO;
import productos.model.Producto;
import productos.model.ProductoMostrar;
import productos.model.ProductosDAO;
import proveedores.model.Proveedor;
import proveedores.model.ProveedoresDAO;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by user on 22/03/2017.
 */
public class DetalleController implements Initializable, PasarParametros {

    @FXML
    private GridPane rootProductos;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label longCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label longNombre;

    @FXML
    private TextField txtPrecioCompra;

    @FXML
    private Label longPrecioCompra;

    @FXML
    private TextField txtPrecioVenta;

    @FXML
    private Label longPrecioVenta;

    @FXML
    private DatePicker txtVencimiento;

    @FXML
    private TextField txtExistencia;

    @FXML
    private Label longExistencia;

    @FXML
    private ComboBox<Proveedor> cbProveedor;

    @FXML
    private ComboBox<Presentacion> cbPresentacion;

    @FXML
    private ComboBox<Dolencia> cbDolencia;

    @FXML
    private ComboBox<Colocacion> cbColocacion;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;


    ProductosDAO productosDAO = new ProductosDAO();

    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    PresentacionesDAO presentacionesDAO = new PresentacionesDAO();
    DolenciasDAO dolenciasDAO = new DolenciasDAO();
    ColocacionesDAO colocacionesDAO = new ColocacionesDAO();

    ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();
    ObservableList<Presentacion> listaPresentaciones = FXCollections.observableArrayList();
    ObservableList<Dolencia> listaDolencias = FXCollections.observableArrayList();
    ObservableList<Colocacion> listaColocaciones = FXCollections.observableArrayList();

    private String mAccion = "new";

    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configCellFactoriesForProveedor();
        configCellFactoriesForPresentacion();
        configCellFacotiresForDolencia();
        configCellFactoriesForColocacion();
        initilizeLabelsListener();
        initListas();
        configPermisos();
        //configFiltrado();
        convertDataPicker();

        btnGuardar.setOnAction((event -> {
            agregarModificar(mAccion);
        }));
        btnCancelar.setOnAction(event -> {
            close();
        });
    }

    private void configPermisos(){
        btnGuardar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRODUCTOS+""));
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if(!productosDAO.agregarModificarEliminarProducto(getProducto(),accion)){
                Util.makeSuccess("Operación en Productos existosa",
                        "Se ha modificado el contenido de la tabla Productos");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private void configCellFactoriesForProveedor(){
        cbProveedor.setCellFactory(new Callback<ListView<Proveedor>, ListCell<Proveedor>>() {
            @Override
            public ListCell<Proveedor> call(ListView<Proveedor> param) {
                ListCell<Proveedor> cell = new ListCell<Proveedor>(){
                    @Override
                    protected void updateItem(Proveedor item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForProveedor(item));
                        }
                    }
                };
                return cell;
            }
        });
        cbProveedor.setButtonCell(new ListCell<Proveedor>() {
            @Override
            protected void updateItem(Proveedor item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForProveedor(item));
                }
            }
        });

        cbProveedor.setConverter(new StringConverter<Proveedor>() {
            private Map<String,Proveedor> map = new HashMap<String, Proveedor>();
            @Override
            public String toString(Proveedor object) {
                if(object != null){
                    String str = getStringForProveedor(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public Proveedor fromString(String string) {
                if(!map.containsKey(string)){
                    cbProveedor.setValue(null);
                    cbProveedor.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private void configCellFactoriesForPresentacion(){
        cbPresentacion.setCellFactory(new Callback<ListView<Presentacion>, ListCell<Presentacion>>() {
            @Override
            public ListCell<Presentacion> call(ListView<Presentacion> param) {
                ListCell<Presentacion> cell = new ListCell<Presentacion>(){
                    @Override
                    protected void updateItem(Presentacion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForPresentacion(item));
                        }
                    }
                };
                return cell;
            }
        });
        cbPresentacion.setButtonCell(new ListCell<Presentacion>() {
            @Override
            protected void updateItem(Presentacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForPresentacion(item));
                }
            }
        });

        cbPresentacion.setConverter(new StringConverter<Presentacion>() {
            private Map<String,Presentacion> map = new HashMap<String, Presentacion>();
            @Override
            public String toString(Presentacion object) {
                if(object != null){
                    String str = getStringForPresentacion(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public Presentacion fromString(String string) {
                if(!map.containsKey(string)){
                    cbPresentacion.setValue(null);
                    cbPresentacion.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private void configCellFacotiresForDolencia(){
        cbDolencia.setCellFactory(new Callback<ListView<Dolencia>, ListCell<Dolencia>>() {
            @Override
            public ListCell<Dolencia> call(ListView<Dolencia> param) {
                ListCell<Dolencia> cell = new ListCell<Dolencia>(){
                    @Override
                    protected void updateItem(Dolencia item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForDolencia(item));
                        }
                    }
                };
                return cell;
            }
        });
        cbDolencia.setButtonCell(new ListCell<Dolencia>() {
            @Override
            protected void updateItem(Dolencia item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForDolencia(item));
                }
            }
        });

        cbDolencia.setConverter(new StringConverter<Dolencia>() {
            private Map<String,Dolencia> map = new HashMap<String, Dolencia>();
            @Override
            public String toString(Dolencia object) {
                if(object != null){
                    String str = getStringForDolencia(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public Dolencia fromString(String string) {
                if(!map.containsKey(string)){
                    cbDolencia.setValue(null);
                    cbDolencia.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private void configCellFactoriesForColocacion() {
        cbColocacion.setCellFactory(new Callback<ListView<Colocacion>, ListCell<Colocacion>>() {
            @Override
            public ListCell<Colocacion> call(ListView<Colocacion> param) {
                ListCell<Colocacion> cell = new ListCell<Colocacion>(){
                    @Override
                    protected void updateItem(Colocacion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForColocacion(item));
                        }
                    }
                };
                return cell;
            }
        });
        cbColocacion.setButtonCell(new ListCell<Colocacion>() {
            @Override
            protected void updateItem(Colocacion item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForColocacion(item));
                }
            }
        });

        cbColocacion.setConverter(new StringConverter<Colocacion>() {
            private Map<String,Colocacion> map = new HashMap<String, Colocacion>();
            @Override
            public String toString(Colocacion object) {
                if(object != null){
                    String str = getStringForColocacion(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public Colocacion fromString(String string) {
                if(!map.containsKey(string)){
                    cbColocacion.setValue(null);
                    cbColocacion.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private String getStringForProveedor(Proveedor proveedor){
        return proveedor.getNombre();
    }

    private String getStringForPresentacion(Presentacion presentacion){
        return presentacion.getNombre();
    }

    private String getStringForDolencia(Dolencia dolencia){
        return dolencia.getNombre();
    }

    private String getStringForColocacion(Colocacion colocacion){
        return colocacion.getNombre();
    }

    public void convertDataPicker(){
        String pattern = "yyyy-MM-dd";
        txtVencimiento.setConverter(new StringConverter<LocalDate>() {
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

    private boolean camposLlenos(){
        return txtNombre.getText().length() > 0 &&
                txtPrecioCompra.getText().length() > 0 &&
                txtPrecioVenta.getText().length() > 0 &&
                txtExistencia.getText().length() > 0 &&
                txtVencimiento.getValue().toString().length() > 0;
    }

    private Producto getProducto(){
        final long codigo = txtCodigo.getText().trim().length() > 0 ?
                Long.parseLong(txtCodigo.getText().trim()) : 0;
        final float preCompra = txtPrecioCompra.getText().trim().length() > 0 ?
                Float.parseFloat(txtPrecioCompra.getText().trim()) : 0f;
        final float preVenta = txtPrecioVenta.getText().trim().length() > 0 ?
                Float.parseFloat(txtPrecioVenta.getText().trim()) : 0f;
        final String fechaVencimiento = txtVencimiento.getValue().toString();
        final int stock = txtExistencia.getText().trim().length() > 0 ?
                Integer.parseInt(txtExistencia.getText().trim()) : 0;
        final int idProveedor = cbProveedor.getSelectionModel().isEmpty() ? 0 :
               cbProveedor.getSelectionModel().getSelectedItem().getCodigo();
        final int idPresentacion = cbPresentacion.getSelectionModel().isEmpty() ? 0:
                cbPresentacion.getSelectionModel().getSelectedItem().getCodigo();
        final int idDolencia = cbDolencia.getSelectionModel().isEmpty() ? 0 :
                cbDolencia.getSelectionModel().getSelectedItem().getCodigo();
        final int idColocacion = cbColocacion.getSelectionModel().isEmpty() ? 0:
                cbColocacion.getSelectionModel().getSelectedItem().getCodigo();
        return new Producto(codigo,txtNombre.getText(),preCompra,preVenta,fechaVencimiento,
                stock,idProveedor,idPresentacion,idDolencia,idColocacion);
    }

    private void initListas(){
        listaProveedores.addAll(proveedoresDAO.getProveedores());
        cbProveedor.setItems(listaProveedores);
        listaPresentaciones.addAll(presentacionesDAO.getPresentaciones());
        cbPresentacion.setItems(listaPresentaciones);
        listaDolencias.addAll(dolenciasDAO.getDolencias());
        cbDolencia.setItems(listaDolencias);
        listaColocaciones.addAll(colocacionesDAO.getColocaciones());
        cbColocacion.setItems(listaColocaciones);
    }

    private void configFiltrado(){

    }

    private void initilizeLabelsListener(){
        List<TextContador> listLabels = new ArrayList<>();
        listLabels.add(new TextContador(txtCodigo,11,longCodigo));
        listLabels.add(new TextContador(txtNombre,70,longNombre));
        listLabels.add(new TextContador(txtPrecioCompra,11,longPrecioCompra));
        listLabels.add(new TextContador(txtPrecioVenta,11,longPrecioVenta));
        listLabels.add(new TextContador(txtExistencia,11,longExistencia));
        Util.addListenerToLabels(listLabels);
    }

    private void close(){
        Stage thisStage =  (Stage) rootProductos.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        ProductoMostrar producto = (ProductoMostrar) parametro;
        txtCodigo.setText(String.valueOf(producto.getIdProducto()));
        txtNombre.setText(producto.getNomProducto());
        txtPrecioCompra.setText(String.valueOf(producto.getPrecioCompra()));
        txtPrecioVenta.setText(String.valueOf(producto.getPrecioVenta()));
        txtExistencia.setText(String.valueOf(producto.getStock()));
        txtVencimiento.setValue(LocalDate.parse(producto.getVencimiento()));

        ObservableList<Producto> listaProductos = FXCollections.observableArrayList();
        listaProductos.addAll(productosDAO.getProductos());
        Producto pr = null;
        for(Producto producto1 : listaProductos){
            if(producto1.getCodigo() == producto.getIdProducto()){
                pr = producto1;
                break;
            }
        }

        Proveedor pro = null;
        for(Proveedor proveedor : listaProveedores){
            if(proveedor.getCodigo() == pr.getIdProveedor()){
                pro = proveedor;
                break;
            }
        }
        cbProveedor.getSelectionModel().select(pro);
        Presentacion pre = null;
        for (Presentacion presentacion : listaPresentaciones){
            if(presentacion.getCodigo() == pr.getIdPresentacion()){
                pre = presentacion;
                break;
            }
        }
        cbPresentacion.getSelectionModel().select(pre);
        Dolencia dol = null;
        for (Dolencia dolencia : listaDolencias){
            if(dolencia.getCodigo() == pr.getIdDolencia()){
                dol = dolencia;
                break;
            }
        }
        cbDolencia.getSelectionModel().select(dol);
        Colocacion col = null;
        for (Colocacion colocacion : listaColocaciones){
            if(colocacion.getCodigo() == pr.getIdColocacion()){
                col = colocacion;
                break;
            }
        }
        cbColocacion.getSelectionModel().select(col);
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
