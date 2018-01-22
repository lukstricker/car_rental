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
import userInterface.Edit.Quadruple.TupleType;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class Edit extends JDialog {

	// public static class Tuple {
	// public enum TupleType {
	// TextField, TextArea, dateTime
	// };
	//
	// private String Key;
	// private TupleType Type;
	//
	// public Tuple(String key, TupleType tupleType) {
	// this.Key = key;
	// this.Type = tupleType;
	// }
	// }

	// enum that contains all information about a single column
	public static class Quadruple {
		public enum TupleType {
			TextField, date, dateTime, TextFieldInt, ComboBox
		};

		private String Key;
		private TupleType Type;
		private Object Value;
		private boolean Obligatory;
		private TableName extraTable;

		public Quadruple(String key, Object value, TupleType tupleType, boolean obligatory, TableName extraTable) {
			this.Key = key;
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
	private int id;
	private JComboBox<Month> cbMonth;
	private JComboBox<String> cbYear;
	private JComboBox<String> cbDay;

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

	public Edit(TableName tableName, int id, Quadruple[] tuples) {
		this.tableName = tableName;
		this.tuples = tuples;
		this.id = id;
		this.SetLayout();

		for (Quadruple tuple : tuples) {
			JLabel label = new JLabel(tuple.Key + ":");
			content.add(label);

			Object obj = null;
			switch (tuple.Type) {
			case TextField:
			case TextFieldInt:
			case dateTime:
				obj = new JTextField();
				if (id >= 0)
					((JTextField) obj).setText(tuple.Value.toString());
				content.add((JTextField) obj);
				break;
			case ComboBox:
				obj = new JComboBox();
				if (id >= 0) {

				} else {
					String sql = "Select " + tuple.Key + " from " + tuple.extraTable;
					db = new Database();
					ResultSet rs = db.getData(sql);
					try {
						while (rs.next())
							((JComboBox) obj).addItem(rs.getObject(tuple.Key));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					content.add((JComboBox) obj);
				}
				break;
			case date:
				obj = new String();
				cbYear = new JComboBox<String>();
				cbYear.addItem("SELECT");
				for (int i = Year.now().getValue(); i >= 1900; i--)
					cbYear.addItem(String.valueOf(i));
				cbMonth = new JComboBox<Month>();
				cbMonth.setModel(new DefaultComboBoxModel<Month>(Month.values()));
				cbDay = new JComboBox<String>();
				cbDay.addItem("SELECT");
				for (int i = 1; i <= 31; i++) {
					cbDay.addItem(String.valueOf(String.format("%02d", i)));
				}

				content.add(cbYear);
				content.add(cbMonth);
				content.add(cbDay);	
				
				//HILFEEEEEEEEEEEEEEE wia tua i dasses erst beim ok feld check mocht ober in result decht innischreib bei obj			
				obj = cbYear.getSelectedItem() + "-" + cbMonth.getSelectedItem() + "-" + cbDay.getSelectedItem();
				System.out.println(obj);
				
				try {
					obj = parseDateOfBirth();
					System.out.println(obj);
				} catch (Exception e) {
					System.out.println("exception lol");
				}			}

			result.put(tuple.Key, obj);
		}

		this.btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!checkFields()) {
					return;
				}

				db = new Database();

				if (id == -1) {
					String sql = "INSERT INTO " + tableName + " (";
					Iterator it = result.entrySet().iterator();
					// Schleife für Spalten-Namen
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						sql += key + ", ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += ") VALUES (";
					it = result.entrySet().iterator();
					// Schleife für Spalten-Werte
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						String obj = "";
						for (Quadruple tuple : tuples) {
							switch (tuple.Type) {
							case TextField:
							case TextFieldInt:

							case dateTime:
								if (tuple.Key == key) {
									obj = ((JTextField) pair.getValue()).getText();
								}
								break;
							case ComboBox:
								if (tuple.Key == key) {
									obj = ((JComboBox) pair.getValue()).getSelectedItem().toString();
									// eppes wia Select id from table where description = scratch front left door
								}
								break;
							case date:

							}

						}
						sql += "'" + obj + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += ");";
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
						String obj = "";
						for (Quadruple tuple : tuples) {
							if (tuple.Key == key)
								obj = ((JTextField) pair.getValue()).getText();
						}
						sql += key + "='" + obj + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += " Where " + tableName.toString().toLowerCase() + "_id" + "=" + id;

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

		if (id > 0) {
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
			Object obj = result.get(tuple.Key);
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
					if (!checkComboBoxes()) {
						JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return false;
					}
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
			if (tuple.Type == TupleType.dateTime) {
				String input = ((JTextField) obj).getText();
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
					format.parse(input);
				} catch (ParseException e) {
					JOptionPane.showMessageDialog(null, "Date - Time Format: yyyy-mm-dd HH:MM:SS!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkComboBoxes() {
		if (!((cbDay.getSelectedIndex() == 0 && cbMonth.getSelectedIndex() == 0 && cbYear.getSelectedIndex() == 0)
				|| (cbDay.getSelectedIndex() != 0 && cbMonth.getSelectedIndex() != 0
						&& cbYear.getSelectedIndex() != 0))) {
			return false;
		}
		return true;
	}

	private String parseDateOfBirth() throws Exception {

		if (cbDay.getSelectedIndex() == 0 || cbMonth.getSelectedIndex() == 0 || cbYear.getSelectedIndex() == 0) {
			return null;
		} else {
			String day = this.cbDay.getSelectedItem().toString();
			String month = String.valueOf(((Month) this.cbMonth.getSelectedItem()).value);
			String year = this.cbYear.getSelectedItem().toString();
			if (Integer.valueOf(month) == 2 && Integer.valueOf(day) > 29)
				throw new Exception("February has only 27/28 days.");
			if (Integer.valueOf(month) == 2 && Integer.valueOf(day) == 29
					&& (Integer.valueOf(year) % 4 != 0 || (year.endsWith("00") && Integer.valueOf(year) % 400 != 0)))
				throw new Exception("The selected year is not a leap year and therefore the february has not 29 days.");
			if ((Integer.valueOf(month) == 4 || Integer.valueOf(month) == 6 || Integer.valueOf(month) == 9
					|| Integer.valueOf(month) == 11) && Integer.valueOf(day) > 30)
				throw new Exception("The selected month has only 30 days.");

			if (day.length() != 2)
				day = "0" + day;
			if (month.length() != 2)
				month = "0" + month;
			return year + "-" + month + "-" + day;
		}
	}

}
