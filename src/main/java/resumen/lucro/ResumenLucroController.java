package resumen.lucro;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 8/07/2017.
 */
public class ResumenLucroController implements Initializable{

    @FXML
    private RadioButton rdbVentasDia;

    @FXML
    private ToggleGroup tablaVentas;

    @FXML
    private RadioButton rdbVentasSemana;

    @FXML
    private RadioButton rdbVentasMes;

    @FXML
    private DatePicker txtDeste;

    @FXML
    private DatePicker txtHasta;

    @FXML
    private Button btnVer;

    @FXML
    private Label lblTotal;

    @FXML
    private LineChart<?, ?> grapDatos;

    @FXML
    private CategoryAxis yAxis;

    @FXML
    private NumberAxis xAxis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
