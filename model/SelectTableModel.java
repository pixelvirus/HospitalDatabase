package model;

import javax.swing.table.AbstractTableModel;

/**
 * The model representing data obtained from a SELECT sql command.
 * @author devinli
 *
 */
@SuppressWarnings("serial")
public class SelectTableModel extends AbstractTableModel {
	private String[] columnNames;
	private String[][] data;

	public SelectTableModel(String[] columnNames, String[][] data) {
		super();
		this.columnNames = columnNames;
		this.data = data;
	}

	public SelectTableModel(String tableName, String command, String input) {
		switch (command) {
		case "Add":
			//TODO
		case "Delete":
			//TODO
		case "Project":
			//TODO
		case "Update":
			//TODO
		default:
			return;
		}
	}

	/**
	 * Fetch table data from database to display in table.
	 * @param tableName name of table as String
	 */
	public SelectTableModel(String tableName) {
		// TODO Replace stub with method calls to Hospital.java to do work.
		if (tableName.equalsIgnoreCase("Patients")) {
			String[] pColumn = {"PT_ID","PT_NAME","PHONE","ADDRESS"
					,"CITY","PROZIP","INSURANCE"};
			String[][] patients 
			= {{"10001","John Smith","778-895-0451","5959 Student Union Blvd", 
				"Vancouver","BC V6T-1K2","Blue Cross"},
				{"15602","Jane Doe","778-489-0235","33 Suncrest Dr",
					"Delta","BC V4C-2N1", "Great West"}};
			columnNames = pColumn;
			data = patients;
		} else {
			String[] dColumn = {"DO_ID","DO_NAME","SPECIALIZATION"};
			String[][] doctors = {{"469","DAVID_SONG", "cardiology"},
					{"231","Ben Byers","dermatology"}};
			columnNames = dColumn;
			data = doctors;
		}
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return (Object) data[rowIndex][columnIndex];
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

}
