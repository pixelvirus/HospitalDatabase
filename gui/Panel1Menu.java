package gui;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import java.awt.BorderLayout;

import javax.swing.JList;
import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class Panel1Menu extends WizardGUIPanel {
	private final JList list = new JList();

	public Panel1Menu(JWizardComponents wizardComponents) {
		super(wizardComponents, "Please choose an option:");
		initGUI();
	}
	private void initGUI() {
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {
					"1.  Output total costs owing for a patient.", 
					"2.  Find doctors with a given specialty.", 
					"3.  Find average cost of commonly prescribed medications.", 
					"4.  Find doctors who have done procedures in every operating room.", 
					"5.  Modify or view an existing table", 
					"6.  Run sql directly (e.g. create or drop table)"};
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

	@Override
	public void next() {
		switch (getListSelection()) {
		case 1:
			switchPanel(DynamicWizardGUI.PANEL_PATIENT_COST);
			break;
		case 2:
			switchPanel(DynamicWizardGUI.PANEL_DOCTOR_SPECIALTY);
			break;
		case 3:
			switchPanel(DynamicWizardGUI.PANEL_AVG_COST_MED);
			break;
		case 4:
			switchPanel(DynamicWizardGUI.PANEL_DOCTOR_ALL_OP);
			break;
		case 5:
			switchPanel(DynamicWizardGUI.PANEL_MOD_VIEW);
			break;
		case 6:
			switchPanel(DynamicWizardGUI.PANEL_RUN_SQL);
			break;
		default:
			System.out.println("error: Unknown option in menu!");
		}
	}
	
	@Override
	public void update() {
		super.update();
		getWizardComponents().getBackButton().setText("Back");
		setBackButtonEnabled(false);
	}

}
