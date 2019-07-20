package ventas.anulacion;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 26/06/2017.
 */
public class Anulacion {

    private SimpleIntegerProperty idOrden;
    private SimpleStringProperty motivo;

    public Anulacion(int idOrden, String motivo){
        this.idOrden = new SimpleIntegerProperty(idOrden);
        this.motivo = new SimpleStringProperty(motivo);
    }

    public int getIdOrden() {
        return idOrden.get();
    }

    public SimpleIntegerProperty idOrdenProperty() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden.set(idOrden);
    }

    public String getMotivo() {
        return motivo.get();
    }

    public SimpleStringProperty motivoProperty() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo.set(motivo);
    }
}
