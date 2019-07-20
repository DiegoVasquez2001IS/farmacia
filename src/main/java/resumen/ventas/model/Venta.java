package resumen.ventas.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 1/06/2017.
 */
public class Venta {
    private SimpleIntegerProperty idOrdenPedido;
    private SimpleStringProperty fecha;
    private SimpleStringProperty nomCliente;
    private SimpleStringProperty nomEmpleado;
    private SimpleFloatProperty total;
    private SimpleStringProperty tipoPago;
    private SimpleStringProperty estatus;

    public Venta(int idOrdenPedido, String fecha, String nomCliente, String nomEmpleado, float total, String tipoPago,
                 String estatus){
        this.idOrdenPedido = new SimpleIntegerProperty(idOrdenPedido);
        this.fecha = new SimpleStringProperty(fecha);
        this.nomCliente = new SimpleStringProperty(nomCliente);
        this.nomEmpleado = new SimpleStringProperty(nomEmpleado);
        this.total = new SimpleFloatProperty(total);
        this.tipoPago = new SimpleStringProperty(tipoPago);
        this.estatus = new SimpleStringProperty(estatus);
    }

    public int getIdOrdenPedido() {
        return idOrdenPedido.get();
    }

    public SimpleIntegerProperty idOrdenPedidoProperty() {
        return idOrdenPedido;
    }

    public void setIdOrdenPedido(int idOrdenPedido) {
        this.idOrdenPedido.set(idOrdenPedido);
    }

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getNomCliente() {
        return nomCliente.get();
    }

    public SimpleStringProperty nomClienteProperty() {
        return nomCliente;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente.set(nomCliente);
    }

    public String getNomEmpleado() {
        return nomEmpleado.get();
    }

    public SimpleStringProperty nomEmpleadoProperty() {
        return nomEmpleado;
    }

    public void setNomEmpleado(String nomEmpleado) {
        this.nomEmpleado.set(nomEmpleado);
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

    public String getTipoPago() {
        return tipoPago.get();
    }

    public SimpleStringProperty tipoPagoProperty() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago.set(tipoPago);
    }

    public String getEstatus() {
        return estatus.get();
    }

    public SimpleStringProperty estatusProperty() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus.set(estatus);
    }
}
