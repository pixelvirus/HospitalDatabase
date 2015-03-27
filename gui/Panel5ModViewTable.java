package gui;

import jwizardcomponent.JWizardComponents;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;

import database.DatabaseConnection;

@SuppressWarnings("serial")
public class Panel5ModViewTable extends WizardGUIPanel {
	private final JScrollPane scrollPane = new JScrollPane();
	private final JList list = new JList();

	public Panel5ModViewTable(JWizardComponents wizardComponents) {
		super(wizardComponents, "Modify or view an existing table");
		initGUI();
	}

	private void initGUI() {

		getSplitPane().setRightComponent(scrollPane);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"1.  Patients", "2.  Doctors", 
					"3.  Bookings", "4.  OperatingRooms", 
					"5.  BookingReservesOperatingRooms", "6.  RecoveryRooms", 
					"7.  RecoveryRoomBeds", "8.  BookingReservesRecoveryRooms", 
					"9.  Procedures", "10.  BookingForProcedures", 
					"11.  AdmittedTo", "12.  Medications", "13.  Prescribes", 
					"14.  Performs", "15.  Offices", "16.  HasAOffice", 
					"17.  StaffUsers", "18.  PatientUsers", 
					"19.  SUserHasA", "20.  PUserHasA"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(0);

		scrollPane.setViewportView(list);
	}

	/**
	 * @return Integer representing the option not the index.
	 */
	private int getListSelection() { 
		return list.getSelectedIndex() + 1;
	}

	@Override
	public void next() {
		PanelTableOptions tableOptions = (PanelTableOptions)
				getWizardComponents()
				.getWizardPanel(DynamicWizardGUI.PANEL_TABLE_OPTIONS);
		
		String selection = (String) list.getSelectedValue();
		String[] names = selection.trim().split("\\s+");
		tableOptions.setTableName(names[1]);
		
		DatabaseConnection.getInstance().addTableNameToQuery(names[1]);
		switchPanel(DynamicWizardGUI.PANEL_TABLE_OPTIONS);
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MENU);
	}
}
