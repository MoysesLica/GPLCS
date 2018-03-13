package KHM;

import charts.BigRoot;
import charts.chartController;
import chart.LogLineChart;

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
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
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
import simulador.gplcs.SimuladorGPLCS;

/**
 * @author moyses
 */

public class KHMController {

	/*FUNCTION TO GENERATE OUTPUT FILE*/
	public static void generateOutputFile(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc, File file) throws FileNotFoundException {

		/*GET THE PATH AND NAME OF SAVED FILE*/
		String fileName = file.getAbsolutePath().replace("\\", "\\\\");
		
		/*GENERATE FREQUENCY*/
		Vector x  = new Vector();
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE MODEL OF CABLE*/
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
		
        /*CREATE THE VAR OF DATA AND CONTENT THAT WILL BE WRITED ON FILE*/
        String[][] data = null;
		String content = "";

		/*CHOOSE WHAT PARAMETER WILL BE CALCULATED*/
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
	            content = String.format("|%30s|%30s|%30s|%30s|\n", "Frequency(Hz)","Attenuation Constant(Np/m)","Phase Constant(Rad/m)","Propagation Constant()");
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
	            content = String.format("|%30s|%30s|%30s|%30s|\n", "Frequency(Hz)","Real(Ω)","Imaginary(Ω)","Characteristic Impedance(Ω)");
	            break;
	        case "Transfer Function":
	        	Vector TF = model.generateTransferFunctionGain(x);
	            data = new String[x.size()][2];            
	            for(int i = 0; i < x.size(); i++) {
	                data[i] = new String[]{x.get(i).toString(),TF.get(i).toString()};
	            }
	            content = String.format("|%30s|%30s|\n", "Frequency(Hz)","Transfer Function Gain(dB)");
	            break;
	        default:
	                Alert alert = new Alert(Alert.AlertType.ERROR);
	                alert.setTitle("Error");
	                alert.setHeaderText("Parameter to calculate invalid: " + parameterCalc);
	                alert.showAndWait();
	            break;
        }		
        
        /*FORMAT THE CONTENT TO BE WRITED*/
        for(int i = 0; i < data.length; i++) {
        	content += "|";
        	for(int j = 0; j < data[i].length; j++) {
            	content += String.format("%30s|", data[i][j]);
        	}
        	content += "\n";
        }
        
