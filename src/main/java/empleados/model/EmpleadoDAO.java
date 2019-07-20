package empleados.model;

import bd.Conexion;
import util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by user on 4/05/2017.
 */
public class EmpleadoDAO {

    public Collection<Empleado> getEmpleados(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM mostrarempleados";
        final List<Empleado> listaEmpleados = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            Empleado empleado;
            while(rs.next()){
                empleado = new Empleado(rs.getInt("cod_emp"),
                        rs.getString("nom_emp"),
                        rs.getString("Apellidos"),
                        rs.getString("dir_emp"),
                        rs.getString("cargo"),
                        rs.getInt("edad"),
                        rs.getInt("tel"),
                        rs.getInt("cel"),
                        rs.getString("Fecha_ingreso"));
                listaEmpleados.add(empleado);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Empleados";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaEmpleados;
    }

    public Empleado getEmpleado(String codigoEmpleado){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = String.format("select * from empleado where cod_emp = %s",codigoEmpleado);
        Empleado retVal = null;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if(rs.next()){
                retVal = new Empleado(rs.getInt("cod_emp"),
                        rs.getString("nom_emp"),
                        rs.getString("Apellidos"),
                        rs.getString("dir_emp"),
                        rs.getString("cargo"),
                        rs.getInt("edad"),
                        rs.getInt("tel"),
                        rs.getInt("cel"),
                        rs.getString("Fecha_ingreso"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public Collection<EmpleadoCodNomApe> getCodNomApeEmpleados(){
        Connection conn = Conexion.getInstance().getConexion();
        final String consulta = "SELECT * FROM mostrarcodnomapeempleados";
        final List<EmpleadoCodNomApe> listaCodNomApe = new ArrayList<>();
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            EmpleadoCodNomApe empleado;
            while(rs.next()){
                empleado = new EmpleadoCodNomApe(rs.getInt("cod_emp"),
                        rs.getString("nom_emp"),
                        rs.getString("Apellidos"));
                listaCodNomApe.add(empleado);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Empleados";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaCodNomApe;
    }

    public boolean agregarModificarEliminarEmpleado(final Empleado empleado, final String accion){
        Connection conn = Conexion.getInstance().getConexion();
        final String procedimiento = "{ call ProcesoEmpleado(?,?,?,?,?,?,?,?,?,?)}";
        try {
            CallableStatement cs = conn.prepareCall(procedimiento);
            cs.setInt("id",empleado.getCodigo());
            cs.setString("nom",empleado.getNombre());
            cs.setString("apelli",empleado.getApellidos());
            cs.setString("dire",empleado.getDireccion());
            cs.setString("_cargo",empleado.getCargo());
            cs.setInt("_edad",empleado.getEdad());
            cs.setInt("_tel",empleado.getTelefono());
            cs.setInt("_cel",empleado.getCelular());
            cs.setString("ingreso",empleado.getIngreso());
            cs.setString("accion",accion);
            return cs.execute();
        } catch (SQLException e) {
            final String mensaje = "Error en la tabla Empleados";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
            return true;
        }
    }
}
