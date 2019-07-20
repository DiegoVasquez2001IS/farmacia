package clientes.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 28/03/2017.
 */
public class Cliente {

    private SimpleIntegerProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleStringProperty apellidos;
    private SimpleStringProperty direccion;
    private SimpleStringProperty sexo;
    private SimpleStringProperty dpi;
    private SimpleStringProperty nit;
    private SimpleIntegerProperty telefono;
    private SimpleIntegerProperty celular;

    public Cliente(int codigo, String nombre,String apellidos, String direccion, String sexo, String dpi, String nit, int telefono, int celular) {
        this.codigo  = new SimpleIntegerProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);
        this.direccion = new SimpleStringProperty(direccion);
        this.sexo = new SimpleStringProperty(sexo);
        this.dpi = new SimpleStringProperty(dpi);
        this.nit = new SimpleStringProperty(nit);
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

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public String getSexo() {
        return sexo.get();
    }

    public void setSexo(String sexo) {
        this.sexo.set(sexo);
    }

    public String getDpi() {
        return dpi.get();
    }

    public void setDpi(String dpi) {
        this.dpi.set(dpi);
    }

    public String getNit() {
        return nit.get();
    }

    public void setNit(String nit) {
        this.nit.set(nit);
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
