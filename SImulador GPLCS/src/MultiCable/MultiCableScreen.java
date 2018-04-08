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
		return null;
	}
	
	public static ScrollPane getMultiCableScreenKHM1(Stage primaryStage) {
	
		/*SCREEN WIDTH AND HEIGHT*/
	    int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
	    int screenHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;

        /*CREATE THE GRID*/
        GridPane grid = new GridPane();
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(0,0,0,0));
        for (int i = 0; i < 3; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(25);
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
        
        JFXComboBox<Label> topology = new JFXComboBox<Label>();
        topology.getItems().add(new Label("Topology 1, two segments"));
        topology.getItems().add(new Label("Topology 2, two segments with 1 bridge tap"));
        topology.getItems().add(new Label("Topology 3, three segments"));
        topology.setPromptText("Select the topology");
        topology.setId("comboBox");

        Group networkRegion = new Group();

        /*CREATE NETWORK TOPOLOGY REGION*/
        Rectangle networkRegionBorder = new Rectangle( 0, 0, screenWidth*75, screenHeight*30);
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
        
        /*****************************************************************************************************************/
        /*INSERT THE ELEMENTS IN GRID*/
        
        int line = 0;
        
        label.setMaxWidth(Double.MAX_VALUE);
        grid.add(label, 0, line, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
		GridPane.setValignment(label, VPos.CENTER);
		line++;
		
		labelTopology.setMaxWidth(Double.MAX_VALUE);
        grid.add(labelTopology, 0, line, 1, 1);
        GridPane.setHalignment(labelTopology, HPos.CENTER);
		GridPane.setValignment(labelTopology, VPos.CENTER);
		topology.setMaxWidth(Double.MAX_VALUE);
        grid.add(topology, 1, line, 2, 1);
        GridPane.setHalignment(topology, HPos.CENTER);
		GridPane.setValignment(topology, VPos.CENTER);
		line++;
		        
        grid.add(networkRegion, 0, line, 3, 1);
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
        /*double angle = Math.acos( Math.abs((pos[2] - pos[0])) / (Math.sqrt(Math.pow(pos[2] - pos[0], 2) + Math.pow(pos[3] - pos[1], 2))) ) * 180/Math.PI;
        double angle2 = Math.asin( Math.abs((pos[2] - pos[0])) / (Math.sqrt(Math.pow(pos[2] - pos[0], 2) + Math.pow(pos[3] - pos[1], 2))) ) * 180/Math.PI;

        if(pos[3] - pos[1] > 0) {
            textCable.setRotate( angle );        	        	
        }else {
        	textCable.setRotate( angle2 - 90);
        }*/
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

                    	//MultiCableScreen.generateModal(primaryStage, cableText, config, "BT0", cableSegment.indexOf(newSegment));
                    
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

                    	//MultiCableScreen.generateModal(primaryStage, cableText, config, "BT0", cableSegment.indexOf(newSegment));
                    
                    }

                }
                
            }
        	
        });

        /*****************************************************************/
        
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