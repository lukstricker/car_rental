package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Externalizable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager;

import org.postgresql.util.PSQLException;

import userInterface.Edit.Quadruple.TupleType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;

public class Edit extends JDialog {

	public static class ComboBoxItem {
		private String ID;
		private String Value;

		public ComboBoxItem(String id, String value) {
			this.ID = id;
			this.Value = value;
		}

		@Override
		public String toString() {
			return this.Value;
		}
	}

	// enum that contains all information about a single column
	public static class Quadruple {
		public enum TupleType {
			TextField, date, dateTime, TextFieldInt, ComboBox
		};

		private String ColumnName;
		private TupleType Type;
		private Object Value;
		private boolean Obligatory;
		private TableName extraTable;

		public Quadruple(String columnName, Object value, TupleType tupleType, boolean obligatory,
				TableName extraTable) {
			this.ColumnName = columnName;
			this.Value = value;
			this.Type = tupleType;
			this.Obligatory = obligatory;
			this.extraTable = extraTable;
		}
	}

	private JPanel content = new JPanel();
	public HashMap<String, Object> result = new HashMap<String, Object>();
	private TableName tableName;
	private JButton btnOK, btnCancel;
	private Quadruple[] tuples;
	private Database db;
	private String id;

	private enum Month {
		SELECT(-1), JANUARY(1), FEBRUARY(2), MARCH(3), APRIL(4), MAY(5), JUNE(6), JULY(7), AUGUST(8), SEPTEMBER(
				9), OCTOBER(10), NOVEMBER(11), DECEMBER(12);
		private final int value;

		private Month(int value) {
			this.value = value;
		}

	}

	public enum TableName {
		Reservations, Car_Brands, Vehicles, Insurances, Damages, Extra_Equipment, Equipment, Bills, Addresses, Clients, Reservations_Damages, Reservations_Extraequipment, Vehicles_Equipment
	}

