package resumen.ventas.model;

/**
 * Created by user on 12/07/2017.
 */
public class VentaDiaReporte {
    private String noventa;
    private String cliente;
    private String empleado;
    private String fecha;
    private String total;

    public VentaDiaReporte() {
    }

    public VentaDiaReporte(String noventa, String cliente, String empleado, String fecha, String total) {
        this.noventa = noventa;
        this.cliente = cliente;
        this.empleado = empleado;
        this.fecha = fecha;
        this.total = total;
    }

    public String getNoventa() {
        return noventa;
    }

    public void setNoventa(String noventa) {
        this.noventa = noventa;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
