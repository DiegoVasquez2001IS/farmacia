package productos.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 * Created by user on 28/03/2017.
 */
public class Producto {
    private SimpleLongProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleFloatProperty precioCompra;
    private SimpleFloatProperty precioVenta;
    private SimpleStringProperty fechaVencimiento;
    private SimpleIntegerProperty stock;
    private SimpleIntegerProperty idProveedor;
    private SimpleIntegerProperty idPresentacion;
    private SimpleIntegerProperty idDolencia;
    private SimpleIntegerProperty idColocacion;

    public Producto(long codigo, String nombre, float precioCompra, float precioVenta,String fechaVencimiento,
                    int stock,int idProveedor,int idPresentacion,int idDolencia, int idColocacion){
        this.codigo = new SimpleLongProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
        this.stock = new SimpleIntegerProperty(stock);
        this.idProveedor = new SimpleIntegerProperty(idProveedor);
        this.idPresentacion = new SimpleIntegerProperty(idPresentacion);
        this.idDolencia = new SimpleIntegerProperty(idDolencia);
        this.idColocacion = new SimpleIntegerProperty(idColocacion);
    }

    public long getCodigo() {
        return codigo.get();
    }

    public void setCodigo(long codigo) {
        this.codigo.set(codigo);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public float getPrecioCompra() {
        return precioCompra.get();
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra.set(precioCompra);
    }

    public float getPrecioVenta() {
        return precioVenta.get();
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta.set(precioVenta);
    }

    public String getFechaVencimiento() {
        return fechaVencimiento.get();
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento.set(fechaVencimiento);
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public int getIdProveedor() {
        return idProveedor.get();
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor.set(idProveedor);
    }

    public int getIdPresentacion() {
        return idPresentacion.get();
    }

    public void setIdPresentacion(int idPresentacion) {
        this.idPresentacion.set(idPresentacion);
    }

    public int getIdDolencia() {
        return idDolencia.get();
    }

    public void setIdDolencia(int idDolencia) {
        this.idDolencia.set(idDolencia);
    }

    public int getIdColocacion() {
        return idColocacion.get();
    }

    public void setIdColocacion(int idColocacion) {
        this.idColocacion.set(idColocacion);
    }
}
