package charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

//import com.emxsys.chart.*;
//import com.emxsys.chart.extension.LogarithmicAxis;
import java.util.Vector;
import javafx.scene.chart.NumberAxis;

/**
 * @author moyses
 */
public class chartController {

   public static LineChart createLogLineChart(Vector x, Vector y, String title, String serieLabel, String labelAxisX, String labelAxisY) {
       
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(labelAxisX);
        yAxis.setLabel(labelAxisY);
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(title);
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName(serieLabel);
        //populating the series with data
        
        for(int i = 0; i < x.size(); i++){
        
            series.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y.get(i).toString())
            ));
            
        }
        
        lineChart.getData().add(series);
        
        return lineChart;
        
    }    
}
