package proveedores;

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
import proveedores.model.Proveedor;
import proveedores.model.ProveedoresDAO;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 28/02/2017.
 */
public class DetalleController implements Initializable, PasarParametros {

    @FXML
    private GridPane rootProveedores;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label longCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label longNombre;

    @FXML
    private TextField txtDireccion;

    @FXML
    private Label longDireccion;

    @FXML
    private TextField txtTelefono;

    @FXML
    private Label longTelefono;

    @FXML
    private TextField txtCelular;

    @FXML
    private Label longCelular;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    ProveedoresDAO dao = new ProveedoresDAO();

    private String mAccion = "new";

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
                ConstantesPermisos.PROVEEDORES+""));
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if(!dao.AgregarModificarEliminarProveedor(getProveedor(),accion)){
                Util.makeSuccess("Operación en Proveedores existosa",
                        "Se ha modificado el contenido de la tabla Proveedores");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private void initializeLabelsCounter(){
        List<TextContador> listaLabel = new ArrayList<>();
        listaLabel.add(new TextContador(txtCodigo,11,longCodigo));
        listaLabel.add(new TextContador(txtNombre,40,longNombre));
        listaLabel.add(new TextContador(txtDireccion,70,longDireccion));
        listaLabel.add(new TextContador(txtTelefono,8,longTelefono));
        listaLabel.add(new TextContador(txtCelular,8,longCelular));
        Util.addListenerToLabels(listaLabel);
    }

    private boolean camposLlenos(){
        return txtNombre.getText().trim().length() > 0;
    }

    /**
     * La validación de la variables final se hace porque no hay como pasar null a un parámetro int
     * en el constructor de proveedores, también se usa en todos los detalles controller
     * @return un proveedor en base a los campos llenados
     */
    private Proveedor getProveedor(){
        final int codigo = txtCodigo.getText().trim().length() > 0 ? Integer.parseInt(txtCodigo.getText()) : 0;
        final int telefono = txtTelefono.getText().trim().length() > 0 ? Integer.parseInt(txtTelefono.getText()) : 0;
        final int celular = txtCelular.getText().trim().length() > 0 ? Integer.parseInt(txtCelular.getText()) : 0;
        return new Proveedor(codigo,txtNombre.getText(),txtDireccion.getText(),telefono,celular);
    }

    private void close(){
        Stage thisStage =  (Stage) rootProveedores.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        final Proveedor proveedor = (Proveedor) parametro;
        txtCodigo.setText(String.valueOf(proveedor.getCodigo()));
        txtNombre.setText(proveedor.getNombre());
        txtDireccion.setText(proveedor.getDireccion());
        txtTelefono.setText(String.valueOf(proveedor.getTelefono()));
        txtCelular.setText(String.valueOf(proveedor.getCelular()));
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
