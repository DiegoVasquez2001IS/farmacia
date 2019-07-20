package proveedores.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 28/03/2017.
 */
public class Proveedor {

    private SimpleIntegerProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleStringProperty direccion;
    private SimpleIntegerProperty telefono;
    private SimpleIntegerProperty celular;

    public Proveedor(int codigo, String nombre, String direccion, int telefono, int celular) {
        this.codigo = new SimpleIntegerProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.direccion = new SimpleStringProperty(direccion);
        this.telefono = new SimpleIntegerProperty(telefono);
        this.celular = new SimpleIntegerProperty(celular);
    }

    public int getCodigo() {
        return codigo.get();
    }

    public void setCodigo(int codigo) {
        this.codigo.set(codigo);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public int getTelefono() {
        return telefono.get();
    }

    public void setTelefono(int telefono) {
        this.telefono.set(telefono);
    }

    public int getCelular() {
        return celular.get();
    }

    public void setCelular(int celular) {
        this.celular.set(celular);
    }
}
