package nivelesusuario;

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

/**
 * Created by user on 5/07/2017.
 */
public class NivelUsuarioController implements Initializable {

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
    private TableView<NivelUsuario> tblDatos;

    @FXML
    private TableColumn<NivelUsuario, Integer> colCodigo;

    @FXML
    private TableColumn<NivelUsuario, String> colNombre;

    private ObservableList<NivelUsuario> listaNiveles = FXCollections.observableArrayList();

    NivelesUsuarioDAO nivelesUsuarioDAO = new NivelesUsuarioDAO();

    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToList();
        matchDataWithTable();
        configFiltrado();
        configPermisos();
        btnEliminar.setOnAction((event -> {
            elimiarNivelUsuario();
        }));

        btnModificar.setOnAction((event -> {
            abrirDetallesParaModificacion();
        }));

        btnAniadir.setOnAction((event -> {
            abrirDetalles();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.NIVELES_DE_USUARIO+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.NIVELES_DE_USUARIO+""));
    }

    private void setDataToList(){
        listaNiveles.setAll(nivelesUsuarioDAO.getNivelesUsuario());
    }

    private void matchDataWithTable(){
        colCodigo.setCellValueFactory(new PropertyValueFactory<NivelUsuario, Integer>("idNivel"));
        colNombre.setCellValueFactory(new PropertyValueFactory<NivelUsuario, String>("nomNivel"));
        colCodigo.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.25));
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.75));
        tblDatos.setItems(listaNiveles);
    }

    private void configFiltrado(){
        FilteredList<NivelUsuario> filteredList = new FilteredList<NivelUsuario>(listaNiveles, n -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(nivelUsuario -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (nivelUsuario.getNomNivel().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(nivelUsuario.getIdNivel()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<NivelUsuario> sortedList = new SortedList<NivelUsuario>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void elimiarNivelUsuario(){
        final NivelUsuario nivelUsuario = tblDatos.getSelectionModel().getSelectedItem();
        if(nivelUsuario != null) {
            if (!nivelesUsuarioDAO.agregarModificarEliminarNivelUsuario(nivelUsuario, "eliminar")) {
                Util.makeSuccess("Operación en Niveles Usuario existosa",
                        "Se ha modificado el contenido de la tabla Niveles Usuario");
                setDataToList();
            }
        }
    }

    private void abrirDetallesParaModificacion(){
        final NivelUsuario nivelUsuario = tblDatos.getSelectionModel().getSelectedItem();
        if(nivelUsuario != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("niveles_usuarios/detalles.fxml"));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Modificar Nivel Usuario");
            stage.setScene(new Scene(parent));
            PasarParametros controller = loader.getController();
            controller.asignarParametros(nivelUsuario);
            controller.aisgnarAccion("actualizar");
            stage.setOnHiding((event -> {
                setDataToList();
            }));
            stage.show();
        }
    }

    private void abrirDetalles(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("niveles_usuarios/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Nivel Usuario");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                setDataToList();
            }
        });
        stage.show();
    }
}
