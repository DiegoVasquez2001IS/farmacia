package resumen.productos.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 4/06/2017.
 */
public class ResumenProducto {

    private SimpleLongProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleStringProperty vencimiento;
    private SimpleIntegerProperty stock;
    private SimpleFloatProperty precioVenta;
    private SimpleStringProperty nomProveedor;

    public ResumenProducto(long idProducto, String nomProducto, String vencimiento, int stock,
                           float precioVenta, String proveedor) {
        this.idProducto = new SimpleLongProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nomProducto);
        this.vencimiento = new SimpleStringProperty(vencimiento);
        this.stock = new SimpleIntegerProperty(stock);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.nomProveedor = new SimpleStringProperty(proveedor);
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

    public String getVencimiento() {
        return vencimiento.get();
    }

    public SimpleStringProperty vencimientoProperty() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento.set(vencimiento);
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

    public float getPrecioVenta() {
        return precioVenta.get();
    }

    public SimpleFloatProperty precioVentaProperty() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta.set(precioVenta);
    }

    public String getNomProveedor() {
        return nomProveedor.get();
    }

    public SimpleStringProperty nomProveedorProperty() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor.set(nomProveedor);
    }
}
