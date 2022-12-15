package C195.DAO;

import C195.Helpers.JDBC;
import C195.Models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class userDAO {
    /**
     * method to get a user by username
     * @param username username input
     * @return returns a User
     * @throws SQLException
     */
    public static User getUser(String username) throws SQLException {
            User userResult;
            Connection connection = JDBC.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM users WHERE User_Name = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int userid = rs.getInt("User_ID");
                String user_name = rs.getString("User_Name");
                String password = rs.getString("Password");
                Date createddate = rs.getDate("Create_Date");
                String createdby = rs.getString("Created_By");
                Date lastupdated = rs.getDate("Last_Update");
                String lastupdatedby = rs.getString("Last_Updated_By");
                userResult = new User(userid, user_name, password, createddate, createdby, lastupdated, lastupdatedby);
                return userResult;
            }
            connection.close();
        return null;
    }
}
