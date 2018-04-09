package MultiCable;

import CableSynthesis.CableSynthesisScreen;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import BT0.BT0;
import BT0.BT0Controller;
import GPLCS.SimuladorGPLCS;
import KHM1.KHM1;
import KHM1.KHM1Controller;
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
	
		/*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "Multi Cable Transfer Function";
        Label label = new Label(ApplicationName);
        label.setId("title");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABEL*/
        Label helpLabel = new Label("Select one of models above to start the calculation of transfer function to multiples cables:");
        helpLabel.setId("HelpLabel");
        helpLabel.setAlignment(Pos.CENTER);

        /*CREATE 3 BUTTONS*/
        JFXButton buttonKHM1 = new JFXButton("KHM 1");
        buttonKHM1.setId("KHM1");
        buttonKHM1.setFocusTraversable(false);
        buttonKHM1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                primaryStage.getScene().setRoot(MultiCableScreen.getMultiCableScreenKHM1(primaryStage));
            	String css = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        JFXButton buttonTNOEAB = new JFXButton("TNO/EAB");
        buttonTNOEAB.setId("TNOEAB");
        buttonTNOEAB.setFocusTraversable(false);
        
        JFXButton buttonBT0    = new JFXButton("BT0");
        buttonBT0.setId("BT0");
        buttonBT0.setFocusTraversable(false);

        /*ADDING BACK BUTTON*/
        Region iconBack = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.REPLY)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);
        
        Button back = new Button("Back", iconBack);
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	/*COME BACK TO INITIAL SCREEN*/
            	primaryStage.getScene().setRoot(CableSynthesisScreen.getCableSynthesisScene(primaryStage));
            	String css = CableSynthesisScreen.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        int line = 0;
        
        /*ADDING LINE*/
        grid.add(label, 0, line, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        grid.add(helpLabel, 0, line, 3, 1);
        GridPane.setHalignment(helpLabel, HPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        grid.add(buttonKHM1, 0, line, 1, 1);
        GridPane.setHalignment(buttonKHM1, HPos.RIGHT);
        grid.add(buttonTNOEAB, 1, line, 1, 1);
        GridPane.setHalignment(buttonTNOEAB, HPos.CENTER);
        grid.add(buttonBT0, 2, line, 1, 1);
        GridPane.setHalignment(buttonBT0, HPos.LEFT);
        
        line++;
                        
        /*ADDING LINE*/
        grid.add(back, 1, line, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        line++;
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;

	}
	
	public static ScrollPane getMultiCableScreenKHM1(Stage primaryStage) {
	
		/*SCREEN WIDTH AND HEIGHT*/
	    int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
	    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/100;

        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        for (int i = 0; i < 5; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(20);
            grid.getColumnConstraints().add(column);
        }

        /*CREATE THE LABEL OF SCREEN*/
        String ApplicationName = "Generate Transfer Function to Multiples Cables\nTransmission Line Model: KH Model 1";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS PREDEFINED*/
        Label labelTopology = new Label("Select the topology to generate the curves: ");
        labelTopology.setId("HelpLabel");
        labelTopology.setAlignment(Pos.CENTER);
        labelTopology.setWrapText(true);
        
        JFXComboBox<Label> topology = new JFXComboBox<Label>();
        topology.getItems().add(new Label("Topology 1, two segments"));
        topology.getItems().add(new Label("Topology 2, two segments with 1 bridge tap"));
        topology.getItems().add(new Label("Topology 3, three segments"));
        topology.setPromptText("Select the topology");
        topology.setId("comboBox");

        Group networkRegion = new Group();

        /*CREATE NETWORK TOPOLOGY REGION*/
        Rectangle networkRegionBorder = new Rectangle( 0, 0, screenWidth*75, screenHeight*55);
        networkRegionBorder.setStroke(Color.BLACK);
        networkRegionBorder.setFill(Color.TRANSPARENT);
        networkRegionBorder.setStrokeWidth(1);
        
        networkRegion.getChildren().add(networkRegionBorder);
        networkRegionBorder.toFront();
        
        Vector<GenericCableModel> config = new Vector<GenericCableModel>();
		Vector<Text> cableText = new Vector<Text>();
		Vector<Line> cableSegment = new Vector<Line>();
        
        /*DRAW DIFERENT TOPOLOGIES*/
        topology.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	/*CLEAN THE VARS*/
            	networkRegion.getChildren().clear();
            	config.clear();
            	cableText.clear();
            	cableSegment.clear();
            	
            	networkRegion.getChildren().add(networkRegionBorder);
            	
            	switch(newValue.getText().split(",")[0]) {
            	
            		case "Topology 1":
            			MultiCableScreen.generateConfiguration1(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment);
            			break;

            		case "Topology 2":
            			MultiCableScreen.generateConfiguration2(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment);
            			break;

            		case "Topology 3":
            			MultiCableScreen.generateConfiguration3(
            					networkRegion, networkRegionBorder, primaryStage, config, cableText, cableSegment);
            			break;

            	}
            	
            }
        });
        
        /*CREATE THE INPUTS CONFIG*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("2.2MHz - 106MHz"));
        frequency.getItems().add(new Label("2.2MHz - 212MHz"));
        frequency.getItems().add(new Label("2.2MHz - 424MHz"));
        frequency.getItems().add(new Label("2.2MHz - 848MHz"));
        frequency.getItems().add(new Label("Custom"));
        frequency.setPromptText("Frequency Band");
        
        JFXTextField minF = new JFXTextField();
        minF.setLabelFloat(true);
        minF.setPromptText("Minimum Frequency (in MHz)");

        JFXTextField maxF = new JFXTextField();
        maxF.setLabelFloat(true);
        maxF.setPromptText("Maximum Frequency (in MHz)");

        JFXTextField step = new JFXTextField();
        step.setLabelFloat(true);
        step.setPromptText("Step (in MHz)");
        
        /*GENERATE CALC BUTTON*/
        Region calcIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.CALCULATOR)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button calculate = new Button("Generate Graphs", calcIcon);
        calculate.setId("calculate");
        /*SET BUTTON ONCLICK FUNCTION*/
        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                
            	/**********************************************************************/
            	/*FUNCTIONS TO CALCULATE THE TRANSFER FUNCTION*/
            	
            	boolean haveNull = false;
            	
            	for(int i = 0; i < config.size(); i++)
            		if(config.get(i) == null)
            			haveNull = true;
            	
            	if(haveNull) {
            		
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please configure all cable segments before calculate the transfer function!");
					alert.showAndWait();
            	
					return;
					
            	}
            		
        		/*SEND ALL DATA TO GENERATE THE GRAPH*/
        		Vector<GenericCableModel> data = new Vector<GenericCableModel>();
        		
        		if(topology.getValue() == null) {
        			
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Select at least a type of topology!");
					alert.showAndWait();
					return;
        			
        		}
        		
        		switch(topology.getValue().getText().split(",")[0]) {
            	
            		case "Topology 1":
            			
            			break;

            		case "Topology 2":

            			break;

            		case "Topology 3":

            			break;
            			
            	}
        		            	
            	/**********************************************************************/
                
            }
        });
        
        
        /*ADDING BACK BUTTON*/
        Region iconBack = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.REPLY)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);

        Button back = new Button("Back", iconBack);
        back.setId("back-button");
        back.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	primaryStage.getScene().setRoot(MultiCableScreen.getMultiCableScreen(primaryStage));
            	String css = MultiCableScreen.class.getResource("MultiCableScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
        
        /*****************************************************************************************************************/
        /*INSERT THE ELEMENTS IN GRID*/
        
        int line = 0;
        
        label.setMaxWidth(Double.MAX_VALUE);
        grid.add(label, 0, line, 5, 1);
        GridPane.setHalignment(label, HPos.CENTER);
		GridPane.setValignment(label, VPos.CENTER);
		line++;
		
		labelTopology.setMaxWidth(Double.MAX_VALUE);
        grid.add(labelTopology, 1, line, 1, 1);
        GridPane.setHalignment(labelTopology, HPos.CENTER);
		GridPane.setValignment(labelTopology, VPos.CENTER);
		topology.setMaxWidth(Double.MAX_VALUE);
        grid.add(topology, 2, line, 2, 1);
        GridPane.setHalignment(topology, HPos.CENTER);
		GridPane.setValignment(topology, VPos.CENTER);
		line++;
		
		frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 1, line, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
		GridPane.setValignment(frequency, VPos.CENTER);
		calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 2, line, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
		GridPane.setValignment(calculate, VPos.CENTER);
		back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 3, line, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
		GridPane.setValignment(back, VPos.CENTER);
		line++;
		
        final int lineFrequencyCustom = line;
        
		frequency.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	if(newValue.getText().contains("Custom")) {
            		
            		/*ADDING LINE*/
                    minF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(minF, 1, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(minF, HPos.CENTER);
                    GridPane.setValignment(minF, VPos.CENTER);
                    
                    maxF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(maxF, 2, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(maxF, HPos.CENTER);
                    GridPane.setValignment(maxF, VPos.CENTER);
                    
                    step.setMaxWidth(Double.MAX_VALUE);
                    grid.add(step, 3, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(step, HPos.CENTER);
                    GridPane.setValignment(step, VPos.CENTER);
            		
            	}else {
            		
            		grid.getChildren().remove(minF);
            		grid.getChildren().remove(maxF);
            		grid.getChildren().remove(step);
            		
            	}
            	
            }
        });
		        
		line++;
		
		grid.add(networkRegion, 0, line, 5, 1);
        GridPane.setHalignment(networkRegion, HPos.CENTER);
		GridPane.setValignment(networkRegion, VPos.CENTER);
		line++;
		
		grid.setAlignment(Pos.CENTER);
		
        /*****************************************************************************************************************/
		
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
	
	}
	
	public static void generateConfiguration1(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine2[2] , posLine2[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.PURPLE, networkRegion, "End Point");
		
	}

	public static void generateConfiguration2(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine1[2], (Ye - Ys)/2, posLine1[2] , (Ye - Ys)/2 + tamLine};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		MultiCableScreen.generateStandardLineWithOffsetOnText(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, -90);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine2[2] , posLine2[3] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.PURPLE, networkRegion, "End Point");
		MultiCableScreen.generateStandardPoint(posPoint4, Color.GOLD, networkRegion, "Bridge Tap");
		
	}

	public static void generateConfiguration3(
			Group networkRegion, Rectangle networkRegionBorder, Stage primaryStage,
	        Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment
			) {
			
		double Xs = networkRegion.getBoundsInParent().getMinX();
		double Ys = networkRegion.getBoundsInParent().getMinY();
		double Xe = networkRegion.getBoundsInParent().getMaxX();
		double Ye = networkRegion.getBoundsInParent().getMaxY();
		
		int tamLine = 150;
		int startX = 100;
		
		/*GENERATE THE SEGMENTS*/
		double[] posLine1 = {startX, (Ye - Ys)/2, startX + tamLine, (Ye - Ys)/2};
		double[] posLine2 = {posLine1[2], (Ye - Ys)/2, posLine1[2] + tamLine, (Ye - Ys)/2};
		double[] posLine3 = {posLine2[2], (Ye - Ys)/2, posLine2[2] + tamLine, (Ye - Ys)/2};

		MultiCableScreen.generateStandardLine(
				posLine1, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		MultiCableScreen.generateStandardLine(
				posLine2, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		MultiCableScreen.generateStandardLine(
				posLine3, Color.BLUE, primaryStage, networkRegion, config, cableText, cableSegment, 0);
		
		/*GENERATE THE POINTS*/
		double[] posPoint1 = { posLine1[0] , posLine1[1] };
		double[] posPoint2 = { posLine2[0] , posLine2[1] };
		double[] posPoint3 = { posLine3[0] , posLine3[1] };
		double[] posPoint4 = { posLine3[2] , posLine3[3] };

		MultiCableScreen.generateStandardPoint(posPoint1, Color.RED, networkRegion, "Start Point");
		MultiCableScreen.generateStandardPoint(posPoint2, Color.BLACK, networkRegion, "");
		MultiCableScreen.generateStandardPoint(posPoint3, Color.BLACK, networkRegion, "");		
		MultiCableScreen.generateStandardPoint(posPoint4, Color.PURPLE, networkRegion, "End Point");
		
	}
	
	public static void generateStandardLine(
			double[] pos, Color color, Stage primaryStage, Group networkRegion,
			Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, double angle
			) {

		int standardLineStroke = 5;

		config.add(null);
		
		Line newSegment = new Line(pos[0],pos[1],pos[2],pos[3]);
		newSegment.setStroke(color);
		newSegment.setStrokeWidth(standardLineStroke);
		cableSegment.add(newSegment);
        newSegment.toBack();
        networkRegion.getChildren().add(newSegment);
		        
        /*ADD TEXT OF CABLE*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);
        
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        Text textCable = new Text("Id Segment = "+(cableText.size())+"\nUnconfigured");        	        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        
        textCable.setFill(Color.RED);

        textCable.setRotate(angle);
        
        textCable.applyCss(); 
                
        textCable.setX((pos[0] + pos[2])/2 - textCable.getLayoutBounds().getWidth()/2);
        textCable.setY((pos[1] + pos[3])/2 - 35);        
        
        cableText.add(textCable);
        networkRegion.getChildren().add(textCable);
        
        /*****************************************************************/
        
        newSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });

        newSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
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
        
	}

	public static void generateStandardLineWithOffsetOnText(
			double[] pos, Color color, Stage primaryStage, Group networkRegion,
			Vector<GenericCableModel> config, Vector<Text> cableText, Vector<Line> cableSegment, double angle
			) {

		int standardLineStroke = 5;
		
		config.add(null);

		Line newSegment = new Line(pos[0],pos[1],pos[2],pos[3]);
		newSegment.setStroke(color);
		newSegment.setStrokeWidth(standardLineStroke);
		cableSegment.add(newSegment);
        newSegment.toBack();
        networkRegion.getChildren().add(newSegment);
		        
        /*ADD TEXT OF CABLE*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);
        
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        Text textCable = new Text("Id Segment = "+(cableText.size())+"\nUnconfigured");        	        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        
        textCable.setFill(Color.RED);

        textCable.setRotate(angle);
        
        textCable.applyCss(); 
                
        textCable.setX((pos[0] + pos[2])/2 - 80);
        textCable.setY((pos[1] + pos[3])/2);        
        
        cableText.add(textCable);
        networkRegion.getChildren().add(textCable);
        
        /*****************************************************************/
        
        newSegment.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            		primaryStage.getScene().setCursor(Cursor.HAND);
            }
        });

        newSegment.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
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
        
	}

	public static void generateModal(Stage primaryStage, Vector<Text> text, Vector<GenericCableModel> config, String cableModel, int position) {
		
    	Stage dialog = new Stage();
		    	
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.3);
            grid.getColumnConstraints().add(column);
        }
        
		/*CREATE THE CONTROLS*/
			/*TO BT0*/
			if(cableModel.contains("BT0")) {
		        /*CREATE THE GRID*/
		        
		        /*CREATE THE LABEL OF SCREEN*/
		        String ApplicationName = "KH Model 1 Multi Cable Transfer Function Cable Configuration";
		        Label label = new Label(ApplicationName);
		        label.setId("LabelScreen");
		        label.setAlignment(Pos.CENTER);
		        
		        /*CREATE THE INPUTS PARAMETERS*/        
		        JFXTextField k1 = new JFXTextField();
		        k1.setLabelFloat(true);
		        k1.setPromptText("K1");

		        JFXTextField k2 = new JFXTextField();
		        k2.setLabelFloat(true);
		        k2.setPromptText("K2");

		        JFXTextField k3 = new JFXTextField();
		        k3.setLabelFloat(true);
		        k3.setPromptText("K3");
		        
		        JFXTextField h1 = new JFXTextField();
		        h1.setLabelFloat(true);
		        h1.setPromptText("H1");
		        
		        JFXTextField h2 = new JFXTextField();
		        h2.setLabelFloat(true);
		        h2.setPromptText("H2");		        
		        
		        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
		        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
		        cableTypes.getItems().add(new Label("Custom"));
		        cableTypes.getItems().add(new Label("CAT5"));
		        cableTypes.getItems().add(new Label("B05a"));
		        cableTypes.getItems().add(new Label("T05b"));
		        cableTypes.getItems().add(new Label("T05h"));
		        cableTypes.getItems().add(new Label("T05u"));
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
		            	
		            		case "CAT5":
		        				descriptionCable.setText("Typical Category 5 cable commonly used in structured cabling for computer networks, such as Ethernet");
		        				k1.setText("1.97311e-003");
		        				k2.setText("1.24206e-008");
		        				k3.setText("3.03005e-005");
		        				h1.setText("98.5944");
		        				h2.setText("6.0876e+003");
		        				k1.setDisable(true);
		        				k2.setDisable(true);
		        				k3.setDisable(true);
		        				h1.setDisable(true);
		        				h2.setDisable(true);
		    				break;
		            		case "B05a":
		            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
		        				k1.setText("1.67334e-003");
		        				k2.setText("1.35369e-007");
		        				k3.setText("3.13189e-005");
		        				h1.setText("106.6383");
		        				h2.setText("5.5601e+003");
		        				k1.setDisable(true);
		        				k2.setDisable(true);
		        				k3.setDisable(true);
		        				h1.setDisable(true);
		        				h2.setDisable(true);
		        			break;
		            		case "T05b":
		    					descriptionCable.setText("Medium quality multi-quad cable used in buildings");            			
		        				k1.setText("1.70454e-003");
		        				k2.setText("4.98183e-011");
		        				k3.setText("3.10070e-005");
		        				h1.setText("132.3825");
		        				h2.setText("6.9128e+003");
		        				k1.setDisable(true);
		        				k2.setDisable(true);
		        				k3.setDisable(true);
		        				h1.setDisable(true);
		        				h2.setDisable(true);
		        			break;
		            		case "T05h":
		    					descriptionCable.setText("Low quality cable used for in-house telephony wiring");
		        				k1.setText("2.48426e-003");
		        				k2.setText("4.65719e-008");
		        				k3.setText("3.07543e-005");
		        				h1.setText("100.3102");
		        				h2.setText("6.9374e+003");
		        				k1.setDisable(true);
		        				k2.setDisable(true);
		        				k3.setDisable(true);
		        				h1.setDisable(true);
		        				h2.setDisable(true);
		        			break;
		            		case "T05u":
		        				descriptionCable.setText("KPN cable, a typical access line used in the Netherlands");
		        				k1.setText("1.78466e-003");
		        				k2.setText("2.51367e-008");
		        				k3.setText("2.87051e-005");
		        				h1.setText("127.0785");
		        				h2.setText("6.9114e+003");
		        				k1.setDisable(true);
		        				k2.setDisable(true);
		        				k3.setDisable(true);
		        				h1.setDisable(true);
		        				h2.setDisable(true);
		    				break;
		            		default:
		        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");
		        				k1.setText("");
		        				k2.setText("");
		        				k3.setText("");
		        				h1.setText("");
		        				h2.setText("");
		        				k1.setDisable(false);
		        				k2.setDisable(false);
		        				k3.setDisable(false);
		        				h1.setDisable(false);
		        				h2.setDisable(false);
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
		                
		                double k1_value ;
		                double k2_value  ;
		                double k3_value  ;
		                double h1_value;
		                double h2_value  ;

		                double cableLength_value;
		                
		                /*VALIDATE INFO'S*/
		                try{
		                	
		                    k1_value = Double.parseDouble(k1.getText());
		                    k2_value = Double.parseDouble(k2.getText());
		                    k3_value = Double.parseDouble(k3.getText());
		                    h1_value = Double.parseDouble(h1.getText());
		                    h2_value = Double.parseDouble(h2.getText());

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
		                
		                config.set(position, (GenericCableModel) new KHM1(k1_value, k2_value, k3_value, 
		                		h1_value, h2_value, cableLength_value)         	
		    			);
		                
		                /*ALTER TEXT OF CABLE*/		                
		                text.get(position).setText(text.get(position).getText().replace("Unconfigured", "Configured"));		                	
		                
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
				k1.setMaxWidth(Double.MAX_VALUE);
				grid.add(k1, 0, line, 1, 1);
				GridPane.setHalignment(k1, HPos.CENTER);
				GridPane.setValignment(k1, VPos.CENTER);
				k2.setMaxWidth(Double.MAX_VALUE);
				grid.add(k2, 1, line, 1, 1);
				GridPane.setHalignment(k2, HPos.CENTER);
				GridPane.setValignment(k2, VPos.CENTER);
				k3.setMaxWidth(Double.MAX_VALUE);
				grid.add(k3, 2, line, 1, 1);
				GridPane.setHalignment(k3, HPos.CENTER);
				GridPane.setValignment(k3, VPos.CENTER);
				line++;
				
				/*ADDING LINE*/
				h1.setMaxWidth(Double.MAX_VALUE);
				grid.add(h1, 0, line, 1, 1);
				GridPane.setHalignment(h1, HPos.CENTER);
				GridPane.setValignment(h1, VPos.CENTER);
				h2.setMaxWidth(Double.MAX_VALUE);
				grid.add(h2, 1, line, 1, 1);
				GridPane.setHalignment(h2, HPos.CENTER);
				GridPane.setValignment(h2, VPos.CENTER);
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

	public static void generateStandardPoint(
			double[] pos,  Color color, Group networkRegion, String text) {
		
		double sizeCircle = 7; 
		
		/*GENERATE POINT*/
		Circle circle = new Circle(pos[0], pos[1], sizeCircle);
		
		/*GENERATE FIRST POINT*/
		circle.setFill(color);
		
        /*CREATE LABEL TO FIRST CABLE(START POINT)*/
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setRadius(2.0f);
        ds.setColor(Color.BLUE);

		Text textCable = new Text(text);        	
        
        textCable.setEffect(ds);
        textCable.setCache(true);
        textCable.setStyle(
        		"    -fx-font: 12px Monospaced;\r\n" + 
        		"    -fx-stroke-width: 1;"
        		);
        textCable.setFill(Color.RED);
        textCable.applyCss(); 
        
        textCable.setX(pos[0] - textCable.getLayoutBounds().getWidth()*3/4);
        textCable.setY(pos[1] + 25);        
        
        networkRegion.getChildren().add(textCable);
		
		circle.toFront();

        networkRegion.getChildren().add(circle);

	}

	
}