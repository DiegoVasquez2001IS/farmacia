package compras.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 30/06/2017.
 */
public class DetalleCompra {
    private SimpleIntegerProperty codDetalle;
    private SimpleIntegerProperty idCompra;
    private SimpleLongProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleFloatProperty precioCompra;
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty subTotal;
    private SimpleStringProperty fechaVencimiento;

    public DetalleCompra(int codDetalle, int idCompra, long idProducto,String nomProducto,
                         float precioCompra,int cantidad,float subTotal,String fechaVencimiento) {
        this.codDetalle = new SimpleIntegerProperty(codDetalle);
        this.idCompra = new SimpleIntegerProperty(idCompra);
        this.idProducto = new SimpleLongProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nomProducto);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.subTotal = new SimpleFloatProperty(subTotal);
        this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
    }

    public int getCodDetalle() {
        return codDetalle.get();
    }

    public SimpleIntegerProperty codDetalleProperty() {
        return codDetalle;
    }

    public void setCodDetalle(int codDetalle) {
        this.codDetalle.set(codDetalle);
    }

    public int getIdCompra() {
        return idCompra.get();
    }

    public SimpleIntegerProperty idCompraProperty() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra.set(idCompra);
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

    public String getNomProducto() {
        return nomProducto.get();
    }

    public SimpleStringProperty nomProductoProperty() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto.set(nomProducto);
    }

    public float getPrecioCompra() {
        return precioCompra.get();
    }

    public SimpleFloatProperty precioCompraProperty() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra.set(precioCompra);
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

    public float getSubTotal() {
        return subTotal.get();
    }

    public SimpleFloatProperty subTotalProperty() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal.set(subTotal);
    }

    public String getFechaVencimiento() {
        return fechaVencimiento.get();
    }

    public SimpleStringProperty fechaVencimientoProperty() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento.set(fechaVencimiento);
    }
}
