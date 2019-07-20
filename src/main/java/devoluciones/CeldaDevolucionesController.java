package devoluciones;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 3/07/2017.
 */
public class CeldaDevolucionesController implements Initializable, PasarDetalleDevolucion{

    @FXML
    private HBox rootCeldaDevoluciones;

    @FXML
    private Label lblNombre;

    @FXML
    private TextField txtDevuelto;

    @FXML
    private Label lblPrecio;

    @FXML
    private Label lblComprado;

    @FXML
    private Label lblSubtotal;

    private DetalleDevolucion mDetalle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtDevuelto.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(validarCantidad(txtDevuelto.getText())){
                    if(Integer.parseInt(txtDevuelto.getText()) <= mDetalle.getStockComprado()){
                        for(int i = 0; i< DevolucionesController.listaDetalleDevolucion.size(); i++){
                            DetalleDevolucion dd = DevolucionesController.listaDetalleDevolucion.get(i);
                            if(dd.getIdProducto() == mDetalle.getIdProducto()){
                                mDetalle.setStockDevuelto(Integer.parseInt(txtDevuelto.getText()));
                                mDetalle.setTotalDevuelto(getDosDecimales(mDetalle.getStockDevuelto()*mDetalle.getPrecioCompra()));
                                DevolucionesController.listaDetalleDevolucion.set(i,mDetalle);
                                break;
                            }
                        }
                    }else{
                        txtDevuelto.setText(String.valueOf(mDetalle.getStockComprado()));
                    }
                }
            }
        }));
    }

    private boolean validarCantidad(String numero){
        if (!numero.isEmpty()){
            if (!numero.matches("\\d*")) {
                txtDevuelto.setText(numero.replaceAll("[^\\d]", ""));
            }else {
                if(Integer.parseInt(numero) < 0){
                    txtDevuelto.setText("0");
                }else{
                    return true;
                }
            }
        }else{
            txtDevuelto.setText("0");
        }
        return false;
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    public void setAncho(double ancho){
        rootCeldaDevoluciones.setPrefWidth(ancho);
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

    public void setComprado(String cantidad){
        lblComprado.setText(cantidad);
    }

    public void setCantidadDevuelta(String cantidad){
        txtDevuelto.setText(cantidad);
    }

    @Override
    public void pasar(DetalleDevolucion dd) {
        if(dd != null){
            mDetalle = dd;
        }
    }
}
