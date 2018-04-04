package MultiCable;

import java.awt.Toolkit;
import java.util.Map;
import java.util.Vector;

import GPLCS.SimuladorGPLCS;
import TransmissionLine.GenericCableModel;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
			
        networkRegionBorder.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if(started.get(0)) {
            		if(!firstClick.get(0)) {

            			/*GET FIRSTS X AND Y*/
            			firstClick.set(0, true);
                		firstPoint.set(0, event.getX());
                		firstPoint.set(1, event.getY());
            		
                		/*GENERATE POINT*/
                		Circle circle = new Circle(event.getX(), event.getY(), sizeCircle);
                		circle.toFront();
                		
                		/*ON CLICK ON POINT, GENERATE CONNECTION*/
                		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent me) {
                            	if(started.get(0)) {
                            		if(!firstClick.get(0)) {
                            			firstClick.set(0, true);
                                		firstPoint.set(0, circle.getCenterX());
                                		firstPoint.set(1, circle.getCenterY());                            			
                            		} else {
                                		Line newSegment = new Line(firstPoint.get(0), firstPoint.get(1), circle.getCenterX(), circle.getCenterY());
                                		newSegment.setStroke(colors.get(cableSegment.size() % 7));
                                		newSegment.setStrokeWidth(sizeLine);
                                		cableSegment.add(newSegment);
                                		
                            			networkRegion.getChildren().add(newSegment);
                                        newSegment.toBack();
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

                                                    	Group pane = new Group();
                                                    	Button button = new Button("Click me");
                                                    	button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                        	public void handle(MouseEvent event) {
                                                                networkRegionBorder.setFill(Color.BLACK);
                                                            }
                                                        });

                                                    	pane.getChildren().add(button);

                                                    	Stage dialog = new Stage();
                                                    	dialog.setScene(new Scene(pane));
                                                    	
                                                    	dialog.initOwner(primaryStage);
                                                    	dialog.initModality(Modality.APPLICATION_MODAL); 
                                                    	dialog.showAndWait();

                                                    	// process result of dialog operation. 

                                                    
                                                    }
                                                }
                                                
                                            }
                                        	
                                        });

                                        /*****************************************************************/
                            			primaryStage.getScene().setCursor(Cursor.DEFAULT);
                                        firstClick.set(0, false);
                                		started.set(0, false);
                            		}
                            	}
                            }
                        });
                		
                		networkRegion.getChildren().add(circle);
                		
            		}else {
            			
            			firstClick.set(0, false);
                		started.set(0, false);
            			
                		Line newSegment = new Line(firstPoint.get(0), firstPoint.get(1), event.getX(), event.getY());
                		newSegment.setStroke(colors.get(cableSegment.size() % 7));
                		newSegment.setStrokeWidth(sizeLine);
                		cableSegment.add(newSegment);
                        newSegment.toBack();
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

                                    	MultiCableScreen.generateModal(primaryStage, config, "KHM 1");
                                    
                                    }

                                }
                                
                            }
                        	
                        });

                        /*****************************************************************/
            			networkRegion.getChildren().add(newSegment);
                		/*ON CLICK ON POINT, GENERATE CONNECTION*/
                		
                		/*GENERATE POINT*/
            			Circle circle = new Circle(event.getX(), event.getY(), sizeCircle);
                		circle.toFront();

                		/*ON CLICK ON POINT, GENERATE CONNECTION*/
            			circle.setOnMousePressed(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent me) {
                            	if(started.get(0)) {
                            		if(!firstClick.get(0)) {
                            			firstClick.set(0, true);
                                		firstPoint.set(0, circle.getCenterX());
                                		firstPoint.set(1, circle.getCenterY());                            			
                            		} else {
                                		Line newSegment = new Line(firstPoint.get(0), firstPoint.get(1), circle.getCenterX(), circle.getCenterY());
                                		newSegment.setStroke(colors.get(cableSegment.size() % 7));
                                		newSegment.setStrokeWidth(sizeLine);
                                		cableSegment.add(newSegment);
                                		
                            			networkRegion.getChildren().add(newSegment);
                                        newSegment.toBack();
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

                                                    	Group pane = new Group();
                                                    	Button button = new Button("Click me");
                                                    	button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                                        	public void handle(MouseEvent event) {
                                                                networkRegionBorder.setFill(Color.BLACK);
                                                            }
                                                        });

                                                    	pane.getChildren().add(button);

                                                    	Stage dialog = new Stage();
                                                    	dialog.setScene(new Scene(pane));
                                                    	
                                                    	dialog.initOwner(primaryStage);
                                                    	dialog.initModality(Modality.APPLICATION_MODAL); 
                                                    	dialog.showAndWait();

                                                    	// process result of dialog operation. 

                                                    
                                                    }
                                                }
                                                
                                            }
                                        	
                                        });

                                        /*****************************************************************/
                                        primaryStage.getScene().setCursor(Cursor.DEFAULT);   
                                        firstClick.set(0, false);
                                		started.set(0, false);
                            		}
                            	}
                            }
                        });

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
        	grid.add(label, 0, line, 2, 1);
	        GridPane.setHalignment(label, HPos.CENTER);
	        line++;
	        
        	/*ADDING LINE*/
	        grid.add(helpLabel, 0, line, 2, 1);
	        GridPane.setHalignment(helpLabel, HPos.CENTER);
	        line++;

        	/*ADDING LINE*/
	        grid.add(lineGroup, 0, line, 2, 1);
	        GridPane.setHalignment(lineGroup, HPos.CENTER);
	        line++;

        	/*ADDING LINE*/
	        networkRegion.maxWidth(Double.MAX_VALUE);
	        networkRegion.maxHeight(Double.MAX_VALUE);
	        grid.add(networkRegion, 0, line, 2, 1);
	        GridPane.setHalignment(networkRegion, HPos.CENTER);
	        line++;

	        
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        
        return scroll;
        
	}
	
	public static void generateModal(Stage primaryStage, Vector<GenericCableModel> config, String cableModel) {
		
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(50,50,50,50));
		
		/*CREATE THE CONTROLS*/
		
    	Stage dialog = new Stage();
        /*CREATE WINDOW ICON AND TITLE*/
        try {
	        Image image = new Image(SimuladorGPLCS.class.getResourceAsStream("logo_ufpa.png"));
	        dialog.getIcons().add(image);
	        dialog.setTitle("Cable Segment Configuration");
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	dialog.setScene(new Scene(pane));
    	
    	dialog.initOwner(primaryStage);
    	dialog.initModality(Modality.APPLICATION_MODAL); 
    	dialog.showAndWait();
    	
	}
	
}
