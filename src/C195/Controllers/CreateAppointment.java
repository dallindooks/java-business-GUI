package C195.Controllers;

import C195.DAO.ContactDAO;
import C195.DAO.CustomerDAO;
import C195.Helpers.JDBC;
import C195.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

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
            customer_input.setItems(CustomerDAO.getAllCustomerNames());
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
            } else {
                availableTimes.add(String.valueOf(open - 12) + ":00 PM");
                availableTimes.add(String.valueOf(open - 12) + ":30 PM");
            }
            open++;
        }
        start_time_input.setItems(availableTimes);
        end_time_input.setItems(availableTimes);

    }
    public LocalDateTime dateStringFormatter(LocalDate localDate, String stringToDate){
        String[] stringArr = stringToDate.split(" ");
        String formattedTime = "";
        if (stringArr[1].equals("PM")) {
            String[] stringNumArr = stringArr[0].split(":");
            int stringTimeNum = parseInt(stringNumArr[0]) + 12;
            formattedTime = String.valueOf(stringTimeNum).concat(":").concat(stringNumArr[1]);
        } else {
            String[] stringNumArr = stringArr[0].split(":");
            formattedTime = (parseInt(stringNumArr[0]) >= 10) ? stringArr[0] : "0".concat(stringArr[0]);
        };
        LocalDateTime formattedDateTime = LocalDateTime.of(localDate, LocalTime.parse(formattedTime));
        return formattedDateTime;
    }

    public void addAppointment(ActionEvent actionEvent) throws SQLException {
        LocalDateTime startTime = dateStringFormatter(start_datePicker.getValue() ,start_time_input.getValue().toString());
        LocalDateTime endTime = dateStringFormatter(end_datePicker.getValue(), end_time_input.getValue().toString());
        System.out.println(endTime);

//        Connection connection = JDBC.getConnection();
//        String query = "INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        PreparedStatement ps = connection.prepareStatement(query);
//        ps.setInt(1, getNewAppointmentId());
//        ps.setString(2, title_input.getText());
//        ps.setString(3, description_input.getText());
//        ps.setString(4, location_input.getText());
//        ps.setString(5, type_input.getText());
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

    public void toMain(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/C195/Views/Appointments.fxml")));
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Appointments");
        stage.setScene(scene);
    }
}
