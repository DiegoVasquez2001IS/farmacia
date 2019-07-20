package pos.model;

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
 * Created by user on 4/05/2017.
 */
public class PosDAO {

    public Collection<Pos> getProductos(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.productos_motostrar";
        final List<Pos> listaProductos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Pos pos;
            while(rs.next()){
                pos = new Pos(rs.getLong("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_venta"),
                        rs.getString("fecha_venc"),
                        rs.getInt("stock"),
                        rs.getString("nom_col"),
                        rs.getString("nom_des"),
                        rs.getString("nom_prov"),
                        rs.getString("nom_pre"));
                listaProductos.add(pos);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Productos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProductos;
    }

    public Collection<Pos> getProductosXProveedor(String mNomProveedor){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("SELECT * FROM farmacia.productos_motostrar " +
                        "where nom_prov = \"%s\";",
                mNomProveedor);
        System.out.println("CONSULTA: " + consulta);
        final List<Pos> listaProductos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Pos pos;
            while(rs.next()){
                pos = new Pos(rs.getInt("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_venta"),
                        rs.getString("fecha_venc"),
                        rs.getInt("stock"),
                        rs.getString("nom_col"),
                        rs.getString("nom_des"),
                        rs.getString("nom_prov"),
                        rs.getString("nom_pre"));
                listaProductos.add(pos);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Productos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProductos;
    }
}
