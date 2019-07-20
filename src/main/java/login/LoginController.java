package login;

import bd.Conexion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sesion.Sesion;
import usuarios.model.Usuario;
import usuarios.model.UsuarioDAO;
import util.Util;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

/**
 * Created by user on 19/02/2017.
 */
public class LoginController implements Initializable {

    @FXML
    private GridPane rootLogin;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnEntrar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Label lblStatus;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conectarABaseDatos();
        llenarListaUsuarios();
        btnEntrar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(puedeEntrar(txtUsuario.getText(),txtPassword.getText())){
                    abrirEscritorio();
                }else{
                    lblStatus.setText("Usuario o Clave invalida");
                    //System.out.println("Usuario o pass inválido");
                }
            }
        });
        btnCancelar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        });
    }

    private void abrirEscritorio(){
            close();
            Scene escritorioScene = new Scene(Util.getWindow("escritorio/escritorio.fxml",getClass()));
            Stage stage = new Stage();
            stage.setMaximized(true);
            stage.setScene(escritorioScene);
            stage.setTitle("Escritorio");
            stage.show();
    }

    private void close(){
        Stage thisStage =  (Stage) rootLogin.getScene().getWindow();
        thisStage.close();
    }

    private void llenarListaUsuarios(){
        listaUsuarios.addAll(usuarioDAO.getUsuarios());
    }

    private void conectarABaseDatos(){
        System.out.println(Conexion.getInstance().conectar());
    }

    private boolean puedeEntrar(String usuario,String pass){
        for(Usuario user : listaUsuarios){
            if(user.getNick().equals(usuario) && user.getPass().equals(md5(pass))){
                Sesion.setmUsuarioActual(user);
                return true;
            }
        }
        return false;
    }

    public String md5( String source ) {
        try {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            byte[] bytes = md.digest( source.getBytes("UTF-8") );
            return getString( bytes );
        } catch( Exception e )  {
            e.printStackTrace();
            return null;
        }
    }

    private String getString( byte[] bytes )
    {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<bytes.length; i++ )
        {
            byte b = bytes[ i ];
            String hex = Integer.toHexString((int) 0x00FF & b);
            if (hex.length() == 1)
            {
                sb.append("0");
            }
            sb.append( hex );
        }
        return sb.toString();
    }
}
