package clientes;

import clientes.model.Cliente;
import clientes.model.ClienteDAO;
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
public class ClientesController implements Initializable {

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
    private TableView<Cliente> tblDatos;

    @FXML
    private TableColumn<Cliente, Integer> colCodigo;

    @FXML
    private TableColumn<Cliente, String> colNombre;

    @FXML
    private TableColumn<Cliente, String> colApellidos;

    @FXML
    private TableColumn<Cliente, String> colDireccion;

    @FXML
    private TableColumn<Cliente, String> colSexo;

    @FXML
    private TableColumn<Cliente, String> colDpi;

    @FXML
    private TableColumn<Cliente, String> colNit;

    @FXML
    private TableColumn<Cliente, Integer> colTelefono;

    @FXML
    private TableColumn<Cliente, Integer> colCelular;

    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    ClienteDAO clienteDAO = new ClienteDAO();
    PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configPermisos();
        matchDataWithTable();
        configFiltrado();
        btnAniadir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirDetalles();
            }
        });
        btnModificar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirDetallesParaModificacion();
            }
        });
        btnEliminar.setOnAction((event) -> {
            eliminarCliente();
        });
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.CLIENTES+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.CLIENTES+""));
    }

    private void eliminarCliente(){
        final Cliente c = tblDatos.getSelectionModel().getSelectedItem();
        if(c != null){
            if(!clienteDAO.agregarModificarEliminarCliente(c,"eliminar")){
                Util.makeSuccess("Operación en Clientes existosa",
                        "Se ha modificado el contenido de la tabla clientes");
                addDataToList();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final Cliente c = tblDatos.getSelectionModel().getSelectedItem();
        if(c != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("clientes/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Cliente");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(c);
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

    private void configFiltrado(){

        // 1. Envolver observable list in to filteredlist
        FilteredList<Cliente> listaFiltrada = new FilteredList<Cliente>(listaClientes,c -> true);

        // 2. Asignar el predicado y los diferentes filtros que puede tener
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(cliente -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (cliente.getApellidos().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if(cliente.getDpi().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(cliente.getNit().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(String.valueOf(cliente.getCelular()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Envolver el FilteredList en un SortedList.
        SortedList<Cliente> listaOrdenada = new SortedList<Cliente>(listaFiltrada);

        // 4. Bind the SortedList comparator to the TableView comparator.
        listaOrdenada.comparatorProperty().bind(tblDatos.comparatorProperty());

        // 5. agregar sorted (y filtered) datos a la tabla
        tblDatos.setItems(listaOrdenada);

    }

    private void abrirDetalles(){
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
        stage.setTitle("Cliente");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToList();
            }
        });
        stage.show();
    }

    private void addDataToList(){
        listaClientes.setAll(clienteDAO.getClientes());
    }

    private void matchDataWithTable(){
        addDataToList();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidos"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("sexo"));
        colDpi.setCellValueFactory(new PropertyValueFactory<Cliente, String>("dpi"));
        colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("telefono"));
        colCelular.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("celular"));
        tblDatos.setItems(listaClientes);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre,colApellidos,
                colDireccion,colSexo,colDpi,colNit,colTelefono,colCelular);
    }
}
