package resumen.clientes.model;

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
public class ResumenClienteDAO {

    public Collection<ResumenCliente> getClientes(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM farmacia.total_ventaxclientes;";
        final List<ResumenCliente> listaClientes = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            ResumenCliente resumenCliente;
            while(rs.next()){
                resumenCliente = new ResumenCliente(rs.getInt("id_cliente"),
                        rs.getString("nom_cli"),
                        rs.getFloat("total_compra"));
                listaClientes.add(resumenCliente);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Resumen Clientes";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaClientes;
    }
}
