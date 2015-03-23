package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import model.SelectTableModel;

/**
 * Simple GUI for the Hospital Staff View
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
	@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
	public StaffUI() {
		super("Hospital Staff View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// TODO implement actions for the buttons
		JButton btnAdd = new JButton("Add");
		panel.add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		panel.add(btnDelete);
		
		JButton btnUpdate = new JButton("Update");
		panel.add(btnUpdate);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JList list = new JList();
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
		list.setModel(new AbstractListModel() {
			// Populate the list with the name of the tables.
			String[] values = new String[] {"Patients", "Doctors"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		contentPane.add(list, BorderLayout.WEST);
	}

}
