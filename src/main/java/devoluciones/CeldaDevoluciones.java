package devoluciones;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by user on 3/07/2017.
 */
public class CeldaDevoluciones extends ListCell<DetalleDevolucion> {

    private Node layout;
    private CeldaDevolucionesController mController;

    public CeldaDevoluciones() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("devoluciones/celda_devoluciones.fxml"));
        try {
            layout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();
    }

    @Override
    protected void updateItem(DetalleDevolucion item, boolean empty) {
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

    private void addContent(DetalleDevolucion dd){
        setText(null);
        mController.setAncho(getListView().getWidth()-16);
        mController.setNombre(dd.getNomProducto());
        mController.setPrecio(String.valueOf(dd.getPrecioCompra()));
        mController.setComprado(String.valueOf(dd.getStockComprado()));
        mController.setCantidadDevuelta(dd.getStockDevuelto()+"");
        mController.setSubtotal(dd.getTotalDevuelto()+"");
        setGraphic(layout);
    }
}
