package TNO_EAB;

import charts.chartController;
import Complex.Complex;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Vector;

import GPLCS.SimuladorGPLCS;
import KHM.KHM;
import KHM.KHMController;
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
//            	TNO_EABController.generateCharacteristicImpedance(model, x, axisScale);
                break;
            case "Transfer Function":
//            	TNO_EABController.generateTransferFunction(model, x, axisScale);
                break;
            case "Primary Parameters":
//            	TNO_EABController.generatePrimaryParameters(model, x, axisScale);
                break;
            default:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(axisScale + " - " + parameterCalc);
                    alert.showAndWait();
                break;
        }
        
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
            graph = chartController.createLinearLineChart(x, beta, "Phase Constant", "Phase Constant", "Frequency (Hz)", "Radians/km", false);                                    

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
