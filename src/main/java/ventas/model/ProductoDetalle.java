package ventas.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 23/06/2017.
 */
public class ProductoDetalle {
    private SimpleIntegerProperty idDetalle;
    private SimpleIntegerProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty precio;
    private SimpleFloatProperty descuento;
    private SimpleFloatProperty importe;

    public ProductoDetalle(int idDetalle,int idProducto,String nomProducto,int cantidad,
                           float precio,float descuento,float importe){
        this.idDetalle = new SimpleIntegerProperty(idDetalle);
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nomProducto);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleFloatProperty(precio);
        this.descuento = new SimpleFloatProperty(descuento);
        this.importe = new SimpleFloatProperty(importe);
    }

    public int getIdDetalle() {
        return idDetalle.get();
    }

    public SimpleIntegerProperty idDetalleProperty() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle.set(idDetalle);
    }

    public int getIdProducto() {
        return idProducto.get();
    }

    public SimpleIntegerProperty idProductoProperty() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto.set(idProducto);
    }

    public String getNomProducto() {
        return nomProducto.get();
    }

    public SimpleStringProperty nomProductoProperty() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto.set(nomProducto);
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

    public float getPrecio() {
        return precio.get();
    }

    public SimpleFloatProperty precioProperty() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio.set(precio);
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
