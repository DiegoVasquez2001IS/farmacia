package resumen.ventas.model;

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
 * Created by user on 1/06/2017.
 */
public class VentaDAO {

    Connection conn = Conexion.getInstance().getConexion();
    final String ventasDia = "SELECT * FROM ventas_del_dia";
    final String ventasSemana = "SELECT * FROM ventas_de_la_semana";
    final String ventasMes = "SELECT * FROM ventas_del_mes";
    final String ventasDiaRango = "SELECT * FROM ventas_del_dia_rango";
    final String ventasRango = "SELECT * FROM ventas_rango_cliente";


    final String compraDia = "SELECT costo_x_compra FROM farmacia.lucro_del_dia;";
    final String compraMes = "SELECT costo_x_compra FROM farmacia.lucro_del_mes;";
    final String compraSemana = "SELECT costo_x_compra FROM farmacia.lucro_dela_semana;";

    final String lucroDia = "SELECT lucro_del_dia FROM farmacia.lucro_del_dia;";
    final String lucroSemana = "SELECT lucro_semana FROM farmacia.lucro_dela_semana;";
    final String lucroMes = "SELECT lucro_del_mes FROM farmacia.lucro_del_mes;";

    final String lucroDiaRango = "SELECT lucro_deldia_rango FROM farmacia.lucro_del_dia_rango;";
    final String lucroRangoCliente = "SELECT lucro_rango_cliente FROM farmacia.lucro_rango_cliente;";

    final String compraDiaRango = "SELECT costo_x_compra FROM farmacia.lucro_del_dia_rango;";
    final String compraRangoCliente = "SELECT costo_x_compra FROM farmacia.lucro_rango_cliente;";

    final String costoInventarioCompra = "select total_compra from farmacia.costo_de_inventario;";
    final String costoInventarioVenta = "select total_venta from farmacia.costo_de_inventario;";
    final String costoInventarioLucro = "select lucro from farmacia.costo_de_inventario;";


    String llamarRango = "";
    String llamarDiaRango = "";

    final String totalDia = "SELECT * FROM total_del_dia";
    final String totalSemana = "SELECT * FROM total_venta_semana;";
    final String totalDiaRango = "SELECT * FROM total_ventas_del_dia_rango";
    final String totalRango = "SELECT * FROM total_ventas_rango_cliente";
    final String totalMes = "SELECT * FROM total_venta_mes";

    public final static int VENTASDIA = 101;
    public final static int VENTASSEMANA = 102;
    public final static int VENTASMES = 103;
    public final static int VENTASDIARANGO = 104;
    public final static int VENTASRANGO = 105;
    public final static int TOTALDIA = 106;
    public final static int TOTALSEMANA = 107;
    public final static int TOTALDIARANGO = 108;
    public final static int TOTALRANGO = 109;
    public final static int TOTALMES = 110;
    public final static int COMPRADIA = 111;
    public final static int COMPRASEMANA = 112;
    public final static int COMPRAMES = 113;
    public final static int LUCRODIA = 114;
    public final static int LUCROSEMANA = 115;
    public final static int LUCROMES = 116;
    public final static int COMPRADIARANGO = 117;
    public final static int COMPRARANGO = 118;
    public final static int LUCRODIARANGO = 119;
    public final static int LUCRORANGO = 120;
    public final static int COSTOINVENTARIOCOMPRA = 121;
    public final static int COSTOINVENTARIOVENTA = 122;
    public final static int COSTOINVENTARIOLUCRO = 123;

    public void llamarVentasDiaRango(String rango){
        String consulta =  String.format("select * from(select @f1:='%s')p, ventas_del_dia_rango",rango);
        llamarDiaRango = consulta;
    }

    public void llamarVentasRango(String rango1, String rango2){
        String consulta = String.format("select * from(select @f1:='%s' p, @f2:='%s') p, ventas_rango_cliente",rango1,rango2);
        llamarRango = consulta;
    }

