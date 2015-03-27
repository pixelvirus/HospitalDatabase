package gui;

import java.sql.SQLException;

import jwizardcomponent.JWizardComponents;

import javax.swing.JList;
import javax.swing.AbstractListModel;

import database.DatabaseConnection;

@SuppressWarnings("serial")
public class PanelViewOptions extends WizardGUIPanel {
	@SuppressWarnings("rawtypes")
	private final JList list = new JList();
	private String tableName = "";

	public PanelViewOptions(JWizardComponents wizardComponents) {
		super(wizardComponents, "Please choose an option:");
		initGUI();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initGUI() {
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {
					"1.  Enter Projection and/or Selection criteria", 
			"2.  View entire table"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(0);

		getSplitPane().setRightComponent(list);
	}

	/**
	 * @return Integer representing the option not the index.
	 */
	private int getListSelection() { 
		return list.getSelectedIndex() + 1;
	}

	void setTableName(String name) {
		tableName = name;
	}

	String getTableName() {
		return tableName;
	}

	@Override
	public void next() {
		switch (getListSelection()) {
		case 1:
			PanelProjection projection =(PanelProjection)  getWizardComponents()
			.getWizardPanel(DynamicWizardGUI.PANEL_PROJECTION);
			projection.setTableName(tableName);

			switchPanel(DynamicWizardGUI.PANEL_PROJECTION);
			break;
		case 2:
			PanelShowTable showTable = (PanelShowTable) getWizardComponents()
			.getWizardPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);

			showTable.setTableName(tableName);

			try {
				DatabaseConnection.getInstance().addSelectAllToQuery();
				switchPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);
			} catch (SQLException e) {
				this.getConsoleLog().append(e.getMessage());
			}
			break;
		default:
			this.getConsoleLog().append("error: not a valid option\n");
		}
	}

	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_TABLE_OPTIONS);
	}
}
