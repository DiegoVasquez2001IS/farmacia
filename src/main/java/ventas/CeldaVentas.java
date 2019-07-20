package ventas;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import ventas.model.DetalleOPedido;

import java.io.IOException;

/**
 * Created by user on 23/06/2017.
 */
public class CeldaVentas extends ListCell<DetalleOPedido> {
    private Node graphic;
    private CeldaVentasController mController;

    public CeldaVentas(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ventas/celda_ventas.fxml"));
        try {
            graphic = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();

    }

    @Override
    protected void updateItem(DetalleOPedido item, boolean empty) {
        super.updateItem(item, empty);
        if(empty)
            clearContent();
        else{
            addContent(getItem());
            mController.pasar(getItem());
        }
    }

    private void clearContent(){
        setGraphic(null);
    }

    private void addContent(DetalleOPedido detalleOPedido){
        setText(null);
        mController.setAncho(getListView().getWidth()-16);
        mController.setNombre(detalleOPedido.getNomPro());
        mController.setPrecio(detalleOPedido.getPrecioVenta()+"");
        mController.setCantidad(detalleOPedido.getCantidad());
        mController.setDescuento(detalleOPedido.getDescuento()+"");
        mController.setTotal(detalleOPedido.getImporte()+"");
        setGraphic(graphic);
    }
}
