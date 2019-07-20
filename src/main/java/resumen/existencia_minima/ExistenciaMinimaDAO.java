package resumen.existencia_minima;

import bd.Conexion;
import util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 8/07/2017.
 */
public class ExistenciaMinimaDAO {

    public Collection<ProductoExistenciaMinima> getRangoMenorIgual5(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.existencia_minima_uno;";
        final List<ProductoExistenciaMinima> listaExistencia = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ProductoExistenciaMinima productoExistencia;
            while(rs.next()){
                productoExistencia = new ProductoExistenciaMinima(
                        rs.getInt("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_compra"),
                        rs.getFloat("pre_venta"),
                        rs.getInt("stock"),
                        rs.getString("fecha_venc"),
                        rs.getString("nom_col"),
                        rs.getString("nom_des"),
                        rs.getString("nom_prov"),
                        rs.getString("nom_pre")
                );
                listaExistencia.add(productoExistencia);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la vista Existencia minima";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaExistencia;
    }

    public Collection<ProductoExistenciaMinima> getRangoMayor5Menor11(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.existencia_minima_dos;";
        final List<ProductoExistenciaMinima> listaExistencia = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ProductoExistenciaMinima productoExistencia;
            while(rs.next()){
                productoExistencia = new ProductoExistenciaMinima(
                        rs.getInt("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_compra"),
                        rs.getFloat("pre_venta"),
                        rs.getInt("stock"),
                        rs.getString("fecha_venc"),
                        rs.getString("nom_col"),
                        rs.getString("nom_des"),
                        rs.getString("nom_prov"),
                        rs.getString("nom_pre")
                );
                listaExistencia.add(productoExistencia);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la vista Existencia minima";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaExistencia;
    }
}
