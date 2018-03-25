package KHM1;

import java.awt.Cursor;
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

import CableSynthesis.CableSynthesisController;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class KHM1Screen {
	
	public static ScrollPane getHelpKHM1(Stage primaryStage) {
		
		/*GET THE SCREEN HEIGHT AND WIDTH TO CREATE WINDOW*/
        int screenWidth  = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/100;
		
        GridPane grid = new GridPane();
        grid.setId("GridPane");
        grid.setVgap(25);
        grid.setHgap(10);
        grid.setPadding(new Insets(50,50,50,50));
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100);
        grid.getColumnConstraints().add(col);
        
        Label title = new Label("KM Model 1");
        title.setId("title");
        title.setWrapText(true);
        
        Label description = new Label("KH Model 1 is a transmission line model that are based on propagation constant and characteristic impedance.");
        description.setWrapText(true);

        Label descriptionPC = new Label("Propagation constant is gived by: ");
        description.setWrapText(true);
        
        ImageView PCFormula = new ImageView(KHM1Screen.class.getResource("PropagationConstant.png").toExternalForm());
        PCFormula.setFitWidth(screenWidth*30);
        PCFormula.setPreserveRatio(true);
        
        Label descriptionPropagationConstant = new Label("Where α(f) is the attenuation constant and β(f) is the phase constant.");
        descriptionPropagationConstant.setWrapText(true);

        Label descriptionAlpha = new Label("α(f) is gived by: ");
        descriptionAlpha.setWrapText(true);
       
        ImageView AlphaFormula = new ImageView(KHM1Screen.class.getResource("Alpha.png").toExternalForm());
        AlphaFormula.setFitWidth(screenWidth*30);
        AlphaFormula.setPreserveRatio(true);

        Label descriptionBeta = new Label("β(f) is gived by: ");
        descriptionBeta.setWrapText(true);

        ImageView BetaFormula = new ImageView(KHM1Screen.class.getResource("Beta.png").toExternalForm());
        BetaFormula.setFitWidth(screenWidth*50);
        BetaFormula.setPreserveRatio(true);
        
        Label descriptionCharacteristicImpedance = new Label("The characteristic impedance is gived by: ");
        descriptionCharacteristicImpedance.setWrapText(true);

        ImageView CIFormula = new ImageView(KHM1Screen.class.getResource("CharacteristicImpedance.png").toExternalForm());
        CIFormula.setFitWidth(screenWidth*30);
        CIFormula.setPreserveRatio(true);

        Label descriptionRealCharacteristicImpedance = new Label("Where the real part is gived by: ");
        descriptionRealCharacteristicImpedance.setWrapText(true);

        ImageView RealFormula = new ImageView(KHM1Screen.class.getResource("Real.png").toExternalForm());
        RealFormula.setFitWidth(screenWidth*30);
        RealFormula.setPreserveRatio(true);
        
        Label descriptionImagCharacteristicImpedance = new Label("And the imaginary part is gived by: ");
        descriptionImagCharacteristicImpedance.setWrapText(true);

        ImageView ImagFormula = new ImageView(KHM1Screen.class.getResource("Imaginary.png").toExternalForm());
        ImagFormula.setFitWidth(screenWidth*30);
        ImagFormula.setPreserveRatio(true);
        
        Label descriptionTransferFunction = new Label("The Transfer Function is calculated by: ");
        descriptionTransferFunction.setWrapText(true);

        ImageView TFFormula = new ImageView(KHM1Screen.class.getResource("TF.png").toExternalForm());
        TFFormula.setFitWidth(screenWidth*20);
        TFFormula.setPreserveRatio(true);

        Label descriptionRLGC = new Label("And finaly RLGC is calculated by: ");
        descriptionRLGC.setWrapText(true);

        ImageView RLGCFormula = new ImageView(KHM1Screen.class.getResource("RLGC.png").toExternalForm());
        RLGCFormula.setFitWidth(screenWidth*75);
        RLGCFormula.setPreserveRatio(true);

        Label descriptionW = new Label("Where ω is 2πf.");
        descriptionW.setWrapText(true);

        Label obs = new Label("Obs.: All parameters of cables predetermined on KH Model 1 Screen are measured per km.");
        obs.setId("obs");
        obs.setWrapText(true);

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
            	primaryStage.getScene().setRoot(KHM1Screen.getKHMScreen(primaryStage));
            	String css = KHM1Screen.class.getResource("KHM1Screen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
        
        int line = 0;
        
		title.setMaxWidth(Double.MAX_VALUE);
		grid.add(title, 0, line, 1, 1);
		GridPane.setHalignment(title, HPos.CENTER);
		GridPane.setValignment(title, VPos.CENTER);		
		line++;

		description.setMaxWidth(Double.MAX_VALUE);
		grid.add(description, 0, line, 1, 1);
		GridPane.setHalignment(description, HPos.CENTER);
		GridPane.setValignment(description, VPos.CENTER);		
		line++;

		descriptionPC.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionPC, 0, line, 1, 1);
		GridPane.setHalignment(descriptionPC, HPos.CENTER);
		GridPane.setValignment(descriptionPC, VPos.CENTER);		
		line++;

		grid.add(PCFormula, 0, line, 1, 1);
		GridPane.setHalignment(PCFormula, HPos.CENTER);
		GridPane.setValignment(PCFormula, VPos.CENTER);		
		line++;

		descriptionPropagationConstant.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionPropagationConstant, 0, line, 1, 1);
		GridPane.setHalignment(descriptionPropagationConstant, HPos.CENTER);
		GridPane.setValignment(descriptionPropagationConstant, VPos.CENTER);		
		line++;

		descriptionAlpha.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionAlpha, 0, line, 1, 1);
		GridPane.setHalignment(descriptionAlpha, HPos.CENTER);
		GridPane.setValignment(descriptionAlpha, VPos.CENTER);		
		line++;

		grid.add(AlphaFormula, 0, line, 1, 1);
		GridPane.setHalignment(AlphaFormula, HPos.CENTER);
		GridPane.setValignment(AlphaFormula, VPos.CENTER);		
		line++;

		descriptionBeta.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionBeta, 0, line, 1, 1);
		GridPane.setHalignment(descriptionBeta, HPos.CENTER);
		GridPane.setValignment(descriptionBeta, VPos.CENTER);		
		line++;

		grid.add(BetaFormula, 0, line, 1, 1);
		GridPane.setHalignment(BetaFormula, HPos.CENTER);
		GridPane.setValignment(BetaFormula, VPos.CENTER);		
		line++;

		descriptionCharacteristicImpedance.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionCharacteristicImpedance, 0, line, 1, 1);
		GridPane.setHalignment(descriptionCharacteristicImpedance, HPos.CENTER);
		GridPane.setValignment(descriptionCharacteristicImpedance, VPos.CENTER);		
		line++;

		grid.add(CIFormula, 0, line, 1, 1);
		GridPane.setHalignment(CIFormula, HPos.CENTER);
		GridPane.setValignment(CIFormula, VPos.CENTER);		
		line++;

		descriptionRealCharacteristicImpedance.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionRealCharacteristicImpedance, 0, line, 1, 1);
		GridPane.setHalignment(descriptionRealCharacteristicImpedance, HPos.CENTER);
		GridPane.setValignment(descriptionRealCharacteristicImpedance, VPos.CENTER);		
		line++;

		grid.add(RealFormula, 0, line, 1, 1);
		GridPane.setHalignment(RealFormula, HPos.CENTER);
		GridPane.setValignment(RealFormula, VPos.CENTER);		
		line++;

		descriptionImagCharacteristicImpedance.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionImagCharacteristicImpedance, 0, line, 1, 1);
		GridPane.setHalignment(descriptionImagCharacteristicImpedance, HPos.CENTER);
		GridPane.setValignment(descriptionImagCharacteristicImpedance, VPos.CENTER);		
		line++;

		grid.add(ImagFormula, 0, line, 1, 1);
		GridPane.setHalignment(ImagFormula, HPos.CENTER);
		GridPane.setValignment(ImagFormula, VPos.CENTER);		
		line++;

		descriptionTransferFunction.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionTransferFunction, 0, line, 1, 1);
		GridPane.setHalignment(descriptionTransferFunction, HPos.CENTER);
		GridPane.setValignment(descriptionTransferFunction, VPos.CENTER);		
		line++;

		grid.add(TFFormula, 0, line, 1, 1);
		GridPane.setHalignment(TFFormula, HPos.CENTER);
		GridPane.setValignment(TFFormula, VPos.CENTER);		
		line++;

		descriptionRLGC.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionRLGC, 0, line, 1, 1);
		GridPane.setHalignment(descriptionRLGC, HPos.CENTER);
		GridPane.setValignment(descriptionRLGC, VPos.CENTER);		
		line++;

		grid.add(RLGCFormula, 0, line, 1, 1);
		GridPane.setHalignment(RLGCFormula, HPos.CENTER);
		GridPane.setValignment(RLGCFormula, VPos.CENTER);		
		line++;

		descriptionW.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionW, 0, line, 1, 1);
		GridPane.setHalignment(descriptionW, HPos.CENTER);
		GridPane.setValignment(descriptionW, VPos.CENTER);		
		line++;

		obs.setMaxWidth(Double.MAX_VALUE);
		grid.add(obs, 0, line, 1, 1);
		GridPane.setHalignment(obs, HPos.CENTER);
		GridPane.setValignment(obs, VPos.CENTER);		
		line++;

		back.setMaxWidth(Double.MAX_VALUE);
		grid.add(back, 0, line, 1, 1);
		GridPane.setHalignment(back, HPos.CENTER);
		GridPane.setValignment(back, VPos.CENTER);		
		line++;


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId("ScrollPane");
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        
		return scrollPane;
		
	}

    /*CREATE WINDOW FOR INPUT FILE*/
    public static GridPane getInputFileWindow(Stage primaryStage, File file) {
    	
        	GridPane grid = new GridPane();
			grid.setPadding(new Insets(50, 50, 50, 50));
			grid.setVgap(20);
			grid.setHgap(20);

        	if(file != null) {
        		Scanner scanner;
				try {
					scanner = new Scanner(file);
        	        String textOfFile = "";
					while (scanner.hasNextLine()) {
						textOfFile += scanner.nextLine() + "\n";
        	        }
					final String finalTextOfFile = textOfFile;
					/******************************************/		
					
					ColumnConstraints col1,col2,col3;
					col1 = col2 = col3 = new ColumnConstraints();
		            col1.setPercentWidth(33);
		            col2.setPercentWidth(33);
		            col3.setPercentWidth(33);
		            grid.getColumnConstraints().add(col1);
		            grid.getColumnConstraints().add(col2);
		            grid.getColumnConstraints().add(col3);

					RowConstraints row1, row2, row3, row4, row5, row6, row7;
					row1 = new RowConstraints();
					row2 = new RowConstraints();
					row3 = new RowConstraints();
					row4 = new RowConstraints();
					row5 = new RowConstraints();
					row6 = new RowConstraints();
					row7 = new RowConstraints();
		            row1.setPercentHeight(10);
		            row2.setPercentHeight(25);
		            row3.setPercentHeight(10);
		            row4.setPercentHeight(10);
		            row5.setPercentHeight(10);
		            row6.setPercentHeight(10);
		            row7.setPercentHeight(25);
		            grid.getRowConstraints().addAll(row1,row2,row3,row4,row5,row6,row7);
		            
		            /*CREATE INPUTS*/
								            			            
		            Text help1 = new Text("File content: ");
					help1.setFont(Font.font("System",FontWeight.BOLD,17));

		            Text help2 = new Text("Content Formated: ");
					help2.setFont(Font.font("System",FontWeight.BOLD,17));
					
					Text labelContentFile = new Text(textOfFile);
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
			        JFXButton separate = new JFXButton("Check File", iconSeparate);
			        separate.setId("separate");
			        separate.setMaxWidth(Double.MAX_VALUE);
			        
			        final Boolean checked = new Boolean(false);
			        
			        /*CHECK FILE*/
			        separate.setOnMousePressed(new EventHandler<MouseEvent>() {
			            public void handle(MouseEvent me) {
			            	
			            	/*VERIFY IF COLUMN SEPARATOR IS GIVED*/
			            	if(!fileColumnSeparator.getText().isEmpty()) {
			            	
			            	  boolean error = false;
			            		
			                  ArrayList<String> lines = new ArrayList(Arrays.asList(finalTextOfFile.split("\n")));
			                  /*REMOVE EMPTY LINES*/
			                  for(int i = 0; i < lines.size(); i++)
			                	  if(lines.get(i).isEmpty())
			                		  lines.remove(i);
			                  
			                  ArrayList<ArrayList<String>> linesAndColumns = new ArrayList<ArrayList<String>>();
			                  
				                  for(int i = 0; i < lines.size(); i++){
				                	  
					                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
					                  linesAndColumns.add(columnData);
			                  }
			                  
				                  
			                  /*VERIFY IF HAVE 6 LINES*/
			                  if(linesAndColumns.size() == 6) {
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
			                      
			                      grid.add(formatedTableScroll, 0, 6, 3, 1);
			                      				                      
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

			        Button calc = new Button("Calculate",iconCalc);
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
			            					            		
			                  ArrayList<String> lines = new ArrayList(Arrays.asList(finalTextOfFile.split("\n")));
			                  /*REMOVE EMPTY LINES*/
			                  for(int i = 0; i < lines.size(); i++)
			                	  if(lines.get(i).isEmpty())
			                		  lines.remove(i);
			                  
			                  
				                  for(int i = 0; i < lines.size(); i++){
				                	  
					                  ArrayList<String> columnData = new ArrayList<String>(Arrays.asList(lines.get(i).split(Pattern.quote(fileColumnSeparator.getText().trim()))));
					                  linesAndColumns.add(columnData);
			                  }
			                  
				                  
			                  /*VERIFY IF HAVE 6 LINES*/
			                  if(linesAndColumns.size() == 6) {
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
			            	
			                   double minF = 0;
			                   double maxF = 0;
			                   double cableLength_value = 0;
			                   String axisScale = "";
			                   String parameter = "";
			                   
			                   /*VALIDATE INFO'S*/
			                   try{
			                       cableLength_value = Double.parseDouble(fileCableLength.getText());
			                       minF = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
			                       maxF = Double.parseDouble(fileFrequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
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
				               
			                	   Vector<Double> k1 = new Vector<Double>();
			                	   Vector<Double> k2 = new Vector<Double>();
			                	   Vector<Double> k3 = new Vector<Double>();
			                	   Vector<Double> h1 = new Vector<Double>();
			                	   Vector<Double> h2 = new Vector<Double>();
			                	   Vector<String> headings = new Vector<String>();
			                	   
				                   for(int i = 0; i < linesAndColumns.get(0).size(); i++)
				                	   headings.add(linesAndColumns.get(0).get(i));

				                   for(int i = 0; i < linesAndColumns.get(1).size(); i++)
				                	   k1.add(Double.parseDouble(linesAndColumns.get(1).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(2).size(); i++)
				                	   k2.add(Double.parseDouble(linesAndColumns.get(2).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(3).size(); i++)
				                	   k3.add(Double.parseDouble(linesAndColumns.get(3).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(4).size(); i++)
				                	   h1.add(Double.parseDouble(linesAndColumns.get(4).get(i)));

				                   for(int i = 0; i < linesAndColumns.get(5).size(); i++)
				                	   h2.add(Double.parseDouble(linesAndColumns.get(5).get(i)));

				                   /*GENERATE GRAPHS*/
				                   KHM1Controller.generateGraphs(headings, k1, k2, k3, h1, h2, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
			                	   
			                   }
			                   				            	
			           }
			        });
			        				        
					ScrollPane scrollFileContent = new ScrollPane();
					scrollFileContent.setContent(labelContentFile);

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
			            	primaryStage.getScene().setRoot(KHM1Screen.getKHMScreen(primaryStage));
			            	String css = KHM1Screen.class.getResource("KHM1Screen.css").toExternalForm(); 
			            	primaryStage.getScene().getStylesheets().clear();
			            	primaryStage.getScene().getStylesheets().add(css);

			            }
			        });

			        
					/*LINE 1*/
					grid.add(help1, 0, 0, 3, 1);
					GridPane.setHalignment(help1, HPos.CENTER);
					GridPane.setValignment(help1, VPos.CENTER);
					
					/*LINE 2*/
					grid.add(scrollFileContent, 0, 1, 3, 1);
					GridPane.setHalignment(scrollFileContent, HPos.CENTER);
					GridPane.setValignment(scrollFileContent, VPos.CENTER);
					
					/*LINE 3*/
					grid.add(fileColumnSeparator, 0, 2, 1, 1);
					GridPane.setHalignment(fileColumnSeparator, HPos.CENTER);
					GridPane.setValignment(fileColumnSeparator, VPos.CENTER);
					
					grid.add(fileCableLength, 1, 2, 1, 1);
					GridPane.setHalignment(fileCableLength, HPos.CENTER);
					GridPane.setValignment(fileCableLength, VPos.CENTER);

					grid.add(fileFrequency, 2, 2, 1, 1);
					GridPane.setHalignment(fileFrequency, HPos.CENTER);
					GridPane.setValignment(fileFrequency, VPos.CENTER);

					/*LINE 4*/
					grid.add(fileScale, 0, 3, 1, 1);
					GridPane.setHalignment(fileScale, HPos.CENTER);
					GridPane.setValignment(fileScale, VPos.CENTER);						

					grid.add(fileParameterCalc, 1, 3, 1, 1);
					GridPane.setHalignment(fileParameterCalc, HPos.CENTER);
					GridPane.setValignment(fileParameterCalc, VPos.CENTER);						

					grid.add(separate, 2, 3, 1, 1);
					GridPane.setHalignment(separate, HPos.CENTER);
					GridPane.setValignment(separate, VPos.CENTER);

					/*LINE 5*/
					grid.add(calc, 1, 4, 1, 1);
					GridPane.setHalignment(calc, HPos.CENTER);
					GridPane.setValignment(calc, VPos.CENTER);
					back.setMaxWidth(Double.MAX_VALUE);
					grid.add(back, 2, 4, 1, 1);
					GridPane.setHalignment(back, HPos.CENTER);
					GridPane.setValignment(back, VPos.CENTER);

					/*LINE 6*/
					grid.add(help2, 0, 5, 3, 1);
					GridPane.setHalignment(help2, HPos.CENTER);
					GridPane.setValignment(help2, VPos.CENTER);
																
					/******************************************/
    	            
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        	}
        	
			return grid;
            
       }

    /*GET WINDOW FOR KHM CABLE SYNTHESIS*/
    public static ScrollPane getKHMScreen(Stage primaryStage){
    
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
        String ApplicationName = "KH Model 1 Cable Synthesis";
        Label label = new Label(ApplicationName);
        label.setId("LabelScreen");
        label.setAlignment(Pos.CENTER);

        /*CREATE HELP LABELS PREDEFINED*/
        Label labelPred = new Label("Select the type of cable to generate the curves: ");
        labelPred.setId("HelpLabel");
        labelPred.setAlignment(Pos.CENTER);
                
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

        /*CREATE THE INPUTS CONFIG*/
        JFXComboBox<Label> frequency = new JFXComboBox<Label>();
        frequency.getItems().add(new Label("2.2MHz - 106MHz"));
        frequency.getItems().add(new Label("2.2MHz - 212MHz"));
        frequency.getItems().add(new Label("2.2MHz - 424MHz"));
        frequency.getItems().add(new Label("2.2MHz - 848MHz"));
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
         
            	/*GET THE FILE*/
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setTitle("Open a Parameter File");

            	File file = fileChooser.showOpenDialog(primaryStage);

            	if(file != null) {

            		primaryStage.getScene().setRoot(KHM1Screen.getInputFileWindow(primaryStage, file));
            		String css = KHM1Screen.class.getResource("InputFileWindow.css").toExternalForm(); 
                	primaryStage.getScene().getStylesheets().clear();
                	primaryStage.getScene().getStylesheets().add(css);

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
                Vector<Double> k1_value = new Vector<Double>();
                Vector<Double> k2_value = new Vector<Double>();
                Vector<Double> k3_value = new Vector<Double>();
                Vector<Double> h1_value = new Vector<Double>();
                Vector<Double> h2_value = new Vector<Double>();
                double minF;
                double maxF;
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
                	
                    k1_value.add(Double.parseDouble(k1.getText()));
                    k2_value.add(Double.parseDouble(k2.getText()));
                    k3_value.add(Double.parseDouble(k3.getText()));
                    h1_value.add(Double.parseDouble(h1.getText()));
                    h2_value.add(Double.parseDouble(h2.getText()));
                    cableLength_value = Double.parseDouble(cableLength.getText());
                    minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                    maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
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
                KHM1Controller.generateGraphs(headings, k1_value, k2_value, k3_value, h1_value, h2_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter);
                
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
            		
                	double k1_value;
                    double k2_value;
                    double k3_value;
                    double h1_value;
                    double h2_value;
                    double minF;
                    double maxF;
                    double cableLength_value;
                    String axisScale;
                    String parameter;
                	
                	try{
                        k1_value = Double.parseDouble(k1.getText());
                        k2_value = Double.parseDouble(k2.getText());
                        k3_value = Double.parseDouble(k3.getText());
                        h1_value = Double.parseDouble(h1.getText());
                        h2_value = Double.parseDouble(h2.getText());
                        cableLength_value = Double.parseDouble(cableLength.getText());
                        minF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[0]) * 1e6;
                        maxF = Double.parseDouble(frequency.getValue().getText().replace("MHz", "").split(" - ")[1]) * 1e6;
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
                    
                    try {
						KHM1Controller.generateOutputFile(k1_value, k2_value, k3_value, h1_value, h2_value, cableLength_value, minF, maxF, 51.75e3, axisScale, parameter, selectedDirectory);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
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
            	primaryStage.getScene().setRoot(CableSynthesisController.getCableSynthesisScene(primaryStage));
            	String css = CableSynthesisController.class.getResource("CableSynthesisScreen.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
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
            	primaryStage.getScene().setRoot(KHM1Screen.getHelpKHM1(primaryStage));
            	((ScrollPane)primaryStage.getScene().getRoot()).setVvalue(0);
            	String css = KHM1Screen.class.getResource("KHM1Help.css").toExternalForm(); 
            	primaryStage.getScene().getStylesheets().clear();
            	primaryStage.getScene().getStylesheets().add(css);

            }
        });
        
        /*ADDING ALL ELEMENTS TO GRID*/
        
        /*GENERATE FIRST LINE*/
        grid.add(label, 0, 0, 3, 1);
        GridPane.setHalignment(label, HPos.CENTER);
        
		/*LINE SECOND LINE*/
		grid.add(labelPred, 0, 1, 3, 1);
		GridPane.setHalignment(labelPred, HPos.CENTER);
		GridPane.setValignment(labelPred, VPos.CENTER);
		
		/*LINE THIRD LINE*/
		cableTypes.setMaxWidth(Double.MAX_VALUE);
		grid.add(cableTypes, 0, 2, 1, 1);
		GridPane.setHalignment(cableTypes, HPos.CENTER);
		GridPane.setValignment(cableTypes, VPos.CENTER);		
		descriptionCable.setMaxWidth(Double.MAX_VALUE);
		grid.add(descriptionCable, 1, 2, 2, 1);
		GridPane.setHalignment(descriptionCable, HPos.CENTER);
		GridPane.setValignment(descriptionCable, VPos.CENTER);		
		
        /*GENERATE FOURTH LINE*/
		k1.setMaxWidth(Double.MAX_VALUE);
        grid.add(k1, 0, 3, 1, 1);
        GridPane.setHalignment(k1, HPos.CENTER);
        GridPane.setValignment(k1, VPos.CENTER);
		k2.setMaxWidth(Double.MAX_VALUE);
        grid.add(k2, 1, 3, 1, 1);
        GridPane.setHalignment(k2, HPos.CENTER);
        GridPane.setValignment(k2, VPos.CENTER);
		k3.setMaxWidth(Double.MAX_VALUE);
        grid.add(k3, 2, 3, 1, 1);
        GridPane.setHalignment(k3, HPos.CENTER);
        GridPane.setValignment(k3, VPos.CENTER);
        
        /*GENERATE FIFTH LINE*/
		h1.setMaxWidth(Double.MAX_VALUE);
        grid.add(h1, 0, 4, 1, 1);
        GridPane.setHalignment(h1, HPos.CENTER);
        GridPane.setValignment(h1, VPos.CENTER);
		h2.setMaxWidth(Double.MAX_VALUE);
        grid.add(h2, 1, 4, 1, 1);
        GridPane.setHalignment(h2, HPos.CENTER);
        GridPane.setValignment(h2, VPos.CENTER);
		cableLength.setMaxWidth(Double.MAX_VALUE);
        grid.add(cableLength, 2, 4, 1, 1);
        GridPane.setHalignment(cableLength, HPos.CENTER);
        GridPane.setValignment(cableLength, VPos.CENTER);
        
        /*GENERATE SIXTH LINE*/
        frequency.setMaxWidth(Double.MAX_VALUE);
        grid.add(frequency, 0, 5, 1, 1);
        GridPane.setHalignment(frequency, HPos.CENTER);
        GridPane.setValignment(frequency, VPos.CENTER);
        scale.setMaxWidth(Double.MAX_VALUE);
        grid.add(scale, 1, 5, 1, 1);
        GridPane.setHalignment(scale, HPos.CENTER);
        GridPane.setValignment(scale, VPos.CENTER);
        parameterCalc.setMaxWidth(Double.MAX_VALUE);
        grid.add(parameterCalc, 2, 5, 1, 1);
        GridPane.setHalignment(parameterCalc, HPos.CENTER);
        GridPane.setValignment(parameterCalc, VPos.CENTER);
		
        /*GENERATE SEVENTH LINE*/
        fileInput.setMaxWidth(Double.MAX_VALUE);
        grid.add(fileInput, 0, 6, 1, 1);
        GridPane.setHalignment(fileInput, HPos.CENTER);
        GridPane.setValignment(fileInput, VPos.CENTER);
        calculate.setMaxWidth(Double.MAX_VALUE);
        grid.add(calculate, 1, 6, 1, 1);
        GridPane.setHalignment(calculate, HPos.CENTER);
        GridPane.setValignment(calculate, VPos.CENTER);
        outputFile.setMaxWidth(Double.MAX_VALUE);
        grid.add(outputFile, 2, 6, 1, 1);
        GridPane.setHalignment(outputFile, HPos.CENTER);
        GridPane.setValignment(outputFile, VPos.CENTER);

        /*GENERATE EIGHT LINE*/
        help.setMaxWidth(Double.MAX_VALUE);
		grid.add(help, 0, 7, 1, 1);
		GridPane.setHalignment(help, HPos.CENTER);
		GridPane.setValignment(help, VPos.CENTER);
        back.setMaxWidth(Double.MAX_VALUE);
        grid.add(back, 1, 7, 1, 1);
        GridPane.setHalignment(back, HPos.CENTER);
        GridPane.setValignment(back, VPos.CENTER);
        grid.setAlignment(Pos.CENTER);
        
        /*CREATE SCENE*/
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
                
        return scrollPane;
    
    }


}
