package ventas.anulacion;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 26/06/2017.
 */
public class AnulacionController implements Initializable, PasarNoFactura {

    @FXML
    private GridPane rootAnular;

    @FXML
    private Label lblNoFactura;

    @FXML
    private Button btnAnular;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtMotivo;

    @FXML
    private Label longMotivo;

    private int mNoFactura = 0;
    AnulacionDAO anulacionDAO = new AnulacionDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabelsCounter();
        btnAnular.setOnAction((event -> {
            if(txtMotivo.getText().length()>0){
                anular();
            }else{
                Util.makeAdvertencia("Llenar Motivo","Debe escribir un motivo para la anulación");
            }
        }));
    }

    private void initializeLabelsCounter(){
        List<TextContador> listaLabels = new ArrayList<>();
        listaLabels.add(new TextContador(txtMotivo,70,longMotivo));
        Util.addListenerToLabels(listaLabels);
    }

    @Override
    public void pasarNoFactura(int noFactura) {
        lblNoFactura.setText(String.valueOf(noFactura));
        mNoFactura = noFactura;
    }

    private void anular(){
        if(!anulacionDAO.anularOrden(new Anulacion(mNoFactura,txtMotivo.getText()))){
            close();
        }
    }

    private void close(){
        Stage thisStage =  (Stage) rootAnular.getScene().getWindow();
        thisStage.close();
    }


}
