package pos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import pos.model.ProductosListaCompras;

import java.io.IOException;

/**
 * Created by user on 9/02/2017.
 */
public class PosCell extends ListCell<ProductosListaCompras>{

    private Node graphic;
    private CeldaController mController;

    public PosCell(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pos/celda.fxml"));
        try {
            graphic = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();
    }

    @Override
    protected void updateItem(ProductosListaCompras item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            clearContent();
        }else{
            addContent(item);
            mController.pasar(getItem());
        }
    }

    private void clearContent(){
        setGraphic(null);
    }

    private void addContent(ProductosListaCompras pos){
        setText(null);
        mController.setAncho(getListView().getWidth()-16);
        mController.setNombre(pos.getNombre());
        mController.setPrecio(pos.getPrecio() + "");
        mController.setStock(pos.getStock());
        mController.setCantidad(getItem().getCantidad());
        mController.setDescuento(getItem().getDescuento()+"");
        mController.setTotal(getItem().getSubtotal()+"");
        setGraphic(graphic);
    }

}
