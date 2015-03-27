package gui;

import jwizardcomponent.JWizardComponents;

import javax.swing.JList;
import javax.swing.AbstractListModel;

import database.DatabaseConnection;

@SuppressWarnings("serial")
public class PanelViewOptions extends WizardGUIPanel {
	private final JList list = new JList();
	private String tableName = "";

	public PanelViewOptions(JWizardComponents wizardComponents) {
		super(wizardComponents, "Please choose an option:");
		initGUI();
	}

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
			switchPanel(DynamicWizardGUI.PANEL_PROJECTION);
			break;
		case 2:
			PanelShowTable showTable = (PanelShowTable) getWizardComponents()
			.getWizardPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);

			showTable.setTableName(tableName);

			DatabaseConnection.getInstance().addSelectAllToQuery();
			switchPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);
			break;
		default:
			System.out.println("error: not a valid option");
		}
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_TABLE_OPTIONS);
	}
}
