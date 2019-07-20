package empleados.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 28/03/2017.
 */
public class Empleado {

    private SimpleIntegerProperty codigo;
    private SimpleStringProperty nombre;
    private SimpleStringProperty apellidos;
    private SimpleStringProperty direccion;
    private SimpleStringProperty cargo;
    private SimpleIntegerProperty edad;
    private SimpleIntegerProperty telefono;
    private SimpleIntegerProperty celular;
    private SimpleStringProperty ingreso;

    public Empleado(int codigo, String nombre,String apellidos, String direccion, String cargo, int edad, int telefono, int celular, String ingreso) {
        this.codigo = new SimpleIntegerProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);
        this.direccion = new SimpleStringProperty(direccion);
        this.cargo = new SimpleStringProperty(cargo);
        this.edad = new SimpleIntegerProperty(edad);
        this.telefono = new SimpleIntegerProperty(telefono);
        this.celular = new SimpleIntegerProperty(celular);
        this.ingreso = new SimpleStringProperty(ingreso);
    }

    public int getCodigo() {
        return codigo.get();
    }

    public SimpleIntegerProperty codigoProperty() {
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

    public String getApellidos() {
        return apellidos.get();
    }

    public SimpleStringProperty apellidosProperty() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public String getDireccion() {
        return direccion.get();
    }

    public SimpleStringProperty direccionProperty() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public String getCargo() {
        return cargo.get();
    }

    public SimpleStringProperty cargoProperty() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo.set(cargo);
    }

    public int getEdad() {
        return edad.get();
    }

    public SimpleIntegerProperty edadProperty() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad.set(edad);
    }

    public int getTelefono() {
        return telefono.get();
    }

    public SimpleIntegerProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono.set(telefono);
    }

    public int getCelular() {
        return celular.get();
    }

    public SimpleIntegerProperty celularProperty() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular.set(celular);
    }

    public String getIngreso() {
        return ingreso.get();
    }

    public SimpleStringProperty ingresoProperty() {
        return ingreso;
    }

    public void setIngreso(String ingreso) {
        this.ingreso.set(ingreso);
    }

}
