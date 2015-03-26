package main;

import java.awt.EventQueue;

import gui.DynamicWizardGUI;


public class GuiEntryPoint {

	public static void main(String[] args) {

		// Use dispatch thread to start GUI
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// Main frame for the GUI, each step will be a panel
					DynamicWizardGUI frame = new DynamicWizardGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
