package database.pool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;

public class ConnectionPool 
{
	private static DataSource ds;

	static {
		initPool();
	}

	public static void initPool() 
	{
		DriverAdapterCPDS cpds = new DriverAdapterCPDS();
		try {
			cpds.setDriver("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		cpds.setUrl(SystemInfo.host + "/" + SystemInfo.dbName + "?useUnicode=true&characterEncoding=utf-8");
		cpds.setUser(SystemInfo.username);
		cpds.setPassword(SystemInfo.password);

		SharedPoolDataSource tds = new SharedPoolDataSource();
		tds.setConnectionPoolDataSource(cpds);
		tds.setMaxActive(1000); // max connection _mysql at the same time
		tds.setMaxWait(-1); // maximum number of milliseconds that the pool will wait, -1 is wait indefinitely
		ds = tds;
	}
	
	public static void closeConnection(ResultSet resultset, Statement statement, Connection connect) 
	{
		try {
			if (resultset != null) {
				resultset.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		try {
			return ds.getConnection();
		} catch (Exception e) {
			initPool();
			return ds.getConnection();
		}
	}
	
	public static void main(String [] args) throws SQLException
	{
		
	}
}