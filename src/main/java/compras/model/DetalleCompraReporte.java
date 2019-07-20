package compras.model;

/**
 * Created by user on 5/07/2017.
 */
public class DetalleCompraReporte {
    private String noProducto;
    private String nomProducto;
    private String precioProducto;
    private String cantidadProducto;
    private String totalProducto;

    public DetalleCompraReporte(String noProducto, String nomProducto, String precioProducto, String cantidadProducto, String totalProducto) {
        this.noProducto = noProducto;
        this.nomProducto = nomProducto;
        this.precioProducto = precioProducto;
        this.cantidadProducto = cantidadProducto;
        this.totalProducto = totalProducto;
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

    public String getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(String precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(String cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getTotalProducto() {
        return totalProducto;
    }

    public void setTotalProducto(String totalProducto) {
        this.totalProducto = totalProducto;
    }
}
