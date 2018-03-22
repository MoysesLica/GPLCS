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
	

    public static void generateDiffTNOKHM1(Vector<String> headings, Vector<Double> Z0inf, Vector<Double> nVF, Vector<Double> Rs0, Vector<Double> qL,
    		Vector<Double> qH, Vector<Double> qx, Vector<Double> qy, Vector<Double> qc, Vector<Double> phi, Vector<Double> fd, Vector<Double> k1, Vector<Double> k2, Vector<Double> k3,
    		Vector<Double> h1, Vector<Double> h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc) {
    	
    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE KHM 1 CABLE MODEL*/
        Vector<KHM1> model1 = new Vector<KHM1>();
        /*CREATE THE TNO CABLE MODEL*/
        Vector<TNO_EAB> model2 = new Vector<TNO_EAB>();
        
        
        for(int i = 0; i < Z0inf.size(); i++) {
        	model1.add(new KHM1(k1.get(i),k2.get(i),k3.get(i),h1.get(i),h2.get(i),cableLength));
        	model2.add(new TNO_EAB(Z0inf.get(i), nVF.get(i), Rs0.get(i), qL.get(i), qH.get(i), qx.get(i), qy.get(i), qc.get(i), phi.get(i), fd.get(i),cableLength));
        }
        
        
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffTNOKHM1PropagationConstant(headings, model1, model2, x, axisScale);
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffTNOKHM1CharacteristicImpedance(headings, model1, model2, x, axisScale);
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffTNOKHM1TransferFunction(headings, model1, model2, x, axisScale);
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffTNOKHM1PrimaryParameters(headings, model1, model2, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	public static void generateDiffTNOKHM1PropagationConstant(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x, String axisScale) {
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> alpha1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToAlpha = models1.get(i).generateAlphaPropagationConstant(x);
        	Vector<Double> addToBeta = models1.get(i).generateBetaPropagationConstant(x);
        	
        	alpha1.add(addToAlpha);
        	beta1.add(addToBeta);
        	Vector<Double> gama = models1.get(i).generatePropagationConstant(x, addToAlpha, addToBeta);
            ghama1.add(gama);

        }

        Vector<Vector<Double>> alpha2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToAlpha = models2.get(i).generateAttenuationConstant(x);
        	Vector<Double> addToBeta = models2.get(i).generatePhaseConstant(x);
        	
        	alpha2.add(addToAlpha);
        	beta2.add(addToBeta);
        	Vector<Complex> gama = models2.get(i).generatePropagationConstant(x);
            Vector<Double> doubleGhama = new Vector<Double>();
            for(int j = 0; j < gama.size(); j++) {
            	doubleGhama.add(gama.get(j).abs());
            }
            ghama2.add(doubleGhama);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> alpha = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama = new Vector<Vector<Double>>();
                
        for(int j = 0; j < models1.size(); j++) {

        	Vector<Double> addToAlpha = new Vector<Double>();
        	Vector<Double> addToBeta = new Vector<Double>();
        	Vector<Double> addToGhama = new Vector<Double>();
        	
            for(int i = 0; i < alpha1.get(j).size(); i++) {
            	addToAlpha.add(alpha2.get(j).get(i) - alpha1.get(j).get(i));
            	addToBeta.add(beta2.get(j).get(i) - beta1.get(j).get(i));
            	addToGhama.add(ghama2.get(j).get(i) - ghama1.get(j).get(i));
            }
            
            alpha.add(addToAlpha);
            beta.add(addToBeta);
            ghama.add(addToGhama);
        	
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
            graph = chartController.createLinearLineChart(x, beta, "Phase Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, GHAMA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
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

	private static void generateDiffTNOKHM1CharacteristicImpedance(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {
    	/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> real1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToReal = models1.get(i).generateRealCharacteristicImpedance(x);
        	Vector<Double> addToImag = models1.get(i).generateImagCharacteristicImpedance(x);
        	
        	real1.add(addToReal);
        	imag1.add(addToImag);
        	Vector<Double> CI = models1.get(i).generateCharacteristicImpedance(x, addToReal, addToImag);
            CI1.add(CI);

        }

        Vector<Vector<Double>> real2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToReal = models2.get(i).generateRealCharacteristicImpedance(x);
        	Vector<Double> addToImag = models2.get(i).generateImagCharacteristicImpedance(x);
        	
        	real2.add(addToReal);
        	imag2.add(addToImag);
        	Vector<Complex> CI = models2.get(i).generateCharacteristicImpedance(x);
            Vector<Double> doubleCI = new Vector<Double>();
            for(int j = 0; j < CI.size(); j++) {
            	doubleCI.add(CI.get(j).abs());
            }
            CI2.add(doubleCI);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> real = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI = new Vector<Vector<Double>>();
                
        for(int j = 0; j < CI2.size(); j++) {

        	Vector<Double> addToReal = new Vector<Double>();
        	Vector<Double> addToImag = new Vector<Double>();
        	Vector<Double> addToCI = new Vector<Double>();
        	
            for(int i = 0; i < real1.get(j).size(); i++) {
            	addToReal.add(real2.get(j).get(i) - real1.get(j).get(i));
            	addToImag.add(imag2.get(j).get(i) - imag1.get(j).get(i));
            	addToCI.add(CI2.get(j).get(i) - CI1.get(j).get(i));
            }
            
            real.add(addToReal);
            imag.add(addToImag);
            CI.add(addToCI);
        	
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
	        chart.setTitle("Characteristic Impedance");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
		
	}

	private static void generateDiffTNOKHM1TransferFunction(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> TF1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToTF = models1.get(i).generateTransferFunctionGain(x);        	
            TF1.add(addToTF);

        }

        Vector<Vector<Double>> TF2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToTF = models2.get(i).generateTransferFunctionGain(x);
        	TF2.add(addToTF);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> TF = new Vector<Vector<Double>>();
                
        for(int j = 0; j < TF2.size(); j++) {

        	Vector<Double> addToTF = new Vector<Double>();
        	
            for(int i = 0; i < TF1.get(j).size(); i++) {
            	addToTF.add(TF2.get(j).get(i) - TF1.get(j).get(i));
            }
            
            TF.add(addToTF);
        	
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

    private static void generateDiffTNOKHM1PrimaryParameters(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> SeriesResistance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance1 = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> alpha1 = models1.get(i).generateAlphaPropagationConstant(x);
            Vector<Double> beta1 = models1.get(i).generateBetaPropagationConstant(x);
            Vector<Double> real1 = models1.get(i).generateRealCharacteristicImpedance(x);
            Vector<Double> imag1 = models1.get(i).generateImagCharacteristicImpedance(x);

            SeriesResistance1.add(models1.get(i).generateSeriesResistance(x, alpha1, beta1, real1, imag1));
            SeriesInductance1.add(models1.get(i).generateSeriesInductance(x, alpha1, beta1, real1, imag1));
            ShuntingConductance1.add(models1.get(i).generateShuntingConductance(x, alpha1, beta1, real1, imag1));
            ShuntingCapacitance1.add(models1.get(i).generateShuntingCapacitance(x, alpha1, beta1, real1, imag1));

        }
        
        Vector<Vector<Double>> SeriesResistance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance2 = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> alpha2 = models2.get(i).generateAttenuationConstant(x);
            Vector<Double> beta2 = models2.get(i).generatePhaseConstant(x);
            Vector<Double> real2 = models2.get(i).generateRealCharacteristicImpedance(x);
            Vector<Double> imag2 = models2.get(i).generateImagCharacteristicImpedance(x);

            SeriesResistance2.add(models2.get(i).generateSeriesResistance(x, alpha2, beta2, real2, imag2));
            SeriesInductance2.add(models2.get(i).generateSeriesInductance(x, alpha2, beta2, real2, imag2));
            ShuntingConductance2.add(models2.get(i).generateShuntingConductance(x, alpha2, beta2, real2, imag2));
            ShuntingCapacitance2.add(models2.get(i).generateShuntingCapacitance(x, alpha2, beta2, real2, imag2));

        }

        
        Vector<Vector<Double>> SeriesResistance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> addToSRe = new Vector<Double>();
            Vector<Double> addToSIn = new Vector<Double>();
            Vector<Double> addToSCa = new Vector<Double>();
            Vector<Double> addToSCo = new Vector<Double>();
            
            for(int j = 0; j < SeriesResistance1.get(i).size(); j++) {
            	
            	addToSRe.add(SeriesResistance2.get(i).get(j) - SeriesResistance1.get(i).get(j));
            	addToSIn.add(SeriesInductance2.get(i).get(j) - SeriesInductance1.get(i).get(j));
            	addToSCa.add(ShuntingCapacitance2.get(i).get(j) - ShuntingCapacitance1.get(i).get(j));
            	addToSCo.add(ShuntingConductance2.get(i).get(j) - ShuntingConductance1.get(i).get(j));

            }

            SeriesResistance.add(addToSRe);
            SeriesInductance.add(addToSIn);
            ShuntingConductance.add(addToSCo);
            ShuntingCapacitance.add(addToSCa);
            
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
            graph = chartController.createLogLineChart   (x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "Ω/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "Ω/m", false);                

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Series Resistance");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);
        
        /*CREATE SECOND GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "S/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "S/m", false);                

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Shunting Conductance");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "H/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "H/m", false);                

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Series Inductance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATE FOURTH GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "F/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "F/m", false);                

        /*ADDING GRAPH TO FOURTH TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Shunting Capacitance");
        tab4.setContent(graph);
        tabPane.getTabs().add(tab4);

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
	        chart.setTitle("Characteristic Impedance");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
    	
	}

    public static void generateDiffKHM1TNO(Vector<String> headings, Vector<Double> Z0inf, Vector<Double> nVF, Vector<Double> Rs0, Vector<Double> qL,
    		Vector<Double> qH, Vector<Double> qx, Vector<Double> qy, Vector<Double> qc, Vector<Double> phi, Vector<Double> fd, Vector<Double> k1, Vector<Double> k2, Vector<Double> k3,
    		Vector<Double> h1, Vector<Double> h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc) {
    	
    	/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE KHM 1 CABLE MODEL*/
        Vector<KHM1> model1 = new Vector<KHM1>();
        /*CREATE THE TNO CABLE MODEL*/
        Vector<TNO_EAB> model2 = new Vector<TNO_EAB>();
        
        for(int i = 0; i < Z0inf.size(); i++) {
        	model1.add(new KHM1(k1.get(i),k2.get(i),k3.get(i),h1.get(i),h2.get(i),cableLength));
        	model2.add(new TNO_EAB(Z0inf.get(i), nVF.get(i), Rs0.get(i), qL.get(i), qH.get(i), qx.get(i), qy.get(i), qc.get(i), phi.get(i), fd.get(i),cableLength));
        }
    	
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffKHM1TNOPropagationConstant(headings, model1, model2, x, axisScale);
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffKHM1TNOCharacteristicImpedance(headings, model1, model2, x, axisScale);
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffKHM1TNOTransferFunction(headings, model1, model2, x, axisScale);
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffKHM1TNOPrimaryParameters(headings, model1, model2, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	public static void generateDiffKHM1TNOPropagationConstant(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x, String axisScale) {
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> alpha1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToAlpha = models1.get(i).generateAlphaPropagationConstant(x);
        	Vector<Double> addToBeta = models1.get(i).generateBetaPropagationConstant(x);
        	
        	alpha1.add(addToAlpha);
        	beta1.add(addToBeta);
        	Vector<Double> gama = models1.get(i).generatePropagationConstant(x, addToAlpha, addToBeta);
            ghama1.add(gama);

        }

        Vector<Vector<Double>> alpha2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToAlpha = models2.get(i).generateAttenuationConstant(x);
        	Vector<Double> addToBeta = models2.get(i).generatePhaseConstant(x);
        	
        	alpha2.add(addToAlpha);
        	beta2.add(addToBeta);
        	Vector<Complex> gama = models2.get(i).generatePropagationConstant(x);
            Vector<Double> doubleGhama = new Vector<Double>();
            for(int j = 0; j < gama.size(); j++) {
            	doubleGhama.add(gama.get(j).abs());
            }
            ghama2.add(doubleGhama);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> alpha = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama = new Vector<Vector<Double>>();
                
        for(int j = 0; j < ghama2.size(); j++) {

        	Vector<Double> addToAlpha = new Vector<Double>();
        	Vector<Double> addToBeta = new Vector<Double>();
        	Vector<Double> addToGhama = new Vector<Double>();
        	
            for(int i = 0; i < alpha1.get(j).size(); i++) {
            	addToAlpha.add(alpha1.get(j).get(i) - alpha2.get(j).get(i));
            	addToBeta.add(beta1.get(j).get(i) - beta2.get(j).get(i));
            	addToGhama.add(ghama1.get(j).get(i) - ghama2.get(j).get(i));
            }
            
            alpha.add(addToAlpha);
            beta.add(addToBeta);
            ghama.add(addToGhama);
        	
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
            graph = chartController.createLinearLineChart(x, beta, "Phase Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, GHAMA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ghama, "Propagation Constant", headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
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

	private static void generateDiffKHM1TNOCharacteristicImpedance(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {
    	/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> real1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToReal = models1.get(i).generateRealCharacteristicImpedance(x);
        	Vector<Double> addToImag = models1.get(i).generateImagCharacteristicImpedance(x);
        	
        	real1.add(addToReal);
        	imag1.add(addToImag);
        	Vector<Double> CI = models1.get(i).generateCharacteristicImpedance(x, addToReal, addToImag);
            CI1.add(CI);

        }

        Vector<Vector<Double>> real2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToReal = models2.get(i).generateRealCharacteristicImpedance(x);
        	Vector<Double> addToImag = models2.get(i).generateImagCharacteristicImpedance(x);
        	
        	real2.add(addToReal);
        	imag2.add(addToImag);
        	Vector<Complex> CI = models2.get(i).generateCharacteristicImpedance(x);
            Vector<Double> doubleCI = new Vector<Double>();
            for(int j = 0; j < CI.size(); j++) {
            	doubleCI.add(CI.get(j).abs());
            }
            CI2.add(doubleCI);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> real = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI = new Vector<Vector<Double>>();
                
        for(int j = 0; j < CI2.size(); j++) {

        	Vector<Double> addToReal = new Vector<Double>();
        	Vector<Double> addToImag = new Vector<Double>();
        	Vector<Double> addToCI = new Vector<Double>();
        	
            for(int i = 0; i < real1.get(j).size(); i++) {
            	addToReal.add(real1.get(j).get(i) - real2.get(j).get(i));
            	addToImag.add(imag1.get(j).get(i) - imag2.get(j).get(i));
            	addToCI.add(CI1.get(j).get(i) - CI2.get(j).get(i));
            }
            
            real.add(addToReal);
            imag.add(addToImag);
            CI.add(addToCI);
        	
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
	        chart.setTitle("Characteristic Impedance");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
		
	}

	private static void generateDiffKHM1TNOTransferFunction(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> TF1 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models1.size(); i++) {
        	
        	Vector<Double> addToTF = models1.get(i).generateTransferFunctionGain(x);        	
            TF1.add(addToTF);

        }

        Vector<Vector<Double>> TF2 = new Vector<Vector<Double>>();
        
        for(int i = 0; i < models2.size(); i++) {
        	
        	Vector<Double> addToTF = models2.get(i).generateTransferFunctionGain(x);
        	TF2.add(addToTF);

        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> TF = new Vector<Vector<Double>>();
                
        for(int j = 0; j < TF2.size(); j++) {

        	Vector<Double> addToTF = new Vector<Double>();
        	
            for(int i = 0; i < TF1.get(j).size(); i++) {
            	addToTF.add(TF1.get(j).get(i) - TF2.get(j).get(i));
            }
            
            TF.add(addToTF);
        	
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

    private static void generateDiffKHM1TNOPrimaryParameters(Vector<String> headings, Vector<KHM1> models1, Vector<TNO_EAB> models2, Vector<Double> x,
			String axisScale) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> SeriesResistance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance1 = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> alpha1 = models1.get(i).generateAlphaPropagationConstant(x);
            Vector<Double> beta1 = models1.get(i).generateBetaPropagationConstant(x);
            Vector<Double> real1 = models1.get(i).generateRealCharacteristicImpedance(x);
            Vector<Double> imag1 = models1.get(i).generateImagCharacteristicImpedance(x);

            SeriesResistance1.add(models1.get(i).generateSeriesResistance(x, alpha1, beta1, real1, imag1));
            SeriesInductance1.add(models1.get(i).generateSeriesInductance(x, alpha1, beta1, real1, imag1));
            ShuntingConductance1.add(models1.get(i).generateShuntingConductance(x, alpha1, beta1, real1, imag1));
            ShuntingCapacitance1.add(models1.get(i).generateShuntingCapacitance(x, alpha1, beta1, real1, imag1));

        }
        
        Vector<Vector<Double>> SeriesResistance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance2 = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> alpha2 = models2.get(i).generateAttenuationConstant(x);
            Vector<Double> beta2 = models2.get(i).generatePhaseConstant(x);
            Vector<Double> real2 = models2.get(i).generateRealCharacteristicImpedance(x);
            Vector<Double> imag2 = models2.get(i).generateImagCharacteristicImpedance(x);

            SeriesResistance2.add(models2.get(i).generateSeriesResistance(x, alpha2, beta2, real2, imag2));
            SeriesInductance2.add(models2.get(i).generateSeriesInductance(x, alpha2, beta2, real2, imag2));
            ShuntingConductance2.add(models2.get(i).generateShuntingConductance(x, alpha2, beta2, real2, imag2));
            ShuntingCapacitance2.add(models2.get(i).generateShuntingCapacitance(x, alpha2, beta2, real2, imag2));

        }

        
        Vector<Vector<Double>> SeriesResistance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance = new Vector<Vector<Double>>();

        for(int i = 0; i < models1.size(); i++) {

            Vector<Double> addToSRe = new Vector<Double>();
            Vector<Double> addToSIn = new Vector<Double>();
            Vector<Double> addToSCa = new Vector<Double>();
            Vector<Double> addToSCo = new Vector<Double>();
            
            for(int j = 0; j < SeriesResistance1.get(i).size(); j++) {
            	
            	addToSRe.add(SeriesResistance2.get(i).get(j) - SeriesResistance1.get(i).get(j));
            	addToSIn.add(SeriesInductance2.get(i).get(j) - SeriesInductance1.get(i).get(j));
            	addToSCa.add(ShuntingCapacitance2.get(i).get(j) - ShuntingCapacitance1.get(i).get(j));
            	addToSCo.add(ShuntingConductance2.get(i).get(j) - ShuntingConductance1.get(i).get(j));

            }

            SeriesResistance.add(addToSRe);
            SeriesInductance.add(addToSIn);
            ShuntingConductance.add(addToSCo);
            ShuntingCapacitance.add(addToSCa);
            
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
            graph = chartController.createLogLineChart   (x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "Ω/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesResistance, "Series Resistance", headings, "Frequency (Hz)", "Ω/m", false);                

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Series Resistance");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);
        
        /*CREATE SECOND GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "S/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingConductance, "Shunting Conductance", headings, "Frequency (Hz)", "S/m", false);                

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Shunting Conductance");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "H/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesInductance, "Series Inductance", headings, "Frequency (Hz)", "H/m", false);                

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Series Inductance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATE FOURTH GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "F/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingCapacitance, "Shunting Capacitance", headings, "Frequency (Hz)", "F/m", false);                

        /*ADDING GRAPH TO FOURTH TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Shunting Capacitance");
        tab4.setContent(graph);
        tabPane.getTabs().add(tab4);

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
	        chart.setTitle("Characteristic Impedance");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
    	
	}
	
}
