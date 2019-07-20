package pos.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 19/05/2017.
 */
public class  ProductosListaCompras {

    private SimpleLongProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleFloatProperty precio;
    private SimpleIntegerProperty stock;
    private SimpleIntegerProperty cantidad;
    private SimpleFloatProperty descuento;
    private SimpleFloatProperty subtotal;

    public ProductosListaCompras(long codigo,String nombre, float precio, int stock, int cantidad,float descuento, float subtotal){
        this.codigo = new SimpleLongProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precio = new SimpleFloatProperty(precio);
        this.stock = new SimpleIntegerProperty(stock);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.descuento = new SimpleFloatProperty(descuento);
        this.subtotal = new SimpleFloatProperty(subtotal);
    }

    public long getCodigo() {
        return codigo.get();
    }

    public SimpleLongProperty codigoProperty() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo.set(codigo);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
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

    public int getStock() {
        return stock.get();
    }

    public SimpleIntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
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

    public float getDescuento() {
        return descuento.get();
    }

    public SimpleFloatProperty descuentoProperty() {
        return descuento;
    }

    public void setDescuento(float descuento) {
        this.descuento.set(descuento);
    }

    public float getSubtotal() {
        return subtotal.get();
    }

    public SimpleFloatProperty subtotalProperty() {
        return subtotal;
    }

    public void setSubtotal(float subtotal) {
        this.subtotal.set(subtotal);
    }
}
