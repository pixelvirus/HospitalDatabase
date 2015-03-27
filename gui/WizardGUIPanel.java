package gui;

import jwizardcomponent.JWizardComponents;
import jwizardcomponent.JWizardPanel;

import javax.swing.BoxLayout;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class WizardGUIPanel extends JWizardPanel {
	
	private final JSplitPane splitPane = new JSplitPane();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JSplitPane splitPaneErrorLog = new JSplitPane();
	private final JTextArea errorLog = new JTextArea();
	private final JLabel lblErrorLog = new JLabel("Error Log:");

	public WizardGUIPanel(JWizardComponents wizardComponents, String title) {
		super(wizardComponents, title);
		initGUI();
	}
	private void initGUI() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		add(splitPane);
		splitPane.setDividerLocation(80);
		splitPane.setLeftComponent(splitPaneErrorLog);

		splitPaneErrorLog.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

		splitPaneErrorLog.setRightComponent(scrollPane);
		errorLog.setEditable(false);

		scrollPane.setViewportView(errorLog);

		splitPaneErrorLog.setLeftComponent(lblErrorLog);
		
	}

	public JSplitPane getSplitPane() {
		return splitPane;
	}
	public JTextArea getConsoleLog() {
		return errorLog;
	}

	@Override
	public void update() {
		getWizardComponents().getFinishButton().setText("");
		getWizardComponents().getFinishButton().setEnabled(false);
		getWizardComponents().getCancelButton().setText("Quit");
		getWizardComponents().getBackButton().setText("Back");
		getWizardComponents().getNextButton().setText("Next");
	}
}
