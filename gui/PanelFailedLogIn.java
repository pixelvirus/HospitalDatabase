package gui;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PanelFailedLogIn extends JWizardPanel {
	private final JLabel lblIncorrectPasswordEntered = new JLabel("Incorrect password entered 3 times.");

	public PanelFailedLogIn(JWizardComponents wizardComponents) {
		super(wizardComponents, "Please restart the application");
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		lblIncorrectPasswordEntered.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(lblIncorrectPasswordEntered, BorderLayout.CENTER);
	}
	
	@Override
	public void update() {
		getWizardComponents().getFinishButton().setText("");
		getWizardComponents().getFinishButton().setEnabled(false);
		getWizardComponents().getCancelButton().setText("Quit");
		
		getWizardComponents().getBackButton().setEnabled(false);
		getWizardComponents().getNextButton().setEnabled(false);
	}
}
