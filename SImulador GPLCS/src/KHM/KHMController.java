package KHM;

import com.emxsys.chart.LogLineChart;
import java.util.Vector;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

/**
 * @author moyses
 */

public class KHMController {

    public static LineChart generateAlpha(double k1, double k2, double k3, double h1, double h2, double cableLength, double minF, double maxF, double toneSpacing){

        Vector x  = new Vector();
        
        for(double f = minF; f <= maxF; f += toneSpacing){
        
            x.add(f);
            
        }
        
        KHM model = new KHM(k1,k2,k3,h1,h2,cableLength);
        Vector alpha = model.generateAlpha(x);
        
        return charts.chartController.createLogLineChart(x, alpha, "Propagation Constant - Alpha Graph", "Alpha", "Frequency(Hz)", "Magnitude");
        
    }
    
}
