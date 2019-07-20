package proveedores;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import permisos.PermisosDAO;
import proveedores.model.Proveedor;
import proveedores.model.ProveedoresDAO;
import sesion.Sesion;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 28/02/2017.
 */
public class ProveedoresController implements Initializable {

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
    private TableView<Proveedor> tblDatos;

    @FXML
    private TableColumn<Proveedor, Integer> colCodigo;

    @FXML
    private TableColumn<Proveedor, String> colNombre;

    @FXML
    private TableColumn<Proveedor, String> colDireccion;

    @FXML
    private TableColumn<Proveedor, Integer> colTelefono;

    @FXML
    private TableColumn<Proveedor, Integer> colCelular;

    private final ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();

    ProveedoresDAO proveedoresDAO = new ProveedoresDAO();
    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
        configFiltrado();
        configPermisos();
        btnAniadir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirDetalle();
            }
        });

        btnModificar.setOnAction((event -> {
            abrirDetallesParaModificacion();
        }));

        btnEliminar.setOnAction((event -> {
            eliminarProveedor();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PROVEEDORES+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PROVEEDORES+""));
    }

    private void configFiltrado(){
        FilteredList<Proveedor> filteredList = new FilteredList<Proveedor>(listaProveedores);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(proveedor -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (proveedor.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (proveedor.getDireccion().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(proveedor.getTelefono()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (String.valueOf(proveedor.getCelular()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        SortedList<Proveedor> sortedList = new SortedList<Proveedor>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarProveedor(){
        final Proveedor proveedor = tblDatos.getSelectionModel().getSelectedItem();
        if(proveedor != null){
            if(!proveedoresDAO.AgregarModificarEliminarProveedor(proveedor,"eliminar")){
                Util.makeSuccess("Operación en Proveedores existosa",
                        "Se ha modificado el contenido de la tabla Proveedores");
                addDataToList();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final Proveedor proveedor = tblDatos.getSelectionModel().getSelectedItem();
        if(proveedor != null){
            //Util.loadWindownWithParemeters("proveedores/detalles.fxml",
              //      "Modificar Proveedor",getClass(),proveedor,"actualizar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("proveedores/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Proveedor");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(proveedor);
                controller.aisgnarAccion("actualizar");
                stage.setOnHiding((event -> {
                    addDataToList();
                }));
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.ALL.SEVERE, null, ex);

            }
        }
    }

    private void addDataToList(){
        listaProveedores.setAll(proveedoresDAO.getProveedores());
    }

    private void matchDataWithTable(){
        addDataToList();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("nombre"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedor, String>("direccion"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("telefono"));
        colCelular.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("celular"));
        tblDatos.setItems(listaProveedores);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre, colDireccion,colTelefono,colCelular);
    }

    private void abrirDetalle(){
        //Util.loadWindow("proveedores/detalles.fxml","Añadir Proveedor",getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("proveedores/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Añadir Proveedor");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToList();
            }
        });
        stage.show();
    }
}
