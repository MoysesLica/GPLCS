package MultiCable;

import java.awt.Toolkit;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import BT0.BT0;
import Complex.Complex;
import GPLCS.SimuladorGPLCS;
import KHM1.KHM1;
import TransmissionLine.GenericCableModel;
import charts.chartController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import tables.Table;

public class MultiCableController {

	public static void generateTransferFunction(Vector<GenericCableModel> data, Vector<Double> bridgeTap, double minF, double maxF,
			double toneSpacing, String axisScale, double zl, double zs) {

		/*GENERATE FREQUENCY*/
		Vector<Double> x  = new Vector<Double>();
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }

        Vector<Vector<Complex>> propagationConstant = new Vector<Vector<Complex>>();
        Vector<Vector<Complex>> characteristicImpedance = new Vector<Vector<Complex>>();

        for(int i = 0; i < data.size(); i++) {
        	
        	propagationConstant.add(data.get(i).generatePropagationConstant(x));
        	characteristicImpedance.add(data.get(i).generateCharacteristicImpedance(x));
        	
        }
                
        Vector<Vector<Complex[][]>> matrixABCD = new Vector<Vector<Complex[][]>>();
        for(int i = 0; i < data.size(); i++) {

        	boolean isBridgeTap = false;
        	
        	for(int j = 0; j < bridgeTap.size(); j++) {
        		
        		if(i == bridgeTap.get(j)) {
        			isBridgeTap  = true;
        		}
        		
        	}
        	
        	Vector<Complex[][]> values = new Vector<Complex[][]>();
        	
        	if(isBridgeTap) {
        		
            	for(int j = 0; j < x.size(); j++) {

            		Complex termo1 = propagationConstant.get(i).get(j).times(new Complex(data.get(i).getCableLength(), 0)).exp();
            		Complex termo2 = propagationConstant.get(i).get(j).times(new Complex(-data.get(i).getCableLength(), 0)).exp();            		

            		Complex cosh = termo1.plus(termo2).divides(new Complex(2, 0) )  ;
            		
            		Complex sinh = termo1.minus(termo2).divides(new Complex(2, 0) )  ;

            		Complex A = new Complex(1, 0);
            		Complex B = new Complex(0, 0);
            		Complex C = sinh.divides(cosh).divides(characteristicImpedance.get(i).get(j));
            		Complex D = new Complex(1,0);
            		
            		Complex[][] matrix = {{A, B}, {C, D}};
            		
            		values.add(matrix);
            		
            	}
        		
        	}else {

            	for(int j = 0; j < x.size(); j++) {

            		Complex termo1 = propagationConstant.get(i).get(j).times(new Complex(data.get(i).getCableLength(), 0)).exp();
            		Complex termo2 = propagationConstant.get(i).get(j).times(new Complex(-data.get(i).getCableLength(), 0)).exp();            		
            		
            		Complex cosh = termo1.plus(termo2).divides(new Complex(2, 0) )  ;
            		
            		Complex sinh = termo1.minus(termo2).divides(new Complex(2, 0) )  ;

            		Complex A = cosh;
            		Complex B = characteristicImpedance.get(i).get(j).times(sinh);
            		Complex C = sinh.divides(characteristicImpedance.get(i).get(j));
            		Complex D = cosh;
            		
            		Complex[][] matrix = {{A, B}, {C, D}};
            		
            		values.add(matrix);
            		
            	}
        	
        	}
        	
        	matrixABCD.add(values);
        
        }
        
    	Vector<Vector<Complex[][]>> matrixTot = new Vector<Vector<Complex[][]>>();
		matrixTot.add(matrixABCD.get(0));
		matrixABCD.remove(0);
    	
