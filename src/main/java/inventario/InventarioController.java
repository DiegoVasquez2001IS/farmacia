package inventario;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 20/02/2017.
 */
public class InventarioController implements Initializable{

    @FXML
    private BorderPane rootInventario;

    @FXML
    private MenuItem cmdProductos;

    @FXML
    private MenuItem cmdPresentaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmdProductos.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AnchorPane rootOperacionesProductos = null;
                try {
                     rootOperacionesProductos = FXMLLoader.load(
                            getClass().getClassLoader().getResource("productos/operaciones_productos.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                rootOperacionesProductos.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootInventario.setCenter(rootOperacionesProductos);
            }
        });
    }
}
