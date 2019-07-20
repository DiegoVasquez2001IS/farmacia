package bd;


import util.Util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by user on 28/03/2017.
 */
public class Conexion {

    Connection conn;

    private static Conexion ourInstance = new Conexion();

    public static Conexion getInstance() {
        return ourInstance;
    }

    private Conexion() {
    }

    public String conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/farmacia", "admin", "admin");
            return "Se ha conectado a la base de datos";
        } catch (Exception e) {
            String titulo = "Error al Conectar a la base de datos";
            String mensaje = e.getLocalizedMessage();
            Util.makeError(titulo, mensaje);
            return "Ocurrió un error al conectar con la base de datos " + e.getLocalizedMessage();
        }
    }

    public Connection getConexion()
    {
        return conn;
    }
}
