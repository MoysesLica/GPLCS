package DiffBetweenModels;

import java.awt.Toolkit;
import java.util.Vector;

import Complex.Complex;
import GPLCS.SimuladorGPLCS;
import KHM1.KHM1;
import KHM1.KHM1Controller;
import TNO_EAB.TNO_EAB;
import TNO_EAB.TNO_EABController;
import charts.chartController;
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

public class DiffBetweenModelsController {
	

    public static void generateDiffTNOKHM1(double Z0inf, double nVF, double Rs0, double qL,
    		double qH, double qx, double qy, double qc, double phi, double fd, double k1, double k2, double k3,
    		double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc) {
    	
    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE KHM 1 CABLE MODEL*/
        KHM1 model1 = new KHM1(k1,k2,k3,h1,h2,cableLength);
        /*CREATE THE TNO CABLE MODEL*/
        TNO_EAB model2 = new TNO_EAB(Z0inf, nVF, Rs0, qL, qH, qx, qy, qc, phi, fd,cableLength);        
    	
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffTNOKHM1PropagationConstant(model1, model2, x, axisScale);
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffTNOKHM1CharacteristicImpedance(model1, model2, x, axisScale);
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffTNOKHM1TransferFunction(model1, model2, x, axisScale);
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffTNOKHM1PrimaryParameters(model1, model2, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	public static void generateDiffTNOKHM1PropagationConstant(KHM1 model1, TNO_EAB model2, Vector<Double> x, String axisScale) {
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Double> alpha1 = model1.generateAlphaPropagationConstant(x);
        Vector<Double> beta1 = model1.generateBetaPropagationConstant(x);
        Vector<Double> gama1 = model1.generatePropagationConstant(x, alpha1, beta1);
        
        Vector<Double> alpha2 = model2.generateAttenuationConstant(x);
        Vector<Double> beta2 = model2.generatePhaseConstant(x);
        Vector<Complex> PC2 = model2.generatePropagationConstant(x);
        Vector<Double> gama2 = new Vector<Double>();
        for(int i = 0; i < PC2.size(); i++) {
        	gama2.add(PC2.get(i).abs());
        }
        
        Vector<Double> alpha = new Vector<Double>();
        Vector<Double> beta = new Vector<Double>();
        Vector<Double> gama = new Vector<Double>();
        
        for(int i = 0; i < alpha1.size(); i++) {
        	alpha.add(alpha2.get(i) - alpha1.get(i));
        }

        for(int i = 0; i < beta1.size(); i++) {
        	beta.add(beta2.get(i) - beta1.get(i));
        }

        for(int i = 0; i < gama1.size(); i++) {
        	gama.add(gama2.get(i) - gama1.get(i));
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
            graph = chartController.createLogLineChart   (x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                                    

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

	private static void generateDiffTNOKHM1CharacteristicImpedance(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {
    	/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Double> real1 = model1.generateRealCharacteristicImpedance(x);
        Vector<Double> imag1 = model1.generateImagCharacteristicImpedance(x);
        Vector<Double> CI1 = model1.generateCharacteristicImpedance(x, real1, imag1);
        
        Vector<Double> real2 = model2.generateRealCharacteristicImpedance(x);
        Vector<Double> imag2 = model2.generateImagCharacteristicImpedance(x);
        Vector<Complex> CI2 = model2.generateCharacteristicImpedance(x);
        Vector<Double> CI2Abs = new Vector<Double>();
        for(int i = 0; i < CI2.size(); i++) {
        	CI2Abs.add(CI2.get(i).abs());
        }
        
        Vector<Double> real = new Vector<Double>();
        Vector<Double> imag = new Vector<Double>();
        Vector<Double> CI = new Vector<Double>();
        
        for(int i = 0; i < real1.size(); i++) {
        	real.add(real2.get(i) - real1.get(i));
        }

        for(int i = 0; i < imag1.size(); i++) {
        	imag.add(imag2.get(i) - imag1.get(i));
        }

        for(int i = 0; i < CI1.size(); i++) {
        	CI.add(CI2Abs.get(i) - CI1.get(i));
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
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*CREATING HEADINGS OF TABLE*/
        Vector<String> headings = new Vector<String>();
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

	private static void generateDiffTNOKHM1TransferFunction(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Double> TF1 = model1.generateTransferFunctionGain(x);
        Vector<Double> TF2 = model2.generateTransferFunctionGain(x);
        Vector<Double> TF = new Vector<Double>();
        
        for(int i = 0; i < TF1.size(); i++) {
        	TF.add(TF2.get(i) - TF1.get(i));
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

    private static void generateDiffTNOKHM1PrimaryParameters(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
		Vector<Double> alpha1 = model1.generateAlphaPropagationConstant(x);
        Vector<Double> beta1 =  model1.generateBetaPropagationConstant(x);
		Vector<Double> real1 = model1.generateRealCharacteristicImpedance(x);
        Vector<Double> imag1 = model1.generateImagCharacteristicImpedance(x);
        
        Vector<Double> SeriesResistance1    = model1.generateSeriesResistance(x, alpha1, beta1, real1, imag1);
        Vector<Double> SeriesInductance1    = model1.generateSeriesInductance(x, alpha1, beta1, real1, imag1);
        Vector<Double> ShuntingConductance1 = model1.generateShuntingConductance(x, alpha1, beta1, real1, imag1);
        Vector<Double> ShuntingCapacitance1 = model1.generateShuntingCapacitance(x, alpha1, beta1, real1, imag1);

		Vector<Double> alpha2 = model2.generateAttenuationConstant(x);
        Vector<Double> beta2 =  model2.generatePhaseConstant(x);
		Vector<Double> real2 = model2.generateRealCharacteristicImpedance(x);
        Vector<Double> imag2 = model2.generateImagCharacteristicImpedance(x);
        
        Vector<Double> SeriesResistance2    = model1.generateSeriesResistance(x, alpha2, beta2, real2, imag2);
        Vector<Double> SeriesInductance2    = model1.generateSeriesInductance(x, alpha2, beta2, real2, imag2);
        Vector<Double> ShuntingConductance2 = model1.generateShuntingConductance(x, alpha2, beta2, real2, imag2);
        Vector<Double> ShuntingCapacitance2 = model1.generateShuntingCapacitance(x, alpha2, beta2, real2, imag2);

        Vector<Double> SeriesResistance    = new Vector<Double>();
        Vector<Double> SeriesInductance    = new Vector<Double>();
        Vector<Double> ShuntingConductance = new Vector<Double>();
        Vector<Double> ShuntingCapacitance = new Vector<Double>();

        for(int i = 0; i < SeriesResistance1.size(); i++) {
        	
        	SeriesResistance.add(SeriesResistance2.get(i) - SeriesResistance1.get(i));
        	SeriesInductance.add(SeriesInductance2.get(i) - SeriesInductance1.get(i));
        	ShuntingConductance.add(ShuntingConductance2.get(i) - ShuntingConductance1.get(i));
        	ShuntingCapacitance.add(ShuntingCapacitance2.get(i) - ShuntingCapacitance1.get(i));
        	
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

    public static void generateDiffKHM1TNO(double Z0inf, double nVF, double Rs0, double qL,
    		double qH, double qx, double qy, double qc, double phi, double fd, double k1, double k2, double k3,
    		double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc) {
    	
    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE KHM 1 CABLE MODEL*/
        KHM1 model1 = new KHM1(k1,k2,k3,h1,h2,cableLength);
        /*CREATE THE TNO CABLE MODEL*/
        TNO_EAB model2 = new TNO_EAB(Z0inf, nVF, Rs0, qL, qH, qx, qy, qc, phi, fd,cableLength);        
    	
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffKHM1TNOPropagationConstant(model1, model2, x, axisScale);
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffKHM1TNOCharacteristicImpedance(model1, model2, x, axisScale);
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffKHM1TNOTransferFunction(model1, model2, x, axisScale);
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffKHM1TNOPrimaryParameters(model1, model2, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	public static void generateDiffKHM1TNOPropagationConstant(KHM1 model1, TNO_EAB model2, Vector<Double> x, String axisScale) {
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Double> alpha1 = model1.generateAlphaPropagationConstant(x);
        Vector<Double> beta1 = model1.generateBetaPropagationConstant(x);
        Vector<Double> gama1 = model1.generatePropagationConstant(x, alpha1, beta1);
        
        Vector<Double> alpha2 = model2.generateAttenuationConstant(x);
        Vector<Double> beta2 = model2.generatePhaseConstant(x);
        Vector<Complex> PC2 = model2.generatePropagationConstant(x);
        Vector<Double> gama2 = new Vector<Double>();
        for(int i = 0; i < PC2.size(); i++) {
        	gama2.add(PC2.get(i).abs());
        }
        
        Vector<Double> alpha = new Vector<Double>();
        Vector<Double> beta = new Vector<Double>();
        Vector<Double> gama = new Vector<Double>();
        
        for(int i = 0; i < alpha1.size(); i++) {
        	alpha.add(alpha1.get(i) - alpha2.get(i));
        }

        for(int i = 0; i < beta1.size(); i++) {
        	beta.add(beta1.get(i) - beta2.get(i));
        }

        for(int i = 0; i < gama1.size(); i++) {
        	gama.add(gama1.get(i) - gama2.get(i));
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
            graph = chartController.createLogLineChart   (x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, gama, "Propagation Constant", "Propagation Constant", "Frequency (Hz)", "", false);                                    

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

	private static void generateDiffKHM1TNOCharacteristicImpedance(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {
    	/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Double> real1 = model1.generateRealCharacteristicImpedance(x);
        Vector<Double> imag1 = model1.generateImagCharacteristicImpedance(x);
        Vector<Double> CI1 = model1.generateCharacteristicImpedance(x, real1, imag1);
        
        Vector<Double> real2 = model2.generateRealCharacteristicImpedance(x);
        Vector<Double> imag2 = model2.generateImagCharacteristicImpedance(x);
        Vector<Complex> CI2 = model2.generateCharacteristicImpedance(x);
        Vector<Double> CI2Abs = new Vector<Double>();
        for(int i = 0; i < CI2.size(); i++) {
        	CI2Abs.add(CI2.get(i).abs());
        }
        
        Vector<Double> real = new Vector<Double>();
        Vector<Double> imag = new Vector<Double>();
        Vector<Double> CI = new Vector<Double>();
        
        for(int i = 0; i < real1.size(); i++) {
        	real.add(real1.get(i) - real2.get(i));
        }

        for(int i = 0; i < imag1.size(); i++) {
        	imag.add(imag1.get(i) - imag2.get(i));
        }

        for(int i = 0; i < CI1.size(); i++) {
        	CI.add(CI1.get(i) - CI2Abs.get(i));
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
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω", false);                
        else
            graph =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", "Characteristic Impedance - Module", "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Characteristic Impedance - Module");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);
        
        /*CREATING HEADINGS OF TABLE*/
        Vector<String> headings = new Vector<String>();
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

	private static void generateDiffKHM1TNOTransferFunction(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Double> TF1 = model1.generateTransferFunctionGain(x);
        Vector<Double> TF2 = model2.generateTransferFunctionGain(x);
        Vector<Double> TF = new Vector<Double>();
        
        for(int i = 0; i < TF1.size(); i++) {
        	TF.add(TF1.get(i) - TF2.get(i));
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

    private static void generateDiffKHM1TNOPrimaryParameters(KHM1 model1, TNO_EAB model2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
		Vector<Double> alpha1 = model1.generateAlphaPropagationConstant(x);
        Vector<Double> beta1 =  model1.generateBetaPropagationConstant(x);
		Vector<Double> real1 = model1.generateRealCharacteristicImpedance(x);
        Vector<Double> imag1 = model1.generateImagCharacteristicImpedance(x);
        
        Vector<Double> SeriesResistance1    = model1.generateSeriesResistance(x, alpha1, beta1, real1, imag1);
        Vector<Double> SeriesInductance1    = model1.generateSeriesInductance(x, alpha1, beta1, real1, imag1);
        Vector<Double> ShuntingConductance1 = model1.generateShuntingConductance(x, alpha1, beta1, real1, imag1);
        Vector<Double> ShuntingCapacitance1 = model1.generateShuntingCapacitance(x, alpha1, beta1, real1, imag1);

		Vector<Double> alpha2 = model2.generateAttenuationConstant(x);
        Vector<Double> beta2 =  model2.generatePhaseConstant(x);
		Vector<Double> real2 = model2.generateRealCharacteristicImpedance(x);
        Vector<Double> imag2 = model2.generateImagCharacteristicImpedance(x);
        
        Vector<Double> SeriesResistance2    = model1.generateSeriesResistance(x, alpha2, beta2, real2, imag2);
        Vector<Double> SeriesInductance2    = model1.generateSeriesInductance(x, alpha2, beta2, real2, imag2);
        Vector<Double> ShuntingConductance2 = model1.generateShuntingConductance(x, alpha2, beta2, real2, imag2);
        Vector<Double> ShuntingCapacitance2 = model1.generateShuntingCapacitance(x, alpha2, beta2, real2, imag2);

        Vector<Double> SeriesResistance    = new Vector<Double>();
        Vector<Double> SeriesInductance    = new Vector<Double>();
        Vector<Double> ShuntingConductance = new Vector<Double>();
        Vector<Double> ShuntingCapacitance = new Vector<Double>();

        for(int i = 0; i < SeriesResistance1.size(); i++) {
        	
        	SeriesResistance.add(SeriesResistance1.get(i) - SeriesResistance2.get(i));
        	SeriesInductance.add(SeriesInductance1.get(i) - SeriesInductance2.get(i));
        	ShuntingConductance.add(ShuntingConductance1.get(i) - ShuntingConductance2.get(i));
        	ShuntingCapacitance.add(ShuntingCapacitance1.get(i) - ShuntingCapacitance2	.get(i));
        	
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


	
}
