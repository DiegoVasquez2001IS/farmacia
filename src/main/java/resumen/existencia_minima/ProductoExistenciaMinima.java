package resumen.existencia_minima;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 8/07/2017.
 */
public class ProductoExistenciaMinima {
    private SimpleIntegerProperty idProducto;
    private SimpleStringProperty nomProducto;
    private SimpleFloatProperty precioCompra;
    private SimpleFloatProperty precioVenta;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty fecha;
    private SimpleStringProperty nomColocacion;
    private SimpleStringProperty nomDolencia;
    private SimpleStringProperty nomProveedor;
    private SimpleStringProperty nomPresentacion;

    public ProductoExistenciaMinima(int idProducto,String nomProducto, float precioCompra,
                                    float precioVenta,int stock,String fechaVencimiento,String colocacion,
                                    String dolencia,String proveedor, String presentacion){
        this.idProducto = new SimpleIntegerProperty(idProducto);
        this.nomProducto = new SimpleStringProperty(nomProducto);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.stock = new SimpleIntegerProperty(stock);
        this.fecha = new SimpleStringProperty(fechaVencimiento);
        this.nomColocacion = new SimpleStringProperty(colocacion);
        this.nomDolencia = new SimpleStringProperty(dolencia);
        this.nomProveedor = new SimpleStringProperty(proveedor);
        this.nomPresentacion = new SimpleStringProperty(presentacion);
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

    public String getFecha() {
        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha.set(fecha);
    }

    public String getNomColocacion() {
        return nomColocacion.get();
    }

    public SimpleStringProperty nomColocacionProperty() {
        return nomColocacion;
    }

    public void setNomColocacion(String nomColocacion) {
        this.nomColocacion.set(nomColocacion);
    }

    public String getNomDolencia() {
        return nomDolencia.get();
    }

    public SimpleStringProperty nomDolenciaProperty() {
        return nomDolencia;
    }

    public void setNomDolencia(String nomDolencia) {
        this.nomDolencia.set(nomDolencia);
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

    public String getNomPresentacion() {
        return nomPresentacion.get();
    }

    public SimpleStringProperty nomPresentacionProperty() {
        return nomPresentacion;
    }

    public void setNomPresentacion(String nomPresentacion) {
        this.nomPresentacion.set(nomPresentacion);
    }
}
