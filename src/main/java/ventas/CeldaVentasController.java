package ventas;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import productos.model.ProductosDAO;
import ventas.model.DetalleOPedido;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 23/06/2017.
 */
public class CeldaVentasController implements Initializable, PasarDopedido{

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

    private DetalleOPedido mDetalle;

    ProductosDAO productosDAO = new ProductosDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtCantidad.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if(validarCantidad(txtCantidad.getText())) {
                    if(Integer.parseInt(txtCantidad.getText()) <= productosDAO.getStockProducto(mDetalle.getIdProducto()+"")){
                        for (int i = 0; i < VentasController.listaDetalles.size(); i++) {
                            DetalleOPedido dop = VentasController.listaDetalles.get(i);
                            if (dop.getNumDetalle() == mDetalle.getNumDetalle()) {
                                mDetalle.setCantidad(Integer.parseInt(txtCantidad.getText()));
                                float aux = mDetalle.getPrecioVenta() * mDetalle.getCantidad();
                                float subTotal = aux - (aux * (mDetalle.getDescuento() / 100));
                                mDetalle.setImporte(getDosDecimales(subTotal));
                                VentasController.listaDetalles.set(i,mDetalle);
                                break;
                            }
                        }
                    }

                }
            }
        }));

        txtDescuento.setOnKeyPressed((event -> {
            if(event.getCode() == KeyCode.ENTER){
                if (validarDescuento(txtDescuento.getText())) {
                    for (int i = 0; i < VentasController.listaDetalles.size(); i++) {
                        DetalleOPedido dop = VentasController.listaDetalles.get(i);
                        if (dop.getNumDetalle() == mDetalle.getNumDetalle()) {
                            dop.setDescuento(Float.parseFloat(txtDescuento.getText()));
                            float aux = dop.getPrecioVenta() * dop.getCantidad();
                            float subTotal = aux - (aux * (dop.getDescuento() / 100));
                            dop.setImporte(getDosDecimales(subTotal));
                            VentasController.listaDetalles.set(i, dop);
                            break;
                        }
                    }
                }
            }
        }));
    }

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

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
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

    public void setAncho(double ancho){
        rootCelda.setPrefWidth(ancho);
    }


    @Override
    public void pasar(DetalleOPedido dop) {
        if(dop !=null){
            mDetalle = dop;
        }
    }
}