	public Edit(TableName tableName, String id, Quadruple[] tuples) {
		this.tableName = tableName;
		this.tuples = tuples;
		this.id = id;
		this.SetLayout();

		for (Quadruple tuple : tuples) {
			JLabel label = new JLabel(tuple.ColumnName + ":");
			content.add(label);

			Object obj = null;
			switch (tuple.Type) {
			case TextField:
			case TextFieldInt:
			case dateTime:
				obj = new JTextField();
				if (id != null)
					if (tuple.Value == null)
						((JTextField) obj).setText("");
					else
						((JTextField) obj).setText(tuple.Value.toString());
				content.add((JTextField) obj);
				break;
			case ComboBox:
				obj = new JComboBox();
				ArrayList<ComboBoxItem> items = new ArrayList<ComboBoxItem>();
				String columnNameID = tuple.extraTable + "_id";
				if (tuple.extraTable == TableName.Vehicles)
					columnNameID = "license_plate";
				String sql = "Select " + columnNameID + ", " + tuple.ColumnName + " from " + tuple.extraTable;
				System.out.println(sql);
				db = new Database();
				ResultSet rs = db.getData(sql);
				try {
					while (rs.next()) {
						ComboBoxItem item = new ComboBoxItem(rs.getObject(columnNameID).toString(),
								rs.getObject(tuple.ColumnName).toString());
						items.add(item);
						((JComboBox) obj).addItem(item);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if (id != null) {
					for (int i = 0; i < items.size(); i++) {
						if (items.get(i).Value.equals(tuple.Value.toString())) {
							((JComboBox) obj).setSelectedIndex(i);
							break;
						}
					}
				}
				content.add((JComboBox) obj);
				break;
			case date:
				obj = new ArrayList<JComboBox>();

				JComboBox cbYear = new JComboBox();
				cbYear.addItem("SELECT");
				for (int i = Year.now().getValue(); i >= 1900; i--)
					cbYear.addItem(String.valueOf(i));
				((ArrayList<JComboBox>) obj).add(cbYear);
				JComboBox cbMonth = new JComboBox();
				cbMonth.setModel(new DefaultComboBoxModel<Month>(Month.values()));
				((ArrayList<JComboBox>) obj).add(cbMonth);
				JComboBox cbDay = new JComboBox();
				cbDay.addItem("SELECT");
				for (int i = 1; i <= 31; i++) {
					cbDay.addItem(String.valueOf(String.format("%02d", i)));
				}
				((ArrayList<JComboBox>) obj).add(cbDay);

				if (id != null) {
					Object date = tuple.Value;
					if (date != null && !date.toString().trim().equals("")) {
						String[] parts = date.toString().split("-");
						if (parts.length == 3) {
							String year = parts[0];
							int month = Integer.parseInt(parts[1]);
							String day = parts[2];
							for (int i = 0; i < cbYear.getModel().getSize(); i++) {
								if (year.equals(cbYear.getModel().getElementAt((i)))) {
									cbYear.setSelectedIndex(i);
									break;
								}
							}
							cbMonth.setSelectedIndex(month);
							for (int i = 0; i < cbDay.getModel().getSize(); i++) {
								if (day.equals(cbDay.getModel().getElementAt((i)))) {
									cbDay.setSelectedIndex(i);
									break;
								}
							}
						}
					}
				}

				content.add(cbYear);
				content.add(cbMonth);
				content.add(cbDay);
				break;
			}
			result.put(tuple.ColumnName, obj);
		}

		this.btnOK.addActionListener(new ActionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!checkFields()) {
					return;
				}

				db = new Database();

				if (id == null) {
					String sql = "INSERT INTO " + tableName + " (";
					Iterator it = result.entrySet().iterator();
					// Schleife für Spalten-Namen
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						for (Quadruple tuple : tuples) {
							if (tuple.ColumnName == key) {
								if (tuple.Type == TupleType.ComboBox)
									key = tuple.extraTable + "_id";
								if (tuple.extraTable == TableName.Vehicles)
									key = "license_plate";
								break;
							}
						}
						sql += key + ", ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += ") VALUES (";
					it = result.entrySet().iterator();
					// Schleife für Spalten-Werte
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						String value = "";
						for (Quadruple tuple : tuples) {
							if (tuple.ColumnName == key) {
								switch (tuple.Type) {
								case TextField:
								case TextFieldInt:
									value = ((JTextField) pair.getValue()).getText();
									break;
								case ComboBox:
									value = ((ComboBoxItem) ((JComboBox) pair.getValue()).getSelectedItem()).ID;
									break;
								case date:
									try {
										value = parseDateComboBoxes((ArrayList<JComboBox>) pair.getValue());
									} catch (Exception e) {
										JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
									break;
								case dateTime:
									if (checkDateTime(((JTextField) pair.getValue()).getText()) == false) {
										JOptionPane.showMessageDialog(null, "Date Time Format: YYYY-MM-DD HH:MM:SS",
												"Error", JOptionPane.ERROR_MESSAGE);
										return;
									}
									value = ((JTextField) pair.getValue()).getText();
									break;
								}
								break;
							}
						}
						sql += "'" + value + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += ");";
					System.out.println(sql);
					if (!db.insertData(sql)) {
						JOptionPane.showMessageDialog(null, "Unable to save data!", "Error", JOptionPane.ERROR_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, "Saved successfully!");

					System.out.println(sql);
				} else {

					String sql = "Update " + tableName + " set ";
					Iterator it = result.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						String value = "";
						for (Quadruple tuple : tuples) {
							if (tuple.ColumnName == key) {
								switch (tuple.Type) {
								case TextField:
								case TextFieldInt:
								case dateTime:
									value = ((JTextField) pair.getValue()).getText();
									break;
								case ComboBox:
									key = tuple.extraTable + "_id";
									if (tuple.extraTable == TableName.Vehicles)
										key = "license_plate";
									value = ((ComboBoxItem) ((JComboBox) pair.getValue()).getSelectedItem()).ID;
									break;
								case date:
									try {
										value = parseDateComboBoxes((ArrayList<JComboBox>) pair.getValue());
									} catch (Exception e) {
										JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
												JOptionPane.ERROR_MESSAGE);
										return;
									}
									break;
								}
								break;
							}
						}
						sql += key + "='" + value + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					String whereString = tableName.toString().toLowerCase() + "_id" + "=" + id;
					if (tableName == TableName.Vehicles) {
						whereString = "license_plate = '" + id + "'";
					} else if (tableName == TableName.Reservations_Damages) {
						for (Quadruple tuple : tuples) {
							if (tuple.ColumnName.toLowerCase().equals("reservations_id")) {
								whereString = "damages_id = " + id + " and reservations_id = " + tuple.Value;
								break;
							}
						}
					} else if (tableName == TableName.Reservations_Extraequipment) {
						for (Quadruple tuple : tuples) {
							if (tuple.ColumnName.toLowerCase().equals("reservations_id")) {
								whereString = "reservations_extraequipment_id = " + id + " and reservations_id = "
										+ tuple.Value;
								break;
							}
						}
					} else if (tableName == TableName.Vehicles_Equipment) {
						for (Quadruple tuple : tuples)
							if (tuple.ColumnName.toLowerCase().equals("license_plate")) {
								whereString = "equipment_id = " + id + " and license_plate = '" + tuple.Value + "'";
								break;
							}
					}

					sql += " Where " + whereString;
					System.out.println(sql);
					if (!db.updateData(sql)) {
						JOptionPane.showMessageDialog(null, "Unable to save data!", "Error", JOptionPane.ERROR_MESSAGE);
					} else
						JOptionPane.showMessageDialog(null, "Saved successfully!");
				}
				dispose();
			}
		});

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

	}

