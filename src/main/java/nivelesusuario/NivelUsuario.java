package nivelesusuario;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by user on 5/07/2017.
 */
public class NivelUsuario {

    private SimpleIntegerProperty idNivel;
    private SimpleStringProperty nomNivel;

    public NivelUsuario(int idNivel, String nomNivel) {
        this.idNivel = new SimpleIntegerProperty(idNivel);
        this.nomNivel = new SimpleStringProperty(nomNivel);
    }

    public int getIdNivel() {
        return idNivel.get();
    }

    public SimpleIntegerProperty idNivelProperty() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel.set(idNivel);
    }

    public String getNomNivel() {
        return nomNivel.get();
    }

    public SimpleStringProperty nomNivelProperty() {
        return nomNivel;
    }

    public void setNomNivel(String nomNivel) {
        this.nomNivel.set(nomNivel);
    }
}
