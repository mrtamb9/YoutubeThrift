package database.io;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.pool.ConnectionPool;
import utils.Utils;

public class DataIO {

	DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// call only when start or restart vps
	public boolean clientInsertIP(String ip, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		try {
			String currentTime = simpleDateFormat.format(new Date());
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "INSERT INTO ip_status (ip, status, log, last_time, authentication) VALUES ('" + ip
					+ "', 0, 'waiting', '" + currentTime + "', '" + authentication
					+ "') ON DUPLICATE KEY UPDATE status = 0, log = 'waiting', last_time = '" + currentTime + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// when controller add or load ip|email|password
	public boolean controllerUpdateIP(String ips, String emails, String passwords, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String[] arrayIps = ips.split(",");
		String[] arrayEmails = emails.split(",");
		String[] arrayPasswords = passwords.split(",");
		if (arrayIps.length == 0 || arrayIps.length != arrayEmails.length || arrayIps.length != arrayPasswords.length
				|| arrayEmails.length != arrayPasswords.length) {
			return false;
		}

		String values = "";
		for (int i = 0; i < arrayIps.length; i++) {
			String ip = arrayIps[i].trim();
			String email = arrayEmails[i].trim();
			String password = arrayPasswords[i].trim();
			String value = "('" + ip + "','" + email + "','" + password + "','" + authentication + "')";
			values += value + ",";
		}
		if (values.endsWith(",")) {
			values = values.substring(0, values.length() - 1);
		}

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "INSERT INTO ip_status (ip, email, password, authentication) VALUES " + values
					+ " ON DUPLICATE KEY UPDATE email=VALUES(email), password=VALUES(password);";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// check if status = 0 or not
	public boolean clientCheckStop(String ip, String authentication) {
		boolean check = false;
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT status FROM ip_status WHERE ip = '" + ip + "' AND authentication = '"
					+ authentication + "';";
			resultset = statement.executeQuery(query);
			if (resultset.next()) {
				String status = resultset.getString("status");
				if (status == null || status.compareTo("0") == 0) {
					check = true;
				}
			}
			ConnectionPool.closeConnection(resultset, statement, connect);
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
		}

		return check;
	}

	// set log when client running
	public boolean clientSetLog(String ip, String log, String authentication) {

		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		try {
			String currentTime = simpleDateFormat.format(new Date());
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "UPDATE ip_status SET LOG = '" + log + "', last_time = '" + currentTime + "' WHERE ip = '"
					+ ip + "' AND authentication = '" + authentication + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// get parameter feature search video and view
	public Map<String, String> gettingParameterSearch(String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		Map<String, String> mapParameters = new HashMap<>();

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT * FROM parameters_searchvideo WHERE authentication = '" + authentication + "';";
			resultset = statement.executeQuery(query);
			while (resultset.next()) {
				String parameter = resultset.getString("parameter");
				String value = resultset.getString("value");

				if (parameter.compareTo("max_time") == 0) {
					mapParameters.put("max_time", value);
				} else if (parameter.compareTo("min_time") == 0) {
					mapParameters.put("min_time", value);
				} else if (parameter.compareTo("comments") == 0) {
					mapParameters.put("comments", value);
				} else if (parameter.compareTo("videos") == 0) {
					mapParameters.put("videos", value);
				}
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return mapParameters;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			e.printStackTrace();
			return null;
		}
	}

	// setting parameter for feature search video and view
	public boolean settingParameterSearch(Map<String, String> mapParameters, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String min_time = mapParameters.get("min_time");
		String max_time = mapParameters.get("max_time");
		String comments = Utils.normalizeText(mapParameters.get("comments"));
		String videos = mapParameters.get("videos");

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();

			String query = "UPDATE parameters_searchvideo SET value = CASE parameter WHEN 'videos' THEN '" + videos
					+ "' WHEN 'comments' THEN '" + comments + "' WHEN 'min_time' THEN '" + min_time
					+ "' WHEN 'max_time' THEN '" + max_time + "' END WHERE authentication = '" + authentication + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// get parameter for feature paste link and view
	public Map<String, String> gettingParameterViewSuggest(String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		Map<String, String> mapParameters = new HashMap<>();

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT * FROM parameters_viewsuggest WHERE authentication = '" + authentication + "';";
			resultset = statement.executeQuery(query);
			while (resultset.next()) {
				String parameter = resultset.getString("parameter");
				String value = resultset.getString("value");

				if (parameter.compareTo("min_time_my_video") == 0) {
					mapParameters.put("min_time_my_video", value);
				} else if (parameter.compareTo("max_time_my_video") == 0) {
					mapParameters.put("max_time_my_video", value);
				} else if (parameter.compareTo("min_time_other_video") == 0) {
					mapParameters.put("min_time_other_video", value);
				} else if (parameter.compareTo("max_time_other_video") == 0) {
					mapParameters.put("max_time_other_video", value);
				} else if (parameter.compareTo("comments") == 0) {
					mapParameters.put("comments", value);
				} else if (parameter.compareTo("my_videos") == 0) {
					mapParameters.put("my_videos", value);
				} else if (parameter.compareTo("other_videos") == 0) {
					mapParameters.put("other_videos", value);
				}
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return mapParameters;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return null;
		}
	}

	// set parameter for feature paste link and view
	public boolean settingParameterViewSuggest(Map<String, String> mapParameters, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String min_time_my_video = mapParameters.get("min_time_my_video");
		String max_time_my_video = mapParameters.get("max_time_my_video");
		String min_time_other_video = mapParameters.get("min_time_other_video");
		String max_time_other_video = mapParameters.get("max_time_other_video");
		String my_videos = mapParameters.get("my_videos");
		String other_videos = mapParameters.get("other_videos");
		String comments = Utils.normalizeText(mapParameters.get("comments"));

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();

			String query = "UPDATE parameters_viewsuggest SET value = CASE parameter WHEN 'my_videos' THEN '"
					+ my_videos + "' WHEN 'other_videos' THEN '" + other_videos + "' WHEN 'comments' THEN '" + comments
					+ "' WHEN 'min_time_my_video' THEN '" + min_time_my_video + "' WHEN 'max_time_my_video' THEN '"
					+ max_time_my_video + "' WHEN 'min_time_other_video' THEN '" + min_time_other_video
					+ "' WHEN 'max_time_other_video' THEN '" + max_time_other_video + "' END WHERE authentication = '"
					+ authentication + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// get parameter for feature click channel suggested
	public Map<String, String> gettingParameterClickSuggest(String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		Map<String, String> mapParameters = new HashMap<>();

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT * FROM parameters_clicksuggest WHERE authentication = '" + authentication + "';";
			resultset = statement.executeQuery(query);
			while (resultset.next()) {
				String parameter = resultset.getString("parameter");
				String value = resultset.getString("value");

				if (parameter.compareTo("min_time_my_video") == 0) {
					mapParameters.put("min_time_my_video", value);
				} else if (parameter.compareTo("max_time_my_video") == 0) {
					mapParameters.put("max_time_my_video", value);
				} else if (parameter.compareTo("min_time_other_video") == 0) {
					mapParameters.put("min_time_other_video", value);
				} else if (parameter.compareTo("max_time_other_video") == 0) {
					mapParameters.put("max_time_other_video", value);
				} else if (parameter.compareTo("comments") == 0) {
					mapParameters.put("comments", value);
				} else if (parameter.compareTo("my_channel_ids") == 0) {
					mapParameters.put("my_channel_ids", value);
				} else if (parameter.compareTo("source_videos") == 0) {
					mapParameters.put("source_videos", value);
				}
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return mapParameters;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return null;
		}
	}

	// set parameter for feature click channel suggested
	public boolean settingParameterClickSuggest(Map<String, String> mapParameters, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String min_time_my_video = mapParameters.get("min_time_my_video");
		String max_time_my_video = mapParameters.get("max_time_my_video");
		String min_time_other_video = mapParameters.get("min_time_other_video");
		String max_time_other_video = mapParameters.get("max_time_other_video");
		String my_channel_ids = mapParameters.get("my_channel_ids");
		String source_videos = mapParameters.get("source_videos");
		String comments = Utils.normalizeText(mapParameters.get("comments"));

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();

			String query = "UPDATE parameters_clicksuggest SET value = CASE parameter WHEN 'my_channel_ids' THEN '"
					+ my_channel_ids + "' WHEN 'source_videos' THEN '" + source_videos + "' WHEN 'comments' THEN '"
					+ comments + "' WHEN 'min_time_my_video' THEN '" + min_time_my_video
					+ "' WHEN 'max_time_my_video' THEN '" + max_time_my_video + "' WHEN 'min_time_other_video' THEN '"
					+ min_time_other_video + "' WHEN 'max_time_other_video' THEN '" + max_time_other_video
					+ "' END WHERE authentication = '" + authentication + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// set status choose which feature run
	public boolean setStatus(String ips, String status, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String[] arrayIPs = ips.split(",");
		if (arrayIPs.length == 0) {
			return false;
		}

		String groupIPs = "(";
		for (int i = 0; i < arrayIPs.length; i++) {
			groupIPs += "\'" + arrayIPs[i].trim() + "\',";
		}
		if (groupIPs.endsWith(",")) {
			groupIPs = groupIPs.substring(0, groupIPs.length() - 1);
		}
		groupIPs += ")";

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "UPDATE ip_status SET status = " + status + " WHERE ip IN " + groupIPs
					+ " AND authentication = '" + authentication + "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// delete ip from table ip_status
	public boolean deleteIP(String ips, String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		String[] arrayIPs = ips.split(",");
		if (arrayIPs.length == 0) {
			return false;
		}

		String groupIPs = "(";
		for (int i = 0; i < arrayIPs.length; i++) {
			groupIPs += "\'" + arrayIPs[i].trim() + "\',";
		}
		if (groupIPs.endsWith(",")) {
			groupIPs = groupIPs.substring(0, groupIPs.length() - 1);
		}
		groupIPs += ")";

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "DELETE FROM ip_status WHERE ip IN " + groupIPs + " AND authentication = '" + authentication
					+ "';";
			statement.executeUpdate(query);
			ConnectionPool.closeConnection(resultset, statement, connect);
			return true;
		} catch (SQLException e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return false;
		}
	}

	// load all ip with authentication
	public List<String> gettingAllIP(String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		List<String> listIPs = new ArrayList<>();

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT * FROM ip_status WHERE authentication = '" + authentication + "';";
			resultset = statement.executeQuery(query);
			while (resultset.next()) {
				String ip = resultset.getString("ip");
				String email = resultset.getString("email");
				String status = resultset.getString("status");
				String log = resultset.getString("log");
				String last_time = resultset.getString("last_time");
				String tempString = ip + "###" + email + "###" + status + "###" + log + "###" + last_time;
				listIPs.add(tempString);
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return listIPs;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return null;
		}
	}

	// load all warning ip with authentication, out of time but not be updated
	public List<String> gettingWarningIP(String authentication) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		Date warningTime = Utils.getTimeWarning();
		List<String> listWarningIPs = new ArrayList<>();

		try {
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT * FROM ip_status WHERE authentication = '" + authentication + "';";
			resultset = statement.executeQuery(query);
			while (resultset.next()) {
				String ip = resultset.getString("ip");
				String email = resultset.getString("email");
				String status = resultset.getString("status");
				String log = resultset.getString("log");
				String last_time_string = resultset.getString("last_time");
				String tempString = ip + "###" + email + "###" + status + "###" + log + "###" + last_time_string;
				if (last_time_string != null) {
					Date last_time = simpleDateFormat.parse(last_time_string);
					if (last_time.before(warningTime)) {
						listWarningIPs.add(tempString);
					}
				} else {
					listWarningIPs.add(tempString);
				}
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return listWarningIPs;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return null;
		}
	}

	// get authentication
	public String gettingAuthentication(String username, String password) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultset = null;

		try {
			String authentication = null;
			connect = ConnectionPool.getConnection();
			statement = connect.createStatement();
			String query = "SELECT authentication FROM user_profile WHERE username = '" + username
					+ "' AND password = '" + password + "';";
			resultset = statement.executeQuery(query);
			if (resultset.next()) {
				authentication = resultset.getString("authentication");
			}

			ConnectionPool.closeConnection(resultset, statement, connect);
			return authentication;
		} catch (Exception e) {
			ConnectionPool.closeConnection(resultset, statement, connect);
			return null;
		}
	}

	public static void main(String[] args) {
		DataIO object = new DataIO();
		String authentication = "tamnt1209 thanhtam1209";
		System.out.println(object.gettingWarningIP(authentication));
	}
}
