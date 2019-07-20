package pos.model;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by user on 27/05/2017.
 */
public class OrdenPedido {

    private SimpleIntegerProperty idOrdenPedido;
    private SimpleIntegerProperty idCliente;
    private SimpleIntegerProperty idEmpleado;
    private SimpleIntegerProperty idTipoPago;

    public OrdenPedido(int idOrden, int idCliente, int idEmpleado, int idTipoPago){
        this.idOrdenPedido = new SimpleIntegerProperty(idOrden);
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.idEmpleado = new SimpleIntegerProperty(idEmpleado);
        this.idTipoPago = new SimpleIntegerProperty(idTipoPago);
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

    public int getIdCliente() {
        return idCliente.get();
    }

    public SimpleIntegerProperty idClienteProperty() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
    }

    public int getIdEmpleado() {
        return idEmpleado.get();
    }

    public SimpleIntegerProperty idEmpleadoProperty() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado.set(idEmpleado);
    }

    public int getIdTipoPago() {
        return idTipoPago.get();
    }

    public SimpleIntegerProperty idTipoPagoProperty() {
        return idTipoPago;
    }

    public void setIdTipoPago(int idTipoPago) {
        this.idTipoPago.set(idTipoPago);
    }
}
