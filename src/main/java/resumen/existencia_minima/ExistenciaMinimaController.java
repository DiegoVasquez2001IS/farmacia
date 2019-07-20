package resumen.existencia_minima;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 8/07/2017.
 */
public class ExistenciaMinimaController implements Initializable {

    @FXML
    private RadioButton rdbExistencia5;

    @FXML
    private ToggleGroup grupoExistenciaMinima;

    @FXML
    private RadioButton rdbExistencia10;

    @FXML
    private TextField txtFiltrar;

    @FXML
    private TableView<ProductoExistenciaMinima> tblDatos;

    @FXML
    private TableColumn<ProductoExistenciaMinima, Integer> colIdProducto;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colNomProducto;

    @FXML
    private TableColumn<ProductoExistenciaMinima, Float> colPrecioCompra;

    @FXML
    private TableColumn<ProductoExistenciaMinima, Float> colPrecioVenta;

    @FXML
    private TableColumn<ProductoExistenciaMinima, Integer> colStock;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colFechaVencimiento;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colColocacion;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colDolencia;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colProveedor;

    @FXML
    private TableColumn<ProductoExistenciaMinima, String> colPresentacion;

    private ExistenciaMinimaDAO existenciaMinimaDAO = new ExistenciaMinimaDAO();

    public static ObservableList<ProductoExistenciaMinima> listaProductos = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToTable();
        matchDataWithTable();
        rdbExistencia5.setOnAction((event -> {
            if(rdbExistencia5.isSelected()){
                listaProductos.setAll(existenciaMinimaDAO.getRangoMenorIgual5());
            }
        }));
        rdbExistencia10.setOnAction((event -> {
            if(rdbExistencia10.isSelected()){
                listaProductos.setAll(existenciaMinimaDAO.getRangoMayor5Menor11());
            }
        }));
        configFiltrado();
    }

    private void configFiltrado(){

        // 1. Envolver observable list in to filteredlist
        FilteredList<ProductoExistenciaMinima> listaFiltrada = new FilteredList<ProductoExistenciaMinima>(listaProductos, p -> true);

        // 2. Asignar el predicado y los diferentes filtros que puede tener
        txtFiltrar.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(producto -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (producto.getNomProducto().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (producto.getNomColocacion().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if(producto.getNomProveedor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if(producto.getNomPresentacion().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }else if(producto.getNomDolencia().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if(producto.getFecha().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        // 3. Envolver el FilteredList en un SortedList.
        SortedList<ProductoExistenciaMinima> listaOrdenada = new SortedList<ProductoExistenciaMinima>(listaFiltrada);

        // 4. Bind the SortedList comparator to the TableView comparator.
        listaOrdenada.comparatorProperty().bind(tblDatos.comparatorProperty());

        // 5. agregar sorted (y filtered) datos a la tabla
        tblDatos.setItems(listaOrdenada);

    }

    private void setDataToTable(){
        listaProductos.setAll(existenciaMinimaDAO.getRangoMenorIgual5());
    }

    private void matchDataWithTable(){
        colIdProducto.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, Integer>("idProducto"));
        colNomProducto.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, String>("nomProducto"));
        colPrecioCompra.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, Float>("precioCompra"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, Float>("precioVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, Integer>("stock"));
        colFechaVencimiento.setCellValueFactory(
                new PropertyValueFactory<ProductoExistenciaMinima, String>("fecha")
        );
        colColocacion.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, String>("nomColocacion"));
        colDolencia.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, String>("nomDolencia"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, String>("nomProveedor"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<ProductoExistenciaMinima, String>("nomPresentacion"));
        tblDatos.setItems(listaProductos);
    }
}
