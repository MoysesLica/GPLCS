package DiffBetweenModels;

import java.awt.Toolkit;
import java.util.Vector;

import BT0.BT0;
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
	
	public static void generateDiffBT0KHM1(Vector<String> headings, 
			Vector<Double> k1, Vector<Double> k2, Vector<Double> k3, Vector<Double> h1, Vector<Double> h2,
    		Vector<Double> Roc, Vector<Double> ac, Vector<Double> L0, Vector<Double> Linf,
			Vector<Double> fm, Vector<Double> Nb, Vector<Double> g0, Vector<Double> Nge,
			Vector<Double> C0, Vector<Double> Cinf, Vector<Double> Nce,
			double cableLength, double minF, double maxF, double toneSpacing, String axisScale,
			String parameterCalc, boolean BT0AgainstKHM1) {

		/*START OF FUNCTION*/
    	
		/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE BT0 CABLE MODEL*/
        Vector<BT0> model1 = new Vector<BT0>();
        /*CREATE THE KHM1 CABLE MODEL*/
        Vector<KHM1> model2 = new Vector<KHM1>();
        
        for(int i = 0; i < Roc.size(); i++) {
        	model1.add(new BT0(Roc.get(i), ac.get(i) , L0.get(i),  Linf.get(i),  fm.get(i),  Nb.get(i),
        			g0.get(i),  Nge.get(i),  C0.get(i),  Cinf.get(i),  Nce.get(i), cableLength));
        	model2.add(new KHM1(k1.get(i),k2.get(i),k3.get(i),h1.get(i),h2.get(i),cableLength));
        }
    	
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffPropagationConstant(headings, model1, model2, x, axisScale, BT0AgainstKHM1, "BT0", "KHM1");
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffCharacteristicImpedance(headings, model1, model2, x, axisScale, BT0AgainstKHM1, "BT0", "KHM1");
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffTransferFunction(headings, model1, model2, x, axisScale, BT0AgainstKHM1, "BT0", "KHM1");
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffPrimaryParameters(headings, model1, model2, x, axisScale, BT0AgainstKHM1, "BT0", "KHM1");
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
		
	}

	public static void generateDiffBT0TNO(Vector<String> headings, Vector<Double> Z0inf, Vector<Double> nVF, Vector<Double> Rs0, Vector<Double> qL,
    		Vector<Double> qH, Vector<Double> qx, Vector<Double> qy, Vector<Double> qc, Vector<Double> phi, Vector<Double> fd,
    		Vector<Double> Roc, Vector<Double> ac, Vector<Double> L0, Vector<Double> Linf,
			Vector<Double> fm, Vector<Double> Nb, Vector<Double> g0, Vector<Double> Nge,
			Vector<Double> C0, Vector<Double> Cinf, Vector<Double> Nce,
			double cableLength, double minF, double maxF, double toneSpacing, String axisScale,
			String parameterCalc, boolean BT0AgainstTNO) {

		/*START OF FUNCTION*/
    	
		/*CREATE THE AXIS X VALUES*/
        Vector<Double> x  = new Vector<Double>();        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        /*CREATE THE KHM 1 CABLE MODEL*/
        Vector<BT0> model1 = new Vector<BT0>();
        /*CREATE THE TNO CABLE MODEL*/
        Vector<TNO_EAB> model2 = new Vector<TNO_EAB>();
        
        for(int i = 0; i < Z0inf.size(); i++) {
        	model1.add(new BT0(Roc.get(i), ac.get(i) , L0.get(i),  Linf.get(i),  fm.get(i),  Nb.get(i),
        			g0.get(i),  Nge.get(i),  C0.get(i),  Cinf.get(i),  Nce.get(i), cableLength));
        	model2.add(new TNO_EAB(Z0inf.get(i), nVF.get(i), Rs0.get(i), qL.get(i), qH.get(i), qx.get(i), qy.get(i), qc.get(i), phi.get(i), fd.get(i),cableLength));
        }
    	
        /*CHOOSE CHART TO DISPLAY*/
        switch(parameterCalc){
            case "Propagation Constant":
            	DiffBetweenModelsController.generateDiffPropagationConstant(headings, model1, model2, x, axisScale, BT0AgainstTNO, "BT0", "TNO/EAB");
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffCharacteristicImpedance(headings, model1, model2, x, axisScale, BT0AgainstTNO, "BT0", "TNO/EAB");
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffTransferFunction(headings, model1, model2, x, axisScale, BT0AgainstTNO, "BT0", "TNO/EAB");
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffPrimaryParameters(headings, model1, model2, x, axisScale, BT0AgainstTNO, "BT0", "TNO/EAB");
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
		
	}

    public static void generateDiffKHM1TNO(Vector<String> headings, Vector<Double> Z0inf, Vector<Double> nVF, Vector<Double> Rs0, Vector<Double> qL,
    		Vector<Double> qH, Vector<Double> qx, Vector<Double> qy, Vector<Double> qc, Vector<Double> phi, Vector<Double> fd, Vector<Double> k1, Vector<Double> k2, Vector<Double> k3,
    		Vector<Double> h1, Vector<Double> h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc, boolean KHM1AgainstTNO) {
    	
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
            	DiffBetweenModelsController.generateDiffPropagationConstant(headings, model1, model2, x, axisScale, KHM1AgainstTNO, "KHM1", "TNO/EAB");
                break;
            case "Characteristic Impedance":
            	DiffBetweenModelsController.generateDiffCharacteristicImpedance(headings, model1, model2, x, axisScale, KHM1AgainstTNO, "KHM1", "TNO/EAB");
                break;
            case "Transfer Function":
            	DiffBetweenModelsController.generateDiffTransferFunction(headings, model1, model2, x, axisScale, KHM1AgainstTNO, "KHM1", "TNO/EAB");
                break;
            case "Primary Parameters":
            	DiffBetweenModelsController.generateDiffPrimaryParameters(headings, model1, model2, x, axisScale, KHM1AgainstTNO, "KHM1", "TNO/EAB");
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
    }

	public static void generateDiffPropagationConstant(Vector<String> headings, Vector models1,
			Vector models2, Vector<Double> x, String axisScale, boolean KHM1AgainstTNO, 
			String model1, String model2) {
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/

        Vector<Vector<Double>> alpha1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama1 = new Vector<Vector<Double>>();

        if(model1.equals("KHM1")) {
        	
            for(int i = 0; i < models1.size(); i++) {
            	
            	alpha1.add(((KHM1) models1.get(i)).generateAlphaPropagationConstant(x));
            	beta1.add(((KHM1) models1.get(i)).generateBetaPropagationConstant(x));
                ghama1.add(((KHM1) models1.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }else if(model1.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models1.size(); i++) {
            	
            	alpha1.add(((TNO_EAB) models1.get(i)).generateAttenuationConstant(x));
            	beta1.add(((TNO_EAB) models1.get(i)).generatePhaseConstant(x));
                ghama1.add(((TNO_EAB) models1.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }else if(model1.equals("BT0")) {

            for(int i = 0; i < models1.size(); i++) {
            	
            	alpha1.add(((BT0) models1.get(i)).generateAttenuationConstant(x));
            	beta1.add(((BT0) models1.get(i)).generatePhaseConstant(x));
                ghama1.add(((BT0) models1.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }
        
        Vector<Vector<Double>> alpha2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama2 = new Vector<Vector<Double>>();
        
        if(model2.equals("KHM1")) {
        	
            for(int i = 0; i < models2.size(); i++) {
            	
            	alpha2.add(((KHM1) models2.get(i)).generateAlphaPropagationConstant(x));
            	beta2.add(((KHM1) models2.get(i)).generateBetaPropagationConstant(x));
                ghama2.add(((KHM1) models2.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }else if(model2.equals("TNO/EAB")) {
        	        	
            for(int i = 0; i < models2.size(); i++) {
            	
            	alpha2.add(((TNO_EAB) models2.get(i)).generateAttenuationConstant(x));
            	beta2.add(((TNO_EAB) models2.get(i)).generatePhaseConstant(x));
                ghama2.add(((TNO_EAB) models2.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }else if(model2.equals("BT0")) {
        	
            for(int i = 0; i < models2.size(); i++) {
            	
            	Vector<Double> addToAlpha = ((BT0) models2.get(i)).generateAttenuationConstant(x);
            	Vector<Double> addToBeta = ((BT0) models2.get(i)).generatePhaseConstant(x);
            	
            	alpha2.add(addToAlpha);
            	beta2.add(addToBeta);
                ghama2.add(((BT0) models2.get(i)).generatePropagationConstantAbs(x));

            }
        	
        }
        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> alpha = new Vector<Vector<Double>>();
        Vector<Vector<Double>> beta = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ghama = new Vector<Vector<Double>>();
                
        if(KHM1AgainstTNO) {
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
        }else {
	        for(int j = 0; j < ghama2.size(); j++) {
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
        }
        

        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        String complement = "";
        
        if(KHM1AgainstTNO) {
        	complement += "("+model1+" - "+model2+")";
        }else {
        	complement += "("+model2+" - "+model1+")";
        }
                
        /*CREATE FIRST GRAPH, ALPHA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, alpha, "Attenuation Constant"+complement, headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, alpha, "Attenuation Constant"+complement, headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Attenuation Constant");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, BETA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, beta, "Phase Constant"+complement, headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, beta, "Phase Constant"+complement, headings, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Phase Constant");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, GHAMA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, ghama, "Propagation Constant"+complement, headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, ghama, "Propagation Constant"+complement, headings, "Frequency (Hz)", "", false);                                    

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
	        if(KHM1AgainstTNO) {
		        chart.setTitle("Propagation Constant - Difference between "+model1+" and "+model2);	        	
	        }else {
		        chart.setTitle("Propagation Constant - Difference between "+model2+" and "+model1);	        	
	        }
        } catch (Exception e) {
			e.printStackTrace();
		}
                
        chart.show();
		chart.toFront();
	}

	private static void generateDiffCharacteristicImpedance(Vector<String> headings, Vector models1,
			Vector models2, Vector<Double> x, String axisScale, boolean KHM1AgainstTNO, String model1, String model2) {
    	/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> real1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI1 = new Vector<Vector<Double>>();
        
        if(model1.equals("KHM1")) {
        	
            for(int i = 0; i < models1.size(); i++) {
            	
            	real1.add(((KHM1) models1.get(i)).generateRealCharacteristicImpedance(x));
            	imag1.add(((KHM1) models1.get(i)).generateImagCharacteristicImpedance(x));
                CI1.add(((KHM1) models1.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }else if(model1.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models1.size(); i++) {
            	
            	real1.add(((TNO_EAB) models1.get(i)).generateRealCharacteristicImpedance(x));
            	imag1.add(((TNO_EAB) models1.get(i)).generateImagCharacteristicImpedance(x));
                CI1.add(((TNO_EAB) models1.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }else if(model1.equals("BT0")) {

            for(int i = 0; i < models1.size(); i++) {
            	
            	real1.add(((BT0) models1.get(i)).generateRealCharacteristicImpedance(x));
            	imag1.add(((BT0) models1.get(i)).generateImagCharacteristicImpedance(x));
                CI1.add(((BT0) models1.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }
        

        Vector<Vector<Double>> real2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI2 = new Vector<Vector<Double>>();
        
        if(model2.equals("KHM1")) {
        	
            for(int i = 0; i < models2.size(); i++) {
            	
            	real2.add(((KHM1) models2.get(i)).generateRealCharacteristicImpedance(x));
            	imag2.add(((KHM1) models2.get(i)).generateImagCharacteristicImpedance(x));
                CI2.add(((KHM1) models2.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }else if(model2.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models2.size(); i++) {
            	
            	real2.add(((TNO_EAB) models2.get(i)).generateRealCharacteristicImpedance(x));
            	imag2.add(((TNO_EAB) models2.get(i)).generateImagCharacteristicImpedance(x));
                CI2.add(((TNO_EAB) models2.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }else if(model2.equals("BT0")) {

            for(int i = 0; i < models2.size(); i++) {
            	
            	real2.add(((BT0) models2.get(i)).generateRealCharacteristicImpedance(x));
            	imag2.add(((BT0) models2.get(i)).generateImagCharacteristicImpedance(x));
                CI2.add(((BT0) models2.get(i)).generateCharacteristicImpedanceAbs(x));

            }
        	
        }        
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> real = new Vector<Vector<Double>>();
        Vector<Vector<Double>> imag = new Vector<Vector<Double>>();
        Vector<Vector<Double>> CI = new Vector<Vector<Double>>();

        if(KHM1AgainstTNO) {
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
        }else {
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
        }
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        String complement = "";
        
        if(KHM1AgainstTNO) {
        	complement += "("+model1+" - "+model2+")";
        }else {
        	complement += "("+model2+" - "+model1+")";
        }

        
        /*CREATE FIRST GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, real, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, real, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Characteristic Impedance - Real");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATE SECOND GRAPH, IMAGINARY*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart  (x, imag, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, imag, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                                    

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Characteristic Impedance - Imaginary");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, MODULE*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, CI, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                
        else
            graph = chartController.createLinearLineChart(x, CI, "Characteristic Impedance"+complement, headings, "Frequency (Hz)", "Ω", false);                                    

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
	        if(KHM1AgainstTNO) {
		        chart.setTitle("Characteristic Impedance - Difference between "+model1+" and "+model2);	        	
	        }else {
		        chart.setTitle("Characteristic Impedance - Difference between "+model2+" and "+model1);	        	
	        }
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
		
	}

	private static void generateDiffTransferFunction(Vector<String> headings, Vector models1, Vector models2, Vector<Double> x,
			String axisScale, boolean KHM1AgainstTNO, String model1, String model2) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> TF1 = new Vector<Vector<Double>>();
        
        if(model1.equals("KHM1")) {

            for(int i = 0; i < models1.size(); i++) {
            	
                TF1.add(((KHM1) models1.get(i)).generateTransferFunctionGain(x));

            }
        	
        }else if(model1.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models1.size(); i++) {
            	
                TF1.add(((TNO_EAB) models1.get(i)).generateTransferFunctionGain(x));

            }
        	
        }else if(model1.equals("BT0")) {

            for(int i = 0; i < models1.size(); i++) {
            	
                TF1.add(((BT0) models1.get(i)).generateTransferFunctionGain(x));

            }
        	
        }
        

        Vector<Vector<Double>> TF2 = new Vector<Vector<Double>>();

        if(model2.equals("KHM1")) {

            for(int i = 0; i < models2.size(); i++) {
            	
                TF2.add(((KHM1) models2.get(i)).generateTransferFunctionGain(x));

            }
        	
        }else if(model2.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models2.size(); i++) {
            	
                TF2.add(((TNO_EAB) models2.get(i)).generateTransferFunctionGain(x));

            }
        	
        }else if(model2.equals("BT0")) {

            for(int i = 0; i < models2.size(); i++) {
            	
                TF2.add(((BT0) models2.get(i)).generateTransferFunctionGain(x));

            }
        	
        }
                
        /*GENERATE PARAMETERS TO TAKE DIFFERENCE*/
        Vector<Vector<Double>> TF = new Vector<Vector<Double>>();
                
        if(KHM1AgainstTNO) {
            for(int j = 0; j < TF2.size(); j++) {
            	Vector<Double> addToTF = new Vector<Double>();
                for(int i = 0; i < TF1.get(j).size(); i++) {
                	addToTF.add(TF1.get(j).get(i) - TF2.get(j).get(i));
                }
                TF.add(addToTF);
            }
        }else {
            for(int j = 0; j < TF2.size(); j++) {
            	Vector<Double> addToTF = new Vector<Double>();
                for(int i = 0; i < TF1.get(j).size(); i++) {
                	addToTF.add(TF2.get(j).get(i) - TF1.get(j).get(i));
                }
                TF.add(addToTF);
            }
        }
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        String complement = "";
        
        if(KHM1AgainstTNO) {
        	complement += "("+model1+" - "+model2+")";
        }else {
        	complement += "("+model2+" - "+model1+")";
        }
        
        /*CREATE FIRST GRAPH, TRANSFER FUNCTION GAIN*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, TF, "Transfer Function Gain"+complement, headings, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, TF, "Transfer Function Gain"+complement, headings, "Frequency (Hz)", "", false);                                    

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
	        if(KHM1AgainstTNO) {
		        chart.setTitle("Transfer Function - Difference between "+model1+" and "+model2);	        	
	        }else {
		        chart.setTitle("Transfer Function - Difference between "+model2+" and "+model1);	        	
	        }	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
		
	}

    private static void generateDiffPrimaryParameters(Vector<String> headings, Vector models1, Vector models2, Vector<Double> x,
			String axisScale, boolean KHM1AgainstTNO, String model1, String model2) {

		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
		
        /*GET ALL PARAMETERS TO PLOT*/
        Vector<Vector<Double>> SeriesResistance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance1 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance1 = new Vector<Vector<Double>>();

        if(model1.equals("KHM1")) {

            for(int i = 0; i < models1.size(); i++) {

                SeriesResistance1.add(((KHM1) models1.get(i)).generateSeriesResistance(x));
                SeriesInductance1.add(((KHM1) models1.get(i)).generateSeriesInductance(x));
                ShuntingConductance1.add(((KHM1) models1.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance1.add(((KHM1) models1.get(i)).generateShuntingCapacitance(x));

            }
        	
        }else if(model1.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models1.size(); i++) {

                SeriesResistance1.add(((TNO_EAB) models1.get(i)).generateSeriesResistance(x));
                SeriesInductance1.add(((TNO_EAB) models1.get(i)).generateSeriesInductance(x));
                ShuntingConductance1.add(((TNO_EAB) models1.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance1.add(((TNO_EAB) models1.get(i)).generateShuntingCapacitance(x));

            }
        	
        }else if(model1.equals("BT0")) {

            for(int i = 0; i < models1.size(); i++) {

                SeriesResistance1.add(((BT0) models1.get(i)).generateSeriesResistance(x));
                SeriesInductance1.add(((BT0) models1.get(i)).generateSeriesInductance(x));
                ShuntingConductance1.add(((BT0) models1.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance1.add(((BT0) models1.get(i)).generateShuntingCapacitance(x));

            }
        	
        }
        
        
        Vector<Vector<Double>> SeriesResistance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance2 = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance2 = new Vector<Vector<Double>>();

        if(model2.equals("KHM1")) {

            for(int i = 0; i < models2.size(); i++) {

                SeriesResistance2.add(((KHM1) models2.get(i)).generateSeriesResistance(x));
                SeriesInductance2.add(((KHM1) models2.get(i)).generateSeriesInductance(x));
                ShuntingConductance2.add(((KHM1) models2.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance2.add(((KHM1) models2.get(i)).generateShuntingCapacitance(x));

            }
        	
        }else if(model2.equals("TNO/EAB")) {
        	
            for(int i = 0; i < models2.size(); i++) {

                SeriesResistance2.add(((TNO_EAB) models2.get(i)).generateSeriesResistance(x));
                SeriesInductance2.add(((TNO_EAB) models2.get(i)).generateSeriesInductance(x));
                ShuntingConductance2.add(((TNO_EAB) models2.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance2.add(((TNO_EAB) models2.get(i)).generateShuntingCapacitance(x));

            }
        	
        }else if(model2.equals("BT0")) {

            for(int i = 0; i < models2.size(); i++) {

                SeriesResistance2.add(((BT0) models2.get(i)).generateSeriesResistance(x));
                SeriesInductance2.add(((BT0) models2.get(i)).generateSeriesInductance(x));
                ShuntingConductance2.add(((BT0) models2.get(i)).generateShuntingConductance(x));
                ShuntingCapacitance2.add(((BT0) models2.get(i)).generateShuntingCapacitance(x));

            }
        	
        }
        
        Vector<Vector<Double>> SeriesResistance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> SeriesInductance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingCapacitance = new Vector<Vector<Double>>();
        Vector<Vector<Double>> ShuntingConductance = new Vector<Vector<Double>>();

        if(KHM1AgainstTNO) {
            for(int i = 0; i < models1.size(); i++) {
                Vector<Double> addToSRe = new Vector<Double>();
                Vector<Double> addToSIn = new Vector<Double>();
                Vector<Double> addToSCa = new Vector<Double>();
                Vector<Double> addToSCo = new Vector<Double>();
                for(int j = 0; j < SeriesResistance1.get(i).size(); j++) {	
                	addToSRe.add(SeriesResistance1.get(i).get(j) - SeriesResistance2.get(i).get(j));
                	addToSIn.add(SeriesInductance1.get(i).get(j) - SeriesInductance2.get(i).get(j));
                	addToSCa.add(ShuntingCapacitance1.get(i).get(j) - ShuntingCapacitance2.get(i).get(j));
                	addToSCo.add(ShuntingConductance1.get(i).get(j) - ShuntingConductance2.get(i).get(j));

                }
                SeriesResistance.add(addToSRe);
                SeriesInductance.add(addToSIn);
                ShuntingConductance.add(addToSCo);
                ShuntingCapacitance.add(addToSCa);
            }        	
        }else {
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
        }
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        String complement = "";
        
        if(KHM1AgainstTNO) {
        	complement += "("+model1+" - "+model2+")";
        }else {
        	complement += "("+model2+" - "+model1+")";
        }
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        /*CREATE FIRST GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart      (x, SeriesResistance, "Series Resistance"+complement, headings, "Frequency (Hz)", "Ω/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesResistance, "Series Resistance"+complement, headings, "Frequency (Hz)", "Ω/m", false);                

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Series Resistance");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);
        
        /*CREATE SECOND GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart      (x, ShuntingConductance, "Shunting Conductance"+complement, headings, "Frequency (Hz)", "S/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingConductance, "Shunting Conductance"+complement, headings, "Frequency (Hz)", "S/m", false);                

        /*ADDING GRAPH TO SECOND TAB*/
        Tab tab2 = new Tab();
        tab2.setClosable(false);
        tab2.setText("Shunting Conductance");
        tab2.setContent(graph);
        tabPane.getTabs().add(tab2);

        /*CREATE THIRD GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart      (x, SeriesInductance, "Series Inductance"+complement, headings, "Frequency (Hz)", "H/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, SeriesInductance, "Series Inductance"+complement, headings, "Frequency (Hz)", "H/m", false);                

        /*ADDING GRAPH TO THIRD TAB*/
        Tab tab3 = new Tab();
        tab3.setClosable(false);
        tab3.setText("Series Inductance");
        tab3.setContent(graph);
        tabPane.getTabs().add(tab3);

        /*CREATE FOURTH GRAPH, REAL*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart      (x, ShuntingCapacitance, "Shunting Capacitance"+complement, headings, "Frequency (Hz)", "F/m", false);                
        else
            graph = chartController.createLinearLineChart   (x, ShuntingCapacitance, "Shunting Capacitance"+complement, headings, "Frequency (Hz)", "F/m", false);                

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
	        if(KHM1AgainstTNO) {
		        chart.setTitle("Primary Parameters - Difference between "+model1+" and "+model2);	        	
	        }else {
		        chart.setTitle("Primary Parameters - Difference between "+model2+" and "+model1	);	        	
	        }
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        chart.show();
		chart.toFront();
    	
	}
	
}
