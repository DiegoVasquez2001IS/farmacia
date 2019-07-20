package pago;

import bd.Conexion;
import pago.model.Pago;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 27/05/2017.
 */
public class PagoDAO {

    public Collection<Pago> getPagos(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarpagos";
        final List<Pago> listaPagos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Pago pago;
            while(rs.next()){
                pago = new Pago(rs.getInt("cod_pag"),
                        rs.getString("tipo_pago"));
                listaPagos.add(pago);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Pago";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaPagos;
    }

    public Pago getPagoXId(int idPago){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("SELECT * FROM mostrarpagos where cod_pag=%s;",
                String.valueOf(idPago));
        Pago pago = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                pago = new Pago(rs.getInt("cod_pag"),
                        rs.getString("tipo_pago"));
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Pago";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return pago;
    }

    public boolean agregarModificarEliminarPago(final Pago pago, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesoPago(?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",pago.getIdPago());
            cs.setString("nom",pago.getPago());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Pagos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
