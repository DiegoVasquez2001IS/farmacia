package presentaciones;

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
import presentaciones.model.Presentacion;
import presentaciones.model.PresentacionesDAO;
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
public class PresentacionesController implements Initializable {

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
    private TableView<Presentacion> tblDatos;

    @FXML
    private TableColumn<Presentacion, Integer> colCodigo;

    @FXML
    private TableColumn<Presentacion, String> colNombre;

    private ObservableList<Presentacion> listaPresentaciones = FXCollections.observableArrayList();

    PresentacionesDAO dao = new PresentacionesDAO();
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
            abrirDetalleParaModificacion();
        }));

        btnEliminar.setOnAction((event -> {
            eliminarPresentacion();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRESENTACIONES+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRESENTACIONES+""));
    }

    private void configFiltrado(){
        FilteredList<Presentacion> filteredList = new FilteredList<Presentacion>(listaPresentaciones,p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(presentacion -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(presentacion.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (presentacion.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<Presentacion> sortedList = new SortedList<Presentacion>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarPresentacion(){
        final Presentacion presentacion = tblDatos.getSelectionModel().getSelectedItem();
        if(presentacion != null){
            if(!dao.agregarModificarEliminarPresentacion(presentacion,"eliminar")){
                Util.makeSuccess("Operación en Presentaciones existosa",
                        "Se ha modificado el contenido de la tabla Presentaciones");
                addDataToTable();
            }
        }
    }

    private void abrirDetalleParaModificacion(){
        final Presentacion presentacion = tblDatos.getSelectionModel().getSelectedItem();
        if(presentacion!= null){
            //Util.loadWindownWithParemeters("presentaciones/detalles.fxml","Modificar Presentación",
              //      getClass(), presentacion,"actualizar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("presentaciones/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Presentacion");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(presentacion);
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
        listaPresentaciones.setAll(dao.getPresentaciones());
    }

    private void matchDataWithTable(){
        addDataToTable();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Presentacion, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Presentacion, String>("nombre"));
        tblDatos.setItems(listaPresentaciones);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre);
    }

    private void abrirDetalles(){
        //Util.loadWindow("presentaciones/detalles.fxml","Detalles",getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("presentaciones/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Presentacion");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToTable();
            }
        });
        stage.show();
    }
}
