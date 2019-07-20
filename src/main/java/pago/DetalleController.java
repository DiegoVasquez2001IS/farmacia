package pago;

import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pago.model.Pago;
import permisos.PermisosDAO;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 6/07/2017.
 */
public class DetalleController implements Initializable, PasarParametros {

    @FXML
    private GridPane rootTipoPago;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label longCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label longNombre;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    private String mAccion = "new";

    PagoDAO pagoDAO = new PagoDAO();
    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabelsCounter();
        configPermisos();
        btnGuardar.setOnAction((event) -> {
            agregarModificar(mAccion);
        });

        btnCancelar.setOnAction((event -> {
            close();
        }));
    }

    private void configPermisos(){
        btnGuardar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.TIPO_DE_PAGO+""));
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if (!pagoDAO.agregarModificarEliminarPago(getPago(),accion)){
                Util.makeSuccess("Operación en Tipo Pago existosa",
                        "Se ha modificado el contenido de la tabla Tipo Pago");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private boolean camposLlenos(){
        return txtNombre.getText().trim().length() > 0;
    }

    private Pago getPago(){
        final int codigo = txtCodigo.getText().length() > 0 ? Integer.parseInt(txtCodigo.getText()) : 0;
        return new Pago(codigo,txtNombre.getText());
    }

    private void initializeLabelsCounter(){
        List<TextContador> labelsList = new ArrayList<>();
        labelsList.add(new TextContador(txtCodigo,11,longCodigo));
        labelsList.add(new TextContador(txtNombre,40,longNombre));
        Util.addListenerToLabels(labelsList);
    }

    private void close(){
        Stage thisStage =  (Stage) rootTipoPago.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        final Pago pago = (Pago) parametro;
        txtCodigo.setText(String.valueOf(pago.getIdPago()));
        txtNombre.setText(pago.getPago());
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
