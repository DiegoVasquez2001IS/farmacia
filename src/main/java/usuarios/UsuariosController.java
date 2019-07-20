package usuarios;

import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import usuarios.model.Usuario;
import usuarios.model.UsuarioDAO;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 12/04/2017.
 */
public class UsuariosController implements Initializable {

    @FXML
    private Button btnAniadir;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblNoRegistros;

    @FXML
    private TableView<Usuario> tblDatos;

    @FXML
    private TableColumn<Usuario, Integer> colCodigoUsuario;

    @FXML
    private TableColumn<Usuario, Integer> colCodigoEmpleado;

    @FXML
    private TableColumn<Usuario, Integer> colNivelUsuario;

    @FXML
    private TableColumn<Usuario, String> colNick;

    @FXML
    private TableColumn<Usuario, String> colPass;

    @FXML
    private TableColumn<Usuario, String> colActivo;

    private final ObservableList<Usuario> listaUsuario = FXCollections.observableArrayList();

    UsuarioDAO usuarioDAO = new UsuarioDAO();

    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
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
            eliminarUsuario();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.USUARIOS+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.USUARIOS+""));
    }

    private void eliminarUsuario(){
        final Usuario usuario = tblDatos.getSelectionModel().getSelectedItem();
        if(usuario != null){
            if(!usuarioDAO.agregarModificarEliminarUsuario(usuario,"eliminar")){
                Util.makeSuccess("Operación en Usuarios existosa",
                        "Se ha modificado el contenido de la tabla Usuarios");
                addDataToList();
            }

        }
    }

    private void abrirDetallesParaModificacion(){
        final Usuario usuario = tblDatos.getSelectionModel().getSelectedItem();
        if(usuario != null){
           // Util.loadWindownWithParemeters("usuarios/detalles.fxml",
             //       "Modificar Usuario",getClass(),usuario,"actualizar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("usuarios/detalles.fxml"));
                Parent parent = loader.load();
                Stage stage = new Stage(StageStyle.DECORATED);
                stage.setTitle("Modificar Usuario");
                stage.setScene(new Scene(parent));
                PasarParametros controller = loader.getController();
                controller.asignarParametros(usuario);
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

    private void abrirDetalles(){
        //Util.loadWindow("usuarios/detalles.fxml","Usuarios ",getClass());
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("usuarios/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Usuario");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                addDataToList();
            }
        });
        stage.show();
    }

    private void addDataToList(){
        listaUsuario.setAll(usuarioDAO.getUsuarios());
    }

    private void matchDataWithTable(){
        addDataToList();
        colCodigoUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoUsuario"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("codigoEmpleado"));
        colNivelUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("nivelUsuario"));
        colNick.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nick"));
        colPass.setCellValueFactory(new PropertyValueFactory<Usuario, String>("pass"));
        colActivo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("activo"));
        tblDatos.setItems(listaUsuario);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colCodigoUsuario,colCodigoEmpleado,colNivelUsuario,colNick,colPass,colActivo);
    }
}