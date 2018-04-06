package MultiCable;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import BT0.BT0;
import BT0.BT0Controller;
import GPLCS.SimuladorGPLCS;
import TransmissionLine.GenericCableModel;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MultiCableScreen {

	public static ScrollPane getMultiCableScreen(Stage primaryStage) {

		/*SCREEN WIDTH AND HEIGHT*/
		int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
		int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
		
    	GridPane grid = new GridPane();
    	grid.setAlignment(Pos.CENTER);
    	grid.setPadding(new Insets(50, 50, 50, 50));
		grid.setVgap(20);
		grid.setHgap(20);
		
		int numberColumns = 4;
		
		for(int i = 0; i < numberColumns; i++) {

			ColumnConstraints col = new ColumnConstraints();
	        col.setPercentWidth(100/numberColumns);
	        grid.getColumnConstraints().add(col);

		}
		
        Group networkRegion = new Group();
        
        Group lineGroup = new Group();
        
        Rectangle borderLineCable = new Rectangle(0, 0, screenWidth*4, screenHeight*4);
        borderLineCable.setStroke(Color.BLACK);
        borderLineCable.setStrokeWidth(1);
        borderLineCable.setFill(Color.TRANSPARENT);
        
        borderLineCable.setOnMouseEntered(new EventHandler<MouseEvent>() {
		    @Override public void handle(MouseEvent mouseEvent) {
		    	primaryStage.getScene().setCursor(Cursor.HAND);
		    }
        });
        
        /*VAR OF CONTROL DRAG AND DROP*/
        Vector<Boolean> started = new Vector<Boolean>();
        started.add(false);
        
        borderLineCable.setOnMouseExited(new EventHandler<MouseEvent>() {
		    @Override public void handle(MouseEvent mouseEvent) {
		    	if(!started.get(0))
			    	primaryStage.getScene().setCursor(Cursor.DEFAULT);		    		
		    }
        });               
        
        Line lineCable = new Line(screenWidth*1, screenHeight*2, screenWidth*3, screenHeight*2);
        lineCable.setStroke(Color.BLACK);
        lineCable.setStrokeWidth(3);
        
        lineGroup.getChildren().add(borderLineCable);
        lineGroup.getChildren().add(lineCable);
        borderLineCable.toFront();
        
        /*CREATE NETWORK TOPOLOGY REGION*/
        Rectangle networkRegionBorder = new Rectangle( 0, 0, screenWidth*75, screenHeight*30);
        networkRegionBorder.setStroke(Color.BLACK);
        networkRegionBorder.setFill(Color.TRANSPARENT);
        networkRegionBorder.setStrokeWidth(1);
        
        networkRegion.getChildren().add(networkRegionBorder);
        networkRegionBorder.toFront();
        
        /*************************************************************************************************************************************************/
        /*DRAW FUNCTIONS*/
        Vector<Boolean> firstClick = new Vector<Boolean>();
        firstClick.add(false);
        Vector<Double> firstPoint = new Vector<Double>();
        firstPoint.add(0.0);firstPoint.add(0.0);
        
        borderLineCable.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                started.set(0, true);                
                primaryStage.getScene().setCursor(Cursor.CROSSHAIR);                                
            }
        });
        
        Vector<Line> cableSegment = new Vector<Line>();
        final double sizeCircle = 8.0;
		final double sizeLine = 5.0;
        Vector<Color> colors = new Vector<Color>();
		colors.add(Color.DARKBLUE);
		colors.add(Color.DARKCYAN);
		colors.add(Color.DARKGOLDENROD);
		colors.add(Color.DARKGREEN);
		colors.add(Color.DARKMAGENTA);
		colors.add(Color.DARKORANGE);
		colors.add(Color.DARKRED);
		
		Vector<GenericCableModel> config = new Vector<GenericCableModel>();
		Vector<Text> cableText = new Vector<Text>();
		Vector<Circle> endPoints = new Vector<Circle>();
		Vector<Circle> startPoint = new Vector<Circle>();
			
        /*GENERATE CALC BUTTON*/
        Region calcIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.CALCULATOR)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        

        Button calculate = new Button("Calculate", calcIcon);
        calculate.setId("calculate");
        /*SET BUTTON ONCLICK FUNCTION*/
        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*CHECK IF ALL CABLES ARE CONFIGURED*/
