package empleados;

import domain.PasarParametros;
import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import escritorio.ConstantesPermisos;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import permisos.PermisosDAO;
import sesion.Sesion;
import util.TextContador;
import util.Util;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 22/03/2017.
 */
public class DetalleController implements Initializable, PasarParametros {

    @FXML
    private GridPane rootEmpleados;

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
    private TextField txtCargo;

    @FXML
    private Label longCargo;

    @FXML
    private TextField txtEdad;

    @FXML
    private Label longEdad;

    @FXML
    private TextField txtTelefono;

    @FXML
    private Label longTelefono;

    @FXML
    private TextField txtCelular;

    @FXML
    private Label longCelular;

    @FXML
    private DatePicker txtFecha;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    private String mAccion = "new";

    EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    private PermisosDAO permisosDAO = new PermisosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabelsCounter();
        convertDataPicker();
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
                ConstantesPermisos.EMPLEADOS+""));
    }

    private void initializeLabelsCounter(){
        List<TextContador> listaLabels = new ArrayList<>();
        listaLabels.add(new TextContador(txtCodigo,11,longCodigo));
        listaLabels.add(new TextContador(txtNombre,80,longNombre));
        listaLabels.add(new TextContador(txtApellidos,45,longApellidos));
        listaLabels.add(new TextContador(txtDireccion,60,longDireccion));
        listaLabels.add(new TextContador(txtCargo,40,longCargo));
        listaLabels.add(new TextContador(txtEdad,4,longEdad));
        listaLabels.add(new TextContador(txtTelefono,8,longTelefono));
        listaLabels.add(new TextContador(txtCelular,8,longCelular));
        Util.addListenerToLabels(listaLabels);
    }

    private void agregarModificar(String accion){
        if(camposLlenos()){
            if (!empleadoDAO.agregarModificarEliminarEmpleado(getEmpleado(), accion)) {
                Util.makeSuccess("Operación en Empleados existosa",
                        "Se ha modificado el contenido de la tabla Empleados");
                close();
            }
        }else{
            Util.makeAdvertencia("Campos sin rellenar","Llene los campos obligatorios");
        }
    }

    private Empleado getEmpleado(){
        final int codigo = txtCodigo.getText().trim().length() > 0 ?
                Integer.parseInt(txtCodigo.getText().trim()) : 0;
        final int edad = txtEdad.getText().trim().length() > 0 ?
                Integer.parseInt(txtEdad.getText().trim()) : 0;
        final int telefono = txtTelefono.getText().trim().length() > 0 ?
                Integer.parseInt(txtTelefono.getText().trim()) : 0;
        final int celular = txtCelular.getText().trim().length() > 0 ?
                Integer.parseInt(txtCelular.getText().trim()) : 0;
        return new Empleado(codigo,txtNombre.getText(),txtApellidos.getText(),
                txtDireccion.getText(),txtCargo.getText(),edad,telefono,celular,txtFecha.getValue().toString());
    }

    private boolean camposLlenos(){
        return txtApellidos.getText().length() > 0 &&
                txtNombre.getText().length() > 0 &&
                txtCargo.getText().length() > 0 &&
                txtCelular.getText().length() > 0 &&
                txtFecha.getValue().toString().length() > 0;
    }

    public void convertDataPicker(){
        String pattern = "yyyy-MM-dd";
        txtFecha.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void close(){
        Stage thisStage =  (Stage) rootEmpleados.getScene().getWindow();
        thisStage.close();
    }

    @Override
    public void asignarParametros(Object parametro) {
        Empleado empleado = (Empleado) parametro;
        txtCodigo.setText(String.valueOf(empleado.getCodigo()));
        txtNombre.setText(empleado.getNombre());
        txtApellidos.setText(empleado.getApellidos());
        txtDireccion.setText(empleado.getDireccion());
        txtCargo.setText(empleado.getCargo());
        txtEdad.setText(String.valueOf(empleado.getEdad()));
        txtTelefono.setText(String.valueOf(empleado.getTelefono()));
        txtCelular.setText(String.valueOf(empleado.getCelular()));
        txtFecha.setValue(LocalDate.parse(empleado.getIngreso()));
    }

    @Override
    public void aisgnarAccion(String accion) {
        if(accion != null){
            mAccion = accion;
        }
    }
}
