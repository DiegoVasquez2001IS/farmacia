package colocaciones.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 2/05/2017.
 */
public class ColocacionesDAO {

    public Collection<Colocacion> getColocaciones(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consultaColocaciones = "SELECT * FROM farmacia.mostrarcolocaciones";
        final List<Colocacion> listaColocaciones = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaColocaciones);
            Colocacion colocacion;
            while(rs.next()){
                colocacion = new Colocacion(rs.getInt("id_col"),
                        rs.getString("nom_col"));
                listaColocaciones.add(colocacion);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Colocaciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaColocaciones;
    }

    public boolean agregarModificarEliminarColocacion(final Colocacion colocacion, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procesoColocacion = "{ call ProcesoColocacion(?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procesoColocacion);
            System.out.println("CODIGO------------: " + colocacion.getCodigo());
            cs.setInt("id",colocacion.getCodigo());
            cs.setString("nom",colocacion.getNombre());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Colocaciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
