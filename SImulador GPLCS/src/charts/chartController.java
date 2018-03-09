package charts;

import com.emxsys.chart.LogLineChart;
import com.emxsys.chart.extension.LogarithmicAxis;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
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

import java.util.Vector;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * @author moyses
 */
public class chartController {
    
    public static LogLineChart createLogLineChart(Vector x, Vector y, String title, String serieLabel, String labelAxisX, String labelAxisY, boolean useCustomScale) {

        NumberFormat format = new DecimalFormat("#.###");
                
        final double MIN_X = Double.parseDouble(x.get(0).toString());
        final double MAX_X = Double.parseDouble(x.get(x.size() - 1).toString());
        final double X_TICK_UNIT = 1d;

        ObservableList<XYChart.Series> dataset = FXCollections.observableArrayList();
        LineChart.Series series1 = new LineChart.Series();


    	int minScale = 0;
    	
    	if(useCustomScale) {
        	
        	/*GET THE MIN SCALE OF VALUES*/
    		minScale = (new BigDecimal(y.get(0).toString())).scale();
        	for(int i = 1; i < x.size(); i++)
        		if((new BigDecimal(y.get(i).toString())).scale() < minScale)
        			minScale = (new BigDecimal(y.get(i).toString())).scale();
        	
        	/*SET THE NEW SCALE OF VALUES*/
        	for(int i = 0; i < x.size(); i++)
    			(new BigDecimal(y.get(i).toString())).setScale((new BigDecimal(y.get(i).toString())).scale() - minScale);
        	
            series1.setName(serieLabel);
            for (int i = 1; i < x.size(); i++) {
                series1.getData().add(new XYChart.Data(
                        Double.parseDouble(x.get(i).toString()),
                        Double.parseDouble(y.get(i).toString())
                ));
            }
        	
        }else {
        	
            series1.setName(serieLabel);
            for (int i = 1; i < x.size(); i++) {
                series1.getData().add(new XYChart.Data(
                        Double.parseDouble(x.get(i).toString()),
                        Double.parseDouble(y.get(i).toString())
                ));
            }
        	
        }
        
        dataset.add(series1);

        LogarithmicAxis xAxis = new LogarithmicAxis(labelAxisX, MIN_X, MAX_X, X_TICK_UNIT);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(labelAxisY);
        
        final int scale = minScale;

        if(useCustomScale) {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue()) + " + " + scale;
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }else {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue());
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }
        
        LogLineChart chart = new LogLineChart(xAxis, yAxis, dataset);
        chart.setTitle(title);
        chart.setCreateSymbols(false); 
        
        return chart;
        
    } 

    public static LogLineChart createLogLineChart(Vector x, Vector y, String title, Vector seriesLabel, String labelAxisX, String labelAxisY, boolean useCustomScale) {

        NumberFormat format = new DecimalFormat("#.###");
                
        final double MIN_X = Double.parseDouble(x.get(0).toString());
        final double MAX_X = Double.parseDouble(x.get(x.size() - 1).toString());
        final double X_TICK_UNIT = 1d;

        ObservableList<XYChart.Series> dataset = FXCollections.observableArrayList();
        
        /*ADDING MULTIPLES DATAS*/
    	int minScale = 0;
        for(int k = 0; k < seriesLabel.size(); k++) {
        	
        	LineChart.Series series1 = new LineChart.Series();
        	
        	if(useCustomScale) {
            	
            	/*GET THE MIN SCALE OF VALUES*/
        		minScale = (new BigDecimal(((Vector)y.get(k)).get(0).toString())).scale();
            	for(int i = 1; i < x.size(); i++)
            		if((new BigDecimal(((Vector)y.get(k)).get(i).toString())).scale() < minScale)
            			minScale = (new BigDecimal(((Vector)y.get(k)).get(i).toString())).scale();
            	
            	/*SET THE NEW SCALE OF VALUES*/
            	for(int i = 0; i < x.size(); i++)
        			(new BigDecimal(((Vector)y.get(k)).get(i).toString())).setScale((new BigDecimal(((Vector)y.get(k)).get(i).toString())).scale() - minScale);
            	
                series1.setName(seriesLabel.get(k).toString());
                for (int i = 1; i < x.size(); i++) {
                    series1.getData().add(new XYChart.Data(
                            Double.parseDouble(x.get(i).toString()),
                            Double.parseDouble(((Vector)y.get(k)).get(i).toString())
                    ));
                }
            	
            }else {
            	
                series1.setName(seriesLabel.get(k).toString());
                for (int i = 1; i < x.size(); i++) {
                    series1.getData().add(new XYChart.Data(
                            Double.parseDouble(x.get(i).toString()),
                            Double.parseDouble(((Vector)y.get(k)).get(i).toString())
                    ));
                }
            	
            }
            
            dataset.add(series1);
        	
        }

        LogarithmicAxis xAxis = new LogarithmicAxis(labelAxisX, MIN_X, MAX_X, X_TICK_UNIT);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(labelAxisY);
        
        final int scale = minScale;

        if(useCustomScale) {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue()) + " + " + scale;
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }else {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue());
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }
        
        LogLineChart chart = new LogLineChart(xAxis, yAxis, dataset);
        chart.setTitle(title);
        chart.setCreateSymbols(false); 
        
        return chart;
        
    } 

    public static LineChart createLinearLineChart(Vector x, Vector y, String title, String serieLabel, String labelAxisX, String labelAxisY, boolean useCustomScale) {
       
        NumberFormat format = new DecimalFormat("#.###");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(labelAxisX);
        yAxis.setLabel(labelAxisY);

        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName(serieLabel);
        //populating the series with data
        
        int minScale = 0;
    	
    	if(useCustomScale) {
        	
        	/*GET THE MIN SCALE OF VALUES*/
    		minScale = (new BigDecimal(y.get(0).toString())).scale();
        	for(int i = 1; i < x.size(); i++)
        		if((new BigDecimal(y.get(i).toString())).scale() < minScale)
        			minScale = (new BigDecimal(y.get(i).toString())).scale();
        	
        	/*SET THE NEW SCALE OF VALUES*/
        	for(int i = 0; i < x.size(); i++)
    			(new BigDecimal(y.get(i).toString())).setScale((new BigDecimal(y.get(i).toString())).scale() - minScale);
        	
            series.setName(serieLabel);
            for (int i = 1; i < x.size(); i++) {
                series.getData().add(new XYChart.Data(
                        Double.parseDouble(x.get(i).toString()),
                        Double.parseDouble(y.get(i).toString())
                ));
            }
        	
        }else {
        	
            series.setName(serieLabel);
            for (int i = 1; i < x.size(); i++) {
                series.getData().add(new XYChart.Data(
                        Double.parseDouble(x.get(i).toString()),
                        Double.parseDouble(y.get(i).toString())
                ));
            }
        	
        }
    	
    	final int scale = minScale;
        
    	if(useCustomScale) {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue()) + scale;
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }else {

            yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                @Override
                public String toString(Number number) {
                    return format.format(number.doubleValue());
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return format.parse(string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0 ;
                    }
                }

            });

        }
    	
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(title);

        
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false); 
        
        return lineChart;
        
    }

    public static LineChart createLinearLineChart(Vector x, Vector y, String title, Vector seriesLabel, String labelAxisX, String labelAxisY, boolean useCustomScale) {
        
        NumberFormat format = new DecimalFormat("#.###");

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(labelAxisX);
        yAxis.setLabel(labelAxisY);
    	
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle(title);

        /*CREATE MULTIPLES SERIES*/
        for(int k = 0; k < seriesLabel.size(); k++) {
        	
            XYChart.Series series = new XYChart.Series();
            series.setName(seriesLabel.get(k).toString());
            //populating the series with data
            
            int minScale = 0;
        	
        	if(useCustomScale) {
            	
            	/*GET THE MIN SCALE OF VALUES*/
        		minScale = (new BigDecimal(((Vector)y.get(k)).get(0).toString())).scale();
            	for(int i = 1; i < x.size(); i++)
            		if((new BigDecimal(((Vector)y.get(k)).get(i).toString())).scale() < minScale)
            			minScale = (new BigDecimal(((Vector)y.get(k)).get(i).toString())).scale();
            	
            	/*SET THE NEW SCALE OF VALUES*/
            	for(int i = 0; i < x.size(); i++)
        			(new BigDecimal(((Vector)y.get(k)).get(i).toString())).setScale((new BigDecimal(y.get(i).toString())).scale() - minScale);
            	
                series.setName(seriesLabel.get(k).toString());
                for (int i = 1; i < x.size(); i++) {
                    series.getData().add(new XYChart.Data(
                            Double.parseDouble(x.get(i).toString()),
                            Double.parseDouble(((Vector)y.get(k)).get(i).toString())
                    ));
                }
            	
            }else {
            	
                series.setName(seriesLabel.get(k).toString());
                for (int i = 1; i < x.size(); i++) {
                    series.getData().add(new XYChart.Data(
                            Double.parseDouble(x.get(i).toString()),
                            Double.parseDouble(((Vector)y.get(k)).get(i).toString())
                    ));
                }
            	
            }
        	
        	final int scale = minScale;
            
        	if(useCustomScale) {

                yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                    @Override
                    public String toString(Number number) {
                        return format.format(number.doubleValue()) + scale;
                    }

                    @Override
                    public Number fromString(String string) {
                        try {
                            return format.parse(string);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0 ;
                        }
                    }

                });

            }else {

                yAxis.setTickLabelFormatter(new StringConverter<Number>() {

                    @Override
                    public String toString(Number number) {
                        return format.format(number.doubleValue());
                    }

                    @Override
                    public Number fromString(String string) {
                        try {
                            return format.parse(string);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0 ;
                        }
                    }

                });

            }

            lineChart.getData().add(series);

        }
        
        lineChart.setCreateSymbols(false); 
        
        return lineChart;
        
    }

}
