package pos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import pos.model.ProductosListaCompras;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 5/05/2017.
 */
public class CeldaController implements Initializable,PasarProductosListaCompra, PasarCantidad{

    @FXML
    private GridPane rootCelda;

    @FXML
    private Label txtNombre;

    @FXML
    private Label txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtDescuento;

    @FXML
    private Label txtTotal;

    private int mStock = 0;

    private ProductosListaCompras mPlc;

    public void setNombre(String texto){
        txtNombre.setText(texto);
    }

    public void setPrecio(String texto){
        txtPrecio.setText(texto);
    }

    public void setCantidad(int texto){
        txtCantidad.setText(String.valueOf(texto));
    }

    public void setTotal(String texto){
        txtTotal.setText(texto);
    }

    public void setDescuento(String texto){ txtDescuento.setText(texto); }

    public void setStock(int stock){
        mStock = stock;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCantidad.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(validarCantidad(txtCantidad.getText())) {
                    for (int i = 0; i < PosController.listaCompras.size(); i++) {
                        ProductosListaCompras plc = PosController.listaCompras.get(i);
                        if (plc.getCodigo() == mPlc.getCodigo()) {
                            plc.setCantidad(Integer.parseInt(txtCantidad.getText()));
                            float aux = plc.getPrecio() * plc.getCantidad();
                            float subTotal = aux - (aux * (plc.getDescuento() / 100));
                            plc.setSubtotal(getDosDecimales(subTotal));
                            PosController.listaCompras.set(i, plc);
                            break;
                        }
                    }
                }
            }
        }));

        txtDescuento.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if (validarDescuento(txtDescuento.getText())) {
                    for (int i = 0; i < PosController.listaCompras.size(); i++) {
                        ProductosListaCompras plc = PosController.listaCompras.get(i);
                        if (plc.getCodigo() == mPlc.getCodigo()) {
                            plc.setDescuento(Float.parseFloat(txtDescuento.getText()));
                            float aux = plc.getPrecio() * plc.getCantidad();
                            float subTotal = aux - (aux * (plc.getDescuento() / 100));
                            plc.setSubtotal(getDosDecimales(subTotal));
                            PosController.listaCompras.set(i, plc);
                            break;
                        }
                    }
                }
            }
        }));


        txtCantidad.textProperty().addListener(((observable, oldValue, newValue )-> {
            if(newValue != null && !newValue.isEmpty()){
                if(newValue.matches("\\d+")){
                    if(Integer.parseInt(newValue) >= 0){
                        if(Integer.parseInt(newValue) > mStock){
                            rootCelda.setStyle("-fx-background-color: #FF8A80;");
                        }else{
                            rootCelda.setStyle("");
                        }
                    }
                }
            }
        }));
    }

    private boolean validarCantidad(String numero){
        if (!numero.isEmpty()){
            if (!numero.matches("\\d*")) {
                txtCantidad.setText(numero.replaceAll("[^\\d]", ""));
            }else {
                if(Integer.parseInt(numero) <= 0){
                    txtCantidad.setText("1");
                }else if(Integer.parseInt(numero) > mStock) {
                    txtCantidad.setText(mStock+"");
                }else{
                    return true;
                }
            }
        }else{
            txtCantidad.setText("1");
        }
        return false;
    }

    public void setAncho(double ancho){
        rootCelda.setPrefWidth(ancho);
    }

    private boolean validarDescuento(String numero){
        if(!numero.isEmpty()){
            if(!numero.matches("\\d+.?\\d*")){
                txtDescuento.setText(numero.replaceAll(".",""));
            }else{
                if(Float.parseFloat(numero) < 0f){
                    txtDescuento.setText("0");
                }else{
                    return true;
                }
            }
        }else{
            txtDescuento.setText("0.0");
        }
        return false;
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }

    @Override
    public void pasar(ProductosListaCompras plc) {
        if(plc != null){
            mPlc = plc;
        }
    }

    @Override
    public void pasar(int cantidad) {
        setCantidad(cantidad);
    }
}
