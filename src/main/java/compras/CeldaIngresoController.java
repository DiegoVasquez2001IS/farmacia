package compras;

import compras.model.DetalleCompra;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Created by user on 3/07/2017.
 */
public class CeldaIngresoController implements Initializable, PasarDetalleCompra{

    @FXML
    private HBox rootCeldaIngreso;

    @FXML
    private Label lblNombre;

    @FXML
    private Label lblSubtotal;

    @FXML
    private DatePicker txtFechaVencimiento;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblCantidad;

    private DetalleCompra mDC;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        convertDataPicker();
        configListenerToDatePicker();
    }

    public void setNombre(String nombre){
        lblNombre.setText(nombre);
    }

    public void setSubtotal(String subtotal){
        lblSubtotal.setText(subtotal);
    }

    public void setPrecio(String precio){
        lblPrecio.setText(precio);
    }

    public void setCantidad(String cantidad){
        lblCantidad.setText(cantidad);
    }

    public void setFechaVencimiento(String fecha){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(fecha,dateTimeFormatter);
        if(localDate != null){
            txtFechaVencimiento.setValue(localDate);
        }
    }

    public void convertDataPicker(){
        String pattern = "yyyy-MM-dd";
        txtFechaVencimiento.setConverter(new StringConverter<LocalDate>() {
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

    private void configListenerToDatePicker(){
        txtFechaVencimiento.setOnAction((event -> {
            for (int i = 0; i<IngresoController.listaDetalleCompra.size(); i++){
                DetalleCompra dc = IngresoController.listaDetalleCompra.get(i);
                if(dc.getIdProducto() == mDC.getIdProducto()){
                    mDC.setFechaVencimiento(txtFechaVencimiento.getValue().toString());
                    IngresoController.listaDetalleCompra.set(i,mDC);
                }
            }
        }));

    }

    public void setAncho(double ancho){
        rootCeldaIngreso.setPrefWidth(ancho);
    }

    @Override
    public void pasar(DetalleCompra dc) {
        if(dc!= null){
            mDC = dc;
        }
    }
}
