package empleados;

import domain.PasarParametros;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
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
public class EmpleadosController implements Initializable {

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
    private TableView<Empleado> tblDatos;

    @FXML
    private TableColumn<Empleado, Integer> colCodigo;

    @FXML
    private TableColumn<Empleado, String> colNombre;

    @FXML
    private TableColumn<Empleado, String> colApellidos;

    @FXML
    private TableColumn<Empleado, String> colDireccion;

    @FXML
    private TableColumn<Empleado, String> colCargo;

    @FXML
    private TableColumn<Empleado, Integer> colEdad;

    @FXML
    private TableColumn<Empleado, Integer> colTelefono;

    @FXML
    private TableColumn<Empleado, Integer> colCelular;

    @FXML
    private TableColumn<Empleado, String> colFechaIngreso;


    EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
        configFiltrado();
        configPermisos();
        btnAniadir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                abrirDetalles();
            }
        });
        btnModificar.setOnAction((event -> {
            abrirDetallesParaModificacion();
        }));
        btnEliminar.setOnAction((event -> {
            eliminarEmpleado();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.EMPLEADOS+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.EMPLEADOS+""));
    }

    private void configFiltrado(){
        FilteredList<Empleado> filteredList = new FilteredList<Empleado>(listaEmpleados,e -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(empleado -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (empleado.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(empleado.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (empleado.getApellidos().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (empleado.getCargo().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (String.valueOf(empleado.getTelefono()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (String.valueOf(empleado.getCelular()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<Empleado> sortedList = new SortedList<Empleado>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarEmpleado(){
        final Empleado empleado = tblDatos.getSelectionModel().getSelectedItem();
        if(empleado != null) {
            if (!empleadoDAO.agregarModificarEliminarEmpleado(empleado, "eliminar")) {
                Util.makeSuccess("Operación en Empleados existosa",
                        "Se ha modificado el contenido de la tabla Empleados");
                addDataToList();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final Empleado empleado = tblDatos.getSelectionModel().getSelectedItem();
        if(empleado != null)
            //Util.loadWindownWithParemeters("empleados/detalles.fxml","Modificar Empleados",
              //      getClass(),empleado,"actualizar");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("empleados/detalles.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Modificar Empleado");
            stage.setScene(new Scene(parent));
            PasarParametros controller = loader.getController();
            controller.asignarParametros(empleado);
            controller.aisgnarAccion("actualizar");
            stage.setOnHiding((event -> {
                addDataToList();
            }));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.ALL.SEVERE, null, ex);
        }
    }

    private void addDataToList(){
        listaEmpleados.setAll(empleadoDAO.getEmpleados());
    }

    private void matchDataWithTable(){
        addDataToList();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidos"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccion"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("telefono"));
        colCelular.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("celular"));
        colFechaIngreso.setCellValueFactory(new PropertyValueFactory<Empleado, String>("ingreso"));
    }

    private void abrirDetalles(){
        //Util.loadWindow("empleados/detalles.fxml","Empleados",getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("empleados/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Empleados");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToList();
            }
        });
        stage.show();
    }
}
