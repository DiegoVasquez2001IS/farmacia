package devoluciones;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 7/07/2017.
 */
public class DevolucionesDAO {

    public Collection<Devolucion> getDevoluciones(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM mostrardevoluciones;";
        final List<Devolucion> listaDevoluciones = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Devolucion devolucion;
            while(rs.next()){
                devolucion = new Devolucion(
                        rs.getInt("lineasd"),
                        rs.getInt("cod_compra"),
                        rs.getString("usuario"),
                        rs.getString("noombre_proveedor"),
                        rs.getString("motivo"),
                        rs.getString("fecha"),
                        rs.getFloat("totaldevolucion"));
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Devoluciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaDevoluciones;
    }

    public boolean agregarModificarEliminarDevolucion(final Devolucion devolucion, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call procesodevolucion(?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt(1,devolucion.getIdDevolucion());
            cs.setInt(2,devolucion.getIdCompra());
            cs.setString(3,devolucion.getMotivo());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Devoluciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean agregarModificarEliminarDetalleDevolucion(final DetalleDevolucion detalle, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call procesodetalledevolucion(?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("_linea",detalle.getIdDetalle());
            System.out.println("_linea " + detalle.getIdDetalle());
            cs.setInt("_cod_compras",detalle.getIdCompra());
            System.out.println("_cod_compras " + detalle.getIdCompra());
            cs.setInt("_idproctos_devuelto",detalle.getIdProducto());
            cs.setInt("_stockdevuelto",detalle.getStockDevuelto());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Devolucion";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }


    /**
     * Va a jalar los datos de detalle de compra y los retorna convertidos en detalle
     * de devoluciones
     * @param idCompra
     * @return
     */
    public Collection<DetalleDevolucion> getDetalleDevolucionXIdCompra(String idCompra){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("select * from mostrardetallecompras where idcompra = %s;",idCompra);
        final List<DetalleDevolucion> listaDetalles = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            DetalleDevolucion detalleDevolucion;
            while(rs.next()){
                detalleDevolucion = new DetalleDevolucion(
                        0,
                        rs.getInt("idcompra"),
                        rs.getInt("idproducto"),
                        rs.getString("nompro"),
                        rs.getFloat("precio_compra"),
                        rs.getInt("cantidad"),
                        0,
                        0
                );
                listaDetalles.add(detalleDevolucion);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Compra";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaDetalles;
    }

    public DetalleDevolucion getDetalleDevueltosXIdCompra(String idCompra, String idProducto){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("SELECT * FROM farmacia.detalldevoluciones where cod_compras = %s and idproctos_devuelto = %s;",idCompra,idProducto);
        DetalleDevolucion detalleDevolucion = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while(rs.next()){
                detalleDevolucion = new DetalleDevolucion(
                        rs.getInt("linea"),
                        rs.getInt("cod_compras"),
                        rs.getInt("idproctos_devuelto"),
                        "",
                        0,0,
                        rs.getInt("stockdevuelto"),
                        rs.getFloat("montodevuelto")
                );
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Compra";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return detalleDevolucion;
    }

    String h = "SELECT motivo FROM farmacia.devoluciones where cod_compra = 11;";

    public String getMotivoXIdCompra(String idCompra){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("SELECT motivo FROM farmacia.devoluciones where cod_compra = %s;",idCompra);
        String motivo = "";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                motivo = rs.getString("motivo");
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Devoluciones";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return motivo;
    }

}