        /*CHANGE SYSTEM OUTPUT AND WRITE FILE*/
        PrintStream o = new PrintStream(new File(fileName));
        PrintStream console = System.out;
        System.setOut(o);
        System.out.println(content);
        /*RE-CHANGE SYSTEM OUTPUT AND FINALIZE FUNCTION WITH ALERT*/
        System.setOut(console);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("File saved!");
        alert.setHeaderText("File saved with success!");
        alert.showAndWait();
        
	}
	
	/*FUNCTION TO GENERATE PROPAGATION CONSTANT'S GRAPHS*/
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
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "Nepers/Meter", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "Nepers/Meter", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", "Phase Constant", "Frequency (Hz)", "Radians/Meter", false);                
        else
            graph = chartController.createLinearLineChart(x, beta,  "Phase Constant", "Phase Constant", "Frequency (Hz)", "Radians/Meter", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "I don't know the unit", false);                
        else
            graph = chartController.createLinearLineChart(x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "I don't know the unit", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant");
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
        col1.setText("Frequency(Hz)");
        col2.setText("Attenuation Constant(Np/m)");
        col3.setText("Phase Constant(rad/m)");
        col4.setText("Propagation Constant()");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 0) {
              return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
             } else {
              return new SimpleStringProperty("<no name>");
             }
            }
           });
           col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 1) {
              return new SimpleStringProperty(x[1]);
             } else {
              return new SimpleStringProperty("<no value>");
             }
            }
           });
           col3.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 2) {
              return new SimpleStringProperty(x[2]);
             } else {
              return new SimpleStringProperty("<no value>");
             }
            }
           });
           col4.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 3) {
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
        
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Propagation Constant");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        chart.show();
		chart.toFront();

	}

	/*FUNCTION TO GENERATE PROPAGATION CONSTANT'S GRAPHS FOR MULTIPLES CABLES*/
	public static void generatePropagationConstant(Vector headings, Vector models, Vector x, String axisScale) {
		
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector alpha = new Vector();
        Vector beta = new Vector();
        Vector gama = new Vector();
        
        for(int i = 0; i < models.size(); i++) {
        	
        	Vector addToAlpha = ((KHM)models.get(i)).generateAlphaPropagationConstant(x);
        	Vector addToBeta = ((KHM)models.get(i)).generateBetaPropagationConstant(x);
        	
        	alpha.add(addToAlpha);
        	beta.add(addToBeta);
        	gama.add(((KHM)models.get(i)).generatePropagationConstant(x, addToAlpha, addToBeta));
        }
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, ALPHA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "Nepers/Meter", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "Nepers/Meter", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", headings, "Frequency (Hz)", "Radians/Meter", false);                
        else
            graph = chartController.createLinearLineChart(x, beta,  "Phase Constant", headings, "Frequency (Hz)", "Radians/Meter", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, gama, "Propagation Constant", headings, "Frequency (Hz)", "I don't know the unit", false);                
        else
            graph = chartController.createLinearLineChart(x, gama, "Propagation Constant", headings, "Frequency (Hz)", "I don't know the unit", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        /*CREATING THE COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        col1.setText("Frequency(Hz)");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
	         String[] x = p.getValue();
	         if (x != null && x.length > 0) {
	        	 return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
	         } else {
	          return new SimpleStringProperty("<no name>");
	         }
	        }
	       });
        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col1.setResizable(false);

        table.getColumns().add(col1);        
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
        	/*ADD COLUN GROUP LABEL*/
            TableColumn<String[],String> col = new TableColumn();
            col.setText(headings.get(k).toString());
            col.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            	 public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
            	  String[] x = p.getValue();
            	  if (x != null && x.length > 3) {
            	   return new SimpleStringProperty(x[3]);
            	  } else {
            	   return new SimpleStringProperty("<no value>");
            	  }
            	 }
            	});
            col.setResizable(false);

            /*CREATING THE FOUR COLUMNS OF TABLE*/
            TableColumn<String[],String> col2 = new TableColumn();
            TableColumn<String[],String> col3 = new TableColumn();
            TableColumn<String[],String> col4 = new TableColumn();
            col2.setText("Attenuation Constant(Np/m)");
            col3.setText("Phase Constant(rad/m)");
            col4.setText("Propagation Constant()");
            
            /*CONFIG THE COLUMNS*/
            Vector columnOffset = new Vector();
            columnOffset.add(k);
            
            col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
               col3.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (2 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[2 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
               col4.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (3 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[3 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
            col2.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col3.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col4.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col2.setResizable(false);
            col3.setResizable(false);
            col4.setResizable(false);
            
	        /*ADDING COLUMNS TO TABLE*/
	        table.getColumns().add(col);
	        col.getColumns().addAll(col2, col3, col4);
	        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 3)] = ((Vector)alpha.get(k)).get(i).toString();
	            data[i][2 + (k * 3)] = ((Vector) beta.get(k)).get(i).toString();
	            data[i][3 + (k * 3)] = ((Vector) gama.get(k)).get(i).toString();
	        }
        	
        }

        /*ADDING INFORMATION TO COLUMNS*/
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
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Propagation Constant");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        chart.show();
		chart.toFront();

	}
	
	
	/*FUNCTION TO GENERATE CHARACTERISTIC IMPEDANCE'S GRAPHS*/
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
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance", "Characteristic Impedance", "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance", "Characteristic Impedance", "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance");
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
        col1.setText("Frequency(Hz)");
        col2.setText("Real(Ω)");
        col3.setText("Imaginary(Ω)");
        col4.setText("Characteristic Impedance(Ω)");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 0) {
              return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
             } else {
              return new SimpleStringProperty("<no name>");
             }
            }
           });
           col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 1) {
              return new SimpleStringProperty(x[1]);
             } else {
              return new SimpleStringProperty("<no value>");
             }
            }
           });
           col3.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 2) {
              return new SimpleStringProperty(x[2]);
             } else {
              return new SimpleStringProperty("<no value>");
             }
            }
           });
           col4.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 3) {
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
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Characteristic Impedance");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
		
	}
	
	/*FUNCTION TO GENERATE CHARACTERISTIC IMPEDANCE'S GRAPHS FOR MULTIPLES CABLES*/
	public static void generateCharacteristicImpedance(Vector headings, Vector models, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/

        /*GET ALL PARAMETERS TO PLOT*/
        Vector real = new Vector();
        Vector imag = new Vector();
        Vector CI = new Vector();
        
        for(int i = 0; i < models.size(); i++) {
        	
        	Vector addToReal = ((KHM)models.get(i)).generateRealCharacteristicImpedance(x);
        	Vector addToImag = ((KHM)models.get(i)).generateImagCharacteristicImpedance(x);
        	
        	real.add(addToReal);
        	imag.add(addToImag);
        	CI.add(((KHM)models.get(i)).generateCharacteristicImpedance(x, addToReal, addToImag));
        }

        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", headings, "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", headings, "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", headings, "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", headings, "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance", headings, "Frequency (Hz)", "Ω(Ohms)", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance", headings, "Frequency (Hz)", "Ω(Ohms)", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        /*CREATING THE COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        col1.setText("Frequency(Hz)");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
	         String[] x = p.getValue();
	         if (x != null && x.length > 0) {
	        	 return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
	         } else {
	          return new SimpleStringProperty("<no name>");
	         }
	        }
	       });
        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col1.setResizable(false);

        table.getColumns().add(col1);        
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
        	/*ADD COLUN GROUP LABEL*/
            TableColumn<String[],String> col = new TableColumn();
            col.setText(headings.get(k).toString());
            col.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            	 public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
            	  String[] x = p.getValue();
            	  if (x != null && x.length > 3) {
            	   return new SimpleStringProperty(x[3]);
            	  } else {
            	   return new SimpleStringProperty("<no value>");
            	  }
            	 }
            	});
            col.setResizable(false);

            /*CREATING THE FOUR COLUMNS OF TABLE*/
            TableColumn<String[],String> col2 = new TableColumn();
            TableColumn<String[],String> col3 = new TableColumn();
            TableColumn<String[],String> col4 = new TableColumn();
            col2.setText("Real(Ω)");
            col3.setText("Imaginary(Ω)");
            col4.setText("Characteristic Impedance(Ω)");

            /*CONFIG THE COLUMNS*/
            Vector columnOffset = new Vector();
            columnOffset.add(k);
            
            col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
               col3.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (2 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[2 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
               col4.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (3 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[3 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
            col2.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col3.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col4.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col2.setResizable(false);
            col3.setResizable(false);
            col4.setResizable(false);
            
	        /*ADDING COLUMNS TO TABLE*/
	        table.getColumns().add(col);
	        col.getColumns().addAll(col2, col3, col4);
	        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 3)] = ((Vector)real.get(k)).get(i).toString();
	            data[i][2 + (k * 3)] = ((Vector)imag.get(k)).get(i).toString();
	            data[i][3 + (k * 3)] = ((Vector)  CI.get(k)).get(i).toString();
	        }
        	
        }

        /*ADDING INFORMATION TO COLUMNS*/
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
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Characteristic Impedance");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();

	}
	
	/*FUNCTION TO GENERATE TRANSFER FUNCTION'S GRAPHS*/
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
        tab1.setText("Transfer Function Gain");
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
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 0) {
              return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
             } else {
              return new SimpleStringProperty("<no name>");
             }
            }
           });
           col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
            public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
             String[] x = p.getValue();
             if (x != null && x.length > 1) {
              return new SimpleStringProperty(x[1]);
             } else {
              return new SimpleStringProperty("<no value>");
             }
            }
           });
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
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Transfer Function");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
        
	}
 
	/*FUNCTION TO GENERATE TRANSFER FUNCTION'S GRAPHS FOR MULTIPLES CABLES*/
	public static void generateTransferFunction(Vector headings, Vector models, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector TF = new Vector();
        
        for(int i = 0; i < models.size(); i++) {	
        	TF.add(((KHM)models.get(i)).generateTransferFunctionGain(x));
        }
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, TRANSFER FUNCTION GAIN*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, TF, "Transfer Function Gain", headings, "Frequency (Hz)", "dB", false);                
        else
            graph = chartController.createLinearLineChart(x, TF, "Transfer Function Gain", headings, "Frequency (Hz)", "dB", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Transfer Function Gain");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        /*CREATING THE COLUMNS OF TABLE*/
        TableColumn<String[],String> col1 = new TableColumn();
        col1.setText("Frequency(Hz)");
        
        /*CONFIG THE COLUMNS*/
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
	         String[] x = p.getValue();
	         if (x != null && x.length > 0) {
	        	 return new SimpleStringProperty(String.format("%.1f", Double.parseDouble(x[0].toString())));
	         } else {
	          return new SimpleStringProperty("<no name>");
	         }
	        }
	       });
        
        col1.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        col1.setResizable(false);

        table.getColumns().add(col1);        
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
        	/*ADD COLUN GROUP LABEL*/
            TableColumn<String[],String> col = new TableColumn();
            col.setText(headings.get(k).toString());
            col.setResizable(false);

            /*CREATING THE FOUR COLUMNS OF TABLE*/
            TableColumn<String[],String> col2 = new TableColumn();
            col2.setText("Transfer Function Gain (dB)");

            /*CONFIG THE COLUMNS*/
            Vector columnOffset = new Vector();
            columnOffset.add(k);
            
            col2.setCellValueFactory(new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
                public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
                 String[] x = p.getValue();
                 if (x != null && x.length > (1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3)) {
                  return new SimpleStringProperty(x[1 + (Integer.parseInt(columnOffset.get(columnOffset.size() - 1).toString())) * 3]);
                 } else {
                  return new SimpleStringProperty("<no value>");
                 }
                }
               });
            col2.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col2.setResizable(false);
            
	        /*ADDING COLUMNS TO TABLE*/
	        table.getColumns().add(col);
	        col.getColumns().addAll(col2);
	        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 3)] = ((Vector)TF.get(k)).get(i).toString();
	        }
        	
        }

        /*ADDING INFORMATION TO COLUMNS*/
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
        
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        chart.getIcons().add(image);
	        chart.setTitle("Transfer Function");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        chart.show();
        chart.toFront();

	}

	/*FUNCTION TO CHOOSE WHAT GRAPH WILL BE DISPLAYED*/
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

	/*FUNCTION TO CHOOSE WHAT GRAPH WILL BE DISPLAYED FOR MULTIPLES CABLES*/
    public static void generateGraphs(Vector headings, Vector k1, Vector k2, Vector k3, Vector h1, Vector h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

    	/*CREATE THE AXIS X VALUES*/
        Vector x  = new Vector();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE CABLE MODEL*/
        Vector models = new Vector();
        for(int i = 0; i < headings.size(); i++) {
            KHM model = new KHM(Double.parseDouble(k1.get(i).toString()),Double.parseDouble(k2.get(i).toString()),Double.parseDouble(k3.get(i).toString()),Double.parseDouble(h1.get(i).toString()),Double.parseDouble(h2.get(i).toString()),cableLength);        	
            models.add(model);
        }
        
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	KHMController.generatePropagationConstant(headings, models, x, axisScale);
                break;
            case "Characteristic Impedance":
            	KHMController.generateCharacteristicImpedance(headings, models, x, axisScale);
                break;
            case "Transfer Function":
            	KHMController.generateTransferFunction(headings, models, x, axisScale);
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
