package userInterface;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import java.lang.reflect.Constructor;
import java.sql.Connection;

public class Utilities {
	
	private static Database db;
	
	
	
	public static void delete(int id, String tablename){
		db = new Database();
		String sql = "Delete from " + tablename + " Where " + tablename +"_id = " + id + ";";
		if(!db.deleteData(sql)) {
			JOptionPane.showMessageDialog(null, "Unable to delete data!", "Error", JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "Deleted successfully!");
		
	}
	
	public static String[][] loadAllData(String query){
		String[][] data = null;
		try {
			db = new Database();
			ResultSet rs = db.getData(query);
			int rows = countRows(String.valueOf(Main.getComboBox_1().getSelectedItem()));
			ResultSetMetaData rsmd = rs.getMetaData();

			int columns = rsmd.getColumnCount();
			data = new String[rows][columns];
			int i = 1, l = 0, m =0;
			while(rs.next()){
				i=1;
				while(i<=columns){
					data[l][m] = rs.getString(i);
					i++;
					m++;					
				}
				l++;
				m=0;				
			}	
			db.closeConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;		
	}
	
	private static int countRows(String table){
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
	
	public static String[] getColumns(){
		String[] columns = null;	
		try {
			db = new Database();
			String query = "SELECT * FROM " + String.valueOf(Main.getComboBox_1().getSelectedItem());
			ResultSet rs = db.getData(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int NumberOfColumns = rsmd.getColumnCount();
			columns = new String[NumberOfColumns];

			for (int i = 1; i <= NumberOfColumns; i++ ) {
			  columns[i-1] = rsmd.getColumnName(i);	  
			}
			db.closeConnection();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return columns;
		
		
	}
	
	
}
