package KHM;

import com.emxsys.chart.LogLineChart;
import java.util.Vector;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

/**
 * @author moyses
 */

public class KHMController {

    public static LineChart generatePropagationConstant(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing){

        Vector x  = new Vector();
        
        for(double f = minF; f <= maxF; f += toneSpacing){
            x.add(f);
        }
        
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
        Vector alpha = model.generateAlpha(x);
        Vector beta = model.generateBeta(x);
        Vector gama = new Vector();
        for(int i = 0; i < x.size(); i++){
            double a = Math.pow(Double.parseDouble(alpha.get(i).toString()),2);
            double b = Math.pow(Double.parseDouble(beta.get(i).toString()),2);
            gama.add(Math.sqrt(a + b));
        }
        charts.chartController.teste();
        return charts.chartController.createLogLineChart(x, gama, "Propagation Constant", "PC", "Frequency(Hz)", "Magnitude");
//        return charts.chartController.createLinearLineChart(x, alpha, beta, "Propagation Constant", "Alpha", "Beta", "Frequency(Hz)", "Magnitude");
        
    }
    
}
