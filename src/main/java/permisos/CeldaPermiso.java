package permisos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

/**
 * Created by user on 5/07/2017.
 */
public class CeldaPermiso extends ListCell<Permiso> {

    private Node layout;
    private CeldaPermisoController mController;

    public CeldaPermiso() {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("permisos/celda_permiso.fxml"));
        try {
            layout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mController = loader.getController();
    }

    @Override
    protected void updateItem(Permiso item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            clearContent();
        }else {
            addContent(getItem());
            mController.pasar(getItem());
        }
    }

    private void clearContent(){
        setGraphic(null);
    }

    private void addContent(Permiso dc){
        setText(null);
        mController.setAncho(getListView().getWidth()-32);
        mController.setNombre(dc.getNomModulo());
        mController.setLectura(dc.getLectura());
        mController.setEscritura(dc.getEscritura());
        setGraphic(layout);
    }
}
