package resumen.productos.model;

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
 * Created by user on 4/06/2017.
 */
public class ResumenProductosDAO {

    public static final int PRODUCTOSVECIDOS = 101;
    public static final int PRODUCTOSAVENCER = 102;

    private final String productosVencidos = "SELECT * FROM farmacia.productos_vencidos";
    private final String productosAVencer = "SELECT * FROM farmacia.productos_a_vencidos";

    public Collection<ResumenProducto> getDatos(int tipoConsulta){
        String consulta = "";
        switch (tipoConsulta){
            case PRODUCTOSVECIDOS:
                consulta = productosVencidos;
                break;
            case PRODUCTOSAVENCER:
                consulta = productosAVencer;
                break;
        }
        Connection conn = Conexion.getInstance().getConexion();
        final List<ResumenProducto> listaProductos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ResumenProducto resumenProducto;
            while(rs.next()){
                resumenProducto = new ResumenProducto(rs.getLong("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getString("fecha_venc"),
                        rs.getInt("stock"),
                        rs.getFloat("pre_venta"),
                        rs.getString("nom_prov"));
                listaProductos.add(resumenProducto);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Resumen Producto";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProductos;
    }

}
