package permisos;

import empleados.model.Empleado;
import empleados.model.EmpleadoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import nivelesusuario.NivelUsuario;
import nivelesusuario.NivelesUsuarioDAO;
import sesion.Sesion;
import util.Util;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 5/07/2017.
 */
public class PermisosController implements Initializable{

    @FXML
    private GridPane rootPermisos;

    @FXML
    private TableView<NivelUsuario> tblDatos;

    @FXML
    private TableColumn<NivelUsuario, Integer> colNoNivel;

    @FXML
    private TableColumn<NivelUsuario, String> colNomNivel;

    @FXML
    private Button btnModificar;

    @FXML
    private CheckBox checkImprimir;

    @FXML
    private CheckBox checkGuardar;

    @FXML
    private ListView<Permiso> listaPermisos;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextField txtEmpleado;

    public static ObservableList<Permiso> observablePermisos = FXCollections.observableArrayList();
    static ObservableList<NivelUsuario> listaNivelesUsuario = FXCollections.observableArrayList();
    NivelesUsuarioDAO nivelesUsuarioDAO = new NivelesUsuarioDAO();
    PermisosDAO permisosDAO = new PermisosDAO();
    EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToTable();
        matchDataWithTable();
        listaEmpleados.setAll(empleadoDAO.getEmpleados());
        configClickListenerTableView();
        configCellFactoryToListView();
        configListaChangeListener();
        String nombreEmpleado = "";
        for (Empleado em : listaEmpleados){
            if(em.getCodigo() == Sesion.getmUsuarioActual().getCodigoEmpleado()){
                nombreEmpleado = em.getNombre() + " " + em.getApellidos();
                break;
            }
        }
        txtEmpleado.setText(Sesion.getmUsuarioActual().getNick() + "        " + nombreEmpleado);
        btnModificar.setOnAction((event -> {
            modificarPermisos();
        }));
        configFiltrado();
    }

    private void setDataToTable(){
        listaNivelesUsuario.setAll(nivelesUsuarioDAO.getNivelesUsuario());
    }

    private void matchDataWithTable(){
        colNoNivel.setCellValueFactory(new PropertyValueFactory<NivelUsuario, Integer>("idNivel"));
        colNomNivel.setCellValueFactory(new PropertyValueFactory<NivelUsuario, String>("nomNivel"));
        colNoNivel.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.25));
        colNomNivel.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.75));
        tblDatos.setItems(listaNivelesUsuario);
    }

    private void configClickListenerTableView(){
        tblDatos.setOnMouseClicked((event -> {
            if(event.getClickCount() == 1){
                if(tblDatos.getSelectionModel().getSelectedItem() != null){
                   observablePermisos.setAll(permisosDAO.getPermisoXNivelUsuario(String.valueOf(
                           tblDatos.getSelectionModel().getSelectedItem().getIdNivel()
                   )));
                }
            }
        }));
    }

    private void configListaChangeListener(){
        observablePermisos.addListener(new ListChangeListener<Permiso>() {
            @Override
            public void onChanged(Change<? extends Permiso> c) {
                if(observablePermisos.size() > 0){
                    btnModificar.setDisable(false);
                }else{
                    btnModificar.setDisable(true);
                }
            }
        });
    }

    private void configFiltrado(){

        // 1. Envolver observable list in to filteredlist
        FilteredList<NivelUsuario> listaFiltrada = new FilteredList<NivelUsuario>(listaNivelesUsuario, n -> true);

        // 2. Asignar el predicado y los diferentes filtros que puede tener
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(nivelUsuario -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (nivelUsuario.getNomNivel().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if(String.valueOf(nivelUsuario.getIdNivel()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Envolver el FilteredList en un SortedList.
        SortedList<NivelUsuario> listaOrdenada = new SortedList<NivelUsuario>(listaFiltrada);

        // 4. Bind the SortedList comparator to the TableView comparator.
        listaOrdenada.comparatorProperty().bind(tblDatos.comparatorProperty());

        // 5. agregar sorted (y filtered) datos a la tabla
        tblDatos.setItems(listaOrdenada);

    }

    private void configCellFactoryToListView(){
        listaPermisos.setCellFactory(new Callback<ListView<Permiso>, ListCell<Permiso>>() {
            @Override
            public ListCell<Permiso> call(ListView<Permiso> param) {
                return new CeldaPermiso();
            }
        });
        listaPermisos.setItems(observablePermisos);
    }

    private void modificarPermisos(){
        for(Permiso p : observablePermisos){
            permisosDAO.modificarPermiso(p.getIdPermiso()+"",p.getLectura()+"",p.getEscritura()+"");
        }
        Util.makeSuccess("Se han modificador los permisos", "Permisos modificados");
    }
}
