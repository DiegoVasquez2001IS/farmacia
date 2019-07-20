package util;

import domain.PasarParametros;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by user on 20/02/2017.
 */
public class Util {

    public static void loadWindow(String loc, String title, Class clase){
        try {
            Parent parent = FXMLLoader.load(clase.getClassLoader().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(clase.getName()).log(Level.ALL.SEVERE, null, ex);
        }
    }

    // si se quiere utilizar este método el controller del parámetro loc debe implementar la interface "Pasar Parámetros"
    public static void loadWindownWithParemeters(String loc, String title, Class clase, Object objeto, String accion){
        try {
            FXMLLoader loader = new FXMLLoader(clase.getClassLoader().getResource(loc));
            Parent parent = loader.load();
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            PasarParametros controller = loader.getController();
            controller.asignarParametros(objeto);
            controller.aisgnarAccion(accion);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(clase.getName()).log(Level.ALL.SEVERE, null, ex);
        }
    }

    public static Parent getWindow(String loc, Class clase){
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    clase.getClassLoader().getResource(loc));
            return root;
        } catch (IOException e) {
            e.printStackTrace();
            return new AnchorPane();
        }
    }

    public static void switchScene(String loc, String newTitle, Class clase, Stage stage){
        try {
            Parent parent = FXMLLoader.load(clase.getClassLoader().getResource(loc));
            if(newTitle != null)
                stage.setTitle(newTitle);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(clase.getName()).log(Level.ALL.SEVERE, null, ex);
        }
    }

    public static Node getNodeFromFXML(String loc, Class clase){
        try {
            Node parent = FXMLLoader.load(clase.getClassLoader().getResource(loc));
            return parent;
        } catch (IOException ex) {
            Logger.getLogger(clase.getName()).log(Level.ALL.SEVERE, null, ex);
            return null;
        }
    }


    public static Node getIcon(String loc, Class clase){
        return new ImageView(
                new Image(clase.getClassLoader().getResourceAsStream(loc)));
    }

    public static void makeError(String titulo, String mensaje){
        NotificationType notificationType = NotificationType.ERROR;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titulo);
        tray.setMessage(mensaje);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(AnimationType.FADE);
        tray.showAndWait();
    }

    public static void makeAdvertencia(String titulo, String mensaje){
        NotificationType notificationType = NotificationType.WARNING;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titulo);
        tray.setMessage(mensaje);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(AnimationType.FADE);
        tray.showAndDismiss(new Duration(3000));
    }

    public static void makeSuccess(String titulo, String mensaje){
        NotificationType notificationType = NotificationType.SUCCESS;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(titulo);
        tray.setMessage(mensaje);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(new Duration(3000));
    }

    /**
     * @param labels lista que contiene todos los textfield, longitud y labels de un formulario
     * el método empareja cada uno de los elementos recibidos en la lista para mostrar la longitud
     * de caracteres que pueden ser aceptados por la base de datos
     */
    public static void addListenerToLabels(List<TextContador> labels){
        for (final TextContador contador: labels){
            contador.getLabel().setText(contador.getLongitud() + "");
            contador.getStringProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    int caracteresDisponibles = contador.getLongitud() - newValue.length();
                    contador.getLabel().setText(caracteresDisponibles + "");
                    if(caracteresDisponibles >= 0){
                        contador.getLabel().setTextFill(javafx.scene.paint.Color.web("#000000"));
                    }else{
                        contador.getLabel().setTextFill(javafx.scene.paint.Color.web("#ff0000"));
                    }
                }
            });
        }
    }
}
