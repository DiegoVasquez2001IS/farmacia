package resumen.ventas;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import reportes.Reporte;
import resumen.ventas.model.Venta;
import resumen.ventas.model.VentaDAO;
import resumen.ventas.model.VentaDiaReporte;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by user on 2/06/2017.
 */
public class ResumenVentasController implements Initializable{

    @FXML
    private DatePicker txtDeste;

    @FXML
    private DatePicker txtHasta;

    @FXML
    private Button btnVer;

    @FXML
    private RadioButton rdbVentasDia;

    @FXML
    private ToggleGroup tablaVentas;

    @FXML
    private RadioButton rdbVentasSemana;

    @FXML
    private RadioButton rdbVentasMes;

    @FXML
    private LineChart<String, Number> grapDatos;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField txtFiltrar;

    @FXML
    private TableView<Venta> tblDatos;

    @FXML
    private TableColumn<Venta, Integer> colIdOrden;

    @FXML
    private TableColumn<Venta, String> colFecha;

    @FXML
    private TableColumn<Venta, String> colCliente;

    @FXML
    private TableColumn<Venta, String> colEmpleado;

    @FXML
    private TableColumn<Venta, Float> colTotal;

    @FXML
    private TableColumn<Venta, String> colPago;

    @FXML
    private TableColumn<Venta, String> colEstatus;

    @FXML
    private Label lblCompra;

    @FXML
    private Label lblLucro;

    @FXML
    private Button btnReporte;

    @FXML
    private CheckBox checkImprimir;

    @FXML
    private CheckBox checkGuardar;

    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();

    private VentaDAO ventaDAO = new VentaDAO();

    private ObservableList<XYChart.Series<String,Number>> datosGraph = FXCollections.observableArrayList();

    XYChart.Series<String,Number> series = new XYChart.Series<>();

    Reporte reporte = new Reporte();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matchDataWithTable();
        initTableAndGraphValues();
        initGraph();
        convertDataPicker();
        configFiltrado();
        configGraphListener();
        initLucro();

