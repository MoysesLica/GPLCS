package KHM;

import charts.BigRoot;
import charts.chartController;
import com.emxsys.chart.LogLineChart;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.awt.Toolkit;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author moyses
 */

public class KHMController {

	public static void generatePropagationConstant(KHM model, Vector x, String axisScale) {
		
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        Vector alpha = model.generateAlpha(x);
        Vector beta = model.generateBeta(x);
        Vector gama = new Vector();
        for(int i = 0; i < x.size(); i++){
            gama.add(Math.sqrt(Math.pow(Double.parseDouble(alpha.get(i).toString()), 2) + Math.pow(Double.parseDouble(beta.get(i).toString()), 2)));
        }
        
        LineChart graphPC;
        
        FlowPane rootPC = new FlowPane();
        
        if(axisScale.contains("Logarithmic"))
            graphPC = chartController.createLogLineChart   (x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude", false);                
        else
            graphPC = chartController.createLinearLineChart(x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude", false);                                    
        
        rootPC.getChildren().add(graphPC);
        
        if(axisScale.contains("Logarithmic"))
            graphPC = chartController.createLogLineChart   (x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude", false);                
        else
            graphPC =chartController.createLinearLineChart(x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude", false);                                    

        rootPC.getChildren().add(graphPC);

        if(axisScale.contains("Logarithmic"))
            graphPC = chartController.createLogLineChart   (x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude", false);                
        else
            graphPC = chartController.createLinearLineChart(x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude", false);                                    

        rootPC.getChildren().add(graphPC);
        
        /*ADDING TABLE OF VALUES*/
        
        TableView<String[]> table = new TableView();
        
        table.setEditable(false);
        
        TableColumn<String[],String> col1 = new TableColumn();
        TableColumn<String[],String> col2 = new TableColumn();
        TableColumn<String[],String> col3 = new TableColumn();
        TableColumn<String[],String> col4 = new TableColumn();
        col1.setText("Frequency");
        col2.setText("Alpha");
        col3.setText("Beta");
        col4.setText("Propagation Constant");
        
        col1.setCellValueFactory((Callback<CellDataFeatures<String[], String>, ObservableValue<String>>) new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>0) {
                    return new SimpleStringProperty(x[0]);
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });

        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>1) {
                    return new SimpleStringProperty(x[1]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });

        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>2) {
                    return new SimpleStringProperty(x[2]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });

        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>3) {
                    return new SimpleStringProperty(x[3]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });
        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.27));

        col1.setResizable(false);
        col2.setResizable(false);
        col3.setResizable(false);
        col4.setResizable(false);
        
        table.getColumns().addAll(col1, col2, col3, col4);

        String[][] data = new String[x.size()][4];
        
        for(int i = 0; i < x.size(); i++) {

            data[i] = new String[]{x.get(i).toString(),alpha.get(i).toString(),beta.get(i).toString(),gama.get(i).toString()};
        	
        }
        
        table.getItems().addAll(Arrays.asList(data));
        table.setMinSize(screenWidth*39, screenHeight*45);
        table.setPrefSize(screenWidth*39, screenHeight*45);
        table.setMaxSize(screenWidth*39, screenHeight*45);

        rootPC.getChildren().add(table);

        /*ADDING SCROLL TO SCENE*/
        ScrollPane scrollPanePC = new ScrollPane();
        scrollPanePC.setContent(rootPC);
        scrollPanePC.setPannable(true);
        scrollPanePC.setFitToWidth(true);
        scrollPanePC.setFitToHeight(true);

        Scene scenePC = new Scene(scrollPanePC, screenWidth*80, screenHeight*80);

        Stage chartPC = new Stage();
        chartPC.setScene(scenePC);
        chartPC.show();
		
	}
	
