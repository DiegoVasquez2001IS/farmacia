package usuarios.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 23/04/2017.
 */
public class UsuarioDAO {

    public Collection<Usuario> getUsuarios(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarusuarios";
        final List<Usuario> listaUsuarios = new ArrayList<>();

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Usuario usuario;
            while(rs.next()){
                usuario = new Usuario(rs.getInt("cod_usu"),
                        rs.getInt("id_emp"),
                        rs.getInt("nivel_usu"),
                        rs.getString("nick_usu"),
                        rs.getString("pas_user"),
                        rs.getString("activo"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }

    public Usuario getUsuario(String codigoUsuario){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("select * from usuario where cod_usu = %s",codigoUsuario);
        Usuario retVal = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                retVal = new Usuario(rs.getInt("cod_usu"),
                        rs.getInt("id_emp"),
                        rs.getInt("nivel_usu"),
                        rs.getString("nick_usu"),
                        rs.getString("pas_user"),
                        rs.getString("activo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public boolean agregarModificarEliminarUsuario(final Usuario usuario, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procesoUsuario = "{ call ProcesoUser(?,?,?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procesoUsuario);
            cs.setInt("id",usuario.getCodigoUsuario());
            cs.setInt("idemp",usuario.getCodigoEmpleado());
            cs.setInt("nivel",usuario.getNivelUsuario());
            cs.setString("nick",usuario.getNick());
            cs.setString("passw",usuario.getPass());
            cs.setString("_activo",usuario.getActivo());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Usuarios";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
