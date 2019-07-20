package usuarios.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public  class Usuario {
    private final SimpleIntegerProperty codigoUsuario;
    private final SimpleIntegerProperty codigoEmpleado;
    private final SimpleIntegerProperty nivelUsuario;
    private final SimpleStringProperty nick;
    private final SimpleStringProperty pass;
    private final SimpleStringProperty activo;

    public Usuario(int codigoUsuario, int codigoEmpleado, int nivelUsuario, String nick, String pass, String activo) {
        this.codigoUsuario = new SimpleIntegerProperty(codigoUsuario);
        this.codigoEmpleado = new SimpleIntegerProperty(codigoEmpleado);
        this.nivelUsuario = new SimpleIntegerProperty(nivelUsuario);
        this.nick = new SimpleStringProperty(nick);
        this.pass = new SimpleStringProperty(pass);
        this.activo = new SimpleStringProperty(activo);
    }

    public int getCodigoUsuario() {
        return codigoUsuario.get();
    }

    public SimpleIntegerProperty codigoUsuarioProperty() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario.set(codigoUsuario);
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado.get();
    }

    public SimpleIntegerProperty codigoEmpleadoProperty() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado.set(codigoEmpleado);
    }

    public int getNivelUsuario() {
        return nivelUsuario.get();
    }

    public SimpleIntegerProperty nivelUsuarioProperty() {
        return nivelUsuario;
    }

    public void setNivelUsuario(int nivelUsuario) {
        this.nivelUsuario.set(nivelUsuario);
    }

    public String getNick() {
        return nick.get();
    }

    public SimpleStringProperty nickProperty() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick.set(nick);
    }

    public String getPass() {
        return pass.get();
    }

    public SimpleStringProperty passProperty() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }

    public String getActivo() {
        return activo.get();
    }

    public SimpleStringProperty activoProperty() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo.set(activo);
    }
}

