package escritorio;

import compras.ComprasController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import permisos.PermisosDAO;
import pos.PosController;
import resumen.existencia_minima.ExistenciaMinimaDAO;
import sesion.Sesion;
import util.Util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by user on 19/02/2017.
 */
public class EscritorioController implements Initializable {


    @FXML
    public BorderPane rootEscritorio;

    @FXML
    private TreeView<String> treeViewItems;

    @FXML
    private TreeItem<String> sistemaItem;

    @FXML
    private TreeItem<String> resumenItem;

    @FXML
    private TreeItem<String> resumenVentasItem;

    @FXML
    private TreeItem<String> resumenProductosItem;

    @FXML
    private TreeItem<String> resumenClientesItem;

    @FXML
    private TreeItem<String> principalItem;

    @FXML
    private TreeItem<String> proveedoresItem;

    @FXML
    private TreeItem<String> presentacionesItem;

    @FXML
    private TreeItem<String> dolenciasItem;

    @FXML
    private TreeItem<String> empleadosItem;

    @FXML
    private TreeItem<String> tipoPagoItem;

    @FXML
    private TreeItem<String> clientesItem;

    @FXML
    private TreeItem<String> colocacionesItem;

    @FXML
    private TreeItem<String> usuariosItem;

    @FXML
    private TreeItem<String> nivelesItem;

    @FXML
    private TreeItem<String> permisosItem;

    @FXML
    private TreeItem<String> posItem;

    @FXML
    private TreeItem<String> ventasItem;

    @FXML
    private TreeItem<String> inventarioItem;

    @FXML
    private TreeItem<String> productosItem;

    @FXML
    private TreeItem<String> comprasItem;

    @FXML
    private TreeItem<String> ingresoItem;

    @FXML
    private TreeItem<String> devolucionesItem;

    @FXML
    private TreeItem<String> existenciaMinimaItem;

    @FXML
    private TreeItem<String> cerrarsesionItem;


    PermisosDAO permisosDAO = new PermisosDAO();

    ExistenciaMinimaDAO existenciaMinimaDAO = new ExistenciaMinimaDAO();

