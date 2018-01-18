package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;

import userInterface.Edit.Quadruple.TupleType;

import java.awt.BorderLayout;
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

	public static class Quadruple {
		public enum TupleType {
			TextField, TextArea, dateTime
		};

		private String Key;
		private TupleType Type;
		private Object Value;
		private boolean Obligatory;

		public Quadruple(String key, Object value, TupleType tupleType, boolean obligatory) {
			this.Key = key;
			this.Value = value;
			this.Type = tupleType;
			this.Obligatory = obligatory;
		}
	}

	private JPanel content = new JPanel();
	public HashMap<String, Object> result = new HashMap<String, Object>();
	private TableName tableName;
	private JButton btnOK, btnCancel;
	private Quadruple[] tuples;
	private Database db;

	public enum TableName {
		car_brands, vehicles, insurances, damages, extra_equipment, equipment
	}

	public Edit(TableName tableName, int id, Quadruple[] tuples) {
		this.tableName = tableName;
		this.tuples = tuples;
		this.SetLayout();

		for (Quadruple tuple : tuples) {
			JLabel label = new JLabel(tuple.Key + ":");
			content.add(label);

			Object obj = null;

			switch (tuple.Type) {
			case TextField:
				obj = new JTextField();
				if (id >= 0)
					((JTextField) obj).setText((String) tuple.Value);
				content.add((JTextField) obj);
				break;
			case TextArea:
				obj = new JTextArea();
				if (id >= 0)
					((JTextArea) obj).setText((String) tuple.Value);
				content.add((JTextArea) obj);
				break;
			case dateTime:
				obj = new JTextField();
				content.add((JTextField) obj);
				if (id >= 0)
					((JTextField) obj).setText((String) tuple.Value);

				if (tuple.Obligatory == true)

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
							if (tuple.Key == key) {
								switch (tuple.Type) {
								case TextField:
								case dateTime:
									obj = ((JTextField) pair.getValue()).getText();
									break;
								case TextArea:
									obj = ((JTextArea) pair.getValue()).getText();
									break;
								}
							}
						}
						sql += "'" + obj + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += ");";

					System.out.println(sql);
				} else {

					String sql = "Update " + tableName + " set ";
					Iterator it = result.entrySet().iterator();

					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry) it.next();
						String key = pair.getKey().toString();
						String obj = "";
						for (Quadruple tuple : tuples) {
							if (tuple.Key == key) {

								switch (tuple.Type) {
								case TextField:
								case dateTime:
									obj = ((JTextField) pair.getValue()).getText();
									break;
								case TextArea:
									obj = ((JTextArea) pair.getValue()).getText();
									break;
								}
							}
						}
						sql += key + "='" + obj + "', ";
					}
					sql = sql.substring(0, sql.length() - 2);
					sql += " Where " + tableName.toString().toLowerCase() + "_id" + "=" + "1";
					System.out.println(sql);

				}
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
		footer.add(btnCancel);
		this.pack();
		this.setVisible(true);
		this.setResizable(true);
	}

	private boolean checkFields() {
		for (Quadruple tuple : tuples) {
			Object obj = result.get(tuple.Key);
			if (tuple.Obligatory) {

				switch (tuple.Type) {
				case TextField:
				case dateTime:
					if (((JTextField) obj).getText().trim().length() == 0)
						return false;
					break;
				case TextArea:
					if (((JTextArea) obj).getText().trim().length() == 0)
						return false;
					break;
				}
			}
			if (tuple.Type == TupleType.dateTime) {
				String input = ((JTextField) obj).getText();
				try {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
					format.parse(input);
				} catch (ParseException e) {
					System.out.println("error");
					return false;
				}
			}
		}
		return true;
	}

}
