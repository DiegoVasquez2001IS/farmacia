package resumen.clientes.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 4/06/2017.
 */
public class ResumenCliente {
    private SimpleIntegerProperty idCliente;
    private SimpleStringProperty nombreCliente;
    private SimpleFloatProperty total;

    public ResumenCliente(int idCliente, String nombreCliente, float total) {
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.nombreCliente = new SimpleStringProperty(nombreCliente);
        this.total = new SimpleFloatProperty(total);
    }

    public int getIdCliente() {
        return idCliente.get();
    }

    public SimpleIntegerProperty idClienteProperty() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }

    public String getNombreCliente() {
        return nombreCliente.get();
    }

    public SimpleStringProperty nombreClienteProperty() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente.set(nombreCliente);
    }

    public float getTotal() {
        return total.get();
    }

    public SimpleFloatProperty totalProperty() {
        return total;
    }

    public void setTotal(float total) {
        this.total.set(total);
    }
}
