package C195.DAO;

import C195.Helpers.JDBC;
import C195.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;

public class appointmentDAO {

    public static ObservableList<Appointment> getAllAppointments(int Id) throws SQLException {
        ObservableList<Appointment> appointmentsResult = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM appointments Where User_ID = " + Id;
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            Date createDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdated = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            Appointment appointmentResult = new Appointment(appointmentId, title, description, location, type, start, end, createDate, createdBy, lastUpdated, lastUpdatedBy, customerId, userId, contactId);
            appointmentsResult.add(appointmentResult);
        }
        return appointmentsResult;
    }

    public static void deleteAppointment(int Id) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        String query = "DELETE FROM appointments WHERE Appointment_ID=" + Id;
        System.out.println(query);
        statement.execute(query);
    }
}
