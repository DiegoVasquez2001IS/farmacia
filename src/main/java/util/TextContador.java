package util;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Created by user on 16/03/2017.
 */
public class TextContador {

    private int longitud;
    private TextArea textArea;
    private TextField textField;
    private Label label;

    public TextContador(TextArea textArea, int logintud, Label label){
        this.textArea = textArea;
        this.longitud = logintud;
        this.label = label;
    }

    public TextContador(TextField textField, int longitud, Label label){
        this.textField = textField;
        this.longitud = longitud;
        this.label = label;
    }

    public int getLongitud(){
        return this.longitud;
    }

    public StringProperty getStringProperty(){
        return (textField != null) ? textField.textProperty() : textArea.textProperty();
    }

    public Label getLabel(){
        return this.label;
    }
}
