package charts;

import com.emxsys.chart.LogLineChart;
import com.emxsys.chart.extension.LogarithmicAxis;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

//import com.emxsys.chart.*;
//import com.emxsys.chart.extension.LogarithmicAxis;
import java.util.Vector;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author moyses
 */
public class chartController {
    
    public static LogLineChart createLogLineChart(Vector x, Vector y, String title, String serieLabel, String labelAxisX, String labelAxisY) {

        final double MIN_X = Double.parseDouble(x.get(0).toString());
        final double MAX_X = Double.parseDouble(x.get(x.size() - 1).toString());
        final double X_TICK_UNIT = 1d;

        ObservableList<XYChart.Series> dataset = FXCollections.observableArrayList();
        LineChart.Series series1 = new LineChart.Series();
        series1.setName(serieLabel);
        for (int i = 1; i < x.size(); i++) {
            series1.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y.get(i).toString())
            ));
        }
        dataset.add(series1);

        LogarithmicAxis xAxis = new LogarithmicAxis(labelAxisX, MIN_X, MAX_X, X_TICK_UNIT);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(labelAxisY);
        LogLineChart chart = new LogLineChart(xAxis, yAxis, dataset);
        chart.setTitle(title);
        
        return chart;
        
    } 
    
    public static LogLineChart createLogLineChart(Vector x, Vector y1, Vector y2, String title, String serie1Label, String serie2Label, String labelAxisX, String labelAxisY) {

        final double MIN_X = Double.parseDouble(x.get(0).toString());
        final double MAX_X = Double.parseDouble(x.get(x.size() - 1).toString());
        final double X_TICK_UNIT = 1d;

        ObservableList<XYChart.Series> dataset = FXCollections.observableArrayList();
        LineChart.Series series1 = new LineChart.Series();
        series1.setName(serie1Label);
        for (int i = 1; i < x.size(); i++) {
            series1.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y1.get(i).toString())
            ));
        }
        dataset.add(series1);

        LineChart.Series series2 = new LineChart.Series();
        series2.setName(serie2Label);
        for (int i = 1; i < x.size(); i++) {
            series1.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y2.get(i).toString())
            ));
        }
        dataset.add(series2);

        LogarithmicAxis xAxis = new LogarithmicAxis(labelAxisX, MIN_X, MAX_X, X_TICK_UNIT);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(labelAxisY);
        LogLineChart chart = new LogLineChart(xAxis, yAxis, dataset);
        chart.setTitle(title);
        
        return chart;
        
    } 
    
    public static LineChart createLinearLineChart(Vector x, Vector y, String title, String serieLabel, String labelAxisX, String labelAxisY) {
       
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
   
    public static LineChart createLinearLineChart(Vector x, Vector y1, Vector y2, String title, String serie1Label, String serie2Label, String labelAxisX, String labelAxisY) {
       
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(labelAxisX);
        yAxis.setLabel(labelAxisY);
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(title);
        //defining a series
        XYChart.Series series1 = new XYChart.Series();
        series1.setName(serie1Label);
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(serie2Label);
        //populating the series with data
        
        for(int i = 0; i < x.size(); i++){
            series1.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y1.get(i).toString())
            ));
        }

        for(int i = 0; i < x.size(); i++){
            series2.getData().add(new XYChart.Data(
                    Double.parseDouble(x.get(i).toString()),
                    Double.parseDouble(y2.get(i).toString())
            ));
        }
        
        lineChart.getData().addAll(series1, series2); 
       
        return lineChart;
        
    }    
   
}
