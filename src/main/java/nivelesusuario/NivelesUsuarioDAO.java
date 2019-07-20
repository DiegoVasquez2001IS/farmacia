package nivelesusuario;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 5/07/2017.
 */
public class NivelesUsuarioDAO {

    public Collection<NivelUsuario> getNivelesUsuario(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM mostrarniveles";
        final List<NivelUsuario> listaNiveles = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            NivelUsuario nivelUsuario;
            while(rs.next()){
                nivelUsuario = new NivelUsuario(rs.getInt("idNivel"),rs.getString("nombreNivel"));
                listaNiveles.add(nivelUsuario);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Niveles Usuario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaNiveles;
    }

    public boolean agregarModificarEliminarNivelUsuario(final NivelUsuario nivelUsuario, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesoNivelesdeUsuario(?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("_idNivel",nivelUsuario.getIdNivel());
            cs.setString("_nombreNivel",nivelUsuario.getNomNivel());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Niveles de Usuario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public NivelUsuario getNivelUsuarioXId(String idNivel){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("select * from mostrarniveles where idNivel = %s;",idNivel);
        NivelUsuario nivelUsuario = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                nivelUsuario = new NivelUsuario(rs.getInt("idNivel"),rs.getString("nombreNivel"));
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Niveles Usuario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return nivelUsuario;
    }

    public int getUltimoIdNivelUsuario(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarultimoidnivelusuario;";
        int idNivelUsuario = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                idNivelUsuario = rs.getInt("idNivel");
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Niveles Usuario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return idNivelUsuario;
    }



}
