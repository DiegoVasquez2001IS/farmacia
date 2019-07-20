package pos.model;

import bd.Conexion;
import util.Util;
import ventas.model.DetalleOPedido;

import java.sql.*;

/**
 * Created by user on 27/05/2017.
 */
public class VenderDAO {

    public boolean AgregarModificarOrderPedido(OrdenPedido ordenPedido, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesosOrdenPedido(?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",ordenPedido.getIdOrdenPedido());
            cs.setInt("idcliente",ordenPedido.getIdCliente());
            cs.setInt("idempleado",ordenPedido.getIdEmpleado());
            cs.setInt("idpago",ordenPedido.getIdTipoPago());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean AgregarModificarDetalleOrderPedido(ProductosListaCompras plc,OrdenPedido op, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call procesoDetalleOrden(?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("idpedido",0);
            cs.setInt("idorden",op.getIdOrdenPedido());
            cs.setLong("idpro",plc.getCodigo());
            cs.setInt("_cantidad",plc.getCantidad());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean AgregarModificarDetalleOrderPedido(DetalleOPedido dop,OrdenPedido op, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call procesoDetalleOrden(?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("idpedido",0);
            cs.setInt("idorden",op.getIdOrdenPedido());
            cs.setLong("idpro",dop.getIdProducto());
            cs.setInt("_cantidad",dop.getCantidad());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean eliminarDetallesXOrden(String idOrden){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = String.format("delete from detalleordenpedido where id_orpedido = %s;",idOrden);
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean modificarDetalleOrden(DetalleOPedido dop, int idOrden){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call procesoDetalleOrden(?,?,?,?,?)}";
        System.out.println(dop.getNomPro());
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("idpedido",dop.getNumDetalle());
            cs.setInt("idorden",idOrden);
            cs.setLong("idpro",dop.getIdProducto());
            cs.setInt("_cantidad",dop.getCantidad());
            cs.setString("accion","actualizar");
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }



    public int getUltimoIdOrden(){
        int ret = 0;
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarultimaordenpedido";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);

            while(rs.next()){
                ret = rs.getInt("num_ordenPedido");
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return ret;
    }

    public int getUltimoIdDetalle(){
        int ret = 0;
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarultimoiddetalleorden";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while(rs.next()){
                ret = rs.getInt("num_ordenp");
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return ret;
    }

    public boolean descuentoPorProducto(int idDetalle,float descuento){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call descuentodtordenpedido(?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",idDetalle);
            cs.setFloat("descuento",descuento);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error Descuento Por producto";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public boolean descuentoTotalVenta(int idOrden, float descuento){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call descuento_D_ordenpedido(?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",idOrden);
            cs.setFloat("descuento",descuento);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error Descuento total Orden";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }


}
