package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import database.DatabaseConnection;
import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

@SuppressWarnings("serial")
public class PanelLogIn extends WizardGUIPanel implements ActionListener {

	private DatabaseConnection db;
	private int loginAttempts = 0;

	private final JLabel usernameLabel = new JLabel("Enter username: ");
	private final JLabel passwordLabel = new JLabel("Enter password: ");

	private final JTextField usernameField = new JTextField(10);
	private final JPasswordField passwordField = new JPasswordField(10);

	private final JButton loginButton = new JButton("Log In");
	private final JPanel contentPane = new JPanel();

	public PanelLogIn(JWizardComponents wizardComponents, 
			DatabaseConnection db) {
		super(wizardComponents, "User Login");
		this.db = db;

		iniGUI();
	}

	private void iniGUI() {
		passwordField.setEchoChar('*');

		// register password field and OK button with action event handler
		passwordField.addActionListener(this);
		loginButton.addActionListener(this);

		getSplitPane().setRightComponent(contentPane);

		// layout components using the GridBag layout manager
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the username label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		contentPane.add(usernameLabel);

		// place the text field for the username
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		contentPane.add(usernameField);

		// place password label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		contentPane.add(passwordLabel);

		// place the password field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);

		// place the login button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		contentPane.add(loginButton);
	}

	@Override
	public void update() {
		getWizardComponents().getFinishButton().setText("");
		getWizardComponents().getFinishButton().setEnabled(false);
		getWizardComponents().getCancelButton().setText("Quit");

		getWizardComponents().getBackButton().setEnabled(false);
		getWizardComponents().getNextButton().setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		boolean status;
		try {
			status = db.connect(usernameField.getText(), 
					String.valueOf(passwordField.getPassword()));

			if (status)
				switchPanel(DynamicWizardGUI.PANEL_MENU);

		} catch (SQLException e1) {
			loginAttempts++;
			if (loginAttempts >= 3) {
				switchPanel(DynamicWizardGUI.PANEL_FAILED_LOGIN);
			} else {
				// clear the password
				passwordField.setText("");
			}
			this.getConsoleLog().append(e1.getMessage());
		}
	}

}
