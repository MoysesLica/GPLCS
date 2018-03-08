package KHM;

import charts.BigRoot;
import charts.chartController;
import com.emxsys.chart.LogLineChart;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
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
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * @author moyses
 */

public class KHMController {

	/*FUNCTION TO GENERATE OUTPUT FILE*/
	public static void generateOutputFile(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc, File file) throws FileNotFoundException {

		String fileName = file.getAbsolutePath().replace("\\", "\\\\");
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		Vector x  = new Vector();
        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
		
        String[][] data = null;
        
		String content = "";

        switch(parameterCalc){
	        case "Propagation Constant":
	        	Vector alpha = model.generateAlphaPropagationConstant(x);
	            Vector beta = model.generateBetaPropagationConstant(x);
	            Vector gama = new Vector();
	            for(int i = 0; i < x.size(); i++){
	                gama.add(Math.sqrt(Math.pow(Double.parseDouble(alpha.get(i).toString()), 2) + Math.pow(Double.parseDouble(beta.get(i).toString()), 2)));
	            }
	            data = new String[x.size()][4];
	            for(int i = 0; i < x.size(); i++) {
	                data[i] = new String[]{x.get(i).toString(),alpha.get(i).toString(),beta.get(i).toString(),gama.get(i).toString()};
	            }
	            content = String.format("|%30s|%30s|%30s|%30s|\n", "Frequency(Hz)","Alpha","Beta","Propagation Constant");
	            break;
	        case "Characteristic Impedance":
	        	Vector real = model.generateRealCharacteristicImpedance(x);
	            Vector imag = model.generateImagCharacteristicImpedance(x);
	            Vector CI = new Vector();
	            for(int i = 0; i < x.size(); i++){
	                CI.add(Math.sqrt(Math.pow(Double.parseDouble(real.get(i).toString()), 2) + Math.pow(Double.parseDouble(imag.get(i).toString()), 2)));
	            }
	            data = new String[x.size()][4];
	            
	            for(int i = 0; i < x.size(); i++) {
	
	                data[i] = new String[]{x.get(i).toString(),real.get(i).toString(),imag.get(i).toString(),CI.get(i).toString()};
	            	
	            }
	            content = String.format("|%30s|%30s|%30s|%30s|\n", "Frequency(Hz)","Real","Imaginary","Characteristic Impedance");
	            break;
	        case "Transfer Function":
	        	Vector TF = model.generateTransferFunctionGain(x);
	            data = new String[x.size()][2];            
	            for(int i = 0; i < x.size(); i++) {
	                data[i] = new String[]{x.get(i).toString(),TF.get(i).toString()};
	            }
	            content = String.format("|%30s|%30s|\n", "Frequency(Hz)","Transfer Function Gain");
	            break;
	        default:
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Error");
	                alert.setHeaderText("Parameter to calculate invalid: " + parameterCalc);
	                alert.showAndWait();
	            break;
        }		
        
        for(int i = 0; i < data.length; i++) {
        	content += "|";
        	for(int j = 0; j < data[i].length; j++) {
            	content += String.format("%30s|", data[i][j]);
        	}
        	content += "\n";
        }
        
        // Creating a File object that represents the disk file.
        PrintStream o = new PrintStream(new File(fileName));
 
        // Store current System.out before assigning a new value
        PrintStream console = System.out;
 
        // Assign o to output stream
        System.setOut(o);
        System.out.println(content);
 
        // Use stored value for output stream
        System.setOut(console);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File saved!");
        alert.setHeaderText("File saved with success!");
        alert.showAndWait();
        
	}
	
