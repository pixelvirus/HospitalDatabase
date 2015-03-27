package gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

@SuppressWarnings("serial")
public class PanelStub extends JWizardPanel {
	private final JLabel label = new JLabel("Stub for unimplemented panels");

	public PanelStub(JWizardComponents wizardComponents) {
		super(wizardComponents, "Stub Panel");
		initGUI();
	}

	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(label, BorderLayout.CENTER);	
	}
	
	@Override
	public void update() {
		getWizardComponents().getFinishButton().setText("");
		getWizardComponents().getFinishButton().setEnabled(false);
		getWizardComponents().getCancelButton().setText("Quit");
		
		getWizardComponents().getBackButton().setEnabled(true);
		getWizardComponents().getBackButton().setText("Main Menu");
		getWizardComponents().getNextButton().setEnabled(false);
	}
	
	@Override
	public void back() {
		switchPanel(DynamicWizardGUI.PANEL_MENU);
	}
}
