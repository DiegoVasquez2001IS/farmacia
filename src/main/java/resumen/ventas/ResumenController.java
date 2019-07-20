package resumen.ventas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 10/05/2017.
 */
public class ResumenController implements Initializable {

    @FXML
    private AnchorPane rootResumen;

    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button btnCambiar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle("Stock Monitoring, 2010");
        ObservableList<XYChart.Series<Number,Number>> datosChart =
                FXCollections.observableArrayList(
                        new LineChart.Series("Serie 1",FXCollections.observableArrayList(
                                new XYChart.Data<>(1,2),
                                new XYChart.Data<>(5,3),
                                new XYChart.Data<>(9,3),
                                new XYChart.Data<>(2,7),
                                new XYChart.Data<>(6,9),
                                new XYChart.Data<>(1,2)
                        )),
                        new LineChart.Series<>("Series 2",FXCollections.observableArrayList(
                                new XYChart.Data<>(5,7),
                                new XYChart.Data<>(3,4),
                                new XYChart.Data<>(8,2),
                                new XYChart.Data<>(6,9),
                                new XYChart.Data<>(1,3),
                                new XYChart.Data<>(9,7)
                        )));

        lineChart.setData(datosChart);

        btnCambiar.setOnAction((event -> {
            /*datosChart.set(1,new XYChart.Series<Number,Number>(FXCollections.observableArrayList(
                    new XYChart.Data<>(1,6),
                    new XYChart.Data<>(8,3),
                    new XYChart.Data<>(3,8),
                    new XYChart.Data<>(4,2),
                    new XYChart.Data<>(8,8),
                    new XYChart.Data<>(2,2)
            )));*/
            datosChart.get(0).getData().get(1).setYValue(9);
            //datosChart.get(0).getData().get(2).setYValue(9);

        }));


        //lineChart.getData().addAll(series1,series2,series3);
        //lineChart.setCreateSymbols(false);
        //xAxis.setSide(Side.TOP);


    }

}
