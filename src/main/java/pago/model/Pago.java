package pago.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 27/05/2017.
 */
public class Pago {
    private SimpleIntegerProperty idPago;
    private SimpleStringProperty pago;

    public Pago(int idPago, String pago){
        this.idPago = new SimpleIntegerProperty(idPago);
        this.pago = new SimpleStringProperty(pago);
    }

    public int getIdPago() {
        return idPago.get();
    }

    public SimpleIntegerProperty idPagoProperty() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago.set(idPago);
    }

    public String getPago() {
        return pago.get();
    }

    public SimpleStringProperty pagoProperty() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago.set(pago);
    }
}
