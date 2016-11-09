package database.io;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.pool.ConnectionPool;

public class DataIO {
	
	DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public boolean clientInsertIP(String ip, String authentication) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultset = null;

        try {
        	String currentTime = simpleDateFormat.format(new Date());
            connect = ConnectionPool.getConnection();
            statement = connect.createStatement();
            String query = "INSERT INTO ip_status (ip, status, log, last_time, authentication) VALUES ('"
            		+ ip
            		+ "', 0, 'waiting', '"
            		+ currentTime
            		+ "', '"
            		+ authentication
            		+ "') ON DUPLICATE KEY UPDATE status = 0, log = 'waiting', last_time = '"
            		+ currentTime
            		+ "';";
            System.out.println(query);
            statement.executeUpdate(query);
            ConnectionPool.closeConnection(resultset, statement, connect);
            return true;
        } catch (SQLException e) {
            ConnectionPool.closeConnection(resultset, statement, connect);
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean clientCheckStop(String ip, String authentication) {
        boolean check = false;
        Connection connect = null;
        Statement statement = null;
        ResultSet resultset = null;

        try {
            connect = ConnectionPool.getConnection();
            statement = connect.createStatement();
            String query = "SELECT status FROM ip_status WHERE ip = '"
            		+ ip
            		+ "' AND authentication = '"
            		+ authentication
            		+ "';";
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
            e.printStackTrace();
        }

        return check;
    }
	
	public boolean clientSetLog(String ip, String log, String authentication) {

        Connection connect = null;
        Statement statement = null;
        ResultSet resultset = null;

        try {
        	String currentTime = simpleDateFormat.format(new Date());
            connect = ConnectionPool.getConnection();
            statement = connect.createStatement();
            String query = "UPDATE ip_status SET LOG = '"
            		+ log
            		+ "', last_time = '"
            		+ currentTime
            		+ "' WHERE ip = '"
            		+ ip
            		+ "' AND authentication = '"
            		+ authentication
            		+ "';";
            statement.executeUpdate(query);
            ConnectionPool.closeConnection(resultset, statement, connect);
            return true;
        } catch (SQLException e) {
            ConnectionPool.closeConnection(resultset, statement, connect);
            e.printStackTrace();
            return false;
        }
    }
	
	public Map<String, String> clientGettingParameter() {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultset = null;
        
        Map<String, String> mapParameters = new HashMap<>();

        try {
            connect = ConnectionPool.getConnection();
            statement = connect.createStatement();
            String query = "SELECT * FROM parameters_dexuat;";
            resultset = statement.executeQuery(query);
            while (resultset.next()) {
                String id = resultset.getString("id");
                String value = resultset.getString("value");
                if (value != null) {
                    if (id.compareTo("max_time_second_my_video") == 0) {
                        max_time_second_my_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("min_time_second_my_video") == 0) {
                        min_time_second_my_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("max_time_second_other_video") == 0) {
                        max_time_second_other_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("min_time_second_other_video") == 0) {
                        min_time_second_other_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("comments") == 0) {
                        String[] arrayComments = value.split(",");
                        for (int i = 0; i < arrayComments.length; i++) {
                            listComments.add(arrayComments[i].trim());
                        }
                    } else if (id.compareTo("other_videos") == 0) {
                        String[] arrayVideos = value.split(",");
                        for (int i = 0; i < arrayVideos.length; i++) {
                            listOtherVideos.add(arrayVideos[i].trim());
                        }
                    } else if (id.compareTo("target_videos") == 0) {
                        String[] arrayVideos = value.split(",");
                        for (int i = 0; i < arrayVideos.length; i++) {
                            listTargetVideos.add(arrayVideos[i].trim());
                        }
                    } else if (id.compareTo("comments1") == 0) {
                        String[] arrayComments = value.split(",");
                        for (int i = 0; i < arrayComments.length; i++) {
                            listComments1.add(arrayComments[i].trim());
                        }
                    } else if (id.compareTo("min_time_second1") == 0) {
                        min_time_second1 = Integer.parseInt(value.trim());
                    } else if (id.compareTo("max_time_second1") == 0) {
                        max_time_second1 = Integer.parseInt(value.trim());
                    } else if (id.compareTo("min_time_second_my_channel") == 0) {
                        min_time_second_my_channel = Integer.parseInt(value.trim());
                    } else if (id.compareTo("max_time_second_my_channel") == 0) {
                        max_time_second_my_channel = Integer.parseInt(value.trim());
                    } else if (id.compareTo("min_time_second_source_video") == 0) {
                        min_time_second_source_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("max_time_second_source_video") == 0) {
                        max_time_second_source_video = Integer.parseInt(value.trim());
                    } else if (id.compareTo("channels") == 0) {
                        String[] arrayChannels = value.split(",");
                        for (int i = 0; i < arrayChannels.length; i++) {
                            listChannels.add(arrayChannels[i].trim());
                        }
                    } else if (id.compareTo("source_videos") == 0) {
                        String[] arraySourceVideos = value.split(",");
                        for (int i = 0; i < arraySourceVideos.length; i++) {
                            listSourceVideos.add(arraySourceVideos[i].trim());
                        }
                    } else if (id.compareTo("comments_click_suggest") == 0) {
                        String[] arrayComments = value.split(",");
                        for (int i = 0; i < arrayComments.length; i++) {
                            listCommentsSuggest.add(arrayComments[i].trim());
                        }
                    }
                }
            }
            System.out.println("Load parameter success!");
            System.out.println("username: " + username);
            System.out.println();
            ConnectionPool.closeConnection(resultset, statement, connect);
        } catch (SQLException e) {
            ConnectionPool.closeConnection(resultset, statement, connect);
            System.out.println("Load parameter fail!");
        }
    }
	
	public void setStatus(String ip, int status, String authentication) {
        Connection connect = null;
        Statement statement = null;
        ResultSet resultset = null;

        try {
            connect = ConnectionPool.getConnection();
            statement = connect.createStatement();
            String query = "UPDATE ip_status SET STATUS = "
            		+ status
            		+ " WHERE ip = '"
            		+ ip
            		+ "' AND authentication = '"
            		+ authentication
            		+ "';";
            System.out.println(query);
            statement.executeUpdate(query);
            ConnectionPool.closeConnection(resultset, statement, connect);
        } catch (SQLException e) {
            ConnectionPool.closeConnection(resultset, statement, connect);
            e.printStackTrace();
        }
    }
	
	public static void main(String [] args) {
		DataIO object = new DataIO();
		String ip = "1.2.3.5";
		String authentication = "mrtamb9 thanhtam@@";
		String log = "This is log";
		System.out.println(object.clientSetLog(ip, log, authentication));
	}
}
