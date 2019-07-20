package presentaciones.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 1/05/2017.
 */
public class PresentacionesDAO {

    public boolean agregarModificarEliminarPresentacion(final Presentacion presentacion, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String process = "{ call ProcesoPresentacion(?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(process);
            cs.setInt("id",presentacion.getCodigo());
            cs.setString("nom",presentacion.getNombre());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Presentaciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public Collection<Presentacion> getPresentaciones(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrar_presentaciones";
        final List<Presentacion> listaPresentaciones = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Presentacion presentacion;
            while(rs.next()){
                presentacion = new Presentacion(
                        rs.getInt("cod_pre"),
                        rs.getString("nom_pre"));
                listaPresentaciones.add(presentacion);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Presentaciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaPresentaciones;
    }
}
