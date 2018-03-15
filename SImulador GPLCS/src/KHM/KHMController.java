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

import GPLCS.SimuladorGPLCS;
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
import tables.Table;

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
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "Nepers/km", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "Nepers/km", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", "Phase Constant", "Frequency (Hz)", "Radians/km", false);                
        else
            graph = chartController.createLinearLineChart(x, beta,  "Phase Constant", "Phase Constant", "Frequency (Hz)", "Radians/km", false);                                    

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
        Vector headings = new Vector();
        headings.add("Frequency(Hz)");
        headings.add("Attenuation Constant(Np/m)");
        headings.add("Phase Constant(rad/m)");
        headings.add("Propagation Constant()");
        
        String[][] data = new String[x.size()][4];        
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),alpha.get(i).toString(),beta.get(i).toString(),gama.get(i).toString()};
        }

        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(Table.generateTable(headings, data));
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
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "Nepers/km", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "Nepers/km", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", headings, "Frequency (Hz)", "Radians/km", false);                
        else
            graph = chartController.createLinearLineChart(x, beta,  "Phase Constant", headings, "Frequency (Hz)", "Radians/km", false);                                    

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

        /*CREATING THE COLUMNS OF TABLE*/
        Vector superHeadings = new Vector();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector subHeadings = new Vector();
        subHeadings.add("Attenuation Constant(Np/m)");
        subHeadings.add("Phase Constant(rad/m)");
        subHeadings.add("Propagation Constant()");

        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 3)] = ((Vector)alpha.get(k)).get(i).toString();
	            data[i][2 + (k * 3)] = ((Vector) beta.get(k)).get(i).toString();
	            data[i][3 + (k * 3)] = ((Vector) gama.get(k)).get(i).toString();
	        }
        	
        }        

        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(Table.generateTable(superHeadings, subHeadings, data));
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
        
        /*CREATING HEADINGS OF TABLE*/
        Vector headings = new Vector();
        headings.add("Frequency(Hz)");
        headings.add("Real(Ω)");
        headings.add("Imaginary(Ω)");
        headings.add("Characteristic Impedance(Ω)");
        
        /*GETTING INFORMATION TO COLUMNS*/
        String[][] data = new String[x.size()][4];
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),real.get(i).toString(),imag.get(i).toString(),CI.get(i).toString()};
        }
        
        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(Table.generateTable(headings, data));
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
        Vector superHeadings = new Vector();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector subHeadings = new Vector();
        subHeadings.add("Real(Ω)");
        subHeadings.add("Imaginary(Ω)");
        subHeadings.add("Characteristic Impedance(Ω)");

        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
        	for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 3)] = ((Vector)real.get(k)).get(i).toString();
	            data[i][2 + (k * 3)] = ((Vector)imag.get(k)).get(i).toString();
	            data[i][3 + (k * 3)] = ((Vector)  CI.get(k)).get(i).toString();
	        }
        	
        }

        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(Table.generateTable(superHeadings, subHeadings, data));
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
        Vector headings = new Vector();
        headings.add("Frequency");
        headings.add("Transfer Function Gain(dB)");
        
        String[][] data = new String[x.size()][2];
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),TF.get(i).toString()};
        }        
        
        /*ADDING TABLE TO TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Values");
        tab2.setContent(Table.generateTable(headings, data));
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
        Vector superHeadings = new Vector();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector subHeadings = new Vector();
        subHeadings.add("Transfer Function Gain (dB)");
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + k] = ((Vector)TF.get(k)).get(i).toString();
	        }
        	
        }        

        /*ADDING TABLE TO TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Values");
        tab2.setContent(Table.generateTable(superHeadings, subHeadings, data));
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
            case "Primary Parameters":
            	KHMController.generatePrimaryParameters(model, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	private static void generatePrimaryParameters(KHM model, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
		Vector alpha = model.generateAlphaPropagationConstant(x);
        Vector beta =  model.generateBetaPropagationConstant(x);
		Vector real = model.generateRealCharacteristicImpedance(x);
        Vector imag = model.generateImagCharacteristicImpedance(x);
        
        Vector SeriesResistance    = model.generateSeriesResistance(x, alpha, beta, real, imag);
        Vector SeriesInductance    = model.generateSeriesInductance(x, alpha, beta, real, imag);
        Vector ShuntingConductance = model.generateShuntingConductance(x, alpha, beta, real, imag);
        Vector ShuntingCapacitance = model.generateShuntingCapacitance(x, alpha, beta, real, imag);
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, SeriesResistance, "Series Resistance", "Series Resistance", "Frequency (Hz)", "Ω/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesResistance, "Series Resistance", "Series Resistance", "Frequency (Hz)", "Ω/m", false);                

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Series Resistance");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);
        
        /*CREATE SECOND GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingConductance, "Shunting Conductance", "Shunting Conductance", "Frequency (Hz)", "S/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingConductance, "Shunting Conductance", "Shunting Conductance", "Frequency (Hz)", "S/m", false);                

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Shunting Conductance");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, SeriesInductance, "Series Inductance", "Series Inductance", "Frequency (Hz)", "H/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesInductance, "Series Inductance", "Series Inductance", "Frequency (Hz)", "H/m", false);                

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Series Inductance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATE FOURTH GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", "Shunting Capacitance", "Frequency (Hz)", "F/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", "Shunting Capacitance", "Frequency (Hz)", "F/m", false);                

        /*ADDING GRAPH TO FOURTH TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Shunting Capacitance");
        tab4.setContent(graph);
        tabPane.getTabs().add(tab4);

        Vector headings = new Vector();
        headings.add("Frequency(Hz)");
        headings.add("Series Resistance(Ω/m)");
        headings.add("Series Inductance(H/m)");
        headings.add("Shunting Conductance(S/m)");
        headings.add("Shunting Capacitance(F/m)");

        /*ADDING INFORMATION TO COLUMNS*/
        String[][] data = new String[x.size()][5];        
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),SeriesResistance.get(i).toString(),SeriesInductance.get(i).toString(),ShuntingConductance.get(i).toString(), ShuntingCapacitance.get(i).toString()};
        }        

        /*ADDING TABLE TO TAB*/
        Tab tab5 = new Tab();
        tab5.setClosable(false);
        tab5.setText("Values");
        tab5.setContent(Table.generateTable(headings, data));
        tabPane.getTabs().add(tab5);
        
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
            case "Primary Parameters":
            	KHMController.generatePrimaryParameters(headings, models, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	private static void generatePrimaryParameters(Vector headings, Vector models, Vector x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector SeriesResistance    = new Vector();
        Vector SeriesInductance    = new Vector();
        Vector ShuntingConductance = new Vector();
        Vector ShuntingCapacitance = new Vector();        

        for(int i = 0; i < models.size(); i++) {

    		Vector alpha = ((KHM)models.get(i)).generateAlphaPropagationConstant(x);
            Vector beta =  ((KHM)models.get(i)).generateBetaPropagationConstant(x);
    		Vector real =  ((KHM)models.get(i)).generateRealCharacteristicImpedance(x);
            Vector imag =  ((KHM)models.get(i)).generateImagCharacteristicImpedance(x);

            Vector addToR = ((KHM)models.get(i)).generateSeriesResistance(x, alpha, beta, real, imag);
            Vector addToL = ((KHM)models.get(i)).generateSeriesInductance(x, alpha, beta, real, imag);
            Vector addToG = ((KHM)models.get(i)).generateShuntingConductance(x, alpha, beta, real, imag);
            Vector addToC = ((KHM)models.get(i)).generateShuntingCapacitance(x, alpha, beta, real, imag);

            SeriesResistance.add(addToR);
            SeriesInductance.add(addToL);
            ShuntingConductance.add(addToG);
            ShuntingCapacitance.add(addToC);

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
            graph = chartController.createLogLineChart   (x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Series Resistance");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Series Inductance");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Shunting Conductance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATE FOURTH GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FOURTH TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Shunting Capacitance");
        tab4.setContent(graph);
        tabPane.getTabs().add(tab4);

        /*CREATING THE COLUMNS OF TABLE*/
        /*CREATING THE COLUMNS OF TABLE*/
        Vector superHeadings = new Vector();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector subHeadings = new Vector();
        subHeadings.add("Series Resistance");
        subHeadings.add("Series Inductance");
        subHeadings.add("Shunting Conductance");
        subHeadings.add("Shunting Capacitance");
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 4)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + (k * 4)] = ((Vector) SeriesResistance.get(k)).get(i).toString();
	            data[i][2 + (k * 4)] = ((Vector) SeriesInductance.get(k)).get(i).toString();
	            data[i][3 + (k * 4)] = ((Vector) ShuntingConductance.get(k)).get(i).toString();
	            data[i][4 + (k * 4)] = ((Vector) ShuntingCapacitance.get(k)).get(i).toString();
	        }
        	
        }        
        
        /*ADDING TABLE TO TAB*/
        Tab tab5 = new Tab();
        tab5.setClosable(false);
        tab5.setText("Values");
        tab5.setContent(Table.generateTable(superHeadings, subHeadings, data));
        tabPane.getTabs().add(tab5);
        
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

}
