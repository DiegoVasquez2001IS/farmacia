package compras;

import compras.model.DetalleCompra;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 1/07/2017.
 */
public class CeldaComprasController implements Initializable, PasarDetalleCompra{

    @FXML
    private HBox rootCeldaCompras;

    @FXML
    private Label lblNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private Label lblSubtotal;

    private DetalleCompra mDc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCantidad.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(validarCantidad(txtCantidad.getText())) {
                    for (int i = 0; i < ComprasController.listaDetalleCompra.size(); i++) {
                        DetalleCompra dc = ComprasController.listaDetalleCompra.get(i);
                        if (dc.getIdProducto() == mDc.getIdProducto()) {
                            mDc.setCantidad(Integer.parseInt(txtCantidad.getText()));
                            mDc.setSubTotal(getDosDecimales(mDc.getCantidad() * mDc.getPrecioCompra()));
                            ComprasController.listaDetalleCompra.set(i,mDc);
                            break;
                        }
                    }
                }
            }
        }));
        txtPrecio.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if (validarPrecio(txtPrecio.getText())) {
                    for (int i = 0; i < ComprasController.listaDetalleCompra.size(); i++) {
                        DetalleCompra dc = ComprasController.listaDetalleCompra.get(i);
                        if (dc.getIdProducto() == mDc.getIdProducto()) {
                            mDc.setPrecioCompra(Float.parseFloat(txtPrecio.getText()));
                            mDc.setSubTotal(getDosDecimales(mDc.getCantidad() * mDc.getPrecioCompra()));
                            ComprasController.listaDetalleCompra.set(i, mDc);
                            break;
                        }
                    }
                }
            }
        }));
    }

    public void setNombre(String nombre){
        lblNombre.setText(nombre);
    }

    public void setPrecio(String precio){
        txtPrecio.setText(precio);
    }

    public void setCantidad(String cantidad){
        txtCantidad.setText(cantidad);
    }

    public void setSubTotal(float precio, int cantidad){
        float subtotal = getDosDecimales(precio*cantidad);
        lblSubtotal.setText(subtotal+"");
    }

    public void setAncho(double ancho){
        rootCeldaCompras.setPrefWidth(ancho);
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    private boolean validarCantidad(String numero){
        if (!numero.isEmpty()){
            if (!numero.matches("\\d*")) {
                txtCantidad.setText(numero.replaceAll("[^\\d]", ""));
            }else {
                if(Integer.parseInt(numero) <= 0){
                    txtCantidad.setText("1");
                }else{
                    return true;
                }
            }
        }else{
            txtCantidad.setText("1");
        }
        return false;
    }

    private boolean validarPrecio(String numero){
        if(!numero.isEmpty()){
            if(!numero.matches("\\d+.?\\d*")){
                txtPrecio.setText(numero.replaceAll(".",""));
            }else{
                if(Float.parseFloat(numero) < 0f){
                    txtPrecio.setText("0");
                }else{
                    return true;
                }
            }
        }else{
            txtPrecio.setText("0.0");
        }
        return false;
    }

    @Override
    public void pasar(DetalleCompra dc) {
        this.mDc = dc;
    }
}
