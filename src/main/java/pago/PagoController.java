package pago;

import domain.PasarParametros;
import escritorio.ConstantesPermisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import pago.model.Pago;
import permisos.PermisosDAO;
import sesion.Sesion;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 23/06/2017.
 */
public class PagoController implements Initializable {

    @FXML
    private Button btnAniadir;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Label lblRegistros;

    @FXML
    private TableView<Pago> tblDatos;

    @FXML
    private TableColumn<Pago, Integer> colCodigo;

    @FXML
    private TableColumn<Pago, String> colNombre;

    private ObservableList<Pago> listaPagos = FXCollections.observableArrayList();
    PagoDAO pagoDAO = new PagoDAO();
    private PermisosDAO permisosDAO = new PermisosDAO();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDataToList();
        matchDataWithTable();
        configFiltrado();
        configPermisos();
        btnAniadir.setOnAction((event -> {
            abrirDetalles();
        }));
        btnModificar.setOnAction((event -> {
            abrirDetallesParaModificacion();
        }));

        btnEliminar.setOnAction((event -> {
            eliminarTipoPago();
        }));
    }

    private void configPermisos(){
        btnAniadir.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.TIPO_DE_PAGO+""));
        btnEliminar.setDisable(!permisosDAO.getEscritura(
                Sesion.getmUsuarioActual().getNivelUsuario()+"",
                ConstantesPermisos.TIPO_DE_PAGO+""));
    }

    private void setDataToList(){
        listaPagos.setAll(pagoDAO.getPagos());
    }

    private void configFiltrado(){
        FilteredList<Pago> filteredList = new FilteredList<Pago>(listaPagos, p -> true);
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(pago -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (pago.getPago().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(pago.getIdPago()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        SortedList<Pago> sortedList = new SortedList<Pago>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void eliminarTipoPago(){
        final Pago pago = tblDatos.getSelectionModel().getSelectedItem();
        if(pago != null) {
            if (!pagoDAO.agregarModificarEliminarPago(pago, "eliminar")) {
                Util.makeSuccess("Operación en Tipo Pago existosa",
                        "Se ha modificado el contenido de la tabla Tipo Pago");
                setDataToList();
            }
        }
    }

    private void matchDataWithTable(){
        colCodigo.setCellValueFactory(new PropertyValueFactory<Pago, Integer>("idPago"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Pago, String>("pago"));
        colCodigo.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.25));
        colNombre.prefWidthProperty().bind(tblDatos.widthProperty().multiply(0.75));
        tblDatos.setItems(listaPagos);
    }

    private void abrirDetallesParaModificacion(){
        final Pago pago = tblDatos.getSelectionModel().getSelectedItem();
        if(pago != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pago/detalles.fxml"));
            Parent parent = null;
            try {
                parent = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Modificar Tipo Pago");
            stage.setScene(new Scene(parent));
            PasarParametros controller = loader.getController();
            controller.asignarParametros(pago);
            controller.aisgnarAccion("actualizar");
            stage.setOnHiding((event -> {
                setDataToList();
            }));
            stage.show();
        }
    }

    private void abrirDetalles(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pago/detalles.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene detalleScene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(detalleScene);
        stage.setTitle("Pago");
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                setDataToList();
            }
        });
        stage.show();
    }
}
