package permisos;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 5/07/2017.
 */
public class PermisosDAO {
    public Collection<Permiso> getPermisoXNivelUsuario(String nivelUsuario){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta =
                String.format("SELECT * FROM farmacia.mostrar_permiso where idNivelUsuario = %s;",nivelUsuario);
        final List<Permiso> listaPermisos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Permiso permiso;
            while(rs.next()){
                permiso = new Permiso(
                    rs.getInt("idPermiso"),
                    rs.getInt("idNivelUsuario"),
                    rs.getString("nombreNivel"),
                    rs.getInt("idModulo"),
                    rs.getString("nombre"),
                    rs.getInt("lectura"),
                    rs.getInt("escritura")
                    );
                listaPermisos.add(permiso);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Niveles Usuario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaPermisos;
    }

    public void insertarPermisosNuevoNivelUsuario(String idNivelUsuario){
        Connection conn = Conexion.getInstance().getConexion();
        for(int i = 1; i<22;i++){
            final String procedimiento = String.format("insert into permisos(idNivelUsuario,idModulo,lectura,escritura) values(\n" +
                    "%s,%s,0,0);",idNivelUsuario,i+"");
            try {
                CallableStatement cs = conn.prepareCall(procedimiento);
                cs.execute();
            } catch (SQLException e) {
                final String mensaje = "Error en la tabla Niveles de Usuario";
                e.printStackTrace();
                Util.makeError(mensaje,e.getLocalizedMessage());
            }
        }
    }

    public void modificarPermiso(String idPermiso, String lectura, String escritura){
        Connection conn = Conexion.getInstance().getConexion();

        final String procedimiento = String.format("update permisos set lectura = %s,escritura = %s where idPermiso = %s;"
                ,lectura,escritura,idPermiso);
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Permisos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }

    }

    public boolean getLectura(String idNivelUsuario, String idModulo){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta =
                String.format("select lectura from farmacia.permisos where idNivelUsuario = %s and idModulo=%s;",idNivelUsuario,idModulo);
        boolean retVal = false;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                int aux = rs.getInt("lectura");
                if(aux == 0){
                    return false;
                } else{
                    return true;
                }
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Permisos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return retVal;
    }

    public boolean getEscritura(String idNivelUsuario, String idModulo){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta =
                String.format("select escritura from farmacia.permisos where idNivelUsuario = %s and idModulo=%s;",idNivelUsuario,idModulo);
        boolean retVal = false;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                int aux = rs.getInt("escritura");
                retVal = aux == 1 ? true : false;
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Permisos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return retVal;
    }
}
