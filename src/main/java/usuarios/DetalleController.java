package usuarios;

import domain.PasarParametros;
import empleados.model.EmpleadoCodNomApe;
import empleados.model.EmpleadoDAO;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import nivelesusuario.NivelUsuario;
import nivelesusuario.NivelesUsuarioDAO;
import permisos.PermisosDAO;
import sesion.Sesion;
import usuarios.model.Usuario;
import usuarios.model.UsuarioDAO;
import util.TextContador;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 12/04/2017.
 */
public class DetalleController implements Initializable, PasarParametros{

    @FXML
    private GridPane rootUsuarios;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label longCodigo;

    @FXML
    private ComboBox<String> comboEmpleado;

    @FXML
    private ComboBox<NivelUsuario> comboNivel;

    @FXML
    private TextField txtNick;

    @FXML
    private Label longNick;

    @FXML
    private PasswordField txtClave;

    @FXML
    private Label longClave;

    @FXML
    private PasswordField txtRepetir;

    @FXML
    private ImageView imgClaveValidada;

    @FXML
    private TextField txtActivo;

    @FXML
    private Label longActivo;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Label lblClave;

    private String mAccion = "new";

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    private Image claveCorrecta = new Image("usuarios/imagenes/16_pass_correct.png");
    private Image claveIncorrecta = new Image("usuarios/imagenes/16_pass_invalid.png");

    private ObservableList<EmpleadoCodNomApe> listaEmpleados = FXCollections.observableArrayList();
    private ObservableList<String> listaEmpleadosString = FXCollections.observableArrayList();
    private Map<Integer,String>  mapStringEmpleados = new HashMap<>();
    private Map<String, Integer> mapCodEmpleados = new HashMap<>();

