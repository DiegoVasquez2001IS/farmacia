package compras;

import compras.model.DetalleCompra;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by user on 1/07/2017.
 */
public class CeldaCompras extends ListCell<DetalleCompra> {
    private Node layout;
    private CeldaComprasController mController;

    public CeldaCompras() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("compras/celda_compras.fxml"));
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
            addContent(getItem());
            mController.pasar(getItem());
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
        mController.setSubTotal(dc.getPrecioCompra(),dc.getCantidad());
        setGraphic(layout);
    }
}