    public Collection<Venta> getDatos(int tipoConsulta){
        String consulta = "";
        final List<Venta> listaVentas = new ArrayList<>();
        switch (tipoConsulta){
            case VENTASDIA:
                consulta = ventasDia;
                break;
            case VENTASSEMANA:
                consulta = ventasSemana;
                break;
            case VENTASMES:
                consulta = ventasMes;
                break;
            case VENTASDIARANGO:
                consulta = ventasDiaRango;
                break;
            case VENTASRANGO:
                consulta = ventasRango;
                break;
        }
        try {
            Statement st = conn.createStatement();
            if(consulta.equals(ventasRango)){
                ResultSet rs1 = st.executeQuery(llamarRango);
            }else if(consulta.equals(ventasDiaRango)){
                ResultSet rs1 = st.executeQuery(llamarDiaRango);
            }
            ResultSet rs = st.executeQuery(consulta);
            Venta venta;
            while(rs.next()){
                venta = new Venta(rs.getInt("num_ordenPedido"),
                        rs.getString("fecha"),
                        rs.getString("nom_cli"),
                        rs.getString("nom_empleado"),
                        rs.getFloat("total"),
                        rs.getString("tipo_pago"),
                        rs.getString("Estatus"));
                listaVentas.add(venta);
            }
        } catch (SQLException e) {
            final String mensaje = "Error en las vistas Ventas";
            e.printStackTrace();
            Util.makeError(mensaje,e.getLocalizedMessage());
        }
        return listaVentas;
    }

    public float getTotalVenta(int tipoConsulta){
        String consulta = "";
        String valorTotal = "";
        float retVal = 0;
        switch (tipoConsulta){
            case TOTALDIA:
                consulta = totalDia;
                valorTotal = "total_del_dia";
                break;
            case TOTALDIARANGO:
                consulta = totalDiaRango;
                valorTotal = "total_de_dia";
                break;
            case TOTALSEMANA:
                consulta = totalSemana;
                valorTotal = "total";
                break;
            case TOTALRANGO:
                consulta = totalRango;
                valorTotal = "total_rango";
                break;
            case TOTALMES:
                consulta = totalMes;
                valorTotal = "total_mes";
                break;
            case COSTOINVENTARIOVENTA:
                consulta = costoInventarioVenta;
                valorTotal = "total_venta";
                break;
        }
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()){
                retVal = rs.getFloat(valorTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public float getCompra(int tipoConsulta){
        String consulta = "";
        String valorTotal = "";
        float retVal = 0;
        switch (tipoConsulta){
            case COMPRADIA:
                consulta = compraDia;
                valorTotal = "costo_x_compra";
                break;
            case COMPRASEMANA:
                consulta = compraSemana;
                valorTotal = "costo_x_compra";
                break;
            case COMPRAMES:
                consulta = compraMes;
                valorTotal = "costo_x_compra";
                break;
            case COMPRADIARANGO:
                consulta = compraDiaRango;
                valorTotal = "costo_x_compra";
                break;
            case COMPRARANGO:
                consulta = compraRangoCliente;
                valorTotal = "costo_x_compra";
                break;
            case COSTOINVENTARIOCOMPRA:
                consulta = costoInventarioCompra;
                valorTotal = "total_compra";
                break;
        }
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()){
                retVal = rs.getFloat(valorTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public float getLucro(int tipoConsulta){
        String consulta = "";
        String valorTotal = "";
        float retVal = 0;
        switch (tipoConsulta){
            case LUCRODIA:
                consulta = lucroDia;
                valorTotal = "lucro_del_dia";
                break;
            case LUCROSEMANA:
                consulta = lucroSemana;
                valorTotal = "lucro_semana";
                break;
            case LUCROMES:
                consulta = lucroMes;
                valorTotal = "lucro_del_mes";
                break;
            case LUCRODIARANGO:
                consulta = lucroDiaRango;
                valorTotal = "lucro_deldia_rango";
                break;
            case LUCRORANGO:
                consulta = lucroRangoCliente;
                valorTotal = "lucro_rango_cliente";
                break;
            case COSTOINVENTARIOLUCRO:
                consulta = costoInventarioLucro;
                valorTotal = "lucro";
                break;
        }
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()){
                retVal = rs.getFloat(valorTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }
}
