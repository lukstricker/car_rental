package userInterface;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.lang.reflect.Constructor;
import java.sql.Connection;

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

	public static String[] getColumns() {
		String[] columns = null;
		try {
			db = new Database();
			String query = "SELECT * FROM " + String.valueOf(Main.getComboBox_1().getSelectedItem());
			ResultSet rs = db.getData(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int NumberOfColumns = rsmd.getColumnCount();
			columns = new String[NumberOfColumns];

			for (int i = 1; i <= NumberOfColumns; i++) {
				columns[i - 1] = rsmd.getColumnName(i);
			}
			db.closeConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columns;

	}

	public static void updateTableHeader(JTable table) {
		JTableHeader th = table.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("???");
		th.repaint();
	}

}
