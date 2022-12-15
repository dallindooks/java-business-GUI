package C195.Controllers;

import C195.DAO.ContactDAO;
import C195.DAO.CustomerDAO;
import C195.DAO.appointmentDAO;
import C195.Helpers.JDBC;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import C195.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

public class CreateAppointment implements Initializable {
    public Label end_error;
    public Label start_error;
    public Label type_error;
    public Label location_error;
    public Label description_error;
    public Label title_error;
    public ComboBox contact_input;
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
    public Label customer_error;
    public Label contact_error;

    public ObservableList<Appointment> theirAppointments;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        title_error.setText("");
        start_error.setText("");
        type_error.setText("");
        location_error.setText("");
        end_error.setText("");
        description_error.setText("");
        customer_error.setText("");
        contact_error.setText("");
        try{
            id_input.setText(String.valueOf(getNewAppointmentId()));
            contact_input.setItems(ContactDAO.getAllContactNames());
            customer_input.setItems(CustomerDAO.getAllCustomerNames());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
//        Initialization of business hours
        int[] openHours = Utility.getBusinessHours(8, 22);
        while (openHours[0] < openHours[1]){
            if (openHours[0] < 12) {
                availableTimes.add(String.valueOf(openHours[0]) + ":00 AM");
                availableTimes.add(String.valueOf(openHours[0]) + ":30 AM");
            } else if(openHours[0] == 12){
                availableTimes.add(String.valueOf(openHours[0]) + ":00 PM");
                availableTimes.add(String.valueOf(openHours[0]) + ":30 PM");
            }
            else {
                availableTimes.add(String.valueOf(openHours[0] - 12) + ":00 PM");
                availableTimes.add(String.valueOf(openHours[0] - 12) + ":30 PM");
            }
            openHours[0]++;
        }
        start_time_input.setItems(availableTimes);
        end_time_input.setItems(availableTimes);

    }

    /**
     * method to add and save an appointment to the database. Also it contains all of the logical checks to ensure the inputs are valid.
     * @param actionEvent
     * @throws SQLException
     */
    public void addAppointment(ActionEvent actionEvent) throws SQLException {
        try {
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            ZonedDateTime startTimeZoned;
            ZonedDateTime endTimeZoned;
            ZonedDateTime utcStartZoned = null;
            ZonedDateTime utcEndZoned = null;
            int checksToggle8 = 0;

//            input validation begins here
            if (title_input.getText().equals("")) {
                title_error.setText("Please enter a Title");
            } else {
                checksToggle8++;
                title_error.setText("");
            }

            if (description_input.getText().equals("")) {
                description_error.setText("Please enter a Description");
            } else {
                checksToggle8++;
                description_error.setText("");
            }

            if (location_input.getText().equals("")) {
                location_error.setText("Please enter a Location");
            } else {
                checksToggle8++;
                location_error.setText("");
            }

            if (type_input.getText().equals("")) {
                type_error.setText("Please enter a Type");
            } else {
                checksToggle8++;
                type_error.setText("");
            }

            if (start_datePicker.getValue() == null || start_time_input.getValue() == null) {
                start_error.setText("Please select a start time");
            } else {
                startTime = Utility.dateStringFormatter(start_datePicker.getValue(), start_time_input.getValue().toString());
                startTimeZoned = startTime.atZone(ZoneId.systemDefault());
                utcStartZoned = startTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
                checksToggle8++;
                start_error.setText("");
            }

            if (end_datePicker.getValue() == null || end_time_input.getValue() == null) {
                end_error.setText("Please select an end time");
            } else {
                endTime = Utility.dateStringFormatter(end_datePicker.getValue(), end_time_input.getValue().toString());
                endTimeZoned = endTime.atZone(ZoneId.systemDefault());
                utcEndZoned = endTimeZoned.withZoneSameInstant(ZoneId.of("UTC"));
                if (utcStartZoned.toInstant().isAfter(utcEndZoned.toInstant())){
                    start_error.setText("Start time must occur before the end time");
                    end_error.setText("End time must occur after the start time");
                } else {
                    checksToggle8++;
                    end_error.setText("");
                };
            }

            if (customer_input.getValue().equals(null)){
                customer_error.setText("Please select a customer");
            } else {
                checksToggle8++;
                customer_error.setText("");
            }

            if (contact_input.getValue().equals(null)){
                contact_error.setText("Please select a contact");
            } else {
                checksToggle8++;
                customer_error.setText("");
            }

//          checks for overlapping appointments for the selected customer
            Customer selectedCustomer = CustomerDAO.getCustomerByName(customer_input.getValue().toString());
            theirAppointments = (appointmentDAO.getAllAppointments(Login.getLoggedInUser().userId));
            theirAppointments.stream().filter((appt) -> appt.getCustomerId() == selectedCustomer.getCustomerId()).findAny().get();
            DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Appointment appt: theirAppointments) {
                LocalDateTime apptStart = appt.getStart();
                LocalDateTime apptEnd = appt.getEnd();
                if (!apptStart.isAfter(startTime) && !apptEnd.isBefore(endTime)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText(selectedCustomer.getCustomerName() +  " already has a scheduled appointment at this time.");
                    alert.setContentText("Conflicting appointment has ID: " + appt.getAppointmentId() + " and is scheduled on " +
                    apptStart.format(customFormat) + " and lasts until " + apptEnd.format(customFormat));
                    Optional<ButtonType> confirm = alert.showAndWait();
                    return;
                }
            }
//          end of validation
//          upload new appointment
            Connection connection = JDBC.getConnection();
            String query = "INSERT INTO appointments VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, getNewAppointmentId());
            ps.setString(2, title_input.getText());
            ps.setString(3, description_input.getText());
            ps.setString(4, location_input.getText());
            ps.setString(5, type_input.getText());
            ps.setTimestamp(6, Timestamp.from(utcStartZoned.toInstant()));
            ps.setTimestamp(7, Timestamp.from(utcEndZoned.toInstant()));
            ps.setDate(8, Date.valueOf(LocalDate.now()));
            ps.setString(9, Login.currentUser.username);
            ps.setDate(10, Date.valueOf(LocalDate.now()));
            ps.setString(11, Login.currentUser.username);
            ps.setInt(12, selectedCustomer.getCustomerId());
            ps.setInt(13, Login.currentUser.userId);
            ps.setInt(14, ContactDAO.getContactByName(contact_input.getValue().toString()).getContactId());
            if (checksToggle8 == 8) {
                ps.execute();
                toMain(actionEvent);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Appointment Created Successfully");
                Optional<ButtonType> confirm = alert.showAndWait();
            } else {
                checksToggle8 = 0;
            }
        } catch(Exception err){
            System.out.println(err);
        }

    }

    /**
     * method that returns a unique appointment id
     * @return
     * @throws SQLException
     */
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

    /**
     * navigates to appointments page
     * @param actionEvent
     * @throws IOException
     */
    public void toMain(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Appointments.fxml");
    }
}
