package usuarios;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

/**
 * Created by user on 17/05/2017.
 */
public class CambiarClaveController implements Initializable, CambiarClave {

    @FXML
    private GridPane rootCambiarClave;

    @FXML
    private Button btnListo;

    @FXML
    private PasswordField txtClave;

    @FXML
    private Label lblStatus;

    private String md5Clave = "";

    private DetalleController detalleController = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnListo.setOnAction((event -> {
            if(md5Clave.equals("")){
                lblStatus.setText("Ocurrió un error, vuelva a abrir esta ventana");
            }else{
                if(compararClaves(txtClave.getText().trim())){
                    close();
                    detalleController.habilitarTxtClave();
                }else{
                    lblStatus.setText("La clave es incorrecta vuela a intentarlo");
                }
            }
        }));
    }
    /**
     * @param clave la clave md5 desde la interfaz
     * @return true si la clave md5 con la que escribió son iguales
     */
    private boolean compararClaves(String clave){
        return md5(clave).equals(md5Clave);
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

    @Override
    public void pasarContraseniaVieja(String contrasenia, DetalleController usuariosDetalles) {
        if(contrasenia != null) {
            md5Clave = contrasenia;
        }
        if(usuariosDetalles != null){
            this.detalleController = usuariosDetalles;
        }
    }

    private void close(){
        Stage thisStage =  (Stage) rootCambiarClave.getScene().getWindow();
        thisStage.close();
    }
}
