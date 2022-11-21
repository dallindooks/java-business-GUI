package C195.Controllers;

import C195.DAO.ContactDAO;
import C195.Helpers.JDBC;
import C195.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateAppointment implements Initializable {
    public Label end_error;
    public Label start_error;
    public Label type_error;
    public Label location_error;
    public Label description_error;
    public Label title_error;
    public ComboBox contactInput;
    public ComboBox customer_input;
    public ComboBox end_time_input;
    public DatePicker end_datePicker;
    public ComboBox start_time_input;
    public DatePicker start_datePicker;
    public TextField type_input;
    public TextField location_input;
    public TextField description_input;
    public TextField title_input;
    public TextField id_input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_error.setText("");
        start_error.setText("");
        type_error.setText("");
        location_error.setText("");
        end_error.setText("");
        description_error.setText("");
        try{
            id_input.setText(String.valueOf(getNewAppointmentId()));
            contactInput.setItems(ContactDAO.getAllContactNames());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
        int open = 8;
        int close = 22;
        while (open < close){
            if (open < 12) {
                availableTimes.add(String.valueOf(open) + ":00 AM");
                availableTimes.add(String.valueOf(open) + ":30 AM");
            } else{
                availableTimes.add(String.valueOf(open - 12) + ":00 PM");
                availableTimes.add(String.valueOf(open - 12) + ":30 PM");
            }
            open++;
        }
        start_time_input.setItems(availableTimes);
        end_time_input.setItems(availableTimes);

    }

    public void AddAppointment(ActionEvent actionEvent) throws SQLException {
        Connection connection = JDBC.getConnection();
        String query = "INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, getNewAppointmentId());
        ps.setString(2, title_input.getText());
        ps.setString(3, description_input.getText());
        ps.setString(4, location_input.getText());
        ps.setString(5, type_input.getText());
//        ps.setTimestamp(5, Timestamp.valueOf());
    }

    public int getNewAppointmentId() throws SQLException {
        int lastAppointmentId = 0;
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT MAX(Appointment_ID) FROM appointments";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            lastAppointmentId = rs.getInt("max(Appointment_ID)");
        }
        return lastAppointmentId > 999 ? lastAppointmentId + 1 : null;
    }
}
