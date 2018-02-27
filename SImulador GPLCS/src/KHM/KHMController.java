package KHM;

import charts.chartController;
import com.emxsys.chart.LogLineChart;
import java.awt.Toolkit;
import java.util.Vector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * @author moyses
 */

public class KHMController {

    public static void generatePropagationConstant(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing, String axisScale, String parameterCalc){

        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
        int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        Vector x  = new Vector();
        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
        
        switch(parameterCalc){
        
            case "Propagation Constant":
                Vector alpha = model.generateAlpha(x);
                Vector beta = model.generateBeta(x);
                Vector gama = new Vector();
                for(int i = 0; i < x.size(); i++){
                    gama.add(Math.sqrt(Math.pow(Double.parseDouble(alpha.get(i).toString()), 2) + Math.pow(Double.parseDouble(beta.get(i).toString()), 2)));
                }
                
                LineChart graphPC;
                
                FlowPane rootPC = new FlowPane();
                
                if(axisScale.contains("Logarithmic"))
                    graphPC = chartController.createLogLineChart   (x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude");                
                else
                    graphPC = chartController.createLinearLineChart(x, alpha, "Propagation Constant - Alpha", "Alpha", "Frequency (Hz)", "Magnitude");                                    
                
                rootPC.getChildren().add(graphPC);
                
                if(axisScale.contains("Logarithmic"))
                    graphPC = chartController.createLogLineChart   (x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude");                
                else
                    graphPC =chartController.createLinearLineChart(x, beta, "Propagation Constant - Beta", "Beta", "Frequency (Hz)", "Magnitude");                                    

                rootPC.getChildren().add(graphPC);

                if(axisScale.contains("Logarithmic"))
                    graphPC = chartController.createLogLineChart   (x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude");                
                else
                    graphPC = chartController.createLinearLineChart(x, gama, "Propagation Constant - Module", "Gama", "Frequency (Hz)", "Magnitude");                                    

                rootPC.getChildren().add(graphPC);

                ScrollPane scrollPanePC = new ScrollPane();
                scrollPanePC.setContent(rootPC);
                scrollPanePC.setPannable(true);
                scrollPanePC.setFitToWidth(true);
                scrollPanePC.setFitToHeight(true);

                Scene scenePC = new Scene(scrollPanePC, screenWidth*80, screenHeight*80);

                Stage chartPC = new Stage();
                chartPC.setScene(scenePC);
                chartPC.show();
                
                break;
                
            case "Characteristic Impedance":
                Vector real = model.generateRealCharacteristicImpedance(x);
                Vector imag = model.generateImagCharacteristicImpedance(x);
                Vector CI = new Vector();
                for(int i = 0; i < x.size(); i++){
                    CI.add(Math.sqrt(Math.pow(Double.parseDouble(real.get(i).toString()), 2) + Math.pow(Double.parseDouble(imag.get(i).toString()), 2)));
                }
                
                LineChart graphCI;
                
                FlowPane rootCI = new FlowPane();
                
                if(axisScale.contains("Logarithmic"))
                    graphCI = chartController.createLogLineChart   (x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude");                
                else
                    graphCI = chartController.createLinearLineChart(x, real, "Characteristic Impedance - Real", "Real", "Frequency (Hz)", "Magnitude");                                    
                rootCI.getChildren().add(graphCI);
                
                if(axisScale.contains("Logarithmic"))
                    graphCI = chartController.createLogLineChart   (x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude");                
                else
                    graphCI =chartController.createLinearLineChart(x, imag, "Characteristic Impedance - Imaginary", "Imaginary", "Frequency (Hz)", "Magnitude");                                    

                rootCI.getChildren().add(graphCI);

                if(axisScale.contains("Logarithmic"))
                    graphCI = chartController.createLogLineChart   (x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude");                
                else
                    graphCI = chartController.createLinearLineChart(x, CI, "Characteristic Impedance - Module", "Characteristic Impedance", "Frequency (Hz)", "Magnitude");                                    

                rootCI.getChildren().add(graphCI);

                ScrollPane scrollPaneCI = new ScrollPane();
                scrollPaneCI.setContent(rootCI);
                scrollPaneCI.setPannable(true);
                scrollPaneCI.setFitToWidth(true);
                scrollPaneCI.setFitToHeight(true);

                Scene sceneCI = new Scene(scrollPaneCI, screenWidth*80, screenHeight*80);

                Stage chartCI = new Stage();
                chartCI.setScene(sceneCI);
                chartCI.show();
                
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
