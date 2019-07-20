package resumen.productos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import resumen.productos.model.ResumenProducto;
import resumen.productos.model.ResumenProductosDAO;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 4/06/2017.
 */
public class ResumenController implements Initializable{

    @FXML
    private RadioButton rdbVencidos;

    @FXML
    private ToggleGroup grupoResumenProductos;

    @FXML
    private RadioButton rdbAPunto;

    @FXML
    private TextField txtFiltrar;

    @FXML
    private TableView<ResumenProducto> tblDatos;

    @FXML
    private TableColumn<ResumenProducto, Long> colIdProducto;

    @FXML
    private TableColumn<ResumenProducto, String> colNomProducto;

    @FXML
    private TableColumn<ResumenProducto, String> colVencimiento;

    @FXML
    private TableColumn<ResumenProducto, Integer> colStock;

    @FXML
    private TableColumn<ResumenProducto, Float> colPrecioVenta;

    @FXML
    private TableColumn<ResumenProducto, String> colProveedor;

    private ObservableList<ResumenProducto> listaProductos = FXCollections.observableArrayList();

    ResumenProductosDAO resumenProductosDAO = new ResumenProductosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDataToList(ResumenProductosDAO.PRODUCTOSVECIDOS);
        matchDataWithTable();
        rdbVencidos.setOnAction((event -> {
            addDataToList(ResumenProductosDAO.PRODUCTOSVECIDOS);
        }));

        rdbAPunto.setOnAction((event -> {
            addDataToList(ResumenProductosDAO.PRODUCTOSAVENCER);
        }));
        configFiltrado();
    }

    /**
     *
     * @param tipoDatos son las constantes definidas en ResumenProductosDAO
     * solo puede tomar 2 valores
     */
    private void addDataToList(int tipoDatos){
        listaProductos.setAll(resumenProductosDAO.getDatos(tipoDatos));
    }

    private void configFiltrado(){
        FilteredList<ResumenProducto> filteredList = new FilteredList<ResumenProducto>(listaProductos, p -> true);

        txtFiltrar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(producto -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(producto.getIdProducto()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (producto.getNomProducto().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (producto.getNomProveedor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getVencimiento().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        SortedList<ResumenProducto> sortedList = new SortedList<ResumenProducto>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void matchDataWithTable(){
        colIdProducto.setCellValueFactory(new PropertyValueFactory<ResumenProducto, Long>("idProducto"));
        colNomProducto.setCellValueFactory(new PropertyValueFactory<ResumenProducto, String>("nomProducto"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<ResumenProducto, String>("vencimiento"));
        colStock.setCellValueFactory(new PropertyValueFactory<ResumenProducto, Integer>("stock"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<ResumenProducto, Float>("precioVenta"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<ResumenProducto, String>("nomProveedor"));
        tblDatos.setItems(listaProductos);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colIdProducto,colNomProducto,colVencimiento,
                colStock,colPrecioVenta,colProveedor);
    }
}