	/*FUNCTION TO GENERATE PROPAGATION CONSTANT*/
	public static void generatePropagationConstant(KHM model, Vector x, String axisScale) {
		
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector alpha = model.generateAlphaPropagationConstant(x);
        Vector beta = model.generateBetaPropagationConstant(x);
        Vector gama = model.generatePropagationConstant(x, alpha, beta);
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, ALPHA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Propagation Constant - Alpha");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude", false);                
        else
            graph =chartController.createLinearLineChart(x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Propagation Constant - Beta");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude", false);                
        else
            graph = chartController.createLinearLineChart(x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        /*CREATING THE FOUR COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        TableColumn<String[],String> col2 = new TableColumn();
        TableColumn<String[],String> col3 = new TableColumn();
        TableColumn<String[],String> col4 = new TableColumn();
        col1.setText("Frequency");
        col2.setText("Alpha");
        col3.setText("Beta");
        col4.setText("Propagation Constant");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback<CellDataFeatures<String[],String>,ObservableValue<String>>)new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>0){return new SimpleStringProperty(x[0]);}else{return new SimpleStringProperty("<no name>");}}});
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>1){return new SimpleStringProperty(x[1]);}else{return new SimpleStringProperty("<no value>");}}});
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>2){return new SimpleStringProperty(x[2]);}else{return new SimpleStringProperty("<no value>");}}});
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>3){return new SimpleStringProperty(x[3]);}else{return new SimpleStringProperty("<no value>");}}});        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col1.setResizable(false);
        col2.setResizable(false);
        col3.setResizable(false);
        col4.setResizable(false);
        
        /*ADDING COLUMNS TO TABLE*/
        table.getColumns().addAll(col1, col2, col3, col4);
        
        /*ADDING INFORMATION TO COLUMNS*/
        String[][] data = new String[x.size()][4];        
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),alpha.get(i).toString(),beta.get(i).toString(),gama.get(i).toString()};
        }
        table.getItems().addAll(Arrays.asList(data));

        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(table);
        tabPane.getTabs().add(tab4);

        /*ADDING SCROLL TO SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tabPane);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        /*ADDING ENCLOSER TO ELEMENTS*/
        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(scrollPane);

        /*ADDING ELEMENTS TO SCENE AND SHOW STAGE*/
        root.getChildren().add(borderPane);
        Stage chart = new Stage();
        chart.setScene(scene);
        chart.show();
		
	}
	
	/*FUNCTION TO GENERATE CHARACTERISTIC IMPEDANCE*/
	public static void generateCharacteristicImpedance(KHM model, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
		Vector real = model.generateRealCharacteristicImpedance(x);
        Vector imag = model.generateImagCharacteristicImpedance(x);
        Vector CI = model.generateCharacteristicImpedance(x, real, imag);
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Propagation Constant - Alpha");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Propagation Constant - Beta");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*ADDING TABLE OF VALUES*/
        TableView<String[]> table = new TableView();
        table.setEditable(false);
        
        /*CREATING THE FOUR COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        TableColumn<String[],String> col2 = new TableColumn();
        TableColumn<String[],String> col3 = new TableColumn();
        TableColumn<String[],String> col4 = new TableColumn();
        col1.setText("Frequency");
        col2.setText("Real");
        col3.setText("Imaginary");
        col4.setText("Characteristic Impedance");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback<CellDataFeatures<String[],String>,ObservableValue<String>>)new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>0){return new SimpleStringProperty(x[0]);}else{return new SimpleStringProperty("<no name>");}}});
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>1){return new SimpleStringProperty(x[1]);}else{return new SimpleStringProperty("<no value>");}}});
        col3.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>2){return new SimpleStringProperty(x[2]);}else{return new SimpleStringProperty("<no value>");}}});
        col4.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>3){return new SimpleStringProperty(x[3]);}else{return new SimpleStringProperty("<no value>");}}});        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col3.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col4.prefWidthProperty().bind(table.widthProperty().multiply(0.27));
        col1.setResizable(false);
        col2.setResizable(false);
        col3.setResizable(false);
        col4.setResizable(false);
        
        /*ADDING COLUMNS TO TABLE*/
        table.getColumns().addAll(col1, col2, col3, col4);

        /*ADDING INFORMATION TO COLUMNS*/
        String[][] data = new String[x.size()][4];
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),real.get(i).toString(),imag.get(i).toString(),CI.get(i).toString()};
        }
        table.getItems().addAll(Arrays.asList(data));
        
        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(table);
        tabPane.getTabs().add(tab4);
        
        /*ADDING SCROLL TO SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tabPane);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        /*ADDING ENCLOSER TO ELEMENTS*/
        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(scrollPane);

        /*ADDING ELEMENTS TO SCENE AND SHOW STAGE*/
        root.getChildren().add(borderPane);
        Stage chart = new Stage();
        chart.setScene(scene);
        chart.show();
		
	}
	
	/*FUNCTION TO GENERATE TRANSFER FUNCTION*/
	public static void generateTransferFunction(KHM model, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector TF = model.generateTransferFunctionGain(x);
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, TRANSFER FUNCTION GAIN*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, TF, "Transfer Function Gain", "Transfer Function Gain(dB)", "Frequency (Hz)", "dB", false);                
        else
            graph = chartController.createLinearLineChart(x, TF, "Transfer Function Gain", "Transfer Function Gain(dB)", "Frequency (Hz)", "dB", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Propagation Constant - Alpha");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*ADDING TABLE OF VALUES*/
        TableView<String[]> table = new TableView();
        table.setEditable(false);
        
        /*CREATING THE TWO COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        TableColumn<String[],String> col2 = new TableColumn();
        col1.setText("Frequency");
        col2.setText("Transfer Function Gain(dB)");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback<CellDataFeatures<String[],String>,ObservableValue<String>>)new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>0){return new SimpleStringProperty(x[0]);}else{return new SimpleStringProperty("<no name>");}}});
        col2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[],String>,ObservableValue<String>>(){public ObservableValue<String>call(TableColumn.CellDataFeatures<String[],String>p){String[]x=p.getValue();if(x!=null&&x.length>1){return new SimpleStringProperty(x[1]);}else{return new SimpleStringProperty("<no value>");}}});        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        col2.prefWidthProperty().bind(table.widthProperty().multiply(0.70));        
        col1.setResizable(false);
        col2.setResizable(false);
        
        /*ADDING COLUMNS TO TABLE*/
        table.getColumns().addAll(col1, col2);

        /*ADDING INFORMATION TO COLUMNS*/
        String[][] data = new String[x.size()][2];
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),TF.get(i).toString()};
        }        
        table.getItems().addAll(Arrays.asList(data));

        /*ADDING TABLE TO TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Values");
        tab2.setContent(table);
        tabPane.getTabs().add(tab2);

        /*ADDING SCROLL TO SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(tabPane);
        scrollPane.setPannable(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        /*ADDING ENCLOSER TO ELEMENTS*/
        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(scrollPane);

        /*ADDING ELEMENTS TO SCENE AND SHOW STAGE*/
        root.getChildren().add(borderPane);
        Stage chart = new Stage();
        chart.setScene(scene);
        chart.show();
		
	}
	
 
	/*FUNCTION TO SELECT THE GRAPH TO DISPLAY*/
    public static void generateGraphs(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

    	/*CREATE THE AXIS X VALUES*/
        Vector x  = new Vector();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE CABLE MODEL*/
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
        
        /*CHOOSE CHART TO DISPLAY*/
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
