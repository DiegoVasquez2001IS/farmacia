package devoluciones;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 5/07/2017.
 */
public class DetalleDevolucion {
    private SimpleIntegerProperty idDetalle;
    private SimpleIntegerProperty idCompra;
    private SimpleIntegerProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleFloatProperty precioCompra;
    private SimpleIntegerProperty stockComprado;
    private SimpleIntegerProperty stockDevuelto;
    private SimpleFloatProperty totalDevuelto;

    public DetalleDevolucion(int idDetalle,int idCompra,int idProducto,
                             String nomProducto,float precioCompra,int stockComprado,int stockDevuelto,float totalDevuelto) {
        this.idDetalle = new SimpleIntegerProperty(idDetalle);
        this.idCompra = new SimpleIntegerProperty(idCompra);
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nomProducto);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
        this.stockComprado = new SimpleIntegerProperty(stockComprado);
        this.stockDevuelto = new SimpleIntegerProperty(stockDevuelto);
        this.totalDevuelto = new SimpleFloatProperty(totalDevuelto);
    }

    public int getStockComprado() {
        return stockComprado.get();
    }

    public SimpleIntegerProperty stockCompradoProperty() {
        return stockComprado;
    }

    public void setStockComprado(int stockComprado) {
        this.stockComprado.set(stockComprado);
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

    public int getIdCompra() {
        return idCompra.get();
    }

    public SimpleIntegerProperty idCompraProperty() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra.set(idCompra);
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

    public float getPrecioCompra() {
        return precioCompra.get();
    }

    public SimpleFloatProperty precioCompraProperty() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra.set(precioCompra);
    }

    public int getStockDevuelto() {
        return stockDevuelto.get();
    }

    public SimpleIntegerProperty stockDevueltoProperty() {
        return stockDevuelto;
    }

    public void setStockDevuelto(int stockDevuelto) {
        this.stockDevuelto.set(stockDevuelto);
    }

    public float getTotalDevuelto() {
        return totalDevuelto.get();
    }

    public SimpleFloatProperty totalDevueltoProperty() {
        return totalDevuelto;
    }

    public void setTotalDevuelto(float totalDevuelto) {
        this.totalDevuelto.set(totalDevuelto);
    }
}
