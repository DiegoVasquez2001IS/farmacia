package devoluciones;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 3/07/2017.
 */
public class Devolucion {
    private SimpleIntegerProperty idDevolucion;
    private SimpleIntegerProperty idCompra;
    private SimpleStringProperty usuario;
    private SimpleStringProperty nomProveedor;
    private SimpleStringProperty motivo;
    private SimpleStringProperty fecha;
    private SimpleFloatProperty totalDevolucion;

    public Devolucion(int idDevolucion,int idCompra,String usuario,String nomProveedor,
                      String motivo, String fecha, float total) {
        this.idDevolucion = new SimpleIntegerProperty(idDevolucion);
        this.idCompra = new SimpleIntegerProperty(idCompra);
        this.usuario = new SimpleStringProperty(usuario);
        this.nomProveedor = new SimpleStringProperty(nomProveedor);
        this.motivo = new SimpleStringProperty(motivo);
        this.fecha = new SimpleStringProperty(fecha);
        this.totalDevolucion = new SimpleFloatProperty(total);
    }

    public int getIdDevolucion() {
        return idDevolucion.get();
    }

    public SimpleIntegerProperty idDevolucionProperty() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion.set(idDevolucion);
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

    public String getUsuario() {
        return usuario.get();
    }

    public SimpleStringProperty usuarioProperty() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario.set(usuario);
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

    public String getMotivo() {
        return motivo.get();
    }

    public SimpleStringProperty motivoProperty() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo.set(motivo);
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

    public float getTotalDevolucion() {
        return totalDevolucion.get();
    }

    public SimpleFloatProperty totalDevolucionProperty() {
        return totalDevolucion;
    }

    public void setTotalDevolucion(float totalDevolucion) {
        this.totalDevolucion.set(totalDevolucion);
    }
}
