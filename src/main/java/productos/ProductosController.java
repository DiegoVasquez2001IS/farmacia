package productos;

import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import permisos.PermisosDAO;
import productos.model.Producto;
import productos.model.ProductoMostrar;
import productos.model.ProductosDAO;
import sesion.Sesion;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 20/02/2017.
 */
public class ProductosController implements Initializable {

    @FXML
    private Button btnAniadir;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblRegistros;

    @FXML
    private TableView<ProductoMostrar> tblDatos;

    @FXML
    private TableColumn<ProductoMostrar, Integer> colCodigo;

    @FXML
    private TableColumn<ProductoMostrar, String> colNombre;

    @FXML
    private TableColumn<ProductoMostrar, Float> colPrecioCompra;

    @FXML
    private TableColumn<ProductoMostrar, Float> colPrecioVenta;

    @FXML
    private TableColumn<ProductoMostrar, Integer> colStock;

    @FXML
    private TableColumn<ProductoMostrar, String> colVencimiento;

    @FXML
    private TableColumn<ProductoMostrar, String> colProveedor;

    @FXML
    private TableColumn<ProductoMostrar, String> colPresentacion;

    @FXML
    private TableColumn<ProductoMostrar, String> colDolencia;

    @FXML
    private TableColumn<ProductoMostrar, String> colColocacion;


    ProductosDAO productosDAO = new ProductosDAO();

    private ObservableList<ProductoMostrar> listaProductos = FXCollections.observableArrayList();
    private ObservableList<Producto> listaProducto = FXCollections.observableArrayList();
    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblRegistros.setText("Productos");
        matchDataWithTable();
        configFiltrado();
        configCellFactoriesForColumns();
        configPermisos();
        btnAniadir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirDetalles();
            }
        });
        btnModificar.setOnAction(event -> {
           abrirDetallesParaModificacion();
        });
        btnEliminar.setOnAction(event -> {
            eliminarProducto();
        });
        configColoresTable();
    }

    private void configColoresTable(){

        tblDatos.setRowFactory(tv -> new TableRow<ProductoMostrar>() {
            @Override
            public void updateItem(ProductoMostrar item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getStock() <=5) {
                    setStyle("-fx-background-color: tomato;");
                } else if(item.getStock() < 11 && item.getStock() > 5) {
                    setStyle("-fx-background-color: #FFEE58");         //
                }else
                {
                    setStyle("");
                }
            }
        });
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRODUCTOS+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRODUCTOS+""));
    }

    private void configFiltrado(){
        FilteredList<ProductoMostrar> filteredList = new FilteredList<ProductoMostrar>(listaProductos, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(producto -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (producto.getNomProducto().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(producto.getIdProducto()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (producto.getProveedor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getPresentacion().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getDolencia().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getColocacion().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        SortedList<ProductoMostrar> sortedList = new SortedList<ProductoMostrar>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    /**
     * new Producto(0,0,0,0,) en la acción eliminar solo necesita el id del producto
     */
    private void eliminarProducto(){
        final ProductoMostrar producto = tblDatos.getSelectionModel().getSelectedItem();
        listaProducto.setAll(productosDAO.getProductos());
        Producto pro = null;
        for (Producto aux : listaProducto){
            if(producto.getIdProducto() == aux.getCodigo()){
                pro = aux;
                break;
            }
        }
        if(producto != null) {
            if (!productosDAO.agregarModificarEliminarProducto(pro, "eliminar")) {
                Util.makeSuccess("Operación en Productos existosa",
                        "Se ha modificado el contenido de la tabla Productos");
                addDataToTable();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final ProductoMostrar producto = tblDatos.getSelectionModel().getSelectedItem();
        if(producto != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("productos/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Producto");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(producto);
                controller.aisgnarAccion("actualizar");
                stage.setOnHiding((event -> {
                    addDataToTable();
                }));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.ALL.SEVERE, null, ex);
            }
        }
    }

    private void addDataToTable(){
        listaProductos.setAll(productosDAO.getProductosMostrar());
    }

    private void configCellFactoriesForColumns(){
        colNombre.setCellFactory(new Callback<TableColumn<ProductoMostrar, String>, TableCell<ProductoMostrar, String>>() {
            @Override
            public TableCell<ProductoMostrar, String> call(TableColumn<ProductoMostrar, String> param) {
                TableCell<ProductoMostrar,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colNombre.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });

        colProveedor.setCellFactory(new Callback<TableColumn<ProductoMostrar, String>, TableCell<ProductoMostrar, String>>() {
            @Override
            public TableCell<ProductoMostrar, String> call(TableColumn<ProductoMostrar, String> param) {
                TableCell<ProductoMostrar,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colProveedor.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });

        colDolencia.setCellFactory(new Callback<TableColumn<ProductoMostrar, String>, TableCell<ProductoMostrar, String>>() {
            @Override
            public TableCell<ProductoMostrar, String> call(TableColumn<ProductoMostrar, String> param) {
                TableCell<ProductoMostrar,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colDolencia.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });

        colCodigo.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.05));
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));
        colPrecioCompra.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.05));
        colPrecioVenta.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.05));
        colVencimiento.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colStock.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.05));
        colProveedor.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.125));
        colPresentacion.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.125));
        colDolencia.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.125));
        colColocacion.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.125));
    }

    private void matchDataWithTable(){
        addDataToTable();
        colCodigo.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, Integer>("idProducto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, String>("nomProducto"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, Float>("precioCompra"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, Float>("precioVenta"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, String>("vencimiento"));
        colStock.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, Integer>("stock"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<ProductoMostrar,String>("proveedor"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, String>("presentacion"));
        colDolencia.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, String>("dolencia"));
        colColocacion.setCellValueFactory(new PropertyValueFactory<ProductoMostrar, String>("colocacion"));
        tblDatos.setItems(listaProductos);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre,colPrecioCompra,colPrecioVenta,
                colVencimiento,colStock,colProveedor,colPresentacion,colDolencia,colColocacion);
    }

    private void abrirDetalles(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("productos/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Producto");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToTable();
            }
        });
        stage.show();
    }
}
