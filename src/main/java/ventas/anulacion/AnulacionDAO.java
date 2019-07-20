package ventas.anulacion;

import bd.Conexion;
import util.Util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by user on 26/06/2017.
 */
public class AnulacionDAO {


    public boolean anularOrden(final Anulacion anulacion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call anulacion_orden(?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("ido",anulacion.getIdOrden());
            cs.setString("motivo",anulacion.getMotivo());
            cs.execute();
            return false;
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Anular Orden";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
