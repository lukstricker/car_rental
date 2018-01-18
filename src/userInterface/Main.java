package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import userInterface.Edit.TableName;
import userInterface.Edit.Quadruple.TupleType;

public class Main {

	private JFrame frmCarrental;
	private JTextField textField;
	private JTable table;
	private static String[] tables = { "Addresses", "Bills", "Car_Brands", "Clients", "Damages", "Equipment",
			"Extra_Equipment", "Insurances", "Reservations", "Reservations_Damages", "Reservations_Extraequipment",
			"Vehicles", "Vehicles_Equipment" };
	private static JComboBox comboBox_1 = new JComboBox(tables);
	private Edit edit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmCarrental.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public Main() throws ClassNotFoundException, SQLException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private void initialize() throws ClassNotFoundException, SQLException {
		frmCarrental = new JFrame();
		frmCarrental.setTitle("Car_Rental");
		frmCarrental.setBounds(100, 100, 921, 623);
		frmCarrental.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCarrental.getContentPane().setLayout(new BoxLayout(frmCarrental.getContentPane(), BoxLayout.X_AXIS));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmCarrental.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Car_Rental", null, panel, null);

		JLabel lblTitle = new JLabel("CAR_RENTAL");
		lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 16));

		textField = new JTextField();
		textField.setColumns(10);

		JButton btnSearch = new JButton("SEARCH");

		JButton btnAdd = new JButton("ADD");

		JButton btnEdit = new JButton("EDIT");

		
		JComboBox comboBox = new JComboBox();
		JButton btnDelete = new JButton("DELETE");

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Utilities.delete();

			}

		});

		JScrollPane scrollPane = new JScrollPane();

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup()
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
								.addGroup(gl_panel.createSequentialGroup()
										.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 80,
												GroupLayout.PREFERRED_SIZE)
										.addGap(249)
										.addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 80,
												GroupLayout.PREFERRED_SIZE)
										.addGap(204).addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 80,
												GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup().addGap(411).addComponent(lblTitle))
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(btnSearch)))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(20).addComponent(lblTitle).addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch))
						.addGap(31)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnAdd)
								.addComponent(btnEdit).addComponent(btnDelete))
						.addGap(27)));
		comboBox_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] col = Utilities.getColumns();
				String[][] data = Utilities
						.loadAllData("Select * from " + String.valueOf(comboBox_1.getSelectedItem()));
				comboBox.removeAllItems();
				for (int i = 0; i < col.length; i++) {

					comboBox.addItem(col[i]);
				}
				scrollPane.setViewportView(createTable(col, data));

			}

		});

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String currentTable = String.valueOf(comboBox_1.getSelectedItem());
				System.out.println(currentTable);
				switch (currentTable) {
				case "Insurances":
					edit = new Edit(TableName.insurances, -1, 
							new Edit.Quadruple[] { new Edit.Quadruple("company_name", null, TupleType.TextField, true),
									new Edit.Quadruple("fee", null, TupleType.TextField, true) });
					break;
				case "Equipment":
					edit = new Edit(TableName.equipment, -1, new Edit.Quadruple[] {
							new Edit.Quadruple("description", null, TupleType.TextField, true) });
					break;
				case "Damages":
					edit = new Edit(TableName.damages, -1,
							new Edit.Quadruple[] { new Edit.Quadruple("description", null, TupleType.TextField, true),
									new Edit.Quadruple("position_part", null, TupleType.TextField, true) });
					break;
				case "Vehicles":
					edit = new Edit(TableName.car_brands, -1, new Edit.Quadruple[] {
							new Edit.Quadruple("company_name", null, TupleType.TextField, true) });
					break;
				case "Car_Brands":
					edit = new Edit(TableName.vehicles, -1,
							new Edit.Quadruple[] { new Edit.Quadruple("license_plate", null, TupleType.TextField, true),
									new Edit.Quadruple("initial_registration", null, TupleType.dateTime, true),
									new Edit.Quadruple("price_class", null, TupleType.TextField, true),
									new Edit.Quadruple("capacity", null, TupleType.TextField, true),
									new Edit.Quadruple("price_day", null, TupleType.TextField, true),
									new Edit.Quadruple("price_km", null, TupleType.TextField, true),
									new Edit.Quadruple("model", null, TupleType.TextField, true),
									new Edit.Quadruple("brand_id", null, TupleType.TextField, true),
									new Edit.Quadruple("insurance_id", null, TupleType.TextField, true) });
					break;
				case "Extra_Equipment":
					edit = new Edit(TableName.extra_equipment, -1,
							new Edit.Quadruple[] { new Edit.Quadruple("description", null, TupleType.TextField, true),
									new Edit.Quadruple("price", null, TupleType.TextField, true),
									new Edit.Quadruple("total_quantity", null, TupleType.TextField, true) });
					break;
				}

				// edit.addWindowListener(new WindowListener() {
				//
				// @Override
				// public void windowOpened(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void windowIconified(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void windowDeiconified(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void windowDeactivated(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void windowClosing(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				//
				// @Override
				// public void windowClosed(WindowEvent arg0) {
				// String vorname = ((JTextField)edit.result.get("Vorname")).getText();
				// System.out.print(vorname);
				// }
				//
				// @Override
				// public void windowActivated(WindowEvent arg0) {
				// // TODO Auto-generated method stub
				//
				// }
				// });

			}

		});
		
		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				

			}

		});

		panel.setLayout(gl_panel);

	}

	// creates the selected table table
	private JTable createTable(String[] col, String[][] data) {

		table = new JTable(data, col);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return table;
	}

	public static JComboBox getComboBox_1() {
		return comboBox_1;
	}

}
