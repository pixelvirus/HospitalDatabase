package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

import database.DatabaseConnection;
import jwizardcomponent.JWizardComponents;

@SuppressWarnings("serial")
public class PanelPatientCost extends WizardGUIPanel {
	private final JPanel panel = new JPanel();

	private final JLabel lblSQL 
	= new JLabel("Please enter 5-digit patient's ID number:");

	private final JFormattedTextField input 
	= new JFormattedTextField(createFormatter("#####"));
	
	private static final String DEFAULT_INPUT = "00000";

	public PanelPatientCost(JWizardComponents wizardComponents) {
		super(wizardComponents, "Generate bill for patient");
		initGUI();
	}

	private void initGUI() {

		getSplitPane().setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{446, 0};
		gbl_panel.rowHeights = new int[]{16, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		GridBagConstraints gbc_lblSQL = new GridBagConstraints();
		gbc_lblSQL.insets = new Insets(0, 0, 5, 0);
		gbc_lblSQL.anchor = GridBagConstraints.NORTH;
		gbc_lblSQL.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSQL.gridx = 0;
		gbc_lblSQL.gridy = 2;
		panel.add(lblSQL, gbc_lblSQL);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 4;
		panel.add(input, gbc_textField);
		
		input.setColumns(10);
		input.setValue(DEFAULT_INPUT);
		input.requestFocusInWindow();
	}

	private MaskFormatter createFormatter(String s) {
		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter(s);
		} catch (ParseException e) {
			// do nothing
		}

		if (formatter == null)
			formatter = new MaskFormatter();
		return formatter;
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MENU);
	}
	
	@Override
	public void next() {
		Object value = input.getValue();
		int pid = Integer.parseInt(((String) value));
		DatabaseConnection.getInstance().billPatient(pid);
		switchPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);	
	}
}
