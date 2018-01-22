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
			case date:
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
						while(rs.next()) 
						((JComboBox) obj).addItem(rs.getObject(tuple.Key));
					} catch (SQLException e) {
						e.printStackTrace();
					}					
					content.add((JComboBox) obj);				
				}				
				break;
			}
			
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
							case date:
							case dateTime:
								if (tuple.Key == key) {
									obj = ((JTextField) pair.getValue()).getText();
								}
								break;
							case ComboBox:
								if (tuple.Key == key) {
									obj = ((JComboBox) pair.getValue()).getSelectedItem().toString();
									//eppes wia Select id from table where description = scratch front left door
								}
								break;
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
				if (((JTextField) obj).getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(null, "Please fill all mandatory fields out!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return false;
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
			if (tuple.Type == TupleType.date) {
				String input = ((JTextField) obj).getText();
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
					format.parse(input);
					// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					// LocalDate localDate = LocalDate.now();
					// if((Integer.parseInt(input)) > Integer.parseInt(dtf.format(localDate))) {
					// JOptionPane.showMessageDialog(null, "Date can't be higher than current
					// date!", "Error", JOptionPane.ERROR_MESSAGE);
					// return false;
					// }
				} catch (ParseException e) {
					JOptionPane.showMessageDialog(null, "Date Format: yyyy-mm-dd!", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

}
