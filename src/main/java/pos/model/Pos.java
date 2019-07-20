package pos.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 4/05/2017.
 */
public class Pos {

    private SimpleLongProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleFloatProperty preVenta;
    private SimpleStringProperty fechaVencimiento;
    private SimpleIntegerProperty stock;
    private SimpleStringProperty colocacion;
    private SimpleStringProperty dolencia;
    private SimpleStringProperty proveedor;
    private SimpleStringProperty presentacion;

    public Pos(long codigo, String nombre,float preVenta, String fechaVencimiento, int stock, String colocacion,
               String dolencia, String proveedor, String presentacion){
        this.codigo = new SimpleLongProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.preVenta = new SimpleFloatProperty(preVenta);
        this.fechaVencimiento = new SimpleStringProperty(fechaVencimiento);
        this.stock = new SimpleIntegerProperty(stock);
        this.colocacion = new SimpleStringProperty(colocacion);
        this.dolencia = new SimpleStringProperty(dolencia);
        this.proveedor = new SimpleStringProperty(proveedor);
        this.presentacion = new SimpleStringProperty(presentacion);
    }

    public long getCodigo() {
        return codigo.get();
    }

    public SimpleLongProperty codigoProperty() {
        return codigo;
    }

    public void setCodigo(long codigo) {
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

    public float getPreVenta() {
        return preVenta.get();
    }

    public SimpleFloatProperty preVentaProperty() {
        return preVenta;
    }

    public void setPreVenta(float preVenta) {
        this.preVenta.set(preVenta);
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

    public int getStock() {
        return stock.get();
    }

    public SimpleIntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
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
