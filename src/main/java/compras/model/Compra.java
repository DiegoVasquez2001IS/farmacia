package compras.model;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 30/06/2017.
 */
public class Compra {
    private SimpleIntegerProperty numCompra;
    private SimpleStringProperty numFactura;
    private SimpleStringProperty serie;
    private SimpleIntegerProperty idProveedor;
    private SimpleStringProperty nomProveedor;
    private SimpleStringProperty fechaCompra;
    private SimpleStringProperty fechaEntrega;
    private SimpleIntegerProperty idEmpleado;
    private SimpleStringProperty nomEmpleado;
    private SimpleStringProperty observaciones;
    private SimpleStringProperty condiciones;
    private SimpleFloatProperty total;
    private SimpleStringProperty estatus;

    public Compra(int numCompra,String numFactura,String serie,int idProveedor,
                  String nomProveedor, String fechaCompra, String fechaEntrega,
                  int idEmpleado,String nomEmpleado,String observaciones,
                  String condiciones,float total,String estatus) {
        this.numCompra = new SimpleIntegerProperty(numCompra);
        this.numFactura = new SimpleStringProperty(numFactura);
        this.serie = new SimpleStringProperty(serie);
        this.idProveedor = new SimpleIntegerProperty(idProveedor);
        this.nomProveedor = new SimpleStringProperty(nomProveedor);
        this.fechaCompra = new SimpleStringProperty(fechaCompra);
        this.fechaEntrega = new SimpleStringProperty(fechaEntrega);
        this.idEmpleado = new SimpleIntegerProperty(idEmpleado);
        this.nomEmpleado = new SimpleStringProperty(nomEmpleado);
        this.observaciones = new SimpleStringProperty(observaciones);
        this.condiciones = new SimpleStringProperty(condiciones);
        this.total = new SimpleFloatProperty(total);
        this.estatus = new SimpleStringProperty(estatus);
    }

    public int getNumCompra() {
        return numCompra.get();
    }

    public SimpleIntegerProperty numCompraProperty() {
        return numCompra;
    }

    public void setNumCompra(int numCompra) {
        this.numCompra.set(numCompra);
    }

    public String getNumFactura() {
        return numFactura.get();
    }

    public SimpleStringProperty numFacturaProperty() {
        return numFactura;
    }

    public void setNumFactura(String numFactura) {
        this.numFactura.set(numFactura);
    }

    public String getSerie() {
        return serie.get();
    }

    public SimpleStringProperty serieProperty() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie.set(serie);
    }

    public int getIdProveedor() {
        return idProveedor.get();
    }

    public SimpleIntegerProperty idProveedorProperty() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor.set(idProveedor);
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

    public String getFechaCompra() {
        return fechaCompra.get();
    }

    public SimpleStringProperty fechaCompraProperty() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra.set(fechaCompra);
    }

    public String getFechaEntrega() {
        return fechaEntrega.get();
    }

    public SimpleStringProperty fechaEntregaProperty() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega.set(fechaEntrega);
    }

    public int getIdEmpleado() {
        return idEmpleado.get();
    }

    public SimpleIntegerProperty idEmpleadoProperty() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado.set(idEmpleado);
    }

    public String getNomEmpleado() {
        return nomEmpleado.get();
    }

    public SimpleStringProperty nomEmpleadoProperty() {
        return nomEmpleado;
    }

    public void setNomEmpleado(String nomEmpleado) {
        this.nomEmpleado.set(nomEmpleado);
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public SimpleStringProperty observacionesProperty() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    public String getCondiciones() {
        return condiciones.get();
    }

    public SimpleStringProperty condicionesProperty() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones.set(condiciones);
    }

    public float getTotal() {
        return total.get();
    }

    public SimpleFloatProperty totalProperty() {
        return total;
    }

    public void setTotal(float total) {
        this.total.set(total);
    }

    public String getEstatus() {
        return estatus.get();
    }

    public SimpleStringProperty estatusProperty() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus.set(estatus);
    }
}
