package BT0;

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
import TNO_EAB.TNO_EABScreen;
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

public class BT0Screen {
	
	public static void getHelpBT0(Stage primaryStage) {
		
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;

        Stage stage = new Stage();
        stage.setMaximized(true);
        stage.setResizable(false);
        
        ImageView BT0Help = new ImageView(BT0Screen.class.getResource("BT0.jpg").toExternalForm());
        BT0Help.setPreserveRatio(true);
        BT0Help.setFitWidth(screenWidth*100);
        
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

        pane.getChildren().add(BT0Help);
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
        
    	String css = BT0Screen.class.getResource("BT0Help.css").toExternalForm(); 
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
	        
	        JFXTextField fileMinF = new JFXTextField();
	        fileMinF.setLabelFloat(true);
	        fileMinF.setPromptText("Minimum Frequency (in MHz)");

	        JFXTextField fileMaxF = new JFXTextField();
	        fileMaxF.setLabelFloat(true);
	        fileMaxF.setPromptText("Maximum Frequency (in MHz)");

	        JFXTextField fileStep = new JFXTextField();
	        fileStep.setLabelFloat(true);
	        fileStep.setPromptText("Step (in MHz)");

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

	        Button selectFileModel = new Button("Select File of BT0 Model Parameters", iconSelect);
	        selectFileModel.setId("fileModel");
	        selectFileModel.setMaxWidth(Double.MAX_VALUE);
	        
	        ScrollPane scrollFileContent = new ScrollPane();
			scrollFileContent.setContent(labelContentFile);

	        selectFileModel.setOnMousePressed(new EventHandler<MouseEvent>() {
	            public void handle(MouseEvent me) {

	            	/*GET THE FILE*/
	            	FileChooser fileChooser = new FileChooser();
	            	fileChooser.setTitle("Open a KHM 1 Parameter File");

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
	                  
		                  
	                  /*VERIFY IF HAVE 12 LINES*/
	                  if(linesAndColumns.size() == 12) {
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
	                  
		                  
	                  /*VERIFY IF HAVE 12 LINES*/
	                  if(linesAndColumns.size() == 12) {
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
	                	   
	                       Vector<Double> Roc_value = new Vector<Double>();
	                       Vector<Double> ac_value = new Vector<Double>();
	                       Vector<Double> L0_value  = new Vector<Double>();
	                       Vector<Double> Linf_value= new Vector<Double>();
	                       Vector<Double> fm_value  = new Vector<Double>();
	                       Vector<Double> Nb_value  = new Vector<Double>();
	                       Vector<Double> g0_value  = new Vector<Double>();
	                       Vector<Double> Nge_value = new Vector<Double>();
	                       Vector<Double> C0_value  = new Vector<Double>();
	                       Vector<Double> Cinf_value= new Vector<Double>();
	                       Vector<Double> Nce_value = new Vector<Double>();
	                       
	                       Vector<String> headings = new Vector<String>();
	                	   
		                   for(int i = 0; i < linesAndColumns.get(0).size(); i++)
		                	   headings.add(linesAndColumns.get(0).get(i));

		                   for(int i = 0; i < linesAndColumns.get(1).size(); i++)
		                	   Roc_value.add(Double.parseDouble(linesAndColumns.get(1).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(2).size(); i++)
		                	   ac_value.add(Double.parseDouble(linesAndColumns.get(2).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(3).size(); i++)
		                	   L0_value.add(Double.parseDouble(linesAndColumns.get(3).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(4).size(); i++)
		                	   Linf_value.add(Double.parseDouble(linesAndColumns.get(4).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(5).size(); i++)
		                	   fm_value.add(Double.parseDouble(linesAndColumns.get(5).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(6).size(); i++)
		                	   Nb_value.add(Double.parseDouble(linesAndColumns.get(6).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(7).size(); i++)
		                	   g0_value.add(Double.parseDouble(linesAndColumns.get(7).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(8).size(); i++)
		                	   Nge_value.add(Double.parseDouble(linesAndColumns.get(8).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(9).size(); i++)
		                	   C0_value.add(Double.parseDouble(linesAndColumns.get(9).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(10).size(); i++)
		                	   Cinf_value.add(Double.parseDouble(linesAndColumns.get(10).get(i)));

		                   for(int i = 0; i < linesAndColumns.get(11).size(); i++)
		                	   Nce_value.add(Double.parseDouble(linesAndColumns.get(11).get(i)));

		                   /*GENERATE GRAPHS*/
		                   BT0Controller.generateGraphs(headings, Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter);
	                       
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
	            	primaryStage.getScene().setRoot(BT0Screen.getBT0Screen(primaryStage));
	            	String css = BT0Screen.class.getResource("BT0Screen.css").toExternalForm(); 
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

	
    /*GET WINDOW FOR BT0 CABLE SYNTHESIS*/
    public static ScrollPane getBT0Screen(Stage primaryStage){
    
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

        /*CREATE THE INPUTS CONFIG*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("2.2MHz - 106MHz"));
        frequency.getItems().add(new Label("2.2MHz - 212MHz"));
        frequency.getItems().add(new Label("2.2MHz - 424MHz"));
        frequency.getItems().add(new Label("2.2MHz - 848MHz"));
        frequency.getItems().add(new Label("Custom"));
        frequency.setPromptText("Frequency Band");
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

        
        
        JFXTextField minF = new JFXTextField();
        minF.setLabelFloat(true);
        minF.setPromptText("Minimum Frequency (in MHz)");

        JFXTextField maxF = new JFXTextField();
        maxF.setLabelFloat(true);
        maxF.setPromptText("Maximum Frequency (in MHz)");

        JFXTextField step = new JFXTextField();
        step.setLabelFloat(true);
        step.setPromptText("Step (in MHz)");

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
            	BT0Screen.getHelpBT0(primaryStage);

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

                    double minF_value;
                    double maxF_value;
                    double step_value;

                    double cableLength_value;
                    String axisScale;
                    String parameter;
                    
                    /*VALIDATE INFO'S*/
                    try{
                    	
                        Roc_value=Double.parseDouble(Roc.getText());
                        ac_value=Double.parseDouble(ac.getText());
                        L0_value=Double.parseDouble(L0.getText());
                        Linf_value=Double.parseDouble(Linf.getText());
                        fm_value=Double.parseDouble(fm.getText());
                        Nb_value=Double.parseDouble(Nb.getText());
                        g0_value=Double.parseDouble(g0.getText());
                        Nge_value=Double.parseDouble(Nge.getText());
                        C0_value=Double.parseDouble(C0.getText());
                        Cinf_value=Double.parseDouble(Cinf.getText());
                        Nce_value=Double.parseDouble(Nce.getText());

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
                    }catch(Exception ee){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error, please fill correctly the inputs before continue!");
                        alert.setContentText(ee.toString());
                        alert.showAndWait();
                        return;
                    }
                                    
                    /*GENERATE GRAPHS*/
                    BT0Controller.generateOutputFile(Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter, selectedDirectory);
                	
                }
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
                
                Vector<Double> Roc_value = new Vector<Double>();
                Vector<Double> ac_value = new Vector<Double>();
                Vector<Double> L0_value  = new Vector<Double>();
                Vector<Double> Linf_value= new Vector<Double>();
                Vector<Double> fm_value  = new Vector<Double>();
                Vector<Double> Nb_value  = new Vector<Double>();
                Vector<Double> g0_value  = new Vector<Double>();
                Vector<Double> Nge_value = new Vector<Double>();
                Vector<Double> C0_value  = new Vector<Double>();
                Vector<Double> Cinf_value= new Vector<Double>();
                Vector<Double> Nce_value = new Vector<Double>();

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

                    Roc_value.add(Double.parseDouble(Roc.getText()));
                    ac_value.add(Double.parseDouble(ac.getText()));
                    L0_value.add(Double.parseDouble(L0.getText()));
                    Linf_value.add(Double.parseDouble(Linf.getText()));
                    fm_value.add(Double.parseDouble(fm.getText()));
                    Nb_value.add(Double.parseDouble(Nb.getText()));
                    g0_value.add(Double.parseDouble(g0.getText()));
                    Nge_value.add(Double.parseDouble(Nge.getText()));
                    C0_value.add(Double.parseDouble(C0.getText()));
                    Cinf_value.add(Double.parseDouble(Cinf.getText()));
                    Nce_value.add(Double.parseDouble(Nce.getText()));

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
                BT0Controller.generateGraphs(headings, Roc_value, ac_value,L0_value,Linf_value,fm_value,Nb_value,g0_value,Nge_value,C0_value,Cinf_value,Nce_value, cableLength_value, minF_value, maxF_value, step_value, axisScale, parameter);
                
           }
        });
        
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
         
        		primaryStage.getScene().setRoot(BT0Screen.getInputFileWindow(primaryStage));
        		String css = BT0Screen.class.getResource("InputFileWindow.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);
            	
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
        frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 0, line, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
        GridPane.setValignment(frequency, VPos.CENTER);
        scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 1, line, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
        GridPane.setValignment(scale, VPos.CENTER);
        parameterCalc.setMaxWidth(Double.MAX_VALUE);
        grid.add(parameterCalc, 2, line, 1, 1);
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
        grid.setAlignment(Pos.CENTER);
        line++;
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }

	
}
