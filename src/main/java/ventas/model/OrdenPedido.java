package ventas.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 23/06/2017.
 */
public class OrdenPedido {
    private SimpleIntegerProperty numOrdenPedido;
    private SimpleStringProperty fecha;
    private SimpleIntegerProperty idCliente;
    private SimpleStringProperty nomCliente;
    private SimpleIntegerProperty codEmpleado;
    private SimpleStringProperty nomEmpleado;
    private SimpleFloatProperty descuentoCliente;
    private SimpleFloatProperty total;
    private SimpleIntegerProperty codTipoPago;
    private SimpleStringProperty estatus;
    private SimpleStringProperty motivoAnulacion;

    public OrdenPedido(int numOrden,String fecha,int idCliente,String nomCliente,
                       int codEmpleado,String nomEmpleado,float descuentoCliente,
                       float total,int codTipoPago,String estatus,String motivoAnulacion){
        this.numOrdenPedido = new SimpleIntegerProperty(numOrden);
        this.fecha = new SimpleStringProperty(fecha);
        this.idCliente = new SimpleIntegerProperty(idCliente);
        this.nomCliente = new SimpleStringProperty(nomCliente);
        this.codEmpleado = new SimpleIntegerProperty(codEmpleado);
        this.nomEmpleado = new SimpleStringProperty(nomEmpleado);
        this.descuentoCliente = new SimpleFloatProperty(descuentoCliente);
        this.total = new SimpleFloatProperty(total);
        this.codTipoPago = new SimpleIntegerProperty(codTipoPago);
        this.estatus = new SimpleStringProperty(estatus);
        this.motivoAnulacion = new SimpleStringProperty(motivoAnulacion);
    }

    public int getNumOrdenPedido() {
        return numOrdenPedido.get();
    }

    public SimpleIntegerProperty numOrdenPedidoProperty() {
        return numOrdenPedido;
    }

    public void setNumOrdenPedido(int numOrdenPedido) {
        this.numOrdenPedido.set(numOrdenPedido);
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

    public int getIdCliente() {
        return idCliente.get();
    }

    public SimpleIntegerProperty idClienteProperty() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente.set(idCliente);
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

    public int getCodEmpleado() {
        return codEmpleado.get();
    }

    public SimpleIntegerProperty codEmpleadoProperty() {
        return codEmpleado;
    }

    public void setCodEmpleado(int codEmpleado) {
        this.codEmpleado.set(codEmpleado);
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

    public float getDescuentoCliente() {
        return descuentoCliente.get();
    }

    public SimpleFloatProperty descuentoClienteProperty() {
        return descuentoCliente;
    }

    public void setDescuentoCliente(float descuentoCliente) {
        this.descuentoCliente.set(descuentoCliente);
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

    public int getCodTipoPago() {
        return codTipoPago.get();
    }

    public SimpleIntegerProperty codTipoPagoProperty() {
        return codTipoPago;
    }

    public void setCodTipoPago(int codTipoPago) {
        this.codTipoPago.set(codTipoPago);
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

    public String getMotivoAnulacion() {
        return motivoAnulacion.get();
    }

    public SimpleStringProperty motivoAnulacionProperty() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion.set(motivoAnulacion);
    }
}
