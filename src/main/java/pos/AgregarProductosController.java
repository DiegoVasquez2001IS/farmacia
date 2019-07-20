package pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.controlsfx.control.PopOver;
import pos.model.Pos;
import pos.model.PosDAO;
import ventas.VentasController;
import ventas.model.DetalleOPedido;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 8/07/2017.
 */

public class AgregarProductosController implements Initializable,PasarNoCompra {

    @FXML
    private GridPane rootPos;

    @FXML
    private TableView<Pos> tblDatos;

    @FXML
    private TableColumn<Pos, String> colNombre;

    @FXML
    private TableColumn<Pos, Float> colPrecioVenta;

    @FXML
    private TableColumn<Pos, Integer> colStock;

    @FXML
    private TableColumn<Pos, String> colProveedor;

    @FXML
    private TableColumn<Pos, String> colPresentacion;

    @FXML
    private Label lblNoCompra;

    @FXML
    private TextField txtBuscar;

    public static ObservableList<Pos> listaProductos = FXCollections.observableArrayList();

    PosDAO posDAO = new PosDAO();

    private int mNoCompra = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToTable();
        matchDataWithTable();
        showPopUpForItem();
        configFiltrado();
        configTextWrapperForTableView();
        configClickTable();
    }

    private void setDataToTable(){
        listaProductos.setAll(posDAO.getProductos());
    }
    private void configClickTable(){
       tblDatos.setOnMouseClicked((event -> {
           if(event.getClickCount() == 1){
               if (tblDatos.getSelectionModel().getSelectedItem() != null){
                   agregarProductoALista(tblDatos.getSelectionModel().getSelectedItem());
               }
           }
       }
       ));

    }

    private void agregarProductoALista(Pos pos){
        if(pos.getStock() > 0) {
            if(VentasController.listaDetalles != null){
                if(VentasController.listaCodigos.contains(pos.getCodigo())){
                    for(int i = 0; i<VentasController.listaDetalles.size(); i++){
                        DetalleOPedido dop = VentasController.listaDetalles.get(i);
                        if(dop.getIdProducto() == pos.getCodigo()){
                            if(dop.getCantidad()+1 <= pos.getStock()){
                                dop.setCantidad(dop.getCantidad()+1);
                                dop.setImporte(dop.getCantidad()*dop.getPrecioVenta());
                                VentasController.listaDetalles.set(i,dop);
                            }
                            break;
                        }
                    }
                }else {
                    VentasController.listaCodigos.add(pos.getCodigo());
                    DetalleOPedido dop = new DetalleOPedido(0, mNoCompra, pos.getCodigo(), pos.getNombre(),
                            1, pos.getPreVenta(), 0f, 0f);
                    VentasController.listaDetalles.add(dop);
                }
        }
        }
    }

    private void matchDataWithTable(){
        colNombre.setCellValueFactory(new PropertyValueFactory<Pos, String>("nombre"));
        colPrecioVenta.setCellValueFactory(new PropertyValueFactory<Pos, Float>("preVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<Pos, Integer>("stock"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<Pos, String>("proveedor"));
        colPresentacion.setCellValueFactory(new PropertyValueFactory<Pos, String>("presentacion"));
        tblDatos.setItems(listaProductos);
    }

    private void showPopUpForItem(){
        tblDatos.setRowFactory(new Callback<TableView<Pos>, TableRow<Pos>>() {
            @Override
            public TableRow<Pos> call(TableView<Pos> param) {
                TableRow<Pos> tableRow = new TableRow<>();
                PopOver popOver = new PopOver();

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pos/popup.fxml"));
                tableRow.setOnMouseEntered((event -> {

                    popOver.setDetachable(true);
                    popOver.setDetached(true);
                    popOver.setCornerRadius(4);
                    try {
                        popOver.setContentNode(loader.load());
                        PopController popController = (PopController) loader.getController();
                        if(popController != null) {
                            popController.setCodigo(String.valueOf(tableRow.getItem().getCodigo()));
                            popController.setVencimiento(tableRow.getItem().getFechaVencimiento());
                            popController.setDolencia(tableRow.getItem().getDolencia());
                            popController.setColocacion(tableRow.getItem().getColocacion());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    popOver.show(rootPos,event.getScreenX()+20,event.getScreenY());

                }));
                tableRow.setOnMouseExited((event -> {
                    popOver.hide();
                    loader.setRoot(null);
                    loader.setController(null);

                }));
                return tableRow;
            }
        });

    }

    private void configFiltrado(){
        FilteredList<Pos> filteredList = new FilteredList<Pos>(listaProductos, p -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(producto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(producto.getCodigo()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (producto.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (producto.getColocacion().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getDolencia().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getProveedor().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (producto.getPresentacion().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });
        SortedList<Pos> sortedList = new SortedList<Pos>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void configTextWrapperForTableView(){
        colNombre.setCellFactory(new Callback<TableColumn<Pos, String>, TableCell<Pos, String>>() {
            @Override
            public TableCell<Pos, String> call(TableColumn<Pos, String> param) {
                TableCell<Pos,String> cell = new TableCell<>();
                Text text = new Text();
                cell.setGraphic(text);
                cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                text.wrappingWidthProperty().bind(colNombre.widthProperty());
                text.textProperty().bind(cell.itemProperty());
                return cell;
            }
        });
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.40));
        colPrecioVenta.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colStock.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.10));
        colPresentacion.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));
        colProveedor.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.20));

    }

    @Override
    public void pasarNoCompra(int noCompra) {
            mNoCompra = noCompra;
            lblNoCompra.setText(String.valueOf(mNoCompra));
    }

}
