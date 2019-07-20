package dolencias.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 30/04/2017.
 */
public class DolenciasDAO {

    public boolean agregarModificarEliminarDolencia(final Dolencia dolencia,final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call Procesodolencia(?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",dolencia.getCodigo());
            cs.setString("nom",dolencia.getNombre());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Dolencias";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public Collection<Dolencia> getDolencias(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrardolencias";
        final List<Dolencia> listaDolencias = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Dolencia dolencia;
            while(rs.next()){
                dolencia = new Dolencia(rs.getInt("cod_dole"),
                        rs.getString("nom_des"));
                listaDolencias.add(dolencia);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Dolencias";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaDolencias;
    }
}
