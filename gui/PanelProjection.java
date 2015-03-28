package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DatabaseConnection;
import jwizardcomponent.JWizardComponents;

@SuppressWarnings("serial")
public class PanelProjection extends WizardGUIPanel {

	private final JPanel panel = new JPanel();

	private final JLabel label 
	= new JLabel("Please enter values to perform projection");

	private final JFormattedTextField input = new JFormattedTextField();

	private String tableName = "";

	public PanelProjection(JWizardComponents wizardComponents) {
		super(wizardComponents, "Project table");
		initGUI();
	}

	private void initGUI() {
		getSplitPane().setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{446, 0};
		gbl_panel.rowHeights = new int[]{16, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 
				Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		GridBagConstraints gbc_lblSQL = new GridBagConstraints();
		gbc_lblSQL.insets = new Insets(0, 0, 5, 0);
		gbc_lblSQL.anchor = GridBagConstraints.NORTH;
		gbc_lblSQL.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblSQL.gridx = 0;
		gbc_lblSQL.gridy = 2;
		panel.add(label, gbc_lblSQL);

		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 4;
		panel.add(input, gbc_textField);

		input.setColumns(10);
		input.requestFocusInWindow();
	}

	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_VIEW_OPTIONS);
	}

	@Override
	public void next() {
		Object value = input.getText();
		String attributes = ((String) value).trim();
		// check the input are words spearated by commas.
		boolean legal = attributes.matches("^[a-zA-Z]+(,\\s*[a-zA-Z]+)*$");
		if (legal) {
			try {
				PanelShowTable showTable = (PanelShowTable) getWizardComponents()
						.getWizardPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);

				showTable.setTableName(tableName);

				DatabaseConnection.getInstance().projectTable(attributes);
				switchPanel(DynamicWizardGUI.PANEL_SHOW_TABLE);
			} catch (SQLException e) {
				this.getConsoleLog().append(e.getMessage());
			}
		} else {
			input.setText("");
			this.getConsoleLog()
			.append("Please only use case-insensitive words contain letter a-z"
					+" separated by comma\n.");
		}
	}

	void setTableName(String name) {
		tableName = name.trim();
	}

}
