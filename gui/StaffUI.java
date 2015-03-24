package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import model.SelectTableModel;

import javax.swing.JFormattedTextField;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Simple GUI for the HospitalDatabase Staff View
 * @author devinli
 *
 */
@SuppressWarnings("serial")
public class StaffUI extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StaffUI frame = new StaffUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	public StaffUI() {
		super("HospitalDatabase Staff View");
		initGUI();
	}
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 400);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmJoin = new JMenuItem("Join");
		menuBar.add(mntmJoin);
		
		JMenuItem mntmDivision = new JMenuItem("Division");
		menuBar.add(mntmDivision);
		
		JMenuItem mntmAggregation = new JMenuItem("Aggregation");
		menuBar.add(mntmAggregation);
		
		JMenuItem mntmGroup = new JMenuItem("Group");
		menuBar.add(mntmGroup);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		String[] controlOptions = {"Action...",
				"Add","Delete","Project","Update"};

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		scrollPane.setViewportView(table);


		JPanel controlPanel = new JPanel();
		contentPane.add(controlPanel, BorderLayout.WEST);

		String[] tables = new String[] {"Patients", "Doctors"};
		JList list = new JList(tables);
		list.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Get the JList model
				JList model = (JList) e.getSource();
				int index = model.getSelectedIndex();
				// index < 0 means there is no selected item in list
				if (index < 0) {
					String[] col = {};
					String[][] data = {{}};
					table.setModel(new SelectTableModel(col,data));
				}
				String tableName = (String) model.getSelectedValue();
				table.setModel(new SelectTableModel(tableName));
			}
		});
		controlPanel.setLayout(new BorderLayout(0, 0));
		controlPanel.add(list,BorderLayout.CENTER);
		JComboBox comboBox = new JComboBox(controlOptions);
		comboBox.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		comboBox.setToolTipText("Choose an action to perform.");
		controlPanel.add(comboBox, BorderLayout.NORTH);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String cmd = (String) cb.getSelectedItem();
				cb.setSelectedItem((Object) "Action...");
				String message, title;
				switch (cmd) {
				case "Add":
					//TODO
					message = "Please enter all attributes " 
							+ "separated by semicolon.";
					title = "Add tuple to database";
					break;
				case "Delete":
					//TODO
					message = "Please enter the primary key "
					+ "of the tuple to be deleted.";
					title = "Delete tuple from database";
					break;
				case "Project":
					//TODO
					message = "Please enter the attributes to project on.";
					title = "Project table";
					break;
				case "Update":
					//TODO
					message = "Please enter all attributes"
							+ "separated by semicolon.";
					title = "Update";
					break;
				default:
					return;
				}

				String response = (String) JOptionPane.showInputDialog(cb, 
						message, title, JOptionPane.QUESTION_MESSAGE);
				if (response != null)
					System.out.println(response);
			}
		});
	}

}
