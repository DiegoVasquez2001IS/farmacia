package ventas.model;

/**
 * Created by user on 13/07/2017.
 */
public class DetalleOPedidoReporte {
    private String idDetalle;
    private String noProducto;
    private String nomProducto;
    private String cantidad;
    private String precio;
    private String descuento;
    private String total;

    public DetalleOPedidoReporte(String idDetalle, String noProducto,
                                 String nomProducto, String cantidad, String precio,
                                 String descuento, String total) {
        this.idDetalle = idDetalle;
        this.noProducto = noProducto;
        this.nomProducto = nomProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.descuento = descuento;
        this.total = total;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getNoProducto() {
        return noProducto;
    }

    public void setNoProducto(String noProducto) {
        this.noProducto = noProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
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
