package proveedores.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 1/05/2017.
 */
public class ProveedoresDAO {

    public boolean AgregarModificarEliminarProveedor(final Proveedor proveedor, String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesosProveedor(?,?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",proveedor.getCodigo());
            cs.setString("nom",proveedor.getNombre());
            cs.setString("dire",proveedor.getDireccion());
            cs.setInt("tel",proveedor.getTelefono());
            cs.setInt("cel",proveedor.getCelular());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Proveedores";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }

    public Collection<Proveedor> getProveedores(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarproveedores";
        final List<Proveedor> listaProveedores = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Proveedor proveedor;
            while (rs.next()){
                proveedor = new Proveedor(rs.getInt("cod_prov"),
                        rs.getString("nom_prov"),
                        rs.getString("dir_prov"),
                        rs.getInt("telefono"),
                        rs.getInt("celular"));
                listaProveedores.add(proveedor);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Proveedores";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProveedores;
    }

    public Collection<ProveedorCodNom> getProveedoresCodNom(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "select * from mostrarcodnomproveedores order by nom_prov";
        final List<ProveedorCodNom> listaProveedores = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ProveedorCodNom proveedor;
            while (rs.next()){
                proveedor = new ProveedorCodNom(rs.getInt("cod_prov"),
                        rs.getString("nom_prov"));
                listaProveedores.add(proveedor);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Proveedores";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaProveedores;
    }
}
