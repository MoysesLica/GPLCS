package TNO_EAB;

import charts.chartController;
import Complex.Complex;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Vector;

import GPLCS.SimuladorGPLCS;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tables.Table;

public class TNO_EABController {
	
	/*FUNCTION TO CHOOSE WHAT GRAPH WILL BE DISPLAYED*/
    

	public static void generateGraphs(double Z0inf, double nVF, double Rs0, double qL, double qH, double qx, double qy, double qc, double phi, double fd, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE CABLE MODEL*/
        TNO_EAB model = new TNO_EAB(Z0inf, nVF, Rs0, qL, qH, qx, qy, qc, phi, fd,cableLength);
        
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	TNO_EABController.generatePropagationConstant(model, x, axisScale);
                break;
            case "Characteristic Impedance":
            	TNO_EABController.generateCharacteristicImpedance(model, x, axisScale);
                break;
            case "Transfer Function":
            	TNO_EABController.generateTransferFunction(model, x, axisScale);
                break;
            case "Primary Parameters":
            	TNO_EABController.generatePrimaryParameters(model, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }
	
	public static void generateGraphs(Vector<String> headings, Vector<Double> Z0inf, Vector<Double> nVF, Vector<Double> Rs0, Vector<Double> qL, Vector<Double> qH, Vector<Double> qx, Vector<Double> qy, Vector<Double> qc, Vector<Double> phi, Vector<Double> fd, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE CABLE MODEL*/
        Vector<TNO_EAB> models = new Vector<TNO_EAB>();
        for(int i = 0; i < headings.size(); i++) {
        	TNO_EAB model = new TNO_EAB(Z0inf.get(i), nVF.get(i), Rs0.get(i), qL.get(i), qH.get(i), qx.get(i), qy.get(i), qc.get(i), phi.get(i), fd.get(i),cableLength);        	
            models.add(model);
        }
        
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	TNO_EABController.generatePropagationConstant(headings, models, x, axisScale);
                break;
            case "Characteristic Impedance":
            	TNO_EABController.generateCharacteristicImpedance(headings, models, x, axisScale);
                break;
            case "Transfer Function":
            	TNO_EABController.generateTransferFunction(headings, models, x, axisScale);
                break;
            case "Primary Parameters":
            	TNO_EABController.generatePrimaryParameters(headings, models, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }
	
	private static void generatePropagationConstant(Vector<String> headings, Vector<TNO_EAB> models, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> alpha = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models.size(); i++) {
        	
        	Vector<Double> addToAlpha = models.get(i).generateAttenuationConstant(x);
        	Vector<Double> addToBeta = models.get(i).generatePhaseConstant(x);
        	
        	alpha.add(addToAlpha);
        	beta.add(addToBeta);
        	Vector<Complex> gama = models.get(i).generatePropagationConstant(x);
            Vector<Double> doubleGhama = new Vector<Double>();
            for(int j = 0; j < gama.size(); j++) {
            	doubleGhama.add(gama.get(j).abs());
            }
            ghama.add(doubleGhama);

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
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, beta,  "Phase Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATING THE COLUMNS OF TABLE*/
        Vector<String> superHeadings = new Vector<String>();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector<String> subHeadings = new Vector<String>();
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
	            data[i][1 + (k * 3)] = alpha.get(k).get(i).toString();
	            data[i][2 + (k * 3)] = beta.get(k).get(i).toString();
	            data[i][3 + (k * 3)] = ghama.get(k).get(i).toString();
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

	private static void generateCharacteristicImpedance(Vector<String> headings, Vector<TNO_EAB> models, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> real = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models.size(); i++) {
        	
        	Vector<Double> addToReal = models.get(i).generateRealCharacteristicImpedance(x);
        	Vector<Double> addToImag = models.get(i).generateImagCharacteristicImpedance(x);
        	
        	real.add(addToReal);
        	imag.add(addToImag);

        	Vector<Complex> characteristicImpedance = models.get(i).generateCharacteristicImpedance(x);
            Vector<Double> doubleCI = new Vector<Double>();
            for(int j = 0; j < characteristicImpedance.size(); j++) {
            	doubleCI.add(characteristicImpedance.get(j).abs());
            }
            CI.add(doubleCI);
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
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", headings, "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", headings, "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", headings, "Frequency (Hz)", "Ω", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", headings, "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", headings, "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", headings, "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*ADDING TABLE OF VALUES*/        
        Vector<String> superHeadings = new Vector<String>();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector<String> subHeadings = new Vector<String>();
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
	            data[i][1 + (k * 3)] = real.get(k).get(i).toString();
	            data[i][2 + (k * 3)] = imag.get(k).get(i).toString();
	            data[i][3 + (k * 3)] = CI.get(k).get(i).toString();
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

	/*FUNCTION TO GENERATE TRANSFER FUNCTION'S GRAPHS FOR MULTIPLES CABLES*/
	public static void generateTransferFunction(Vector<String> headings, Vector<TNO_EAB> models, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> TF = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models.size(); i++) {	
        	TF.add(models.get(i).generateTransferFunctionGain(x));
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
            graph = chartController.createLogLineChart   (x, TF, "Transfer Function Gain", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, TF, "Transfer Function Gain", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Transfer Function Gain");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView<String[]>();
        table.setEditable(false);

        /*CREATING THE COLUMNS OF TABLE*/
        Vector<String> superHeadings = new Vector<String>();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector<String> subHeadings = new Vector<String>();
        subHeadings.add("Transfer Function Gain (dB)");
        
        /*CREATE DATA OF TABLE*/
        String[][] data = new String[x.size()][1 + (headings.size() * 3)];        

        for(int i = 0; i < x.size(); i++) {
            data[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < headings.size(); k++) {
        
	        for(int i = 0; i < x.size(); i++) {
	            data[i][1 + k] = TF.get(k).get(i).toString();
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
	
	private static void generatePrimaryParameters(Vector<String> headings, Vector<TNO_EAB> models, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> SeriesResistance    = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance    = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance = new Vector<Vector<Double>>();        

        for(int i = 0; i < models.size(); i++) {

    		Vector<Double> alpha = models.get(i).generateAttenuationConstant(x);
            Vector<Double> beta =  models.get(i).generatePhaseConstant(x);
    		Vector<Double> real =  models.get(i).generateRealCharacteristicImpedance(x);
            Vector<Double> imag =  models.get(i).generateImagCharacteristicImpedance(x);

            Vector<Double> addToR = models.get(i).generateSeriesResistance(x, alpha, beta, real, imag);
            Vector<Double> addToL = models.get(i).generateSeriesInductance(x, alpha, beta, real, imag);
            Vector<Double> addToG = models.get(i).generateShuntingConductance(x, alpha, beta, real, imag);
            Vector<Double> addToC = models.get(i).generateShuntingCapacitance(x, alpha, beta, real, imag);

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
        Vector<String> superHeadings = new Vector<String>();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < headings.size(); i++) {
        	superHeadings.add(headings.get(i).toString());
        }
        
        Vector<String> subHeadings = new Vector<String>();
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
	            data[i][1 + (k * 4)] = SeriesResistance.get(k).get(i).toString();
	            data[i][2 + (k * 4)] = SeriesInductance.get(k).get(i).toString();
	            data[i][3 + (k * 4)] = ShuntingConductance.get(k).get(i).toString();
	            data[i][4 + (k * 4)] = ShuntingCapacitance.get(k).get(i).toString();
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

	
	private static void generatePrimaryParameters(TNO_EAB model, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
		Vector<Double> alpha = model.generateAttenuationConstant(x);
        Vector<Double> beta =  model.generatePhaseConstant(x);
		Vector<Double> real = model.generateRealCharacteristicImpedance(x);
        Vector<Double> imag = model.generateImagCharacteristicImpedance(x);
        
        Vector<Double> SeriesResistance    = model.generateSeriesResistance(x, alpha, beta, real, imag);
        Vector<Double> SeriesInductance    = model.generateSeriesInductance(x, alpha, beta, real, imag);
        Vector<Double> ShuntingConductance = model.generateShuntingConductance(x, alpha, beta, real, imag);
        Vector<Double> ShuntingCapacitance = model.generateShuntingCapacitance(x, alpha, beta, real, imag);
        
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

        Vector<String> headings = new Vector<String>();
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


	public static void generateTransferFunction(TNO_EAB model, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Double> TF = model.generateTransferFunctionGain(x);
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, TRANSFER FUNCTION GAIN*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, TF, "Transfer Function Gain", "Transfer Function Gain(dB)", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, TF, "Transfer Function Gain", "Transfer Function Gain(dB)", "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Transfer Function Gain");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*ADDING TABLE OF VALUES*/
        TableView<String[]> table = new TableView<String[]>();
        table.setEditable(false);
        
        /*CREATING THE TWO COLUMNS OF TABLE*/
        Vector<String> headings = new Vector<String>();
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

	
	private static void generateCharacteristicImpedance(TNO_EAB model, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Double> real = model.generateRealCharacteristicImpedance(x);
        Vector<Double> imag = model.generateImagCharacteristicImpedance(x);
        Vector<Complex> CI = model.generateCharacteristicImpedance(x);
        Vector<Double> Zc = new Vector<Double>();
        for(int i = 0; i < CI.size(); i++) {
        	Zc.add(CI.get(i).abs());
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
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Characteristic Impedance - Real", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Characteristic Impedance - Real", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Characteristic Impedance - Imaginary", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Characteristic Impedance - Imaginary", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, GHAMA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, Zc, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, Zc, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView<String[]>();
        table.setEditable(false);

        /*CREATING THE FOUR COLUMNS OF TABLE*/
        Vector<String> headings = new Vector<String>();
        headings.add("Frequency(Hz)");
        headings.add("Real(Ω)");
        headings.add("Imaginary(Ω)");
        headings.add("Characteristic Impedance(Ω)");
        
        String[][] data = new String[x.size()][4];        
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),real.get(i).toString(),imag.get(i).toString(),Zc.get(i).toString()};
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

	private static void generatePropagationConstant(TNO_EAB model, Vector<Double> x, String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Double> alpha = model.generateAttenuationConstant(x);
        Vector<Double> beta = model.generatePhaseConstant(x);
        Vector<Complex> PC = model.generatePropagationConstant(x);
        Vector<Double> ghama = new Vector<Double>();
        for(int i = 0; i < PC.size(); i++) {
        	ghama.add(PC.get(i).abs());
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
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant", "Attenuation Constant", "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant", "Phase Constant", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, beta, "Phase Constant", "Phase Constant", "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, GHAMA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ghama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ghama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Propagation Constant");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView<String[]>();
        table.setEditable(false);

        /*CREATING THE FOUR COLUMNS OF TABLE*/
        Vector<String> headings = new Vector<String>();
        headings.add("Frequency(Hz)");
        headings.add("Attenuation Constant(Np/m)");
        headings.add("Phase Constant(rad/m)");
        headings.add("Propagation Constant()");
        
        String[][] data = new String[x.size()][4];        
        for(int i = 0; i < x.size(); i++) {
            data[i] = new String[]{x.get(i).toString(),alpha.get(i).toString(),beta.get(i).toString(),ghama.get(i).toString()};
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

}
