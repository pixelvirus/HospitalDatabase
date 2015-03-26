package gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import jwizardcomponent.JWizardComponents;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import database.DatabaseConnection;
import model.ListTableModel;

@SuppressWarnings("serial")
public class PanelShowTable extends WizardGUIPanel implements Observer {
	private final JScrollPane scrollPane = new JScrollPane();
	private final JTable table = new JTable();
	private String tableName = "";

	public PanelShowTable(JWizardComponents wizardComponents) {
		super(wizardComponents, "Table contents");
		initGUI();
	}
	private void initGUI() {
		
		getSplitPane().setRightComponent(scrollPane);
		
		scrollPane.setViewportView(table);
		
		DatabaseConnection.getInstance().addObserver(this);
	}
	
	void setTableName(String name) {
		tableName = name;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		try {
			ListTableModel tableModel = ListTableModel
					.createModelFromResultSet((ResultSet) arg);
			table.setModel(tableModel);
		} catch (SQLException e) {
			this.getConsoleLog().append("\n" + e.getMessage().trim());
		}
		
	}
	
	@Override
	public void update() {
		getWizardComponents().getNextButton().setEnabled(false);
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MOD_VIEW);
	}

}
