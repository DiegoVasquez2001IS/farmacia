package pos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 29/05/2017.
 */
public class PopController implements Initializable{

    @FXML
    private Label lblCodigo;

    @FXML
    private Label lblVencimiento;

    @FXML
    private Label lblColocacion;

    @FXML
    private Label lblDolencia;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setCodigo(String codigo){
        lblCodigo.setText(codigo);
    }

    public void setVencimiento(String vencimiento){
        lblVencimiento.setText(vencimiento);
    }

    public void setColocacion(String colocacion){
        lblColocacion.setText(colocacion);
    }

    public void setDolencia(String dolencia){
        lblDolencia.setText(dolencia);
    }
}
