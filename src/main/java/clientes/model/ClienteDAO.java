package clientes.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 23/04/2017.
 */
public class ClienteDAO {

    public boolean agregarModificarEliminarCliente(final Cliente nuevoCliente, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesosCliente(?,?,?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",nuevoCliente.getCodigo());
            cs.setString("nom",nuevoCliente.getNombre());
            cs.setString("_apellido",nuevoCliente.getApellidos());
            cs.setString("dire",nuevoCliente.getDireccion());
            cs.setString("_sexo",nuevoCliente.getSexo());
            cs.setString("_dpi",nuevoCliente.getDpi());
            cs.setString("_nit",nuevoCliente.getNit());
            cs.setInt("tel",nuevoCliente.getTelefono());
            cs.setInt("ccel",nuevoCliente.getCelular());
            cs.setString("accion",accion);
            // en una inserción cs siempre retorna false
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Clientes";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public Collection<Cliente> getClientes(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarclientes  order by apellidos;";
        final List<Cliente> listaCliente = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Cliente cliente;
            while (rs.next()){
                cliente = new Cliente(rs.getInt("cod_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("direccion"),
                        rs.getString("Sexo"),
                        rs.getString("DPI"),
                        rs.getString("nit"),
                        rs.getInt("telefono"),
                        rs.getInt("cel"));
                listaCliente.add(cliente);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Clientes";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaCliente;
    }

    public Cliente getClienteXId(int idCliente){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format( "SELECT * FROM mostrarclientes where cod_cliente = %s;",
                String.valueOf(idCliente));
        Cliente cliente = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()){
                cliente = new Cliente(rs.getInt("cod_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("direccion"),
                        rs.getString("Sexo"),
                        rs.getString("DPI"),
                        rs.getString("nit"),
                        rs.getInt("telefono"),
                        rs.getInt("cel"));
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Clientes";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return cliente;
    }
}
