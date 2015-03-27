package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import database.DatabaseConnection;
import jwizardcomponent.CancelAction;
import jwizardcomponent.JWizardPanel;
import jwizardcomponent.Utilities;
import jwizardcomponent.frame.JWizardFrame;

/**
 * Main frame for the GUI
 * The steps in the wizard will be represented as panels which can be switched 
 * using the Back and Next buttons. 
 * 
 * @author devinli
 *
 */
@SuppressWarnings("serial")
public class DynamicWizardGUI extends JWizardFrame {

	public static final int PANEL_LOGIN = 0;
	public static final int PANEL_PATIENT_COST = 1;
	public static final int PANEL_DOCTOR_SPECIALTY = 2;
	public static final int PANEL_MENU = 3;
	public static final int PANEL_FAILED_LOGIN = 4;
	public static final int PANEL_MOD_VIEW = 5;
	public static final int PANEL_RUN_SQL = 6;
	public static final int PANEL_TABLE_OPTIONS = 7;
	public static final int PANEL_VIEW_OPTIONS = 8;
	public static final int PANEL_SHOW_TABLE = 9;
	public static final int PANEL_PROJECTION = 10;

	private DatabaseConnection db;

	public DynamicWizardGUI() {
		super();
		iniGUI();
	}

	private void iniGUI() {
		setTitle("Hospital Database Application");

		try {
			db = DatabaseConnection.getInstance();
		} catch (SQLException e3) {
			// No oracle SQL driver found!
			System.err.print(e3.getMessage());
			System.exit(-1);
		}
		JWizardPanel panel = null;
		panel = new PanelLogIn(getWizardComponents(), db);
		getWizardComponents().addWizardPanel(PANEL_LOGIN, panel);

		panel = new PanelPatientCost(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_PATIENT_COST, panel);

		panel = new PanelDoctorSpecialty(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_DOCTOR_SPECIALTY, panel);

		panel = new Panel1Menu(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_MENU, panel);

		panel = new PanelFailedLogIn(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_FAILED_LOGIN, panel);

		panel = new Panel5ModViewTable(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_MOD_VIEW, panel);

		panel = new Panel6RunSQL(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_RUN_SQL, panel);

		panel = new PanelTableOptions(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_TABLE_OPTIONS, panel);

		panel = new PanelViewOptions(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_VIEW_OPTIONS, panel);

		panel = new PanelShowTable(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_SHOW_TABLE, panel);

		panel = new PanelStub(getWizardComponents());
		getWizardComponents().addWizardPanel(PANEL_PROJECTION, panel);

		setSize(800, 600);
		Utilities.centerComponentOnScreen(this);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					db.quit();
				} catch (SQLException e1) {
					WizardGUIPanel currentPanel;
					try {
						currentPanel = (WizardGUIPanel) 
								getWizardComponents().getCurrentPanel();
						currentPanel.getConsoleLog()
						.append("\n" + e1.getMessage().trim());
					} catch (Exception e2) {

					}	
				}
				System.exit(0);
			}
		});
	}

	@Override
	protected CancelAction createCancelAction() {
		return new CancelAction(getWizardComponents()) {
			@Override
			public void performAction() {
				dispose();
				try {
					db.quit();
				} catch (SQLException e) {

				}
				System.exit(0);
			}
		};
	};
}
