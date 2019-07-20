package presentaciones;

import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import permisos.PermisosDAO;
import presentaciones.model.Presentacion;
import presentaciones.model.PresentacionesDAO;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 22/03/2017.
 */
public class DetalleController implements Initializable, PasarParametros{


    @FXML
    private GridPane rootPresentaciones;

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

    private PresentacionesDAO dao = new PresentacionesDAO();
    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabelsCounter();
        configPermisos();
        btnGuardar.setOnAction((event -> {
            agregarModificar(mAccion);
        }));

        btnCancelar.setOnAction((event -> {
            close();
        }));
    }

    private void configPermisos(){
        btnGuardar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.PRESENTACIONES+""));
    }

    private void initializeLabelsCounter(){
        List<TextContador> listLabels = new ArrayList<>();
        listLabels.add(new TextContador(txtCodigo,11,longCodigo));
        listLabels.add(new TextContador(txtNombre,50,longNombre));
        Util.addListenerToLabels(listLabels);
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if(!dao.agregarModificarEliminarPresentacion(getPresentacion(),accion)){
                Util.makeSuccess("Operación en Presentaciones existosa",
                        "Se ha modificado el contenido de la tabla Presentaciones");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private Presentacion getPresentacion(){
        final int codigo = txtCodigo.getText().trim().length() > 0 ? Integer.parseInt(txtCodigo.getText()) : 0;
        return new Presentacion(codigo,txtNombre.getText());
    }

    private boolean camposLlenos(){
        return txtNombre.getText().trim().length() > 0;
    }

    private void close(){
        Stage thisStage =  (Stage) rootPresentaciones.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        final Presentacion presentacion = (Presentacion) parametro;
        txtCodigo.setText(String.valueOf(presentacion.getCodigo()));
        txtNombre.setText(presentacion.getNombre());
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
