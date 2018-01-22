package userInterface;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Utilities {

	private static Database db;

	public static void delete(int id, String tablename) {
		db = new Database();
		String sql = "Delete from " + tablename + " Where " + tablename + "_id = " + id + ";";
		if (!db.deleteData(sql)) {
			JOptionPane.showMessageDialog(null, "Unable to delete data!", "Error", JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "Deleted successfully!");

	}

	/**
	 * @param rs
	 * @returns a DefaultTableModel object
	 * @throws SQLException
	 */
	public static DefaultTableModel loadAllData(String query) throws SQLException {
		return loadAllData(query, true);
	}

	/**
	 * Retrieves data from the ResultSet rs and creates a DefaultTableModel object.
	 * 
	 * @param rs
	 * @param tableEditable
	 * @returns a DefaultTableModel object
	 * @throws SQLException
	 */
	@SuppressWarnings("serial")
	public static DefaultTableModel loadAllData(String query, boolean tableEditable) throws SQLException {
		try {
			db = new Database();
			ResultSet rs = db.getData(query);
			ResultSetMetaData metaData = rs.getMetaData();

			// names of columns
			Vector<String> columnNames = new Vector<String>();
			int columnCount = metaData.getColumnCount();
			for (int column = 1; column <= columnCount; column++) {
				columnNames.add(metaData.getColumnName(column));
			}

			// data of the table
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			while (rs.next()) {
				Vector<Object> vector = new Vector<Object>();
				for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
					if (rs.getObject(columnIndex) != null && rs.getObject(columnIndex).getClass() == String.class
							&& rs.getObject(columnIndex).equals("null"))
						vector.add("");
					else
						vector.add(rs.getObject(columnIndex));
				}
				data.add(vector);
			}
			
			db.closeConnection();

			return new DefaultTableModel(data, columnNames) {
				public boolean isCellEditable(int row, int column) {
					return tableEditable && column != 0;
				}
			};	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static int countRows(String table) {
		int rows = 0;
		try {
			db = new Database();
			String query = "SELECT COUNT(*) FROM " + table;
			ResultSet rs = db.getData(query);
			rs.next();
			rows = rs.getInt(1);
			db.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;
	}

	

}