	public static void generateCharacteristicImpedance(KHM model, Vector x, String axisScale) {

		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
		Vector real = model.generateRealCharacteristicImpedance(x);
        Vector imag = model.generateImagCharacteristicImpedance(x);
        Vector CI = new Vector();
        for(int i = 0; i < x.size(); i++){
            CI.add(Math.sqrt(Math.pow(Double.parseDouble(real.get(i).toString()), 2) + Math.pow(Double.parseDouble(imag.get(i).toString()), 2)));
        }
        
        LineChart graphCI;
        
        FlowPane rootCI = new FlowPane();
        
        if(axisScale.contains("Logarithmic"))
            graphCI = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude", false);                
        else
            graphCI = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude", false);                                    
        rootCI.getChildren().add(graphCI);
        
        if(axisScale.contains("Logarithmic"))
            graphCI = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude", false);                
        else
            graphCI =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude", false);                                    

        rootCI.getChildren().add(graphCI);

        if(axisScale.contains("Logarithmic"))
            graphCI = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude", false);                
        else
            graphCI = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude", false);                                    

        rootCI.getChildren().add(graphCI);
        
        /*ADDING TABLE OF VALUES*/
        
        TableView<String[]> table = new TableView();
        
        table.setEditable(false);
        
        TableColumn<String[],String> col1 = new TableColumn();
        TableColumn<String[],String> col2 = new TableColumn();
        TableColumn<String[],String> col3 = new TableColumn();
        TableColumn<String[],String> col4 = new TableColumn();
        col1.setText("Frequency");
        col2.setText("Real");
        col3.setText("Imaginary");
        col4.setText("Characteristic Impedance");
        
        col1.setCellValueFactory((Callback<CellDataFeatures<String[], String>, ObservableValue<String>>) new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>0) {
                    return new SimpleStringProperty(x[0]);
                } else {
                    return new SimpleStringProperty("<no name>");
                }
            }
        });

        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>1) {
                    return new SimpleStringProperty(x[1]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });

        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>2) {
                    return new SimpleStringProperty(x[2]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });

        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> p) {
                String[] x = p.getValue();
                if (x != null && x.length>3) {
                    return new SimpleStringProperty(x[3]);
                } else {
                    return new SimpleStringProperty("<no value>");
                }
            }
        });
        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.27));

        col1.setResizable(false);
        col2.setResizable(false);
        col3.setResizable(false);
        col4.setResizable(false);
        
        table.getColumns().addAll(col1, col2, col3, col4);

        String[][] data = new String[x.size()][4];
        
        for(int i = 0; i < x.size(); i++) {

            data[i] = new String[]{x.get(i).toString(),real.get(i).toString(),imag.get(i).toString(),CI.get(i).toString()};
        	
        }
        
        table.getItems().addAll(Arrays.asList(data));
        table.setMinSize(screenWidth*39, screenHeight*45);
        table.setPrefSize(screenWidth*39, screenHeight*45);
        table.setMaxSize(screenWidth*39, screenHeight*45);

        rootCI.getChildren().add(table);

        ScrollPane scrollPaneCI = new ScrollPane();
        scrollPaneCI.setContent(rootCI);
        scrollPaneCI.setPannable(true);
        scrollPaneCI.setFitToWidth(true);
        scrollPaneCI.setFitToHeight(true);

        Scene sceneCI = new Scene(scrollPaneCI, screenWidth*80, screenHeight*80);

        Stage chartCI = new Stage();
        chartCI.setScene(sceneCI);
        chartCI.show();
		
	}
	
	public static void generateTransferFunction(KHM model, Vector x, String axisScale) {

		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        Vector TF = model.generatePropagationLoss(x);
        
        LineChart graphTF;
        
        FlowPane rootTF = new FlowPane();
        
        if(axisScale.contains("Logarithmic"))
            graphTF = chartController.createLogLineChart   (x, TF, "Propagation Loss", "Propagation Loss", "Frequency (Hz)", "dB", false);                
        else
            graphTF = chartController.createLinearLineChart(x, TF, "Propagation Loss", "Propagation Loss", "Frequency (Hz)", "dB", false);                                    

        rootTF.getChildren().add(graphTF);

        ScrollPane scrollPaneTF = new ScrollPane();
        scrollPaneTF.setContent(rootTF);
        scrollPaneTF.setPannable(true);
        scrollPaneTF.setFitToWidth(true);
        scrollPaneTF.setFitToHeight(true);

        Scene sceneTF = new Scene(scrollPaneTF, screenWidth*80, screenHeight*80);

        Stage chartTF = new Stage();
        chartTF.setScene(sceneTF);
        chartTF.show();

		
	}
	
    public static void generateGraphs(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

        Vector x  = new Vector();
        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
        
        switch(parameterCalc){
        
            case "Propagation Constant":

            	KHMController.generatePropagationConstant(model, x, axisScale);
                
                break;
                
            case "Characteristic Impedance":
                
            	KHMController.generateCharacteristicImpedance(model, x, axisScale);
                
                break;
                
            case "Transfer Function":

            	KHMController.generateTransferFunction(model, x, axisScale);
            	
                break;
                
            default:
                
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                
                break;
                
        }
        
    }
    
}