        rdbVentasDia.setOnAction((event -> {
            addDataToList(VentaDAO.VENTASDIA);
            lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRADIA)+"");
            lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCRODIA)+"");
            lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALDIA) + "");
        }));
        rdbVentasSemana.setOnAction((event -> {
            addDataToList(VentaDAO.VENTASSEMANA);
            lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRASEMANA) +"");
            lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCROSEMANA)+"");
            lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALSEMANA) + "");
        }));
        rdbVentasMes.setOnAction((event -> {
            addDataToList(VentaDAO.VENTASMES);
            lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRAMES)+"");
            lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCROMES)+"");
            lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALMES)+"");
        }));

        btnVer.setOnAction((event -> {
            if (txtDeste.getValue() != null && txtHasta.getValue() != null){
                ventaDAO.llamarVentasRango(txtDeste.getValue().toString(),txtHasta.getValue().toString());
                listaVentas.setAll(ventaDAO.getDatos(VentaDAO.VENTASRANGO));
                lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALRANGO)+"");
                lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRARANGO)+"");
                lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCRORANGO)+"");
            }else if(txtDeste.getValue() != null && txtHasta.getValue() == null){
                ventaDAO.llamarVentasDiaRango(txtDeste.getValue().toString());
                listaVentas.setAll(ventaDAO.getDatos(VentaDAO.VENTASDIARANGO));
                lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALDIARANGO) + "");
                lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRADIARANGO)+"");
                lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCRODIARANGO)+"");
            } else if(txtDeste.getValue() == null && txtHasta.getValue() == null){
                lblCompra.setText(ventaDAO.getCompra(VentaDAO.COSTOINVENTARIOCOMPRA)+"");
                lblLucro.setText(ventaDAO.getLucro(VentaDAO.COSTOINVENTARIOLUCRO)+"");
                lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.COSTOINVENTARIOVENTA) +"");
            }
        }));

        btnReporte.setOnAction((event -> {
            if(rdbVentasDia.isSelected()){
                if(listaVentas.size() > 0){
                    generarReporte(listaVentas,"Ventas del " + getFechaHoy(),checkGuardar.isSelected(),
                            checkImprimir.isSelected());
                }
            }else if(rdbVentasSemana.isSelected()){
                if(listaVentas.size() > 0){
                    generarReporte(listaVentas,"Ventas de la semana",checkGuardar.isSelected(),
                            checkImprimir.isSelected());
                }
            }else if(rdbVentasMes.isSelected()){
                if(listaVentas.size() > 0){
                    generarReporte(listaVentas,"Ventas del mes",checkGuardar.isSelected(),
                            checkImprimir.isSelected());
                }
            }
        }));
    }

    private void initLucro(){
        lblCompra.setText(ventaDAO.getCompra(VentaDAO.COMPRADIA)+"");
        lblLucro.setText(ventaDAO.getLucro(VentaDAO.LUCRODIA)+"");
        //lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALDIA) + "");
    }

    public String getFechaHoy(){
        DateFormat df = new SimpleDateFormat("EEEE dd-MM");
        Date date = new Date();
        return df.format(date);
    }

    private void initTableAndGraphValues(){
        addDataToList(VentaDAO.VENTASDIA);
        lblTotal.setText(ventaDAO.getTotalVenta(VentaDAO.TOTALDIA) + "");
    }

    private void generarReporte(List<Venta> lista,String titulo,
                                boolean guardar, boolean imprimir){
        ObservableList<VentaDiaReporte> listaDia = FXCollections.observableArrayList();
        listaDia.clear();
        float numeroTotal = 0;
        for(Venta venta : lista){
            listaDia.add(new VentaDiaReporte(venta.getIdOrdenPedido()+"",
                    venta.getNomCliente(),venta.getNomEmpleado(),venta.getFecha(),venta.getTotal()+""));
            numeroTotal+= venta.getTotal();
        }

        reporte.reporteVentasDia(listaDia,titulo,String.valueOf(getDosDecimales(numeroTotal)),guardar,imprimir);
    }

    private float getDosDecimales(float numero){
        float f = Float.parseFloat(String.format("%.2f",numero));
        return f;
    }


    public void convertDataPicker(){
        String pattern = "yyyy-MM-dd";
        txtDeste.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        txtHasta.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void initGraph(){
        grapDatos.setTitle("GRAFICA VENTAS");
        series.setName("Ventas");
        for(Venta venta: listaVentas){
            series.getData().add(new XYChart.Data<>(venta.getFecha().substring(0,venta.getFecha().indexOf(" ")),
                    venta.getTotal()));
        }
        datosGraph.add(series);
        grapDatos.setData(datosGraph);
    }

    private void configGraphListener(){
        listaVentas.addListener(new ListChangeListener<Venta>() {
            @Override
            public void onChanged(Change<? extends Venta> c) {
                series.getData().clear();
                for (Venta venta : listaVentas){
                    series.getData().add(new XYChart.Data<>(venta.getFecha().substring(0,venta.getFecha().indexOf(" ")),
                            venta.getTotal()));
                }
            }
        });
        datosGraph.setAll(series);
        grapDatos.setData(datosGraph);
    }

    private void configFiltrado(){
        FilteredList<Venta> filteredList = new FilteredList<Venta>(listaVentas, v -> true);

        txtFiltrar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(venta -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(venta.getIdOrdenPedido()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (venta.getFecha().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(venta.getNomEmpleado()).toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (venta.getNomCliente().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (venta.getEstatus().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                } else if (venta.getTipoPago().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false; // Does not match.
            });
        });

        SortedList<Venta> sortedList = new SortedList<Venta>(filteredList);
        sortedList.comparatorProperty().bind(tblDatos.comparatorProperty());
        tblDatos.setItems(sortedList);
    }

    private void matchDataWithTable(){
        colIdOrden.setCellValueFactory(new PropertyValueFactory<Venta, Integer>("idOrdenPedido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Venta, String>("fecha"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Venta, String>("nomCliente"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Venta, String>("nomEmpleado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Venta, Float>("total"));
        colPago.setCellValueFactory(new PropertyValueFactory<Venta, String>("tipoPago"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<Venta, String>("estatus"));
        tblDatos.setItems(listaVentas);
        tblDatos.getColumns().clear();
        tblDatos.getColumns().addAll(colIdOrden,colFecha,colCliente,colEmpleado,colTotal,
                colPago,colEstatus);
    }

    private void addDataToList(int tipoConsulta){
        listaVentas.setAll(ventaDAO.getDatos(tipoConsulta));
    }
}
