package gui;

import jwizardcomponent.JWizardComponents;

import javax.swing.JList;
import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class PanelTableOptions extends WizardGUIPanel {
	private String tableName;

	@SuppressWarnings("rawtypes")
	private final JList list = new JList();


	public PanelTableOptions(JWizardComponents wizardComponents) {
		super(wizardComponents, "Please choose an option");
		initGUI();
	}
	private void initGUI() {
		setTableName("");

		getSplitPane().setRightComponent(list);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void setTableName(String name) {
		tableName = name;
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {
					"1.  Insert " + tableName, 
					"2.  Delete " + tableName, 
					"3.  Update " + tableName, 
					"4.  Show " + tableName};

			@Override
			public int getSize() {
				return values.length;
			}
			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(3);
	}

	/**
	 * @return Integer representing the option not the index.
	 */
	private int getListSelection() { 
		return list.getSelectedIndex() + 1;
	}

	@Override
	public void next() {
		switch (getListSelection()) {
		case 1:
			this.getConsoleLog().append("warning: unimplemented function\n");
			break;
		case 2:
			this.getConsoleLog().append("warning: unimplemented function\n");
			break;
		case 3:
			this.getConsoleLog().append("warning: unimplemented function\n");
			break;
		case 4:
			PanelViewOptions viewOptions 
			= (PanelViewOptions) getWizardComponents()
			.getWizardPanel(DynamicWizardGUI.PANEL_VIEW_OPTIONS);
			viewOptions.setTableName(tableName);
			switchPanel(DynamicWizardGUI.PANEL_VIEW_OPTIONS);
			break;
		default:
			this.getConsoleLog().append("error: not a valid option\n");
		}
	}

	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MOD_VIEW);
	}
}
