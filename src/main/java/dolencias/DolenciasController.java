package dolencias;

import dolencias.model.Dolencia;
import dolencias.model.DolenciasDAO;
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

/**
 * Created by user on 28/02/2017.
 */
public class DolenciasController implements Initializable {

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
    private TableView<Dolencia> tblDatos;

    @FXML
    private TableColumn<Dolencia, Integer> colCodigo;

    @FXML
    private TableColumn<Dolencia, String> colNombre;

    private final ObservableList<Dolencia> listaDolencias = FXCollections.observableArrayList();

    private DolenciasDAO dolenciasDAO = new DolenciasDAO();

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
            eliminarDolencia();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.DOLENCIAS+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.DOLENCIAS+""));
    }

    private void configFiltrado(){
        FilteredList<Dolencia> filteredList = new FilteredList<Dolencia>(listaDolencias,d -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(dolencia -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (dolencia.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(dolencia.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<Dolencia> sortedList = new SortedList<Dolencia>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarDolencia(){
        final Dolencia dolencia = tblDatos.getSelectionModel().getSelectedItem();
        if(dolencia != null) {
            if (!dolenciasDAO.agregarModificarEliminarDolencia(dolencia, "eliminar")) {
                Util.makeSuccess("Operación en Dolencias existosa",
                        "Se ha modificado el contenido de la tabla Dolencias");
                addDataToTable();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final Dolencia dolencia = tblDatos.getSelectionModel().getSelectedItem();
        if(dolencia != null) {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("dolencias/detalles.fxml"));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Modificar Dolencia");
            stage.setScene(new Scene(parent));
            PasarParametros controller = loader.getController();
            controller.asignarParametros(dolencia);
            controller.aisgnarAccion("actualizar");
            stage.setOnHiding((event -> {
                addDataToTable();
            }));
            stage.show();
        }
    }

    private void addDataToTable(){
        listaDolencias.setAll(dolenciasDAO.getDolencias());
    }

    private void matchDataWithTable(){
        addDataToTable();
        colCodigo.setCellValueFactory(new PropertyValueFactory<Dolencia, Integer>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Dolencia, String>("nombre"));
        tblDatos.setItems(listaDolencias);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigo,colNombre);
    }

    private void abrirDetalles(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("dolencias/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Dolencia");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToTable();
            }
        });
        stage.show();
    }
}
