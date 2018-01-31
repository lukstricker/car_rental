package userInterface;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
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
import java.awt.event.WindowStateListener;
import java.sql.SQLException;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;
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
	private DefaultTableModel tableModel = new DefaultTableModel();

	private static Edit.TableName[] tables = { TableName.Addresses, TableName.Bills, TableName.Car_Brands,
			TableName.Clients, TableName.Damages, TableName.Equipment, TableName.Extra_Equipment, TableName.Insurances,
			TableName.Reservations, TableName.Vehicles, TableName.Reservations_Damages,
			TableName.Reservations_Extraequipment, TableName.Vehicles_Equipment };

	private JComboBox comboBox_Table = new JComboBox(tables);
	private JComboBox comboBox_Search = new JComboBox();

	private JButton btnAdd, btnEdit, btnDelete;

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
		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String text = textField.getText();
				if (text != null && text.trim().length() > 0) {
					setTableData(text);
				} else
					setTableData(null);

			}
		});

		btnAdd = new JButton("ADD");

		btnEdit = new JButton("EDIT");
		btnEdit.setEnabled(false);

		btnDelete = new JButton("DELETE");
		btnDelete.setEnabled(false);

		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String currentTable = String.valueOf(comboBox_Table.getSelectedItem());
				int currentRow = table.getSelectedRow();
				int id = Integer.parseInt(table.getValueAt(currentRow, 0).toString());
				Utilities.delete(id, currentTable.toLowerCase());
			}
		});

		JScrollPane scrollPane = new JScrollPane();

		table = new JTable(this.tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(new java.awt.event.MouseAdapter() {

			/**
			 * Sets selected the row the user clicked on the tableProject.
			 */
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
			}
		});

		scrollPane.setViewportView(table);

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
								.addComponent(comboBox_Table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(comboBox_Search, GroupLayout.PREFERRED_SIZE, 121,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18).addComponent(btnSearch)))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(20).addComponent(lblTitle).addGap(18)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBox_Table, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBox_Search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch))
						.addGap(31)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 368, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnAdd)
								.addComponent(btnEdit).addComponent(btnDelete))
						.addGap(27)));
		comboBox_Table.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnEdit.setEnabled(false);
				btnDelete.setEnabled(false);
				setTableData(null);
			}

		});

		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Edit edit = null;
				String currentTable = String.valueOf(comboBox_Table.getSelectedItem());
				switch (currentTable) {
				case "Insurances":
					edit = new Edit(TableName.Insurances, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("insurance_name", null, TupleType.TextField, true, null),
									new Edit.Quadruple("fee", null, TupleType.TextFieldInt, true, null) });
					break;
				case "Clients":
					edit = new Edit(TableName.Clients, null, new Edit.Quadruple[] {
							new Edit.Quadruple("first_name", null, TupleType.TextField, true, null),
							new Edit.Quadruple("last_name", null, TupleType.TextField, true, null),
							new Edit.Quadruple("phone", null, TupleType.TextFieldInt, true, null),
							new Edit.Quadruple("driving_license_number", null, TupleType.TextFieldInt, true, null),
							new Edit.Quadruple("addresses_id", null, TupleType.ComboBox, false, TableName.Addresses) });
					break;
				case "Equipment":
					edit = new Edit(TableName.Equipment, null, new Edit.Quadruple[] {
							new Edit.Quadruple("description", null, TupleType.TextField, true, null) });
					break;
				case "Damages":
					edit = new Edit(TableName.Damages, null, new Edit.Quadruple[] {
							new Edit.Quadruple("description", null, TupleType.TextField, true, null) });
					break;
				case "Car_Brands":
					edit = new Edit(TableName.Car_Brands, null, new Edit.Quadruple[] {
							new Edit.Quadruple("company_name", null, TupleType.TextField, true, null) });
					break;
				case "Vehicles":
					edit = new Edit(TableName.Vehicles, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("license_plate", null, TupleType.TextField, true, null),
									new Edit.Quadruple("initial_registration", null, TupleType.date, false, null),
									new Edit.Quadruple("price_class", null, TupleType.TextField, true, null),
									new Edit.Quadruple("capacity", null, TupleType.TextFieldInt, true, null),
									new Edit.Quadruple("price_day", null, TupleType.TextField, true, null),
									new Edit.Quadruple("price_km", null, TupleType.TextField, true, null),
									new Edit.Quadruple("model", null, TupleType.TextField, true, null),
									new Edit.Quadruple("company_name", null, TupleType.ComboBox, false,
											TableName.Car_Brands),
									new Edit.Quadruple("insurance_name", null, TupleType.ComboBox, true, TableName.Insurances) });
					break;
				case "Reservations":
					edit = new Edit(TableName.Reservations, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("date_starttime", null, TupleType.dateTime, true, null),
									new Edit.Quadruple("date_endtime", null, TupleType.dateTime, true, null),
									new Edit.Quadruple("km_at_start", null, TupleType.TextFieldInt, true, null),
									new Edit.Quadruple("km_at_return", null, TupleType.TextFieldInt, false, null),
									new Edit.Quadruple("license_plate", null, TupleType.ComboBox, true, TableName.Vehicles),
									new Edit.Quadruple("clients_id", null, TupleType.ComboBox, true, TableName.Clients),
									new Edit.Quadruple("bills_id", null, TupleType.ComboBox, true, TableName.Bills), });
					break;
				case "Extra_Equipment":
					edit = new Edit(TableName.Extra_Equipment, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", null, TupleType.TextField, true, null),
									new Edit.Quadruple("price", null, TupleType.TextField, true, null),
									new Edit.Quadruple("total_quantity", null, TupleType.TextField, true, null) });
					break;
				case "Bills":
					edit = new Edit(TableName.Bills, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("payment_method", null, TupleType.TextField, true, null),
									new Edit.Quadruple("bill", null, TupleType.TextField, true, null),
									new Edit.Quadruple("date", null, TupleType.date, true, null),
									new Edit.Quadruple("total_price", null, TupleType.TextField, true, null),
									new Edit.Quadruple("date_of_payment", null, TupleType.date, true, null) });
					break;
				case "Addresses":
					edit = new Edit(TableName.Addresses, null,
							new Edit.Quadruple[] { new Edit.Quadruple("city", null, TupleType.TextField, true, null),
									new Edit.Quadruple("cap", null, TupleType.TextFieldInt, true, null),
									new Edit.Quadruple("street", null, TupleType.TextField, true, null),
									new Edit.Quadruple("country", null, TupleType.TextField, true, null) });
					break;
				case "Reservations_Damages":
					edit = new Edit(TableName.Reservations_Damages, null, new Edit.Quadruple[] {
							new Edit.Quadruple("description", null, TupleType.ComboBox, false, TableName.Damages),
							new Edit.Quadruple("reservations_id", null, TupleType.ComboBox, false,
									TableName.Reservations),
							new Edit.Quadruple("fine", null, TupleType.TextField, true, null) });
					break;
				case "Reservations_Extraequipment":
					edit = new Edit(TableName.Reservations_Extraequipment, null,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", null, TupleType.ComboBox, false,
											TableName.Extra_Equipment),
									new Edit.Quadruple("reservations_id", null, TupleType.ComboBox, false,
											TableName.Reservations),
									new Edit.Quadruple("quantity", null, TupleType.TextFieldInt, true, null) });
					break;
				case "Vehicles_Equipment":
					edit = new Edit(TableName.Vehicles_Equipment, null, new Edit.Quadruple[] {
							new Edit.Quadruple("license_plate", null, TupleType.ComboBox, false, TableName.Vehicles),
							new Edit.Quadruple("description", null, TupleType.ComboBox, false, TableName.Equipment) });
					break;

				}
				edit.addWindowListener(new WindowListener() {

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						setTableData(null);

					}

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}
				});
			}

		});

		btnEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Edit edit = null;
				String currentTable = String.valueOf(comboBox_Table.getSelectedItem());
				int currentRow = table.getSelectedRow();
				String id = table.getValueAt(currentRow, 0).toString();
				switch (currentTable) {
				case "Insurances":
					edit = new Edit(TableName.Insurances, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("insurance_name", table.getValueAt(currentRow, 1),
											TupleType.TextField, true, null),
									new Edit.Quadruple("fee", table.getValueAt(currentRow, 2), TupleType.TextFieldInt,
											true, null) });
					break;
				case "Equipment":
					edit = new Edit(TableName.Equipment, id, new Edit.Quadruple[] { new Edit.Quadruple("description",
							table.getValueAt(currentRow, 1), TupleType.TextField, true, null) });
					break;
				case "Damages":
					edit = new Edit(TableName.Damages, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", table.getValueAt(currentRow, 1),
											TupleType.TextField, true, null),
									new Edit.Quadruple("position_part", table.getValueAt(currentRow, 2),
											TupleType.TextField, true, null) });
					break;
				case "Car_Brands":
					edit = new Edit(TableName.Car_Brands, id, new Edit.Quadruple[] { new Edit.Quadruple("company_name",
							table.getValueAt(currentRow, 1), TupleType.TextField, true, null) });
					break;
				case "Vehicles":
					edit = new Edit(TableName.Vehicles, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("license_plate", table.getValueAt(currentRow, 0),
											TupleType.TextField, true, null),
									new Edit.Quadruple("initial_registration", table.getValueAt(currentRow, 1),
											TupleType.date, true, null),
									new Edit.Quadruple("price_class", table.getValueAt(currentRow, 2),
											TupleType.TextField, true, null),
									new Edit.Quadruple("capacity", table.getValueAt(currentRow, 3), TupleType.TextField,
											true, null),
									new Edit.Quadruple("price_day", table.getValueAt(currentRow, 4),
											TupleType.TextField, true, null),
									new Edit.Quadruple("price_km", table.getValueAt(currentRow, 5), TupleType.TextField,
											true, null),
									new Edit.Quadruple("model", table.getValueAt(currentRow, 6), TupleType.TextField,
											true, null),
									new Edit.Quadruple("company_name", table.getValueAt(currentRow, 7), TupleType.ComboBox,
											true, TableName.Car_Brands),
									new Edit.Quadruple("insurance_name", table.getValueAt(currentRow, 8),
											TupleType.ComboBox, true, TableName.Insurances) });
					break;
					
				case "Reservations":
					edit = new Edit(TableName.Reservations, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("date_starttime", table.getValueAt(currentRow, 1), TupleType.dateTime, true, null),
									new Edit.Quadruple("date_endtime", table.getValueAt(currentRow, 2), TupleType.dateTime, false, null),
									new Edit.Quadruple("km_at_start", table.getValueAt(currentRow, 3), TupleType.TextFieldInt, true, null),
									new Edit.Quadruple("km_at_return", table.getValueAt(currentRow, 4), TupleType.TextFieldInt, false, null),
									new Edit.Quadruple("license_plate", table.getValueAt(currentRow, 5), TupleType.ComboBox, true, TableName.Vehicles),
									new Edit.Quadruple("clients_id", table.getValueAt(currentRow, 6), TupleType.ComboBox, true, TableName.Clients),
									new Edit.Quadruple("bills_id", table.getValueAt(currentRow, 7), TupleType.ComboBox, true, TableName.Bills), });
					break;
					
				case "Extra_Equipment":
					edit = new Edit(TableName.Extra_Equipment, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", table.getValueAt(currentRow, 1),
											TupleType.TextField, true, null),
									new Edit.Quadruple("price", table.getValueAt(currentRow, 2), TupleType.TextFieldInt,
											true, null),
									new Edit.Quadruple("total_quantity", table.getValueAt(currentRow, 3),
											TupleType.TextFieldInt, true, null) });
					break;
				case "Bills":
					edit = new Edit(TableName.Bills, id, new Edit.Quadruple[] {
							new Edit.Quadruple("payment_method", table.getValueAt(currentRow, 1), TupleType.TextField,
									true, null),
							new Edit.Quadruple("bill", table.getValueAt(currentRow, 2), TupleType.TextField, true,
									null),
							new Edit.Quadruple("date", table.getValueAt(currentRow, 3), TupleType.date, true, null),
							new Edit.Quadruple("total_price", table.getValueAt(currentRow, 4), TupleType.TextFieldInt,
									true, null),
							new Edit.Quadruple("date_of_payment", table.getValueAt(currentRow, 5), TupleType.date, true,
									null) });
					break;
		
				case "Addresses":
					edit = new Edit(TableName.Addresses, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("city", table.getValueAt(currentRow, 1), TupleType.TextField,
											true, null),
									new Edit.Quadruple("cap", table.getValueAt(currentRow, 2), TupleType.TextFieldInt,
											true, null),
									new Edit.Quadruple("street", table.getValueAt(currentRow, 3), TupleType.TextField,
											true, null),
									new Edit.Quadruple("country", table.getValueAt(currentRow, 4), TupleType.TextField,
											true, null) });
					break;
				case "Reservations_Damages":
					edit = new Edit(TableName.Reservations_Damages, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", table.getValueAt(currentRow, 2),
											TupleType.ComboBox, false, TableName.Damages),
									new Edit.Quadruple("reservations_id", table.getValueAt(currentRow, 1),
											TupleType.ComboBox, false, TableName.Reservations),
									new Edit.Quadruple("fine", table.getValueAt(currentRow, 3), TupleType.TextField,
											true, null) });
					break;
				case "Reservations_Extraequipment":
					edit = new Edit(TableName.Reservations_Extraequipment, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("description", table.getValueAt(currentRow, 1),
											TupleType.ComboBox, false, TableName.Extra_Equipment),
									new Edit.Quadruple("reservations_id", table.getValueAt(currentRow, 2),
											TupleType.ComboBox, false, TableName.Reservations),
									new Edit.Quadruple("quantity", table.getValueAt(currentRow, 3),
											TupleType.TextFieldInt, true, null) });
					break;
				case "Vehicles_Equipment":
					edit = new Edit(TableName.Vehicles_Equipment, id,
							new Edit.Quadruple[] {
									new Edit.Quadruple("license_plate", table.getValueAt(currentRow, 2),
											TupleType.ComboBox, false, TableName.Vehicles),
									new Edit.Quadruple("description", table.getValueAt(currentRow, 1),
											TupleType.ComboBox, false, TableName.Equipment) });
					break;

				}
				edit.addWindowListener(new WindowListener() {

					@Override
					public void windowOpened(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowIconified(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowDeiconified(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowDeactivated(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowClosing(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void windowClosed(WindowEvent arg0) {
						setTableData(null);

					}

					@Override
					public void windowActivated(WindowEvent arg0) {
						// TODO Auto-generated method stub

					}
				});

			}

		});
		panel.setLayout(gl_panel);

	}

	// creates the selected table table
	private void setTableData(String whereField) {
		String table = String.valueOf(comboBox_Table.getSelectedItem());
		String field = String.valueOf(comboBox_Search.getSelectedItem());
		String sql = "";
		switch (table) {
		case "Clients":
			sql = "SELECT c.clients_id, first_name, last_name, phone, driving_license_number, a.city, a.cap, a.street, a.country "
					+ "from clients c inner join addresses a on c.addresses_id = a.addresses_id";
			break;
		case "Reservations":
			sql = "select r.reservations_id, date_starttime, date_endtime, km_at_start, km_at_return, license_plate, c.first_name, c.last_name, b.bill, b.date as bill_date, b.total_price, b.date_of_payment, b.payment_method "
					+ "from reservations r inner join bills b on r.bills_id = b.bills_id "
					+ "inner join clients c on r.clients_id = c.clients_id";
			break;
		case "Vehicles":
			sql = "Select license_plate, initial_registration, price_class, capacity, price_day, price_km, model, b.company_name, i.insurance_name "
					+ "from vehicles v inner join car_brands b on b.car_brands_id = v.car_brands_id "
					+ "inner join insurances i on i.insurances_id = v.insurances_id";
			break;
		case "Reservations_Damages":
			sql = "select d.damages_id, r.reservations_id, d.description, fine, r.license_plate, c.first_name, c.last_name "
					+ "from reservations_damages rd inner join damages d on rd.damages_id = d.damages_id "
					+ "inner join reservations r on rd.reservations_id = r.reservations_id "
					+ "inner join clients c on r.clients_id = c.clients_id";
			break;
		case "Reservations_Extraequipment":
			sql = "select e.extra_equipment_id, r.reservations_id, e.description, quantity, r.license_plate, c.first_name, c.last_name "
					+ "from reservations_extraequipment re inner join extra_equipment e on re.extra_equipment_id = e.extra_equipment_id "
					+ "inner join reservations r on re.reservations_id = r.reservations_id "
					+ "inner join clients c on r.clients_id = c.clients_id";
			break;
		case "Vehicles_Equipment":
			sql = "select e.equipment_id, e.description, license_plate  "
					+ "from vehicles_equipment ve inner join equipment e on ve.equipment_id = e.equipment_id";
			break;
		default:
			sql = "Select * from " + table;
		}
		if (whereField != null && whereField.trim().length() > 0) {
			try {
				Integer.parseInt(whereField);
				sql += " where cast(" + field + " as TEXT) LIKE '" + whereField + "%';";
				System.out.println(sql);
			} catch (NumberFormatException e) {
				sql += " where lower(" + field + ") like lower('" + whereField + "%');";
			}

		} else
			sql += ";";

		try {
			DefaultTableModel data = Utilities.loadAllData(sql);
			this.tableModel = data;
			this.table.setModel(this.tableModel);
			comboBox_Search.removeAllItems();
			for (int i = 0; i < data.getColumnCount(); i++) {
				comboBox_Search.addItem(data.getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
