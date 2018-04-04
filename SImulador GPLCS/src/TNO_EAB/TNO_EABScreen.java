package TNO_EAB;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import CableSynthesis.CableSynthesisScreen;
import KHM1.KHM1Screen;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.GlyphsStack;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TNO_EABScreen {

	public static void getHelpTNOEAB(Stage primaryStage) {
		
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;

        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setResizable(false);
        
        ImageView TNO1Help = new ImageView(TNO_EABScreen.class.getResource("TNO_EAB-1.jpg").toExternalForm());
        TNO1Help.setPreserveRatio(true);
        TNO1Help.setFitWidth(screenWidth*100);
        ImageView TNO2Help = new ImageView(TNO_EABScreen.class.getResource("TNO_EAB-2.jpg").toExternalForm());
        TNO2Help.setPreserveRatio(true);
        TNO2Help.setFitWidth(screenWidth*100);
        
        FlowPane pane = new FlowPane();
        pane.setPadding(new Insets(0, 0, 50, 0));
        pane.maxWidth(Double.MAX_VALUE);
        pane.setHgap(0);
        pane.setVgap(0);
        
        /*ADDING BACK BUTTON*/
        Region iconClose = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.CLOSE)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);

        Button close = new Button("Close Help", iconClose);
        close.setId("back-button");        

        pane.getChildren().add(TNO1Help);
        pane.getChildren().add(TNO2Help);
        pane.getChildren().add(close);
        pane.setAlignment(Pos.CENTER);
        
        pane.setStyle("-fx-background-color: WHITE;");
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("ScrollPane");
        scrollPane.setContent(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
        scrollPane.setStyle("-fx-background-color: WHITE;");
        Scene scene = new Scene(scrollPane);
        
        stage.setScene(scene);
        
    	String css = TNO_EABScreen.class.getResource("TNO_EABHelp.css").toExternalForm(); 
    	stage.getScene().getStylesheets().clear();
    	stage.getScene().getStylesheets().add(css);

        close.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	stage.close();
            	
            }
        });
    	
    	stage.show();
        
        
	}
	
    /*CREATE WINDOW FOR INPUT FILE*/
    public static GridPane getInputFileWindow(Stage primaryStage) {
    	
        	GridPane grid = new GridPane();
			grid.setPadding(new Insets(50, 50, 50, 50));
			grid.setVgap(20);
			grid.setHgap(20);

			/******************************************/		
			
			ColumnConstraints col1,col2,col3;
			col1 = col2 = col3 = new ColumnConstraints();
            col1.setPercentWidth(33);
            col2.setPercentWidth(33);
            col3.setPercentWidth(33);
            grid.getColumnConstraints().add(col1);
            grid.getColumnConstraints().add(col2);
            grid.getColumnConstraints().add(col3);

			RowConstraints row1, row2;
			row1 = new RowConstraints();
			row2 = new RowConstraints();
            row1.setPercentHeight(10);
            row2.setPercentHeight(25);
            grid.getRowConstraints().addAll(row1,row2);
            
            /*CREATE INPUTS*/
						            			            
            Text help1 = new Text("File content: ");
			help1.setFont(Font.font("System",FontWeight.BOLD,17));

            Text help2 = new Text("Content Formated: ");
			help2.setFont(Font.font("System",FontWeight.BOLD,17));
			
			Text labelContentFile = new Text();
			labelContentFile.setFont(Font.font("Monospaced",14));
			labelContentFile.maxWidth(Double.MAX_VALUE);
			
	        JFXTextField fileColumnSeparator = new JFXTextField();
	        fileColumnSeparator.setLabelFloat(true);
	        fileColumnSeparator.setPromptText("Input the column separator character");

	        JFXTextField fileCableLength = new JFXTextField();
	        fileCableLength.setLabelFloat(true);
	        fileCableLength.setPromptText("Input the cable length");

	        JFXComboBox<Label> fileFrequency = new JFXComboBox<Label>();
	        fileFrequency.getItems().add(new Label("2.2MHz - 106MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 212MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 424MHz"));
	        fileFrequency.getItems().add(new Label("2.2MHz - 848MHz"));
	        fileFrequency.getItems().add(new Label("Custom"));
	        fileFrequency.setPromptText("Frequency Band");
	        fileFrequency.setMaxWidth(Double.MAX_VALUE);

	        JFXComboBox<Label> fileScale = new JFXComboBox<Label>();
	        fileScale.getItems().add(new Label("Logarithmic"));
	        fileScale.getItems().add(new Label("Linear"));
	        fileScale.setPromptText("Scale");
	        fileScale.setMaxWidth(Double.MAX_VALUE);
	        
	        JFXComboBox<Label> fileParameterCalc = new JFXComboBox<Label>();
	        fileParameterCalc.getItems().add(new Label("Propagation Constant"));
	        fileParameterCalc.getItems().add(new Label("Characteristic Impedance"));
	        fileParameterCalc.getItems().add(new Label("Transfer Function"));
	        fileParameterCalc.getItems().add(new Label("Primary Parameters"));
	        fileParameterCalc.setPromptText("Parameter to be Calculated");
	        fileParameterCalc.setMaxWidth(Double.MAX_VALUE);				        
	        Region iconSeparate = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.CHECK)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);
	        
	        JFXTextField fileMinF = new JFXTextField();
	        fileMinF.setLabelFloat(true);
	        fileMinF.setPromptText("Minimum Frequency (in MHz)");

	        JFXTextField fileMaxF = new JFXTextField();
	        fileMaxF.setLabelFloat(true);
	        fileMaxF.setPromptText("Maximum Frequency (in MHz)");

	        JFXTextField fileStep = new JFXTextField();
	        fileStep.setLabelFloat(true);
	        fileStep.setPromptText("Step (in MHz)");
	        
	        JFXButton separate = new JFXButton("Check File", iconSeparate);
	        separate.setId("separate");
	        separate.setMaxWidth(Double.MAX_VALUE);
	        
	        Region iconSelect = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.UPLOAD)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);
	        
	        Button selectFileModel = new Button("Select File of TNO/EAB Model Parameters", iconSelect);
	        selectFileModel.setId("fileModel");
	        selectFileModel.setMaxWidth(Double.MAX_VALUE);

			ScrollPane scrollFileContent = new ScrollPane();
			scrollFileContent.setContent(labelContentFile);

	        selectFileModel.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {

	            	/*GET THE FILE*/
	            	FileChooser fileChooser = new FileChooser();
	            	fileChooser.setTitle("Open a TNO/EAB Parameter File");

	            	File file1 = fileChooser.showOpenDialog(primaryStage);	            	
	            	
	            	if(file1 != null) {
	            		
	            		Scanner scanner;
	    				try {
	    					scanner = new Scanner(file1);
	            	        String textOfFile = "";
	    					while (scanner.hasNextLine()) {
	    						textOfFile += scanner.nextLine() + "\n";
	            	        }
	    					
	    					grid.getChildren().remove(scrollFileContent);
	    					labelContentFile.setText(textOfFile);
	    					scrollFileContent.setContent(labelContentFile);
	    					grid.add(scrollFileContent, 0, 1, 3, 1);

	    					/******************************************/		
	    					
	    				} catch (Exception e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				}

	            	}
	            	
	            }
	            
	        });
	        
	        final Boolean checked = new Boolean(false);
	        
	        /*CHECK FILE*/
	        separate.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	            	
	            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator.getText().isEmpty()) {
	            	
	            	  boolean error = false;
	            		
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  
	                  ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
			                  linesAndColumns.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY IF HAVE 11 LINES*/
	                  if(linesAndColumns.size() == 11) {
		                  int numberColumns = linesAndColumns.get(0).size();
		                  for(int i = 1; i < linesAndColumns.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns.get(i).size() != numberColumns) {
		                		  error = true;
			                	  System.out.println("cols");
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
				                	  System.out.println("NAN");
	                			  }
			                  }  
		                  }

	                  }else {
	                	  System.out.println("lines");
	                	  error = true;  
	                  }
	                  
	                  if(error) {
	                	  Alert alert = new Alert(AlertType.ERROR);
	                      alert.setTitle("Error");
	                      alert.setHeaderText("File error, format of file incorrect!");
	                      alert.showAndWait();
	                  }else {

	                	  /*IF EVERYTHING OK GENERATE TABLE OF VALUES TO CONFIRM THAT FILE IS CORRECTLY*/
	                	  TableView<String[]> table = new TableView<String[]>();
	                      table.setEditable(false);
	                      
	                      /*CREATING THE COLUMNS OF TABLE*/
	                      
	                      for(int i = 0; i < linesAndColumns.get(0).size(); i++) {
	                    	  Vector<Integer> actualI = new Vector<Integer>();
	                    	  actualI.add(i);
	                    	  TableColumn<String[],String> col = new TableColumn<String[],String>();
	                    	  col.setText(linesAndColumns.get(0).get(i));
	                    	  col.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
		                    	   public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
		                    	    String[] x = p.getValue();
		                    	    if (x != null && x.length > Integer.parseInt(actualI.get(0).toString())) {
		                    	     return new SimpleStringProperty(x[Integer.parseInt(actualI.get(0).toString())]);
		                    	    } else {
		                    	     return new SimpleStringProperty("<no name>");
		                    	    }
		                    	   }
		                    	  });
	                    	  table.getColumns().addAll(col);					                                
	                      }				                      

	                      /*ADDING INFORMATION TO COLUMNS*/
	                      String[][] data = new String[linesAndColumns.size() - 1][linesAndColumns.get(0).size()];
	                      for(int i = 1; i < linesAndColumns.size(); i++) {
	                    	  for(int j = 0; j < linesAndColumns.get(0).size(); j++) {				                    		  
	                    		  data[i - 1][j] = linesAndColumns.get(i).get(j);
	                    	  }
	                      }
	                      table.getItems().addAll(Arrays.asList(data));
	                      
	                      ScrollPane formatedTableScroll = new ScrollPane();
	                      formatedTableScroll.setContent(table);
	                      formatedTableScroll.setFitToWidth(true);
	                      formatedTableScroll.setFitToHeight(false);
	                      formatedTableScroll.setMaxHeight(Double.MAX_VALUE);
	                      
	                      grid.getChildren().remove(labelContentFile);
	                      grid.add(formatedTableScroll, 0, 1, 3, 1);
	                      			                      				                      
	                  }

	            		
	            	}else {
	            		
	            		Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("Error");
	                    alert.setHeaderText("Please give column separator for file!");
	                    alert.showAndWait();
	                    return;
	            		
	            	}
	                
	           }
	        });
	        
	        Region iconCalc = GlyphsStack.create().add(
	        		GlyphsBuilder.create(FontAwesomeIcon.class)
	        			.icon(FontAwesomeIconName.CALCULATOR)
	        			.style("-fx-fill: white;")
	        			.size("1em")
	        			.build()
	        		);

	        Button calc = new Button("Generate Graphs",iconCalc);
	        calc.setId("calc");
	        calc.setMaxWidth(Double.MAX_VALUE);
	        /*SEND DATA TO CALCULATE*/
	        calc.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {
	                boolean error = false;
	            	
	                /*              CHECK FILE           */
	                ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
	                
	            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
	            	if(!fileColumnSeparator.getText().isEmpty()) {
	            					            		
	                  ArrayList<String> lines = new ArrayList(Arrays.asList(labelContentFile.getText().split("\n")));
	                  /*REMOVE EMPTY LINES*/
	                  for(int i = 0; i < lines.size(); i++)
	                	  if(lines.get(i).isEmpty())
	                		  lines.remove(i);
	                  
	                  
		                  for(int i = 0; i < lines.size(); i++){
		                	  
			                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
			                  linesAndColumns.add(columnData);
	                  }
	                  
		                  
	                  /*VERIFY IF HAVE 11 LINES*/
	                  if(linesAndColumns.size() == 11) {
		                  int numberColumns = linesAndColumns.get(0).size();
		                  for(int i = 1; i < linesAndColumns.size(); i++) {
		                	  /*VERIFY IF HAVE THE SAME LENGTH OF COLUMNS*/
		                	  if(linesAndColumns.get(i).size() != numberColumns) {
		                		  error = true;
		                	  }
		                	  /*VERIFY IF ALL CELLS AFTER 1 IS NUMBERS*/
		                	  for(int j = 0; j < linesAndColumns.get(i).size(); j++) {
		                		  try {
	                				  Double.parseDouble(linesAndColumns.get(i).get(j));
	                			  }catch(NumberFormatException e) {
	                				  error = true;
	                			  }
			                  }  
		                  }

	                  }else {
	                	  error = true;  
	                  }
	                  
	            	}else {
	            		error = true;
	            	}
	                /*              END CHECK FILE            */
	            	
	                   double minF_value = 0;
	                   double maxF_value = 0;
	                   double step_value = 0;
	                   double cableLength_value = 0;
	                   String axisScale = "";
	                   String parameter = "";
	                   
	                   /*VALIDATE INFO'S*/
	                   try{
	                       cableLength_value = Double.parseDouble(fileCableLength.getText());

	                       if(fileFrequency.getValue().getText().contains("Custom")) {
	                    	   
	                    	   minF_value = Double.parseDouble(fileMinF.getText()) * 1e6;
		                       maxF_value = Double.parseDouble(fileMaxF.getText()) * 1e6;
	                    	   step_value = Double.parseDouble(fileStep.getText()) * 1e6;

	                       }else {
		                       
	                    	   minF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
		                       maxF_value = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
	                    	   step_value = 51.75e3;
		                       
	                       }

	                       axisScale = fileScale.getValue().getText();
	                       parameter = fileParameterCalc.getValue().getText();
	                   }catch(NumberFormatException e){
	                	   error = true;
	                   }
	                   
	                   if(error) {
	                	   Alert alert = new Alert(AlertType.ERROR);
	                       alert.setTitle("Error");
	                       alert.setHeaderText("Error, please fill correctly the inputs before continue, if inputs are correctly check file consistence!");
	                       alert.showAndWait();
	                       return;				                       
	                   }else {
		               
	                	   Vector<Double> Z0Inf = new Vector<Double>();
	                	   Vector<Double> nVF = new Vector<Double>();
	                	   Vector<Double> Rs0 = new Vector<Double>();
	                	   Vector<Double> qL = new Vector<Double>();
	                	   Vector<Double> qH = new Vector<Double>();
	                	   Vector<Double> qx = new Vector<Double>();
	                	   Vector<Double> qy = new Vector<Double>();
	                	   Vector<Double> qc = new Vector<Double>();
	                	   Vector<Double> phi = new Vector<Double>();
	                	   Vector<Double> fd = new Vector<Double>();
	                	   Vector<String> headings = new Vector<String>();
	                	   
		                   for(int i = 0; i < linesAndColumns.get(0).size(); i++)
		                	   headings.add(linesAndColumns.get(0).get(i));
		                   for(int i = 0; i < linesAndColumns.get(1).size(); i++)
		                	   Z0Inf.add(Double.parseDouble(linesAndColumns.get(1).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(2).size(); i++)
		                	   nVF.add(Double.parseDouble(linesAndColumns.get(2).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(3).size(); i++)
		                	   Rs0.add(Double.parseDouble(linesAndColumns.get(3).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(4).size(); i++)
		                	   qL.add(Double.parseDouble(linesAndColumns.get(4).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(5).size(); i++)
		                	   qH.add(Double.parseDouble(linesAndColumns.get(5).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(6).size(); i++)
		                	   qx.add(Double.parseDouble(linesAndColumns.get(6).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(7).size(); i++)
		                	   qy.add(Double.parseDouble(linesAndColumns.get(7).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(8).size(); i++)
		                	   qc.add(Double.parseDouble(linesAndColumns.get(8).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(9).size(); i++)
		                	   phi.add(Double.parseDouble(linesAndColumns.get(9).get(i)));
		                   for(int i = 0; i < linesAndColumns.get(10).size(); i++)
		                	   fd.add(Double.parseDouble(linesAndColumns.get(10).get(i)));

		                   /*GENERATE GRAPHS*/
		                   TNO_EABController.generateGraphs(headings, Z0Inf, nVF, Rs0, qL, qH, qx, qy, qc, phi, fd, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter);
	                	   
	                   }
	                   				            	
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
	            	primaryStage.getScene().setRoot(TNO_EABScreen.getTNO_EABScreen(primaryStage));
	            	String css = TNO_EAB.class.getResource("TNO_EABScreen.css").toExternalForm(); 
	            	primaryStage.getScene().getStylesheets().clear();
	            	primaryStage.getScene().getStylesheets().add(css);

	            }
	        });
	        
	        int line = 0;
	        
			/*ADDING LINE*/
			grid.add(help1, 0, line, 3, 1);
			GridPane.setHalignment(help1, HPos.CENTER);
			GridPane.setValignment(help1, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			grid.add(scrollFileContent, 0, line, 3, 1);
			GridPane.setHalignment(scrollFileContent, HPos.CENTER);
			GridPane.setValignment(scrollFileContent, VPos.CENTER);
			line++;


			/*ADDING LINE*/
			grid.add(selectFileModel, 0, line, 1, 1);
			GridPane.setHalignment(selectFileModel, HPos.CENTER);
			GridPane.setValignment(selectFileModel, VPos.CENTER);
			
			grid.add(fileColumnSeparator, 1, line, 1, 1);
			GridPane.setHalignment(fileColumnSeparator, HPos.CENTER);
			GridPane.setValignment(fileColumnSeparator, VPos.CENTER);
			
			grid.add(separate, 2, line, 1, 1);
			GridPane.setHalignment(separate, HPos.CENTER);
			GridPane.setValignment(separate, VPos.CENTER);
			line++;
											
			/*ADDING LINE*/
			grid.add(fileCableLength, 0, line, 1, 1);
			GridPane.setHalignment(fileCableLength, HPos.CENTER);
			GridPane.setValignment(fileCableLength, VPos.CENTER);

			grid.add(fileScale, 1, line, 1, 1);
			GridPane.setHalignment(fileScale, HPos.CENTER);
			GridPane.setValignment(fileScale, VPos.CENTER);						

			grid.add(fileFrequency, 2, line, 1, 1);
			GridPane.setHalignment(fileFrequency, HPos.CENTER);
			GridPane.setValignment(fileFrequency, VPos.CENTER);
			line++;

			
			/*ADDING LINE*/
			grid.add(fileParameterCalc, 0, line, 1, 1);
			GridPane.setHalignment(fileParameterCalc, HPos.CENTER);
			GridPane.setValignment(fileParameterCalc, VPos.CENTER);						

			line++;
			
			final int lineFrequencyCustomFile = line;
			
			fileFrequency.valueProperty().addListener(new ChangeListener<Label>() {
	            @Override
	            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

	            	if(newValue.getText().contains("Custom")) {
	            		
	            		/*ADDING LINE*/
	                    fileMinF.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileMinF, 0, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileMinF, HPos.CENTER);
	                    GridPane.setValignment(fileMinF, VPos.CENTER);
	                    
	                    fileMaxF.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileMaxF, 1, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileMaxF, HPos.CENTER);
	                    GridPane.setValignment(fileMaxF, VPos.CENTER);
	                    
	                    fileStep.setMaxWidth(Double.MAX_VALUE);
	                    grid.add(fileStep, 2, lineFrequencyCustomFile, 1, 1);
	                    GridPane.setHalignment(fileStep, HPos.CENTER);
	                    GridPane.setValignment(fileStep, VPos.CENTER);
	            		
	            	}else {
	            		
	            		grid.getChildren().remove(fileMinF);
	            		grid.getChildren().remove(fileMaxF);
	            		grid.getChildren().remove(fileStep);
	            		
	            	}
	            	
	            }
	        });
			
			line++;
			
			/*ADDING LINE*/
			grid.add(calc, 1, line, 1, 1);
			GridPane.setHalignment(calc, HPos.CENTER);
			GridPane.setValignment(calc, VPos.CENTER);
			line++;
			
			/*ADDING LINE*/
			back.setMaxWidth(Double.MAX_VALUE);
			grid.add(back, 1, line, 1, 1);
			GridPane.setHalignment(back, HPos.CENTER);
			GridPane.setValignment(back, VPos.CENTER);
			line++;				
			
			/******************************************/
                    	
			return grid;
            
       }

    public static ScrollPane getTNO_EABScreen(Stage primaryStage){
        
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
        String ApplicationName = "TNO/EAB Model Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS PREDEFINED*/
        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        labelPred.setId("HelpLabel");
        labelPred.setAlignment(Pos.CENTER);
                
        /*CREATE THE INPUTS PARAMETERS*/        
        JFXTextField phi = new JFXTextField();
        phi.setLabelFloat(true);
        phi.setPromptText("φ");

        JFXTextField qH = new JFXTextField();
        qH.setLabelFloat(true);
        qH.setPromptText("qH");

        JFXTextField qL = new JFXTextField();
        qL.setLabelFloat(true);
        qL.setPromptText("qL");
        
        JFXTextField qx = new JFXTextField();
        qx.setLabelFloat(true);
        qx.setPromptText("qx");
        
        JFXTextField qy = new JFXTextField();
        qy.setLabelFloat(true);
        qy.setPromptText("qy");

        JFXTextField Rs0 = new JFXTextField();
        Rs0.setLabelFloat(true);
        Rs0.setPromptText("Rs0");

        JFXTextField Z0inf = new JFXTextField();
        Z0inf.setLabelFloat(true);
        Z0inf.setPromptText("Z0∞");

        JFXTextField nVF = new JFXTextField();
        nVF.setLabelFloat(true);
        nVF.setPromptText("ηVF");

        JFXTextField qc = new JFXTextField();
        qc.setLabelFloat(true);
        qc.setPromptText("qc");

        JFXTextField fd = new JFXTextField();
        fd.setLabelFloat(true);
        fd.setPromptText("fd");
        
        /*ADDING HELP BUTTON*/
        Region iconHelp = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.INFO_CIRCLE)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);
        
        Button help = new Button("Help", iconHelp);
        help.setId("back-button");
        help.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
            	
            	/*COME BACK TO CABLE SYNTHESIS SCREEN*/
            	TNO_EABScreen.getHelpTNOEAB(primaryStage);

            }
        });
        
        final Label descriptionCable = new Label("Enter manually the parameters or choose at side a predefined cable type");
        descriptionCable.setId("descriptionCable");
        descriptionCable.setWrapText(true);
        
        /*CREATE COMBOBOX AND LABEL OF PREDEFINED CABLES*/
        JFXComboBox<Label> cableTypes = new JFXComboBox<Label>();
        cableTypes.getItems().add(new Label("Custom"));
        cableTypes.getItems().add(new Label("CAT5"));
        cableTypes.getItems().add(new Label("B05a"));
        cableTypes.getItems().add(new Label("T05b"));
        cableTypes.getItems().add(new Label("T05h"));
        cableTypes.getItems().add(new Label("T05u"));
        cableTypes.setPromptText("Custom");
        /*ON CHANGE CABLE TYPE*/
        cableTypes.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	/******** STANDARD CABLES TYPE *********/
            	switch(newValue.getText()) {
            	
            		case "CAT5":
        				descriptionCable.setText("Typical Category 5 cable commonly used in structured cabling for computer networks, such as Ethernet");
        				Z0inf.setText("98.000000");
        				nVF.setText("0.690464");
        				Rs0.setText("165.900000e-3");
        				qL.setText("2.150000");
        				qH.setText("0.859450");
        				qx.setText("0.500000");
        				qy.setText("0.722636");
        				qc.setText("0");
        				phi.setText("0.973846e-3");
        				fd.setText("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
    				break;
            		case "B05a":
            			descriptionCable.setText("Cable Aerial Drop-wire No 55 (CAD55), a typical copper line used in the UK");
            			Z0inf.setText("105.0694");
        				nVF.setText  ("0.6976");
        				Rs0.setText  ("0.1871");
        				qL.setText   ("1.5315");
        				qH.setText   ("0.7415");
        				qx.setText   ("1");
        				qy.setText   ("0");
        				qc.setText   ("1.0016");
        				phi.setText  ("-0.2356");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);        				
        			break;
            		case "T05b":
    					descriptionCable.setText("Medium quality multi-quad cable used in buildings");            			
            			Z0inf.setText("132.348256");
        				nVF.setText  ("0.675449");
        				Rs0.setText  ("170.500000e-3");
        				qL.setText   ("1.789725");
        				qH.setText   ("0.725776");
        				qx.setText   ("0.799306");
        				qy.setText   ("1.030832");
        				qc.setText   ("0");
        				phi.setText  ("0.005222e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
        			break;
            		case "T05h":
    					descriptionCable.setText("Low quality cable used for in-house telephony wiring");        				
            			Z0inf.setText("98.369783");
        				nVF.setText  ("0.681182");
        				Rs0.setText  ("170.800000e-3");
        				qL.setText   ("1.700000");
        				qH.setText   ("0.650000");
        				qx.setText   ("0.777307");
        				qy.setText   ("1.500000");
        				qc.setText   ("0");
        				phi.setText  ("3.023930e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
        			break;
            		case "T05u":
        				descriptionCable.setText("KPN cable, a typical access line used in the Netherlands");        				
            			Z0inf.setText("125.636455");
        				nVF.setText  ("0.729623");
        				Rs0.setText  ("180.000000e-3");
        				qL.setText   ("1.666050");
        				qH.setText   ("0.740000");
        				qx.setText   ("0.848761");
        				qy.setText   ("1.207166");
        				qc.setText   ("0");
        				phi.setText  ("1.762056e-3");
        				fd.setText   ("1.000000");
        				Z0inf.setDisable(true);
        				nVF.setDisable(true);
        				Rs0.setDisable(true);
        				qL.setDisable(true);
        				qH.setDisable(true);
        				qx.setDisable(true);
        				qy.setDisable(true);
        				qc.setDisable(true);
        				phi.setDisable(true);
        				fd.setDisable(true);
    				break;
            		default:
        				descriptionCable.setText("Enter manually the parameters or choose at side a predefined cable type");        				
        				Z0inf.setText("");
        				nVF.setText  ("");
        				Rs0.setText  ("");
        				qL.setText   ("");
        				qH.setText   ("");
        				qx.setText   ("");
        				qy.setText   ("");
        				qc.setText   ("");
        				phi.setText  ("");
        				fd.setText   ("");
        				Z0inf.setDisable(false);
        				nVF.setDisable(false);
        				Rs0.setDisable(false);
        				qL.setDisable(false);
        				qH.setDisable(false);
        				qx.setDisable(false);
        				qy.setDisable(false);
        				qc.setDisable(false);
        				phi.setDisable(false);
        				fd.setDisable(false);
        			break;
            	}
            	
            }
        });
                
        JFXTextField cableLength = new JFXTextField();
        cableLength.setLabelFloat(true);
        cableLength.setPromptText("Cable Length");

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
        
        JFXComboBox<Label> scale = new JFXComboBox<Label>();
        scale.getItems().add(new Label("Logarithmic"));
        scale.getItems().add(new Label("Linear"));
        scale.setPromptText("Scale");
        JFXComboBox<Label> parameterCalc = new JFXComboBox<Label>();
        parameterCalc.getItems().add(new Label("Propagation Constant"));
        parameterCalc.getItems().add(new Label("Characteristic Impedance"));
        parameterCalc.getItems().add(new Label("Transfer Function"));
        parameterCalc.getItems().add(new Label("Primary Parameters"));
        parameterCalc.setPromptText("Parameter to be Calculated");

        /*GENERATE FILE INPUT BUTTON*/
        Region inputIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.UPLOAD)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button fileInput = new Button("Select Parameter File", inputIcon);
        fileInput.setId("fileInput");
        /*SET BUTTON ONCLICK FUNCTION*/
        fileInput.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
         
        		primaryStage.getScene().setRoot(TNO_EABScreen.getInputFileWindow(primaryStage));
        		String css = TNO_EABScreen.class.getResource("InputFileWindow.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            	
            }
        });
				        
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
                            	
                Vector<String> headings = new Vector<String>();
                Vector<Double> Z0inf_value = new Vector<Double>();
                Vector<Double> nVF_value = new Vector<Double>();
                Vector<Double> Rs0_value = new Vector<Double>();
                Vector<Double> qL_value = new Vector<Double>();
                Vector<Double> qH_value = new Vector<Double>();
                Vector<Double> qx_value = new Vector<Double>();
                Vector<Double> qy_value = new Vector<Double>();
                Vector<Double> qc_value = new Vector<Double>();
                Vector<Double> phi_value = new Vector<Double>();
                Vector<Double> fd_value = new Vector<Double>();
                double minF_value;
                double maxF_value;
                double step_value;
                double cableLength_value;
                String axisScale;
                String parameter;
                
                /*VALIDATE INFO'S*/
                try{
                	
                	if(cableTypes.getValue() == null) {
                		
                		headings.add("Custom");
                		
                	}else {

                		headings.add(cableTypes.getValue().getText());

                	}
                	
                	Z0inf_value.add(Double.parseDouble(Z0inf.getText()));
                	nVF_value.add(Double.parseDouble(nVF.getText()));
                	Rs0_value.add(Double.parseDouble(Rs0.getText()));
                	qL_value.add(Double.parseDouble(qL.getText()));
                	qH_value.add(Double.parseDouble(qH.getText()));
                	qx_value.add(Double.parseDouble(qx.getText()));
                	qy_value.add(Double.parseDouble(qy.getText()));
                	qc_value.add(Double.parseDouble(qc.getText()));
                	phi_value.add(Double.parseDouble(phi.getText()));
                	fd_value.add(Double.parseDouble(fd.getText()));
                    cableLength_value = Double.parseDouble(cableLength.getText());
                    
                    if(frequency.getValue().getText().contains("Custom")) {

                    	minF_value = Double.parseDouble(minF.getText()) * 1e6;
                        maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
                    	step_value = Double.parseDouble(step.getText()) * 1e6;

                    }else {                        	

                    	minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                        maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                    	step_value = 51.75e3;
                        
                    }
                    
                    axisScale = scale.getValue().getText();
                    parameter = parameterCalc.getValue().getText();
                }catch(Exception e){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                    alert.setContentText(e.toString());
                    alert.showAndWait();
                    return;
                }
                
                /*GENERATE GRAPHS*/
                TNO_EABController.generateGraphs(headings, Z0inf_value, nVF_value, Rs0_value, qL_value, qH_value, qx_value, qy_value, qc_value, phi_value, fd_value, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter);
                
            }
        });
        
        /*CREATE OUTPUT FILE BUTTON*/
        Region outputIcon = GlyphsStack.create().add(
        		GlyphsBuilder.create(FontAwesomeIcon.class)
        			.icon(FontAwesomeIconName.DOWNLOAD)
        			.style("-fx-fill: white;")
        			.size("1em")
        			.build()
        		);        
        Button outputFile = new Button("Generate Result File", outputIcon);
        outputFile.setId("fileOutput");
        /*CREATE ONCLICK FUNCTION*/
        outputFile.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	
            	/*SAVE FILE WINDOW*/
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Save Result Output File");
                File selectedDirectory = fileChooser.showSaveDialog(primaryStage);
            	
            	if(selectedDirectory != null){
            		
            		double Z0inf_value;
                    double nVF_value;
                    double Rs0_value;
                    double qL_value;
                    double qH_value;
                    double qx_value;
                    double qy_value;
                    double qc_value;
                    double phi_value;
                    double fd_value;
                    double minF_value;
                    double maxF_value;
                    double step_value;
                    double cableLength_value;
                    String axisScale;
                    String parameter;
                    
                    /*VALIDATE INFO'S*/
                    try{
                    	Z0inf_value = Double.parseDouble(Z0inf.getText());
                    	nVF_value = Double.parseDouble(nVF.getText());
                    	Rs0_value = Double.parseDouble(Rs0.getText());
                    	qL_value = Double.parseDouble(qL.getText());
                    	qH_value = Double.parseDouble(qH.getText());
                    	qx_value = Double.parseDouble(qx.getText());
                    	qy_value = Double.parseDouble(qy.getText());
                    	qc_value = Double.parseDouble(qc.getText());
                    	phi_value = Double.parseDouble(phi.getText());
                    	fd_value = Double.parseDouble(fd.getText());
                        cableLength_value = Double.parseDouble(cableLength.getText());
                        
                        if(frequency.getValue().getText().contains("Custom")) {

                        	minF_value = Double.parseDouble(minF.getText()) * 1e6;
                            maxF_value = Double.parseDouble(maxF.getText()) * 1e6;
                        	step_value = Double.parseDouble(step.getText()) * 1e6;

                        }else {                        	

                        	minF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                            maxF_value = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
                        	step_value = 51.75e3;
                            
                        }
                        
                        axisScale = scale.getValue().getText();
                        parameter = parameterCalc.getValue().getText();
                    }catch(Exception ex){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                        alert.setContentText(ex.toString());
                        alert.showAndWait();
                        return;
                    }
                    
                    try {
						TNO_EABController.generateOutputFile(Z0inf_value, nVF_value, Rs0_value, qL_value, qH_value, qx_value, qy_value, qc_value, phi_value, fd_value, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter, selectedDirectory);
					} catch (Exception e1) {
						e1.printStackTrace();
					}         
            
            	}
                    
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
        Z0inf.setMaxWidth(Double.MAX_VALUE);
        grid.add(Z0inf, 0, line, 1, 1);
        GridPane.setHalignment(Z0inf, HPos.CENTER);
        GridPane.setValignment(Z0inf, VPos.CENTER);
        nVF.setMaxWidth(Double.MAX_VALUE);
        grid.add(nVF, 1, line, 1, 1);
        GridPane.setHalignment(nVF, HPos.CENTER);
        GridPane.setValignment(nVF, VPos.CENTER);
        Rs0.setMaxWidth(Double.MAX_VALUE);
        grid.add(Rs0, 2, line, 1, 1);
        GridPane.setHalignment(Rs0, HPos.CENTER);
        GridPane.setValignment(Rs0, VPos.CENTER);
        line++;
        
        /*ADDING LINE*/
		qL.setMaxWidth(Double.MAX_VALUE);
        grid.add(qL, 0, line, 1, 1);
        GridPane.setHalignment(qL, HPos.CENTER);
        GridPane.setValignment(qL, VPos.CENTER);
        qH.setMaxWidth(Double.MAX_VALUE);
        grid.add(qH, 1, line, 1, 1);
        GridPane.setHalignment(qH, HPos.CENTER);
        GridPane.setValignment(qH, VPos.CENTER);
        qx.setMaxWidth(Double.MAX_VALUE);
        grid.add(qx, 2, line, 1, 1);
        GridPane.setHalignment(qx, HPos.CENTER);
        GridPane.setValignment(qx, VPos.CENTER);
        line++;

        /*ADDING LINE*/
        qy.setMaxWidth(Double.MAX_VALUE);
        grid.add(qy, 0, line, 1, 1);
        GridPane.setHalignment(qy, HPos.CENTER);
        GridPane.setValignment(qy, VPos.CENTER);
        qc.setMaxWidth(Double.MAX_VALUE);
        grid.add(qc, 1, line, 1, 1);
        GridPane.setHalignment(qc, HPos.CENTER);
        GridPane.setValignment(qc, VPos.CENTER);
        phi.setMaxWidth(Double.MAX_VALUE);
        grid.add(phi, 2, line, 1, 1);
        GridPane.setHalignment(phi, HPos.CENTER);
        GridPane.setValignment(phi, VPos.CENTER);
        line++;
                
        /*ADDING LINE*/
        fd.setMaxWidth(Double.MAX_VALUE);
        grid.add(fd, 0, line, 1, 1);
        GridPane.setHalignment(fd, HPos.CENTER);
        GridPane.setValignment(fd, VPos.CENTER);
        cableLength.setMaxWidth(Double.MAX_VALUE);
        grid.add(cableLength, 1, line, 1, 1);
        GridPane.setHalignment(cableLength, HPos.CENTER);
        GridPane.setValignment(cableLength, VPos.CENTER);
        frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 2, line, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
        GridPane.setValignment(frequency, VPos.CENTER);
        line++;

        /*ADDING LINE*/
        scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 0, line, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
        GridPane.setValignment(scale, VPos.CENTER);
        parameterCalc.setMaxWidth(Double.MAX_VALUE);
        grid.add(parameterCalc, 1, line, 1, 1);
        GridPane.setHalignment(parameterCalc, HPos.CENTER);
        GridPane.setValignment(parameterCalc, VPos.CENTER);
        line++;
        
        final int lineFrequencyCustom = line;
        
		frequency.valueProperty().addListener(new ChangeListener<Label>() {
            @Override
            public void changed(ObservableValue<? extends Label> observable, Label oldValue, Label newValue) {

            	if(newValue.getText().contains("Custom")) {
            		
            		/*ADDING LINE*/
                    minF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(minF, 0, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(minF, HPos.CENTER);
                    GridPane.setValignment(minF, VPos.CENTER);
                    
                    maxF.setMaxWidth(Double.MAX_VALUE);
                    grid.add(maxF, 1, lineFrequencyCustom, 1, 1);
                    GridPane.setHalignment(maxF, HPos.CENTER);
                    GridPane.setValignment(maxF, VPos.CENTER);
                    
                    step.setMaxWidth(Double.MAX_VALUE);
                    grid.add(step, 2, lineFrequencyCustom, 1, 1);
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
		
        /*ADDING LINE*/
        fileInput.setMaxWidth(Double.MAX_VALUE);
        grid.add(fileInput, 0, line, 1, 1);
        GridPane.setHalignment(fileInput, HPos.CENTER);
        GridPane.setValignment(fileInput, VPos.CENTER);
        calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 1, line, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
        GridPane.setValignment(calculate, VPos.CENTER);
        outputFile.setMaxWidth(Double.MAX_VALUE);
        grid.add(outputFile, 2, line, 1, 1);
        GridPane.setHalignment(outputFile, HPos.CENTER);
        GridPane.setValignment(outputFile, VPos.CENTER);
        line++;
        
        /*ADDING LINE*/
        help.setMaxWidth(Double.MAX_VALUE);
		grid.add(help, 0, line, 1, 1);
		GridPane.setHalignment(help, HPos.CENTER);
		GridPane.setValignment(help, VPos.CENTER);
        back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 1, line, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        GridPane.setValignment(back, VPos.CENTER);
        line++;
        
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }

	
}
