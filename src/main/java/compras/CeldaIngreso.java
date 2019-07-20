package compras;

import compras.model.DetalleCompra;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by user on 3/07/2017.
 */
public class CeldaIngreso extends ListCell<DetalleCompra> {

    private Node layout;
    private CeldaIngresoController mController;

    public CeldaIngreso() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("compras/celda_ingreso.fxml"));
        try {
            layout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();
    }

    @Override
    protected void updateItem(DetalleCompra item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            clearContent();
        }else{
            addContent(item);
            mController.pasar(item);
        }
    }

    private void clearContent(){
        setGraphic(null);
    }

    private void addContent(DetalleCompra dc){
        setText(null);
        mController.setAncho(getListView().getWidth()-16);
        mController.setNombre(dc.getNomProducto());
        mController.setPrecio(String.valueOf(dc.getPrecioCompra()));
        mController.setCantidad(String.valueOf(dc.getCantidad()));
        mController.setSubtotal(String.valueOf(dc.getSubTotal()));
        mController.setFechaVencimiento(dc.getFechaVencimiento());
        setGraphic(layout);
    }


}