    int mNivelUsuario = 0;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mNivelUsuario = Sesion.getmUsuarioActual().getNivelUsuario();
        sistemaItem.setExpanded(true);
        resumenItem.setExpanded(true);
        principalItem.setExpanded(true);
        //treeViewItems.getSelectionModel().select(resumenVentasItem);
        //setResumenCenter();
        setStyleSheetToFXMLDocument();
        setupClickHandlerToTreeView();
        setupCellFactoryToTreeView();
        configPermisos();
    }

    private void configPermisos() {
        boolean existenciaMinima = false;

        if(permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.EXISTENCIA_MINIMA+"")){
            if(existenciaMinimaDAO.getRangoMayor5Menor11().size() >0 ||
                    existenciaMinimaDAO.getRangoMenorIgual5().size()>0){
                existenciaMinima = true;
            }
            if(existenciaMinima) {
                Util.makeAdvertencia("PRODUCTOS CON BAJA EXISTENCIA", "Revise RESUMEN -> Existencia Minima");
            }
        }
        if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.RESUMEN+"")){
            sistemaItem.getChildren().remove(resumenItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.RESUMEN_DE_VENTAS+"")) {
            resumenItem.getChildren().remove(resumenVentasItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.RESUMEN_DE_PRODUCTOS+"")) {
            resumenItem.getChildren().remove(resumenProductosItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.RESUMEN_DE_CLIENTES+"")) {
            resumenItem.getChildren().remove(resumenClientesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.PROVEEDORES+"")){
            principalItem.getChildren().remove(proveedoresItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.PRESENTACIONES+"")) {
            principalItem.getChildren().remove(presentacionesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.DOLENCIAS+"")) {
            principalItem.getChildren().remove(dolenciasItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.EMPLEADOS+"")) {
            principalItem.getChildren().remove(empleadosItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.TIPO_DE_PAGO+"")) {
            principalItem.getChildren().remove(tipoPagoItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.CLIENTES+"")) {
            principalItem.getChildren().remove(clientesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.COLOCACIONES+"")) {
            principalItem.getChildren().remove(colocacionesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.USUARIOS+"")) {
            principalItem.getChildren().remove(usuariosItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.NIVELES_DE_USUARIO+"")) {
            principalItem.getChildren().remove(nivelesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.PERMISOS+"")) {
            principalItem.getChildren().remove(permisosItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.PUNTO_DE_VENTA+"")) {
            sistemaItem.getChildren().remove(posItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.VENTAS+"")) {
            posItem.getChildren().remove(ventasItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.PRODUCTOS+"")) {
            inventarioItem.getChildren().remove(productosItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.COMPRAS+"")) {
            inventarioItem.getChildren().remove(comprasItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.INGRESO+"")) {
            comprasItem.getChildren().remove(ingresoItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.DEVOLUCIONES+"")) {
            inventarioItem.getChildren().remove(devolucionesItem);
        } if(!permisosDAO.getLectura(String.valueOf(mNivelUsuario),ConstantesPermisos.EXISTENCIA_MINIMA+"")){
            resumenItem.getChildren().remove(existenciaMinimaItem);
        }
    }

    private void setupClickHandlerToTreeView(){
        treeViewItems.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                TreeItem<String> selectedItem = newValue;
                setCenterFromTreeItem(newValue.getValue());
            }
        });
    }

    private void setupCellFactoryToTreeView(){
        treeViewItems.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        setGraphic(getGraphicFor(item));

                    }
                }
            };
            return cell ;
        });
    }

    private void setCenterFromTreeItem(String valueItem){
        switch (valueItem) {
            case "Productos":
                AnchorPane rootProductos = (AnchorPane) Util.getWindow("productos/operaciones_productos.fxml",
                        getClass());
                rootProductos.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                rootEscritorio.setCenter(rootProductos);
                break;

            case "Proveedores":
                AnchorPane rootProveedores = (AnchorPane) Util.getWindow(
                        "proveedores/operaciones_proveedores.fxml",
                        getClass()
                );
                rootProveedores.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootProveedores);
                break;

            case "Presentaciones":
                AnchorPane rootPresentaciones = (AnchorPane) Util.getWindow(
                        "presentaciones/operaciones_presentaciones.fxml",
                        getClass()
                );
                rootPresentaciones.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootPresentaciones);
                break;

            case "Dolencias":
                AnchorPane rootDolencias = (AnchorPane) Util.getWindow(
                        "dolencias/operaciones_dolencias.fxml",
                        getClass()
                );
                rootDolencias.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootDolencias);
                break;

            case "Empleados":
                AnchorPane rootEmpleados = (AnchorPane) Util.getWindow(
                        "empleados/operaciones_empleados.fxml",
                        getClass()
                );
                rootEmpleados.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootEmpleados);
                break;

            case "Clientes":
                AnchorPane rootClientes = (AnchorPane) Util.getWindow(
                        "clientes/operaciones_clientes.fxml",
                        getClass()
                );
                rootClientes.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootClientes);
                break;

            case "Gastos":
                AnchorPane rootGastos = (AnchorPane) Util.getWindow(
                        "colocaciones/operaciones_colocaciones.fxml",
                        getClass()
                );
                rootGastos.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootGastos);
                break;

            case "Usuarios":
                AnchorPane rootUsuarios = (AnchorPane) Util.getWindow(
                        "usuarios/operaciones_usuarios.fxml",
                        getClass()
                );
                rootUsuarios.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootUsuarios);
                break;
            case "Punto de Venta":
                //Util.loadWindow("pos/ventas.fxml","Punto de venta",getClass());
                abrirPuntoVenta();
                break;
            case "Colocaciones":
                AnchorPane rootColocaciones = (AnchorPane) Util.getWindow(
                        "colocaciones/operaciones_colocaciones.fxml",
                        getClass()
                );
                rootColocaciones.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootColocaciones);
                break;
            case "Resumen":
                //setResumenCenter();
                break;
            case "De Ventas":
                ScrollPane rootVentas = (ScrollPane) Util.getWindow(
                        "resumen/resumenventas.fxml",getClass());
                rootVentas.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootVentas);
                break;
            case "De Clientes":
                GridPane rootDeClientes = (GridPane) Util.getWindow(
                        "resumen/resumenclientes.fxml",getClass());
                rootDeClientes.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootDeClientes);
                break;
            case "De Productos":
                GridPane rootDeProductos = (GridPane) Util.getWindow(
                        "resumen/resumenproductos.fxml",getClass());
                rootDeProductos.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootDeProductos);
                break;
            case "Tipo de Pago":
                AnchorPane rootTipoPago = (AnchorPane) Util.getWindow(
                        "pago/operaciones_pago.fxml",
                        getClass()
                );
                rootTipoPago.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootTipoPago);
                break;
            case "Ventas":
                GridPane rootPosVentas = (GridPane) Util.getWindow(
                        "ventas/ventas.fxml",getClass());
                rootPosVentas.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootPosVentas);
                break;
            case "Compras":
                abrirCompras();
                break;
            case "Ingreso":
                GridPane rootIngreso = (GridPane) Util.getWindow(
                        "compras/ingreso.fxml",getClass());
                rootIngreso.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootIngreso);
                break;
            case "Devoluciones":
                GridPane rootDevoluciones = (GridPane) Util.getWindow(
                        "devoluciones/devoluciones.fxml",getClass());
                rootDevoluciones.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootDevoluciones);
                break;
            case "Permisos":
                GridPane rootPermisos = (GridPane) Util.getWindow(
                        "permisos/permisos.fxml",getClass());
                rootPermisos.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootPermisos);
                break;
            case "Niveles de Usuario":
                AnchorPane rootNiveles = (AnchorPane) Util.getWindow(
                        "niveles_usuarios/operaciones_niveles.fxml",
                        getClass()
                );
                rootNiveles.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootNiveles);
                break;
            case "Existencia Minima":
                GridPane rootExistencia = (GridPane) Util.getWindow(
                        "resumen/esistencia_minima.fxml",getClass());
                rootExistencia.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootExistencia);
                break;
            case "Modificar Compra":
                GridPane rootModificarCompra = (GridPane) Util.getWindow(
                        "compras/modificar_compra.fxml",getClass());
                rootModificarCompra.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
                rootEscritorio.setCenter(rootModificarCompra);
                break;
            case "Cerrar sesion":
                rootEscritorio.visibleProperty().setValue(false);
                Util.loadWindow("login/login.fxml","Login",getClass());

                break;
        }

    }


    private void setResumenCenter(){
        ScrollPane rootResumen = (ScrollPane) Util.getWindow("resumen/resumenventas.fxml",getClass());
        rootResumen.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        rootEscritorio.setCenter(rootResumen);
    }

    private void abrirCompras(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("compras/compras.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene posScene = new Scene(root);
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setScene(posScene);
        stage.setTitle("Realizar Una Compra");
        stage.setOnHiding((event -> {
            ComprasController.listaDetalleCompra.clear();
            ComprasController.listaProductos.clear();
            ComprasController.listaCodigos.clear();
        }));
        stage.show();
    }

    private void abrirPuntoVenta(){
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("pos/pos.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene posScene = new Scene(root);
        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setScene(posScene);
        stage.setTitle("Punto de Venta");
        stage.setOnHiding((event -> {
            PosController.listaCodigos.clear();
            PosController.listaCompras.clear();
        }));
        stage.show();
    }

    private Node getGraphicFor(String item){
        switch (item){
            case "Sistema":
                return getBlackImageFromEscritorio("24_compania.png");
            case "Resumen":
                return getBlackImageFromEscritorio("24_resumen.png");
            case "Inventario":
                return getBlackImageFromEscritorio("24_inventario.png");
            case "Productos":
                return getBlackImageFromEscritorio("24_productos.png");
            case "Proveedores":
                return getBlackImageFromEscritorio("24_proveedor.png");
            case "Presentaciones":
                return getBlackImageFromEscritorio("24_presentaciones.png");
            case "Dolencias":
                return getBlackImageFromEscritorio("24_dolencias.png");
            case "Empleados":
                return getBlackImageFromEscritorio("24_empleado.png");
            case "Clientes":
                return getBlackImageFromEscritorio("24_clientes.png");
            case "Gastos":
                return getBlackImageFromEscritorio("24_gastos.png");
            case "Principal":
                return getBlackImageFromEscritorio("24_principal.png");
            case "Usuarios":
                return getBlackImageFromEscritorio("24_usuarios.png");
            case "Punto de Venta":
                return getBlackImageFromEscritorio("24_pos.png");
            case "Colocaciones":
                return getBlackImageFromEscritorio("24_vitrina.png");
            case "De Ventas":
                return getBlackImageFromEscritorio("24_graphic.png");
            case "De Productos":
                return getBlackImageFromEscritorio("24_graphic.png");
            case "De Clientes":
                return getBlackImageFromEscritorio("24_graphic.png");
            case "Tipo de Pago":
                return getBlackImageFromEscritorio("24_pago.png");
            case "Niveles de Usuario":
                return getBlackImageFromEscritorio("24_niveles.png");
            case "Permisos":
                return getBlackImageFromEscritorio("24_permisos.png");
            case "Compania":
                return getBlackImageFromEscritorio("24_compania.png");
                default:
                    return getBlackImageFromEscritorio("25_cerrarsesion.png");
        }
    }

    private void setStyleSheetToFXMLDocument(){
        rootEscritorio.getStylesheets().add("escritorio/escritorio.css");
    }

    private Node getBlackImageFromEscritorio(String nombreImagen){
        return Util.getIcon("escritorio/imagenes/black/" + nombreImagen,getClass());
    }

}