	private void SetLayout() {
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(content, BorderLayout.CENTER);
		content.setLayout(new GridLayout(20, 2));

		JPanel footer = new JPanel();
		getContentPane().add(footer, BorderLayout.SOUTH);

		btnOK = new JButton("OK");
		footer.add(btnOK);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();

			}
		});
		footer.add(btnCancel);

		if (id != null) {
			this.setTitle("Modify " + tableName);
		} else
			this.setTitle("Add " + tableName);
		this.pack();
		this.setSize(new Dimension(423, 420));
		this.setVisible(true);
		this.setResizable(false);
	}

	private boolean checkFields() {
		for (Quadruple tuple : tuples) {
			Object obj = result.get(tuple.ColumnName);
			if (tuple.Obligatory) {
				switch (tuple.Type) {
				case TextField:
				case TextFieldInt:
					if (((JTextField) obj).getText().trim().length() == 0) {
						JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return false;
					}
					break;
				case date:
					try {
						if (!checkDateComboBoxes((ArrayList<JComboBox>) obj)) {
							JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out!", "Error",
									JOptionPane.ERROR_MESSAGE);
							return false;

						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return false;
					}
				case dateTime:
					// das macht Lukas
					break;
				}
			}
			if (tuple.Type == TupleType.TextFieldInt) {
				try {
					Integer.parseInt(((JTextField) obj).getText().toString());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Insert an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkDateTime(String value) {
		System.out.println(value);
		if (!value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			return false;
		}
		return true;
	}

	private boolean checkDateComboBoxes(ArrayList<JComboBox> boxes) throws Exception {
		JComboBox cbYear = boxes.get(0);
		JComboBox cbMonth = boxes.get(1);
		JComboBox cbDay = boxes.get(2);
		if (cbDay.getSelectedIndex() == 0 || cbMonth.getSelectedIndex() == 0 || cbYear.getSelectedIndex() == 0) {
			return false;

		} else {
			String day = cbDay.getSelectedItem().toString();
			String month = String.valueOf(((Month) cbMonth.getSelectedItem()).value);
			String year = cbYear.getSelectedItem().toString();
			if (Integer.valueOf(month) == 2 && Integer.valueOf(day) > 29)
				throw new Exception("February has only 27/28 days.");
			if (Integer.valueOf(month) == 2 && Integer.valueOf(day) == 29
					&& (Integer.valueOf(year) % 4 != 0 || (year.endsWith("00") && Integer.valueOf(year) % 400 != 0)))
				throw new Exception("The selected year is not a leap year and therefore the february has not 29 days.");
			if ((Integer.valueOf(month) == 4 || Integer.valueOf(month) == 6 || Integer.valueOf(month) == 9
					|| Integer.valueOf(month) == 11) && Integer.valueOf(day) > 30)
				throw new Exception("The selected month has only 30 days.");

			return true;
		}
	}

	private String parseDateComboBoxes(ArrayList<JComboBox> boxes) throws Exception {
		if (!checkDateComboBoxes(boxes))
			return "NULL";

		JComboBox cbYear = boxes.get(0);
		JComboBox cbMonth = boxes.get(1);
		JComboBox cbDay = boxes.get(2);

		String day = cbDay.getSelectedItem().toString();
		String month = String.valueOf(((Month) cbMonth.getSelectedItem()).value);
		String year = cbYear.getSelectedItem().toString();

		if (day.length() != 2)
			day = "0" + day;
		if (month.length() != 2)
			month = "0" + month;

		return year + "-" + month + "-" + day;

	}

}
