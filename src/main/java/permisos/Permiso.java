package permisos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 5/07/2017.
 */
public class Permiso {
    private SimpleIntegerProperty idPermiso;
    private SimpleIntegerProperty idNivelUsuario;
    private SimpleStringProperty nomNivelUsuario;
    private SimpleIntegerProperty idModulo;
    private SimpleStringProperty nomModulo;
    private SimpleIntegerProperty lectura;
    private SimpleIntegerProperty escritura;

    public Permiso(int idPermiso,int idNivelUsuario,String nomNivelUsuario,int idModulo,
                   String nomModulo,int lectura,int escritura) {
        this.idPermiso = new SimpleIntegerProperty(idPermiso);
        this.idNivelUsuario = new SimpleIntegerProperty(idNivelUsuario);
        this.nomNivelUsuario = new SimpleStringProperty(nomNivelUsuario);
        this.idModulo = new SimpleIntegerProperty(idModulo);
        this.nomModulo = new SimpleStringProperty(nomModulo);
        this.lectura = new SimpleIntegerProperty(lectura);
        this.escritura = new SimpleIntegerProperty(escritura);
    }

    public int getIdPermiso() {
        return idPermiso.get();
    }

    public SimpleIntegerProperty idPermisoProperty() {
        return idPermiso;
    }

    public void setIdPermiso(int idPermiso) {
        this.idPermiso.set(idPermiso);
    }

    public int getIdNivelUsuario() {
        return idNivelUsuario.get();
    }

    public SimpleIntegerProperty idNivelUsuarioProperty() {
        return idNivelUsuario;
    }

    public void setIdNivelUsuario(int idNivelUsuario) {
        this.idNivelUsuario.set(idNivelUsuario);
    }

    public String getNomNivelUsuario() {
        return nomNivelUsuario.get();
    }

    public SimpleStringProperty nomNivelUsuarioProperty() {
        return nomNivelUsuario;
    }

    public void setNomNivelUsuario(String nomNivelUsuario) {
        this.nomNivelUsuario.set(nomNivelUsuario);
    }

    public int getIdModulo() {
        return idModulo.get();
    }

    public SimpleIntegerProperty idModuloProperty() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo.set(idModulo);
    }

    public String getNomModulo() {
        return nomModulo.get();
    }

    public SimpleStringProperty nomModuloProperty() {
        return nomModulo;
    }

    public void setNomModulo(String nomModulo) {
        this.nomModulo.set(nomModulo);
    }

    public int getLectura() {
        return lectura.get();
    }

    public SimpleIntegerProperty lecturaProperty() {
        return lectura;
    }

    public void setLectura(int lectura) {
        this.lectura.set(lectura);
    }

    public int getEscritura() {
        return escritura.get();
    }

    public SimpleIntegerProperty escrituraProperty() {
        return escritura;
    }

    public void setEscritura(int escritura) {
        this.escritura.set(escritura);
    }
}
