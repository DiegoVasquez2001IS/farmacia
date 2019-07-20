package colocaciones;

import colocaciones.model.Colocacion;
import colocaciones.model.ColocacionesDAO;
import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
 * Created by user on 2/05/2017.
 */
public class ColocacionesController implements Initializable{

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
    private TableView<Colocacion> tblDatos;

    @FXML
    private TableColumn<Colocacion, Integer> colCodigo;

    @FXML
    private TableColumn<Colocacion, String> colNombre;

    private ObservableList<Colocacion> listaColocaciones = FXCollections.observableArrayList();

    ColocacionesDAO dao = new ColocacionesDAO();

    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
        configFiltrado();
        configPermisos();
        btnAniadir.setOnAction((event -> {
            abrirDetalles();
        }));
        btnModificar.setOnAction((event -> {
            abrirDetallesParaModificacion();
        }));
        btnEliminar.setOnAction((event -> {
            eliminarColocacion();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.COLOCACIONES+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.COLOCACIONES+""));
    }

    private void configFiltrado(){
        FilteredList<Colocacion> filteredList = new FilteredList<Colocacion>(listaColocaciones,c -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(colocacion -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (colocacion.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(colocacion.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<Colocacion> sortedList = new SortedList<Colocacion>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarColocacion(){
        final Colocacion colocacion = tblDatos.getSelectionModel().getSelectedItem();
        if(colocacion != null){
            if(!dao.agregarModificarEliminarColocacion(colocacion,"eliminar")){
                Util.makeSuccess("Operación en Colocaciones existosa",
                        "Se ha modificado el contenido de la tabla Colocaciones");
                addDataToTable();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final Colocacion colocacion = tblDatos.getSelectionModel().getSelectedItem();
        if(colocacion != null){
            //Util.loadWindownWithParemeters("colocaciones/detalles.fxml","Modificar Colocación",
              //      getClass(),colocacion,"actualizar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("colocaciones/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Colocacion");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(colocacion);
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
        listaColocaciones.setAll(dao.getColocaciones());
    }

    private void matchDataWithTable(){
        addDataToTable();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Colocacion, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Colocacion, String>("nombre"));
        tblDatos.setItems(listaColocaciones);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre);
    }

    private void abrirDetalles(){
        //Util.loadWindow("colocaciones/detalles.fxml","Colocaciones",getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("colocaciones/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Colocaciones");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToTable();
            }
        });
        stage.show();
    }
}
