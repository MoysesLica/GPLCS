package tables;

import java.util.Arrays;
import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class Table {

	public static TableView generateTable(Vector columnsHeadings, Vector subHeadings, String data[][]) {
		
        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        TableColumn<String[],String> col1 = new TableColumn();    
        col1.setText(columnsHeadings.get(0).toString());
        col1.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
	         String[] x = p.getValue();
	         if (x != null && x.length > 0) {
	        	 return new SimpleStringProperty(String.format("%e", Double.parseDouble(x[0].toString())));
	         } else {
	          return new SimpleStringProperty("<no name>");
	         }
	        }
	       });
        
        table.getColumns().add(col1);        

        for(int i = 1; i < columnsHeadings.size(); i++) {

        	Vector actualI = new Vector();
    		actualI.add(i);
        	
            TableColumn<String[],String> col = new TableColumn();        	
            col.setText(columnsHeadings.get(i).toString());
            col.setText(columnsHeadings.get(i).toString());
            
        	for(int j = 0; j < subHeadings.size(); j++) {
        		
            	Vector actualJ = new Vector();
        		actualJ.add(j);
        		
                TableColumn<String[],String> subCol = new TableColumn();        	
                subCol.setText(subHeadings.get(j).toString());
                subCol.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
        	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
        	         String[] x = p.getValue();
        	         if (x != null && x.length > (Integer.parseInt(actualJ.get(0).toString()) + 1) + ((Integer.parseInt(actualI.get(0).toString()) - 1) * subHeadings.size()) ) {
        	        	 return new SimpleStringProperty(String.format("%.20f", Double.parseDouble(x[(Integer.parseInt(actualJ.get(0).toString()) + 1) + ((Integer.parseInt(actualI.get(0).toString()) - 1) * subHeadings.size())].toString())));
        	         } else {
        	          return new SimpleStringProperty("<no name>");
        	         }
        	        }
        	       });

                subCol.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
                subCol.setResizable(true);

                col.getColumns().add(subCol);        
                
        	}

            table.getColumns().add(col);        

        }
                
        table.getItems().addAll(Arrays.asList(data));
		
		return table;
		
	}
	
	public static TableView generateTable(Vector columnsHeadings, String data[][]) {
		
        /*ADDING TABLE OF VALUES*/        
        TableView<String[]> table = new TableView();
        table.setEditable(false);

        for(int i = 0; i < columnsHeadings.size(); i++) {
        	
        	Vector actualI = new Vector();
        	actualI.add(i);
        		
        	/*CREATING THE COLUMNS OF TABLE*/
            TableColumn<String[],String> col = new TableColumn();
            col.setText(columnsHeadings.get(i).toString());
            
            /*CONFIG THE COLUMNS*/
            col.setCellValueFactory((Callback < CellDataFeatures < String[], String > , ObservableValue < String >> ) new Callback < TableColumn.CellDataFeatures < String[], String > , ObservableValue < String >> () {
    	        public ObservableValue < String > call(TableColumn.CellDataFeatures < String[], String > p) {
    	         String[] x = p.getValue();
    	         if (x != null && x.length > Integer.parseInt(actualI.get(0).toString())) {
    	        	 return new SimpleStringProperty(String.format("%.10f", Double.parseDouble(x[Integer.parseInt(actualI.get(0).toString())].toString())));
    	         } else {
    	          return new SimpleStringProperty("<no name>");
    	         }
    	        }
    	       });
            
            col.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
            col.setResizable(true);

            table.getColumns().add(col);        	
        	
        }

        /*ADDING INFORMATION TO COLUMNS*/
        table.getItems().addAll(Arrays.asList(data));

        return table;
		
	}
	
}