    	/*GENERATE THE MATRIX TOTAL*/
    	while(matrixABCD.size() > 0) {
    		
    		Vector<Complex[][]> newMatrix = new Vector<Complex[][]>();
    		
    		for(int i = 0; i < x.size(); i++) {
    			
    			Complex a1 = matrixTot.get(0).get(i)[0][0];
    			Complex b1 = matrixTot.get(0).get(i)[0][1];
    			Complex c1 = matrixTot.get(0).get(i)[1][0];
    			Complex d1 = matrixTot.get(0).get(i)[1][1];

    			Complex a2 = matrixABCD.get(0).get(i)[0][0];
    			Complex b2 = matrixABCD.get(0).get(i)[0][1];
    			Complex c2 = matrixABCD.get(0).get(i)[1][0];
    			Complex d2 = matrixABCD.get(0).get(i)[1][1];

    			Complex a = a1.times(a2).plus(b1.times(c2));
    			Complex b = a1.times(b2).plus(b1.times(d2));
    			Complex c = c1.times(a2).plus(d1.times(c2));
    			Complex d = c1.times(b2).plus(d1.times(d2));
    			
    			Complex[][] tot = {{a,b},{c,d}};
    			
    			newMatrix.add(tot);
    			
    		}
    		
    		matrixTot.set(0, newMatrix);
    		matrixABCD.remove(0);
    		
    	}
    	
    	Vector<Double> transferFunction = new Vector<Double>();
    	
    	for(int i = 0; i < matrixTot.get(0).size(); i++) {

    		Complex A = matrixTot.get(0).get(i)[0][0];
    		Complex B = matrixTot.get(0).get(i)[0][1];
    		Complex C = matrixTot.get(0).get(i)[1][0];
    		Complex D = matrixTot.get(0).get(i)[1][1];
    	
    		Complex h = new Complex((zs + zl), 0).divides(A.times(new Complex(zl, 0)).plus(B).plus(C.times(new Complex(zs*zl, 0))).plus(D.times(new Complex(zs, 0))));
    		
    		transferFunction.add(20*Math.log10(h.abs()));    		
    	
    	}
    	   
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;
        
        /*CREATE CHAR VAR*/
        LineChart graph;
        
        /*CREATE THE SCENE*/
        Group root = new Group();
        Scene scene = new Scene(root, screenWidth*50, screenHeight*50, Color.WHITE);

        /*CREATE TAB PANE*/
        TabPane tabPane = new TabPane();
        
        Vector<String> heading = new Vector<String>();
        heading.add("Transfer Function");
        
        Vector<Vector<Double>> vectorPlot = new Vector<Vector<Double>>();
        vectorPlot.add(transferFunction);
        
        /*CREATE FIRST GRAPH, ALPHA*/
        if(axisScale.contains("Logarithmic"))
            graph = chartController.createLogLineChart   (x, vectorPlot, "Transfer Function", heading, "Frequency (Hz)", "", false);                
        else
            graph = chartController.createLinearLineChart(x, vectorPlot, "Transfer Function", heading, "Frequency (Hz)", "", false);                                    

        /*ADDING GRAPH TO FIRST TAB*/
        Tab tab1 = new Tab();
        tab1.setClosable(false);
        tab1.setText("Transfer Function");
        tab1.setContent(graph);
        tabPane.getTabs().add(tab1);

        /*CREATING THE COLUMNS OF TABLE*/
        Vector<String> superHeadings = new Vector<String>();
        superHeadings.add("Frequency(Hz)");
        
        for(int i = 0; i < heading.size(); i++) {
        	superHeadings.add(heading.get(i).toString());
        }
        
        Vector<String> subHeadings = new Vector<String>();
        subHeadings.add("transferFunction");

        /*CREATE DATA OF TABLE*/
        String[][] dataTable = new String[x.size()][1 + (heading.size() * 1)];        

        for(int i = 0; i < x.size(); i++) {
            dataTable[i][0] = x.get(i).toString();
        }

        for(int k = 0; k < heading.size(); k++) {
        
	        for(int i = 0; i < x.size(); i++) {
	            dataTable[i][1 + (k * 1)] = vectorPlot.get(k).get(i).toString();
	        }
        	
        }        

        /*ADDING TABLE TO TAB*/
        Tab tab4 = new Tab();
        tab4.setClosable(false);
        tab4.setText("Values");
        tab4.setContent(Table.generateTable(superHeadings, subHeadings, dataTable));
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
	        chart.setTitle("Transfer Function, multiples cables");
	        
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                
        chart.show();
		chart.toFront();
    	
	}

	
		
}