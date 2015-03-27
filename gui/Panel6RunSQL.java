package gui;

import jwizardcomponent.JWizardComponents;

import javax.swing.JPanel;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import database.DatabaseConnection;

import java.sql.SQLException;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class Panel6RunSQL extends WizardGUIPanel {
	private final JPanel panel = new JPanel();
	private final JLabel lblSQL = new JLabel("Please enter the SQL command in the text field below:");
	private final JTextArea input = new JTextArea();

	public Panel6RunSQL(JWizardComponents wizardComponents) {
		super(wizardComponents, "Run sql directly (e.g. create or drop table)");
		input.setColumns(10);
		initGUI();
	}

	private void initGUI() {
		this.getSplitPane().setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		panel.add(lblSQL, BorderLayout.NORTH);
		panel.add(input, BorderLayout.CENTER);
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
