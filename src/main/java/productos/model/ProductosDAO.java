package productos.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 3/05/2017.
 */
public class ProductosDAO {

    public Collection<Producto> getProductos(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrartodosproductos";
        final List<Producto> listaProductos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Producto producto;
            while(rs.next()){
                producto = new Producto(
                        rs.getLong("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_compra"),
                        rs.getFloat("pre_venta"),
                        rs.getString("fecha_venc"),
                        rs.getInt("stock"),
                        rs.getInt("id_prov"),
                        rs.getInt("id_pres"),
                        rs.getInt("id_dole"),
                        rs.getInt("cod_col"));
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Productos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProductos;
    }

    public int getStockProducto(String idProducto){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("select stock from mostrartodosproductos where cod_pro = %s;",
                idProducto);
        int stock = 0;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                stock = rs.getInt("stock");
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Productos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return stock;
    }

    public Collection<ProductoMostrar> getProductosMostrar(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.inventario_producto";
        final List<ProductoMostrar> listaProductos = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ProductoMostrar producto;
            while(rs.next()){
                producto = new ProductoMostrar(rs.getLong("cod_pro"),
                        rs.getString("nom_pro"),
                        rs.getFloat("pre_compra"),
                        rs.getFloat("pre_venta"),
                        rs.getInt("stock"),
                        rs.getString("fecha_venc"),
                        rs.getString("nom_col"),
                        rs.getString("nom_des"),
                        rs.getString("nom_prov"),
                        rs.getString("nom_pre"));
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la Vista Mostrar Productos Inventario";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProductos;
    }

    public boolean agregarModificarEliminarProducto(Producto producto, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesosProductos(?,?,?,?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setLong("id",producto.getCodigo());
            System.out.println(producto.getCodigo() + "----codigo");
            cs.setString("nom",producto.getNombre());
            System.out.println(producto.getNombre() + "");
            cs.setFloat("precompra",producto.getPrecioCompra());
            System.out.println(producto.getPrecioCompra() + "");
            cs.setFloat("preventa",producto.getPrecioVenta());
            System.out.println(producto.getPrecioVenta() + "");
            cs.setString("fechavencimiento",producto.getFechaVencimiento());
            System.out.println(producto.getFechaVencimiento() + "");
            cs.setInt("can",producto.getStock());
            System.out.println(producto.getStock() + "");
            cs.setInt("idprov",producto.getIdProveedor());
            System.out.println(producto.getIdProveedor() + "");
            cs.setInt("idpres",producto.getIdPresentacion());
            System.out.println(producto.getIdPresentacion() + "");
            cs.setInt("idcate",producto.getIdDolencia());
            System.out.println(producto.getIdDolencia() + "");
            cs.setInt("idcolo",producto.getIdColocacion());
            System.out.println(producto.getIdColocacion() + "");
            System.out.println(accion+ "");
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Productos";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