//            	if( config.size() == cableSegment.size() ) {
            	if(true) {
            	
//            		if( endPoints.size() != 0 ) {
            		if(true) {
            		
            			Map<Integer, Line> mapCableSegments = new HashMap<Integer, Line>();
            			for(int i = 0; i < cableSegment.size(); i++)
            				mapCableSegments.put(i, cableSegment.get(i));
            			
            			Vector<Map<Integer, Integer>> intersections = MultiCableController.organizeInMap(startPoint.get(0), mapCableSegments, endPoints);
            			/*PEGA O PRIMEIRO E SEGUE*/
            			for(int i = 0; i < intersections.size(); i++) {
            				System.err.println(intersections.get(i).toString());
            			}
            			
	            	}else {

	                    Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error");
	                    alert.setHeaderText("Error, set at minimum one end point!");
	                    alert.showAndWait();

	            	}
            		
            	}else {
            		
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error, set the configuration of each cable segment!");
                    alert.showAndWait();
            		
            	}
            	
            }
        });
            	
            	
        networkRegionBorder.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if(started.get(0)) {
            		if(!firstClick.get(0)) {

            			/*GET FIRSTS X AND Y*/
            			firstClick.set(0, true);
                		firstPoint.set(0, event.getX());
                		firstPoint.set(1, event.getY());
            		
                		Circle circle = MultiCableScreen.generateStandardPoint(event.getX(), event.getY(), sizeCircle, started, firstClick, firstPoint, colors, sizeLine, cableSegment, cableText, config, primaryStage, networkRegion, endPoints, startPoint);

                		networkRegion.getChildren().add(circle);
                		
            		}else {
            			
            			firstClick.set(0, false);
                		started.set(0, false);
            			
                		/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
                		
                		Line newSegment = MultiCableScreen.generateStandardLine(firstPoint.get(0), firstPoint.get(1), event.getX(), event.getY(),
                				colors.get(cableSegment.size() % 7), sizeLine, started, cableText, config, cableSegment, primaryStage, networkRegion);
                		
                		/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
                		                		
                		Circle circle = MultiCableScreen.generateStandardPoint(event.getX(), event.getY(), sizeCircle, started, firstClick, firstPoint, colors, sizeLine, cableSegment, cableText, config, primaryStage, networkRegion, endPoints, startPoint);

                		/*GENERATE POINT*/
                		networkRegion.getChildren().add(circle);
            			
                        primaryStage.getScene().setCursor(Cursor.DEFAULT);                                
            			
            		}
            	}
            }
        });
        
        /*CREATE THE LABEL OF SCREEN*/
        String ScreenName = "Multi Cable Synthesis";
        Label label = new Label(ScreenName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Drag and Drop the items to create a topology of network.");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);
        
        /*SET ELEMENTS TO GRID*/
        
        	int line = 0;
        
        	/*ADDING LINE*/
        	grid.add(label, 0, line, 4, 1);
	        GridPane.setHalignment(label, HPos.CENTER);
	        line++;
	        
        	/*ADDING LINE*/
	        grid.add(helpLabel, 0, line, 4, 1);
	        GridPane.setHalignment(helpLabel, HPos.CENTER);
	        line++;
	        
        	/*ADDING LINE*/
	        lineGroup.maxWidth(Double.MAX_VALUE);
	        grid.add(lineGroup, 1, line, 1, 1);
	        GridPane.setHalignment(lineGroup, HPos.CENTER);
	        
	        calculate.maxWidth(Double.MAX_VALUE);
	        calculate.maxHeight(Double.MAX_VALUE);
	        grid.add(calculate, 2, line, 1, 1);
	        GridPane.setHalignment(calculate, HPos.CENTER);

	        line++;
	        
        	/*ADDING LINE*/
	        networkRegion.maxWidth(Double.MAX_VALUE);
	        networkRegion.maxHeight(Double.MAX_VALUE);
	        grid.add(networkRegion, 0, line, 4, 1);
	        GridPane.setHalignment(networkRegion, HPos.CENTER);
	        line++;

	        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        
        return scroll;
        
	}
	
	public static Circle generateStandardPoint(double a, double b, double sizeCircle, Vector<Boolean> started, Vector<Boolean> firstClick, Vector<Double> firstPoint, Vector<Color> colors, double sizeLine, Vector<Line> cableSegment, Vector<Text> cableText, Vector<GenericCableModel> config, Stage primaryStage, Group networkRegion, Vector<Circle> endPoints, Vector<Circle> startPoint) {
		
		/*ON CLICK ON POINT, GENERATE CONNECTION*/
		
		/*GENERATE POINT*/
		Circle circle = new Circle(a, b, sizeCircle);
		
		/*ON DOUBLE CLICK GENERATE MODAL TO CONFIG SEGMENT*/
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

        	public void handle(MouseEvent event) {
        		
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){

                    	circle.setFill(Color.BLUEVIOLET);
                    	
                    	endPoints.add(circle);
                    	
            	        /*CREATE LABEL TO END POINT*/
            	        DropShadow ds = new DropShadow();
            	        ds.setOffsetY(3.0f);
            	        ds.setRadius(2.0f);
            	        ds.setColor(Color.BLUE);

            			Text textCable = new Text("End Point: " + endPoints.size());        	
            	        
            	        textCable.setEffect(ds);
            	        textCable.setCache(true);
            	        textCable.setStyle(
            	        		"    -fx-font: 12px Monospaced;\r\n" + 
            	        		"    -fx-stroke-width: 1;"
            	        		);
            	        textCable.setFill(Color.RED);
            	        textCable.applyCss(); 
            	        
            	        textCable.setX(circle.getCenterX() - textCable.getLayoutBounds().getWidth()*3/4);
            	        textCable.setY(circle.getCenterY() - 12);        
            	        
            	        networkRegion.getChildren().add(textCable);
                    	
                    }

                }
                
            }
        	
        });
		
		/*GENERATE FIRST POINT*/
		if(cableSegment.size() == 0) {
			circle.setFill(Color.RED);
			
	        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
	        DropShadow ds = new DropShadow();
	        ds.setOffsetY(3.0f);
	        ds.setRadius(2.0f);
	        ds.setColor(Color.BLUE);

			Text textCable = new Text("Start Point");        	
	        
	        textCable.setEffect(ds);
	        textCable.setCache(true);
	        textCable.setStyle(
	        		"    -fx-font: 12px Monospaced;\r\n" + 
	        		"    -fx-stroke-width: 1;"
	        		);
	        textCable.setFill(Color.RED);
	        textCable.applyCss(); 
	        
	        textCable.setX(a - textCable.getLayoutBounds().getWidth()*3/4);
	        textCable.setY(b - 12);        
	        
	        startPoint.add(circle);
	        
	        networkRegion.getChildren().add(textCable);

		}else {


			/*ON CLICK ON POINT, GENERATE CONNECTION*/
			circle.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	            	if(started.get(0)) {
	            		if(!firstClick.get(0)) {
	            			firstClick.set(0, true);
	                		firstPoint.set(0, circle.getCenterX());
	                		firstPoint.set(1, circle.getCenterY());                            			
	            		} else {

	                		/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	                		
	                		Line newSegment = MultiCableScreen.generateStandardLine(firstPoint.get(0), firstPoint.get(1), a, b,
	                				colors.get(cableSegment.size() % 7), sizeLine, started, cableText, config, cableSegment, primaryStage, networkRegion);
	                		
	                		/*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%*/
	            			
	                        primaryStage.getScene().setCursor(Cursor.DEFAULT);   
	                        firstClick.set(0, false);
	                		started.set(0, false);
	            		}
	            	}
	            }
	        });

		}
		
		circle.toFront();
		
		return circle;
		
	}
	
	public static Line generateStandardLine(double a, double b, double c, double d, Color color, double size, Vector<Boolean> started, Vector<Text> cableText, Vector<GenericCableModel> config, Vector<Line> cableSegment, Stage primaryStage, Group networkRegion) {
		
		Line newSegment = new Line(a,b,c,d);
		newSegment.setStroke(color);
		newSegment.setStrokeWidth(size);
		cableSegment.add(newSegment);
        newSegment.toBack();
        networkRegion.getChildren().add(newSegment);
		        
        /*ADD TEXT OF CABLE*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);
        
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        Text textCable = new Text("Unconfigured");        	        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        
        textCable.setFill(Color.RED);
        double angle = Math.acos( Math.abs((c - a)) / (Math.sqrt(Math.pow(c - a, 2) + Math.pow(d - b, 2))) ) * 180/Math.PI;
        double angle2 = Math.asin( Math.abs((c - a)) / (Math.sqrt(Math.pow(c - a, 2) + Math.pow(d - b, 2))) ) * 180/Math.PI;

        if(d - b > 0) {
            textCable.setRotate( angle );        	        	
        }else {
        	textCable.setRotate( angle2 - 90);
        }
        
        textCable.applyCss(); 
                
        textCable.setX((a + c)/2 - textCable.getLayoutBounds().getWidth()/2);
        textCable.setY((b + d)/2 - 12);        
        
        cableText.add(textCable);
        networkRegion.getChildren().add(textCable);
        
        /*****************************************************************/
        
        newSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	if(!started.get(0))
            		primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });

        newSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	if(!started.get(0))
            		primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });
        
        /*ON DOUBLE CLICK GENERATE MODAL TO CONFIG SEGMENT*/
        newSegment.setOnMouseClicked(new EventHandler<MouseEvent>() {

        	public void handle(MouseEvent event) {
        		
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){

                    	MultiCableScreen.generateModal(primaryStage, cableText, config, "BT0", cableSegment.indexOf(newSegment));
                    
                    }

                }
                
            }
        	
        });

        /*****************************************************************/
        
        return newSegment;
		
	}
	
	public static void generateModal(Stage primaryStage, Vector<Text> text, Vector<GenericCableModel> config, String cableModel, int position) {
		
    	Stage dialog = new Stage();
		
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(50,50,50,50));

		/*CREATE THE CONTROLS*/
			/*TO BT0*/
			if(cableModel.contains("BT0")) {
		        /*CREATE THE GRID*/
		        
		        /*CREATE THE LABEL OF SCREEN*/
		        String ApplicationName = "British Telecom Model 0 Cable Synthesis";
		        Label label = new Label(ApplicationName);
		        label.setId("LabelScreen");
		        label.setAlignment(Pos.CENTER);

		        /*CREATE HELP LABELS PREDEFINED*/
		        Label labelPred = new Label("Select the type of cable to generate the curves: ");
		        labelPred.setId("HelpLabel");
		        labelPred.setAlignment(Pos.CENTER);
		                
		        /*CREATE THE INPUTS PARAMETERS*/        
		        JFXTextField Roc = new JFXTextField();
		        Roc.setLabelFloat(true);
		        Roc.setPromptText("R0c");

		        JFXTextField ac = new JFXTextField();
		        ac.setLabelFloat(true);
		        ac.setPromptText("ac");

		        JFXTextField L0 = new JFXTextField();
		        L0.setLabelFloat(true);
		        L0.setPromptText("L0");
		        
		        JFXTextField Linf = new JFXTextField();
		        Linf.setLabelFloat(true);
		        Linf.setPromptText("L∞");

		        JFXTextField fm = new JFXTextField();
		        fm.setLabelFloat(true);
		        fm.setPromptText("fm");

		        JFXTextField Nb = new JFXTextField();
		        Nb.setLabelFloat(true);
		        Nb.setPromptText("Nb");

		        JFXTextField g0 = new JFXTextField();
		        g0.setLabelFloat(true);
		        g0.setPromptText("g0");
		        
		        JFXTextField Nge = new JFXTextField();
		        Nge.setLabelFloat(true);
		        Nge.setPromptText("Nge");

		        JFXTextField Cinf = new JFXTextField();
		        Cinf.setLabelFloat(true);
		        Cinf.setPromptText("C∞");

		        JFXTextField C0 = new JFXTextField();
		        C0.setLabelFloat(true);
		        C0.setPromptText("C0");

		        JFXTextField Nce = new JFXTextField();
		        Nce.setLabelFloat(true);
		        Nce.setPromptText("Nce");
		        
		        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
		        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
		        cableTypes.getItems().add(new Label("Custom"));
		        cableTypes.getItems().add(new Label("B05a"));
		        cableTypes.getItems().add(new Label("BT_dw1"));
		        cableTypes.setPromptText("Custom");
		        
		        final Label descriptionCable = new Label("Enter manually the parameters or choose at side a predefined cable type");
		        descriptionCable.setId("descriptionCable");
		        descriptionCable.setWrapText(true);
		        
		        /*ON CHANGE CABLE TYPE*/
		        cableTypes.valueProperty().addListener(new ChangeListener<Label>() {
		            @Override
		            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

		            	/******** STANDARD CABLES TYPE *********/
		            	switch(newValue.getText()) {
		            	
			        		case "BT_dw1":
			        			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
			        			Roc.setText("65.32");
			    				ac.setText("2.7152831e-3");
			    				L0.setText("0.884242e-3");
			    				Linf.setText("800.587e-6");
			    				fm.setText("263371");
			    				Nb.setText("1.30698");
			    				g0.setText("855e-9");
			    				Nge.setText("0.746");
			    				C0.setText("46.5668e-9");
			    				Cinf.setText("28.0166e-9");
			    				Nce.setText("0.117439");
			    				Roc.setDisable(true);
			    				ac.setDisable(true);
			    				L0.setDisable(true);
			    				Linf.setDisable(true);
			    				fm.setDisable(true);
			    				Nb.setDisable(true);
			    				g0.setDisable(true);
			    				Nge.setDisable(true);
			    				C0.setDisable(true);
			    				Cinf.setDisable(true);
			    				Nce.setDisable(true);
							break;

		            		case "B05a":
		            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
		            			Roc.setText("187.0831");
		        				ac.setText("0.0457");
		        				L0.setText("6.5553e-004");
		        				Linf.setText("5.0973e-004");
		        				fm.setText("8.1241e+005");
		        				Nb.setText("1.0142");
		        				g0.setText("1.0486e-010");
		        				Nge.setText("1.1500");
		        				C0.setText("-6.9514e-011");
		        				Cinf.setText("4.5578e-008");
		        				Nce.setText("-0.1500");
		        				Roc.setDisable(true);
		        				ac.setDisable(true);
		        				L0.setDisable(true);
		        				Linf.setDisable(true);
		        				fm.setDisable(true);
		        				Nb.setDisable(true);
		        				g0.setDisable(true);
		        				Nge.setDisable(true);
		        				C0.setDisable(true);
		        				Cinf.setDisable(true);
		        				Nce.setDisable(true);
		    				break;
		            		default:
		        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");
		        				Roc.setText("");
		        				ac.setText("");
		        				L0.setText("");
		        				Linf.setText("");
		        				fm.setText("");
		        				Nb.setText("");
		        				g0.setText("");
		        				Nge.setText("");
		        				C0.setText("");
		        				Cinf.setText("");
		        				Nce.setText("");
		        				Roc.setDisable(false);
		        				ac.setDisable(false);
		        				L0.setDisable(false);
		        				Linf.setDisable(false);
		        				fm.setDisable(false);
		        				Nb.setDisable(false);
		        				g0.setDisable(false);
		        				Nge.setDisable(false);
		        				C0.setDisable(false);
		        				Cinf.setDisable(false);
		        				Nce.setDisable(false);
		        			break;
		            	}
		            	
		            }
		        });

		        JFXTextField cableLength = new JFXTextField();
		        cableLength.setLabelFloat(true);
		        cableLength.setPromptText("Cable Length");
		        
		        /*GENERATE CALC BUTTON*/
		        Region calcIcon = GlyphsStack.create().add(
		        		GlyphsBuilder.create(FontAwesomeIcon.class)
		        			.icon(FontAwesomeIconName.SAVE)
		        			.style("-fx-fill: white;")
		        			.size("1em")
		        			.build()
		        		);        

		        Button calculate = new Button("Save Cable Configuration", calcIcon);
		        calculate.setId("calculate");
		        /*SET BUTTON ONCLICK FUNCTION*/
		        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
		            public void handle(MouseEvent me) {
		                
		                double Roc_value ;
		                double ac_value  ;
		                double L0_value  ;
		                double Linf_value;
		                double fm_value  ;
		                double Nb_value  ;
		                double g0_value  ;
		                double Nge_value ;
		                double C0_value  ;
		                double Cinf_value;
		                double Nce_value ;

		                double cableLength_value;
		                
		                /*VALIDATE INFO'S*/
		                try{
		                	
		                    Roc_value = Double.parseDouble(Roc.getText());
		                    ac_value = Double.parseDouble(ac.getText());
		                    L0_value = Double.parseDouble(L0.getText());
		                    Linf_value = Double.parseDouble(Linf.getText());
		                    fm_value = Double.parseDouble(fm.getText());
		                    Nb_value = Double.parseDouble(Nb.getText());
		                    g0_value = Double.parseDouble(g0.getText());
		                    Nge_value = Double.parseDouble(Nge.getText());
		                    C0_value = Double.parseDouble(C0.getText());
		                    Cinf_value = Double.parseDouble(Cinf.getText());
		                    Nce_value = Double.parseDouble(Nce.getText());

		                    cableLength_value = Double.parseDouble(cableLength.getText());
		                    
		                }catch(Exception e){
		                    Alert alert = new Alert(AlertType.ERROR);
		                    alert.setTitle("Error");
		                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
		                    alert.setContentText(e.toString());
		                    alert.showAndWait();
		                    return;
		                }
		                                
		                /*ADD TO CONFIG ARRAY*/
		                
		                config.insertElementAt((GenericCableModel) new BT0(Roc_value, ac_value, L0_value,  Linf_value,  fm_value,  Nb_value,
		            			g0_value,  Nge_value,  C0_value,  Cinf_value,  Nce_value, cableLength_value), position        	
		    			);
		                
		                /*ALTER TEXT OF CABLE*/		                
		                if(text.get(position).getText().contains("Start")) {
			                text.get(position).setText("Start Point\nConfigured");		                	
		                }else {
			                text.get(position).setText("Configured");		                			                	
		                }
		                
		                dialog.close();
		                
		           }
		            
		        });
		        
		        /*ADDING ALL ELEMENTS TO GRID*/
		        int line = 0;
		        
		        /*ADDING LINE*/
		        grid.add(label, 0, line, 3, 1);
		        GridPane.setHalignment(label, HPos.CENTER);
		        line++;
		        
				/*ADDING LINE*/
				grid.add(labelPred, 0, line, 3, 1);
				GridPane.setHalignment(labelPred, HPos.CENTER);
				GridPane.setValignment(labelPred, VPos.CENTER);
				line++;
				
				/*ADDING LINE*/
				cableTypes.setMaxWidth(Double.MAX_VALUE);
				grid.add(cableTypes, 0, line, 1, 1);
				GridPane.setHalignment(cableTypes, HPos.CENTER);
				GridPane.setValignment(cableTypes, VPos.CENTER);		
				descriptionCable.setMaxWidth(Double.MAX_VALUE);
				grid.add(descriptionCable, 1, line, 2, 1);
				GridPane.setHalignment(descriptionCable, HPos.CENTER);
				GridPane.setValignment(descriptionCable, VPos.CENTER);		
				line++;
				
				/*ADDING LINE*/		
				Roc.setMaxWidth(Double.MAX_VALUE);
				grid.add(Roc, 0, line, 1, 1);
				GridPane.setHalignment(Roc, HPos.CENTER);
				GridPane.setValignment(Roc, VPos.CENTER);
				ac.setMaxWidth(Double.MAX_VALUE);
				grid.add(ac, 1, line, 1, 1);
				GridPane.setHalignment(ac, HPos.CENTER);
				GridPane.setValignment(ac, VPos.CENTER);
				L0.setMaxWidth(Double.MAX_VALUE);
				grid.add(L0, 2, line, 1, 1);
				GridPane.setHalignment(L0, HPos.CENTER);
				GridPane.setValignment(L0, VPos.CENTER);
				line++;
				
				/*ADDING LINE*/
				Linf.setMaxWidth(Double.MAX_VALUE);
				grid.add(Linf, 0, line, 1, 1);
				GridPane.setHalignment(Linf, HPos.CENTER);
				GridPane.setValignment(Linf, VPos.CENTER);
				fm.setMaxWidth(Double.MAX_VALUE);
				grid.add(fm, 1, line, 1, 1);
				GridPane.setHalignment(fm, HPos.CENTER);
				GridPane.setValignment(fm, VPos.CENTER);
				Nb.setMaxWidth(Double.MAX_VALUE);
				grid.add(Nb, 2, line, 1, 1);
				GridPane.setHalignment(Nb, HPos.CENTER);
				GridPane.setValignment(Nb, VPos.CENTER);
				line++;

				/*ADDING LINE*/
				g0.setMaxWidth(Double.MAX_VALUE);
				grid.add(g0, 0, line, 1, 1);
				GridPane.setHalignment(g0, HPos.CENTER);
				GridPane.setValignment(g0, VPos.CENTER);
				Nge.setMaxWidth(Double.MAX_VALUE);
				grid.add(Nge, 1, line, 1, 1);
				GridPane.setHalignment(Nge, HPos.CENTER);
				GridPane.setValignment(Nge, VPos.CENTER);
				Cinf.setMaxWidth(Double.MAX_VALUE);
				grid.add(Cinf, 2, line, 1, 1);
				GridPane.setHalignment(Cinf, HPos.CENTER);
				GridPane.setValignment(Cinf, VPos.CENTER);
				line++;

				/*ADDING LINE*/
				C0.setMaxWidth(Double.MAX_VALUE);
				grid.add(C0, 0, line, 1, 1);
				GridPane.setHalignment(C0, HPos.CENTER);
				GridPane.setValignment(C0, VPos.CENTER);
				Nce.setMaxWidth(Double.MAX_VALUE);
				grid.add(Nce, 1, line, 1, 1);
				GridPane.setHalignment(Nce, HPos.CENTER);
				GridPane.setValignment(Nce, VPos.CENTER);
				cableLength.setMaxWidth(Double.MAX_VALUE);
				grid.add(cableLength, 2, line, 1, 1);
				GridPane.setHalignment(cableLength, HPos.CENTER);
				GridPane.setValignment(cableLength, VPos.CENTER);
				line++;
		        		        				
		        /*ADDING LINE*/
		        calculate.setMaxWidth(Double.MAX_VALUE/2);
		        grid.add(calculate, 0, line, 3, 1);
		        GridPane.setHalignment(calculate, HPos.CENTER);
		        GridPane.setValignment(calculate, VPos.CENTER);
		        line++;
		        
		        grid.setAlignment(Pos.CENTER);
		        			
			}
		
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        dialog.getIcons().add(image);
	        dialog.setTitle("Cable Segment Configuration");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dialog.setScene(new Scene(grid));
    	dialog.getScene().getStylesheets().add(MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm());
    	
    	dialog.initOwner(primaryStage);
    	dialog.initModality(Modality.APPLICATION_MODAL); 
    	dialog.showAndWait();
    	
	}
	
}
