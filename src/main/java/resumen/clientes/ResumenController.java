package resumen.clientes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import resumen.clientes.model.ResumenCliente;
import resumen.clientes.model.ResumenClienteDAO;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 4/06/2017.
 */
public class ResumenController implements Initializable{

    @FXML
    private TableView<ResumenCliente> tblDatos;

    @FXML
    private TableColumn<ResumenCliente, Integer> colIdCliente;

    @FXML
    private TableColumn<ResumenCliente, String> colNombre;

    @FXML
    private TableColumn<ResumenCliente, Float> colTotal;

    @FXML
    private TextField txtFiltrar;

    private ObservableList<ResumenCliente> listaClientes = FXCollections.observableArrayList();
    private ResumenClienteDAO resumenClienteDAO = new ResumenClienteDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDataToList();
        matchDataWithTable();
        configFiltrado();
    }

    private void configFiltrado(){
        FilteredList<ResumenCliente> filteredList = new FilteredList<ResumenCliente>(listaClientes, c -> true);

        txtFiltrar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(resumenCliente -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(resumenCliente.getIdCliente()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (resumenCliente.getNombreCliente().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        SortedList<ResumenCliente> sortedList = new SortedList<ResumenCliente>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void addDataToList(){
        listaClientes.addAll(resumenClienteDAO.getClientes());
    }

    private void matchDataWithTable(){
        colIdCliente.setCellValueFactory(new PropertyValueFactory<ResumenCliente, Integer>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<ResumenCliente, String>("nombreCliente"));
        colTotal.setCellValueFactory(new PropertyValueFactory<ResumenCliente, Float>("total"));
        tblDatos.setItems(listaClientes);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colIdCliente,colNombre,colTotal);
    }
}
