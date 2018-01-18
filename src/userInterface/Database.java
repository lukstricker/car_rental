package userInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.table.DefaultTableModel;

public class Database {

	private Connection connection;
	private Statement statement;
	private static String databaseName = "car_rental";
	private static String username = "postgres";
	private static String password = "12345678";
	public static final String connectionString = "jdbc:postgresql://localhost:5432/" + databaseName;

	public Database() {
		ConnectToDatabase();
	}

	private void ConnectToDatabase() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your PostgreSQL JDBC Driver?");
			e.printStackTrace();
			return;
		}
		try {
			connection = DriverManager.getConnection(connectionString, username, password);
			statement = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean insertData(String sql) {
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ResultSet getData(String sql) {
		ResultSet result = null;
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateData(String sql) {
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
//			if(dateOfBirth == null)
//				ps.setNull(1, java.sql.Types.DATE);
//			else
//				ps.setString(1, dateOfBirth);
			ps.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean deleteData(String sql) {
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void closeConnection() {
		try {
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

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
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}

		return new DefaultTableModel(data, columnNames);

	}

}
