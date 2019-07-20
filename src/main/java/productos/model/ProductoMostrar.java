package productos.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 5/06/2017.
 */
public class ProductoMostrar {
    private SimpleLongProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleFloatProperty precioCompra;
    private SimpleFloatProperty precioVenta;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty vencimiento;
    private SimpleStringProperty colocacion;
    private SimpleStringProperty dolencia;
    private SimpleStringProperty proveedor;
    private SimpleStringProperty presentacion;

    public ProductoMostrar(long idProducto, String nombre, float precioCompra, float precioVenta, int stock, String vencimiento,
                           String colocacion, String dolencia,String proveedor, String presentacion) {
        this.idProducto = new SimpleLongProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nombre);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.stock = new SimpleIntegerProperty(stock);
        this.vencimiento = new SimpleStringProperty(vencimiento);
        this.colocacion = new SimpleStringProperty(colocacion);
        this.dolencia = new SimpleStringProperty(dolencia);
        this.proveedor = new SimpleStringProperty(proveedor);
        this.presentacion = new SimpleStringProperty(presentacion);
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

    public float getPrecioVenta() {
        return precioVenta.get();
    }

    public SimpleFloatProperty precioVentaProperty() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta.set(precioVenta);
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

    public String getVencimiento() {
        return vencimiento.get();
    }

    public SimpleStringProperty vencimientoProperty() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento.set(vencimiento);
    }

    public String getColocacion() {
        return colocacion.get();
    }

    public SimpleStringProperty colocacionProperty() {
        return colocacion;
    }

    public void setColocacion(String colocacion) {
        this.colocacion.set(colocacion);
    }

    public String getDolencia() {
        return dolencia.get();
    }

    public SimpleStringProperty dolenciaProperty() {
        return dolencia;
    }

    public void setDolencia(String dolencia) {
        this.dolencia.set(dolencia);
    }

    public String getProveedor() {
        return proveedor.get();
    }

    public SimpleStringProperty proveedorProperty() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor.set(proveedor);
    }

    public String getPresentacion() {
        return presentacion.get();
    }

    public SimpleStringProperty presentacionProperty() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion.set(presentacion);
    }
}
