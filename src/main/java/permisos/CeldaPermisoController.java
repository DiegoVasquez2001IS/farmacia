package permisos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by user on 5/07/2017.
 */
public class CeldaPermisoController implements Initializable,PasarPermiso {

    @FXML
    private HBox rootCeldaPermisos;

    @FXML
    private Label lblNombre;

    @FXML
    private CheckBox checkLectura;

    @FXML
    private CheckBox checkEscritura;

    private Permiso mPermiso;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        checkLectura.setOnAction((event -> {
            if(checkLectura.isSelected()){
                for(int i = 0; i< PermisosController.observablePermisos.size(); i++){
                    Permiso permiso = PermisosController.observablePermisos.get(i);
                    if (permiso.getIdPermiso() == mPermiso.getIdPermiso()){
                        mPermiso.setLectura(1);
                        PermisosController.observablePermisos.set(i,mPermiso);
                    }
                }
            }else {
                for(int i = 0; i< PermisosController.observablePermisos.size(); i++){
                    Permiso permiso = PermisosController.observablePermisos.get(i);
                    if (permiso.getIdPermiso() == mPermiso.getIdPermiso()){
                        mPermiso.setLectura(0);
                        PermisosController.observablePermisos.set(i,mPermiso);
                    }
                }
            }
        }));

        checkEscritura.setOnAction((event -> {
            if(checkEscritura.isSelected()){
                for(int i = 0; i< PermisosController.observablePermisos.size(); i++){
                    Permiso permiso = PermisosController.observablePermisos.get(i);
                    if (permiso.getIdPermiso() == mPermiso.getIdPermiso()){
                        mPermiso.setEscritura(1);
                        PermisosController.observablePermisos.set(i,mPermiso);
                    }
                }
            }else {
                for(int i = 0; i< PermisosController.observablePermisos.size(); i++){
                    Permiso permiso = PermisosController.observablePermisos.get(i);
                    if (permiso.getIdPermiso() == mPermiso.getIdPermiso()){
                        mPermiso.setEscritura(0);
                        PermisosController.observablePermisos.set(i,mPermiso);
                    }
                }
            }
        }));
    }

    public void setAncho(double ancho){
        rootCeldaPermisos.setPrefWidth(ancho);
    }

    public void setNombre(String nombre){
        lblNombre.setText(nombre);
    }

    public void setLectura(int lectura){
        checkLectura.setSelected(lectura==1);
    }

    public void setEscritura(int escritura){
        checkEscritura.setSelected(escritura==1);
    }

    @Override
    public void pasar(Permiso permiso) {
        if(permiso != null){
            mPermiso = permiso;
        }
    }
}
