package pos.model;

/**
 * Created by user on 11/06/2017.
 */
public class ProductoReporte {

    private String cantidad;
    private String nombre;
    private String precio;
    private String descuento;
    private String total;

    public ProductoReporte(String cantidad, String nombre, String precio, String descuento, String total) {
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
