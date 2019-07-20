package ventas.model;

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
 * Created by user on 23/06/2017.
 */
public class OrdenPedidoDAO {

    public Collection<OrdenPedido> getOrdenesPedido(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.mostraropedido;";
        final List<OrdenPedido> listaPedidos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            OrdenPedido ordenPedido;
            while(rs.next()){
                ordenPedido = new OrdenPedido(
                        rs.getInt("num_ordenPedido"),
                        rs.getString("fecha"),
                        rs.getInt("id_cliente"),
                        rs.getString("nom_cli"),
                        rs.getInt("cod_emp"),
                        rs.getString("nom_empleado"),
                        rs.getFloat("descuento_cliente"),
                        rs.getFloat("total"),
                        Integer.parseInt(rs.getString("cod_tipoPago")),
                        rs.getString("Estatus"),
                        rs.getString("motivo_anulacion"));
                listaPedidos.add(ordenPedido);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Ordenes de Pedido";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaPedidos;
    }

    public Collection<DetalleOPedido> getDetallesXOrdenPedido(int idOrden){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("SELECT * FROM detalleordenpedido where id_orpedido = %s;",String.valueOf(idOrden));
        final List<DetalleOPedido> listaDetalle = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            DetalleOPedido detalleOPedido;
            while(rs.next()){
                detalleOPedido = new DetalleOPedido(
                        rs.getInt("num_ordenp"),
                        idOrden,
                        rs.getInt("id_producto"),
                        rs.getString("nom_pro"),
                        rs.getInt("cantidad"),
                        rs.getFloat("precio_venta"),
                        rs.getFloat("descuento"),
                        rs.getFloat("importe"));
                listaDetalle.add(detalleOPedido);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Detalle Orden";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaDetalle;
    }

}
