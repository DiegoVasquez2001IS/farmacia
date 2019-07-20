package ventas.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 23/06/2017.
 */
public class DetalleOPedido {
    private SimpleIntegerProperty numDetalle;
    private SimpleIntegerProperty numOrdenPedido;
    private SimpleLongProperty idProducto;
    private SimpleStringProperty nomPro;
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty precioVenta;
    private SimpleFloatProperty descuento;
    private SimpleFloatProperty importe;

    public DetalleOPedido(int numDetalle,int numOrden,long idProducto,String nomProducto,
                          int cantidad,float precioVenta,float descuento,float importe){
        this.numDetalle = new SimpleIntegerProperty(numDetalle);
        this.numOrdenPedido = new SimpleIntegerProperty(numOrden);
        this.idProducto = new SimpleLongProperty(idProducto);
        this.nomPro = new SimpleStringProperty(nomProducto);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.descuento = new SimpleFloatProperty(descuento);
        this.importe = new SimpleFloatProperty(importe);
    }

    public int getNumDetalle() {
        return numDetalle.get();
    }

    public SimpleIntegerProperty numDetalleProperty() {
        return numDetalle;
    }

    public void setNumDetalle(int numDetalle) {
        this.numDetalle.set(numDetalle);
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

    public long getIdProducto() {
        return idProducto.get();
    }

    public SimpleLongProperty idProductoProperty() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto.set(idProducto);
    }

    public String getNomPro() {
        return nomPro.get();
    }

    public SimpleStringProperty nomProProperty() {
        return nomPro;
    }

    public void setNomPro(String nomPro) {
        this.nomPro.set(nomPro);
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public float getPrecioVenta() {
        return precioVenta.get();
    }

    public SimpleFloatProperty precioVentaProperty() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta.set(precioVenta);
    }

    public float getDescuento() {
        return descuento.get();
    }

    public SimpleFloatProperty descuentoProperty() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento.set(descuento);
    }

    public float getImporte() {
        return importe.get();
    }

    public SimpleFloatProperty importeProperty() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe.set(importe);
    }
}
