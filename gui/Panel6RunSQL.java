package gui;

import jwizardcomponent.JWizardComponents;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import database.DatabaseConnection;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class Panel6RunSQL extends WizardGUIPanel {
	private final JPanel panel = new JPanel();
	private final JLabel lblSQL = new JLabel("Please enter the SQL command in the text field below:");
	private final JTextField input = new JTextField();

	public Panel6RunSQL(JWizardComponents wizardComponents) {
		super(wizardComponents, "Run sql directly (e.g. create or drop table)");
		input.setColumns(10);
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
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MENU);
	}
	
	@Override
	public void next() {
		Object value = input.getText();
		String query = ((String) value).trim();
		try {
			DatabaseConnection.getInstance().shipSQLtoOracle(query);
			switchPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);
		} catch (SQLException e) {
			this.getConsoleLog().append(e.getMessage());
		}
	}
}
