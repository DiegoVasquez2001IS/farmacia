package clientes;

import clientes.model.Cliente;
import clientes.model.ClienteDAO;
import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import permisos.PermisosDAO;
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
    private GridPane rootClientes;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Label longCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private Label longNombre;

    @FXML
    private TextField txtApellidos;

    @FXML
    private Label longApellidos;

    @FXML
    private TextField txtDireccion;

    @FXML
    private Label longDireccion;

    @FXML
    private RadioButton rdbMasculino;

    @FXML
    private RadioButton rdbFemenino;

    @FXML
    private TextField txtDpi;

    @FXML
    private CheckBox checkNit;

    @FXML
    private Label longDpi;

    @FXML
    private TextField txtNit;

    @FXML
    private Label longNit;

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

    private String mAccion = "new";

    ClienteDAO clienteDAO = new ClienteDAO();

    PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        iniciarLabelsCounter();
        configCheckNit();
        configPermisos();
        btnGuardar.setOnAction((event) -> {
            agregarModificar(mAccion);
        });

        btnCancelar.setOnAction((event) -> {
            close();
        });
    }

    private void configPermisos(){
        btnGuardar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.CLIENTES+""));
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if(!clienteDAO.agregarModificarEliminarCliente(crearCliente(),accion)){
                Util.makeSuccess("Operación en Clientes existosa",
                        "Se ha modificado el contenido de la tabla clientes");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private boolean camposLlenos(){
        return txtNombre.getText().length() > 0 &&
                txtApellidos.getText().length() > 0 && txtDireccion.getText().length() > 0 &&
                txtNit.getText().length() > 0 && txtCelular.getText().length() > 0;
    }

    private void configCheckNit() {
        checkNit.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    txtNit.setText("C/F");
                    txtNit.setEditable(false);
                }else{
                    txtNit.setText("");
                    txtNit.setEditable(true);
                }
            }
        });
    }

    private Cliente crearCliente(){
        final String sexo = (rdbMasculino.isSelected()) ? "m" : (rdbFemenino.isSelected()) ? "f" : "";
        final int codigo = txtCodigo.getText().trim().length() > 0 ? Integer.parseInt(txtCodigo.getText().trim()) : 0;
        final int telefono = txtTelefono.getText().trim().length() > 0 ? Integer.parseInt(txtTelefono.getText().trim()) : 0;
        final Cliente nuevoCliente = new Cliente(codigo,
                txtNombre.getText(),txtApellidos.getText(),txtDireccion.getText(),
                sexo,txtDpi.getText(),txtNit.getText(),telefono,Integer.parseInt(txtCelular.getText()));
        return nuevoCliente;
    }

    private void iniciarLabelsCounter(){
       final List<TextContador> listaContadores = new ArrayList<>();
       listaContadores.add(new TextContador(txtCodigo,11,longCodigo));
       listaContadores.add(new TextContador(txtNombre,60,longNombre));
       listaContadores.add(new TextContador(txtApellidos,60,longApellidos));
       listaContadores.add(new TextContador(txtDireccion,60,longDireccion));
       listaContadores.add(new TextContador(txtDpi,15,longDpi));
       listaContadores.add(new TextContador(txtNit,12,longNit));
       listaContadores.add(new TextContador(txtTelefono,8,longTelefono));
       listaContadores.add(new TextContador(txtCelular,8,longCelular));
       Util.addListenerToLabels(listaContadores);
    }

    private void close(){
        Stage thisStage =  (Stage) rootClientes.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        final Cliente cliente = (Cliente) parametro;
        txtCodigo.setText(String.valueOf(cliente.getCodigo()));
        txtNombre.setText(cliente.getNombre());
        txtApellidos.setText(cliente.getApellidos());
        txtDireccion.setText(cliente.getDireccion());
        if(cliente.getSexo().equals("m")){
            rdbMasculino.setSelected(true);
            rdbFemenino.setSelected(false);
        }else{
            rdbMasculino.setSelected(false);
            rdbFemenino.setSelected(true);
        }
        txtDpi.setText(cliente.getDpi());
        txtNit.setText(cliente.getNit());
        txtTelefono.setText(String.valueOf(cliente.getTelefono()));
        txtCelular.setText(String.valueOf(cliente.getCelular()));
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