    private NivelesUsuarioDAO nivelesUsuarioDAO = new NivelesUsuarioDAO();
    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabelsCounter();
        configValidarClave();
        initComboEmpleado();
        configCambiarClave();
        configPermisos();
        btnGuardar.setOnAction((event -> {
            agregarModificar(mAccion);
        }));
        btnCancelar.setOnAction((event -> {
            close();
        }));
        llenarComboNivel();
        convertComboNivelToString();

    }

    private void configPermisos(){
        btnGuardar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.USUARIOS+""));
    }

    private void llenarComboNivel(){
        ObservableList<NivelUsuario> listaNiveles = FXCollections.observableArrayList(nivelesUsuarioDAO.getNivelesUsuario());
        comboNivel.setItems(listaNiveles);
    }

    private void initComboEmpleado(){
        listaEmpleados.addAll(empleadoDAO.getCodNomApeEmpleados());
        for(EmpleadoCodNomApe em : listaEmpleados){
            listaEmpleadosString.add(em.getNombre() + " " + em.getApellidos());
            mapStringEmpleados.put(em.getCodigo(),em.getNombre() + " " + em.getApellidos());
            mapCodEmpleados.put(em.getNombre() + " " + em.getApellidos(),em.getCodigo());
        }
        comboEmpleado.setItems(listaEmpleadosString);
    }

    private void configValidarClave(){
        txtRepetir.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.length() > 0){
                if(newValue.equals(txtClave.getText())){
                    imgClaveValidada.setImage(claveCorrecta);

                }else{
                    imgClaveValidada.setImage(claveIncorrecta);
                }
            }
        }));
        txtClave.textProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.length() > 0){
                if(newValue.equals(txtRepetir.getText())){
                    imgClaveValidada.setImage(claveCorrecta);
                }else{
                    imgClaveValidada.setImage(claveIncorrecta);
                }
            }
        }));
    }

    private void configCambiarClave(){
        txtClave.setOnMouseClicked((event -> {
            if(event.getClickCount() == 2){
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("usuarios/cambiarClave.fxml"));
                    Parent parent = loader.load();
                    Stage stage = new Stage(StageStyle.DECORATED);
                    stage.setTitle("Cambiar Clave");
                    stage.setScene(new Scene(parent));
                    CambiarClave controller = loader.getController();
                    controller.pasarContraseniaVieja(txtClave.getText(),this);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(getClass().getName()).log(Level.ALL.SEVERE, null, ex);
                }
            }
        }));
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if (!usuarioDAO.agregarModificarEliminarUsuario(getUsuario(),accion)){
                Util.makeSuccess("Operación en Usuarios existosa",
                        "Se ha modificado el contenido de la tabla Usuarios");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private boolean camposLlenos(){
        return
                comboNivel.getSelectionModel().getSelectedItem()!=null && txtNick.getText().length() > 0 &&
                txtClave.getText().length() > 0 && txtRepetir.getText().equals(txtClave.getText()) &&
                txtActivo.getText().length() > 0;
    }

    private Usuario getUsuario(){
        final int codigoUsuario = txtCodigo.getText().trim().length() > 0 ?
                Integer.parseInt(txtCodigo.getText().trim()) : 0;
        final int codigoEmpleado = mapCodEmpleados.get(comboEmpleado.getSelectionModel().getSelectedItem());
        return new Usuario(codigoUsuario,codigoEmpleado,comboNivel.getSelectionModel().getSelectedItem().getIdNivel(),txtNick.getText(),
                txtClave.getText(),txtActivo.getText());
    }

    private void initializeLabelsCounter(){
        List<TextContador> labelsList = new ArrayList<>();
        labelsList.add(new TextContador(txtCodigo,11,longCodigo));
        labelsList.add(new TextContador(txtNick,30,longNick));
        labelsList.add(new TextContador(txtClave,50,longClave));
        labelsList.add(new TextContador(txtActivo,2,longActivo));
        Util.addListenerToLabels(labelsList);
    }

    private void convertComboNivelToString(){
        comboNivel.setCellFactory(new Callback<ListView<NivelUsuario>, ListCell<NivelUsuario>>() {
            @Override
            public ListCell<NivelUsuario> call(ListView<NivelUsuario> param) {
                ListCell<NivelUsuario> cell = new ListCell<NivelUsuario>(){
                    @Override
                    protected void updateItem(NivelUsuario item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        }else {
                            setText(getStringForNivelUsuario(item));
                        }
                    }
                };
                return cell;
            }
        });

        comboNivel.setButtonCell(new ListCell<NivelUsuario>() {
            @Override
            protected void updateItem(NivelUsuario item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                }else {
                    setText(getStringForNivelUsuario(item));
                }
            }
        });

        comboNivel.setConverter(new StringConverter<NivelUsuario>() {
            private Map<String,NivelUsuario> map = new HashMap<String, NivelUsuario>();
            @Override
            public String toString(NivelUsuario object) {
                if(object != null){
                    String str = getStringForNivelUsuario(object);
                    map.put(str,object);
                    return str;
                }else{
                    return "";
                }
            }

            @Override
            public NivelUsuario fromString(String string) {
                if(!map.containsKey(string)){
                    comboNivel.setValue(null);
                    comboNivel.getEditor().clear();
                    return null;
                }
                return map.get(string);
            }
        });
    }

    private String getStringForNivelUsuario(NivelUsuario nivelUsuario){
        return nivelUsuario.getIdNivel() + "\t" + nivelUsuario.getNomNivel();
    }


    private void close(){
        Stage thisStage =  (Stage) rootUsuarios.getScene().getWindow();
        thisStage.close();
    }

    public void habilitarTxtClave(){
        txtClave.setText("");
        txtRepetir.setText("");
        txtClave.setEditable(true);
        txtRepetir.setEditable(true);
    }

    @Override
    public void asignarParametros(Object parametro) {
        Usuario usuario = (Usuario) parametro;
        txtCodigo.setText(String.valueOf(usuario.getCodigoUsuario()));
        comboEmpleado.getSelectionModel().select(mapStringEmpleados.get(usuario.getCodigoEmpleado()));
        comboNivel.getSelectionModel().select(nivelesUsuarioDAO.getNivelUsuarioXId(usuario.getNivelUsuario()+""));
        txtNick.setText(usuario.getNick());
        txtClave.setText(usuario.getPass());
        txtClave.setEditable(false);
        txtRepetir.setText(usuario.getPass());
        txtRepetir.setEditable(false);
        txtActivo.setText(usuario.getActivo());
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
