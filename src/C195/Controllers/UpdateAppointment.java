package C195.Controllers;

import C195.DAO.ContactDAO;
import C195.DAO.CustomerDAO;
import C195.Helpers.JDBC;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import C195.Models.Contact;
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
import java.util.*;

import static java.lang.Integer.parseInt;

public class UpdateAppointment implements Initializable {
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
    ObservableList<Contact> contactObservableList = FXCollections.observableArrayList();
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();

    public Appointment selectedAppointment;

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
            contactObservableList = ContactDAO.getAllContacts();
            customerObservableList = CustomerDAO.getAllCustomers();
            id_input.setText(String.valueOf(getNewAppointmentId()));
            contact_input.setItems(ContactDAO.getAllContactNames());
            customer_input.setItems(CustomerDAO.getAllCustomerNames());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ObservableList<String> availableTimes = FXCollections.observableArrayList();
//        Initialization of business hours
        int[] openHours = Utility.getBusinessHours(8, 22);
        System.out.println(openHours[0]);
        System.out.println(openHours[1]);
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
        selectedAppointment = Appointments.selectedAppointment;
        id_input.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        title_input.setText(selectedAppointment.getTitle());
        description_input.setText(selectedAppointment.getDescription());
        location_input.setText(selectedAppointment.getLocation());
        type_input.setText(selectedAppointment.getType());
        start_datePicker.setValue(LocalDate.from(selectedAppointment.start));
        end_datePicker.setValue(LocalDate.from(selectedAppointment.end));
        String[] startTimeArr = selectedAppointment.start.toString().split("T");
        String[] startbreakdown = startTimeArr[1].split(":");
        int startNum = parseInt(startbreakdown[0]);
        String startString = startNum > 12 ? String.valueOf(startNum-12) + ":" + startbreakdown[1] + " PM" : String.valueOf(startNum) + ":" + startbreakdown[1] + " AM";
        start_time_input.setValue(startString);
        String[] endTimeArr = selectedAppointment.end.toString().split("T");
        String[] endbreakdown = endTimeArr[1].split(":");
        int endNum = parseInt(endbreakdown[0]);
        String endString = endNum > 12 ? String.valueOf(endNum-12) + ":" + endbreakdown[1] + " PM" : String.valueOf(endNum) + ":" + endbreakdown[1] + " AM";
        end_time_input.setValue(endString);
        contactObservableList.forEach( (contact) -> {
            if (contact.getContactId() == selectedAppointment.getContactId()) contact_input.setValue(contact.getContactName());
        });
        customerObservableList.forEach( (customer) -> {
            if (customer.getCustomerId() == selectedAppointment.getCustomerId()) customer_input.setValue(customer.getCustomerName());
        });
    }

    public void updateAppointment(ActionEvent actionEvent) throws SQLException {
        try {
            LocalDateTime startTime;
            LocalDateTime endTime;
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
//          end of validation
//          upload new appointment
            Connection connection = JDBC.getConnection();
            String query = "UPDATE appointments SET Title = ?, " +
                    "Description = ?, Location = ?, Type = ?, " +
                    "Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?," +
                    "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = " + selectedAppointment.getAppointmentId();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, title_input.getText());
            ps.setString(2, description_input.getText());
            ps.setString(3, location_input.getText());
            ps.setString(4, type_input.getText());
            ps.setTimestamp(5, Timestamp.from(utcStartZoned.toInstant()));
            ps.setTimestamp(6, Timestamp.from(utcEndZoned.toInstant()));
            ps.setDate(7, Date.valueOf(LocalDate.now()));
            ps.setString(8, Login.currentUser.username);
            ps.setInt(9, CustomerDAO.getCustomerByName(customer_input.getValue().toString()).getCustomerId());
            ps.setInt(10, Login.currentUser.userId);
            ps.setInt(11, ContactDAO.getContactByName(contact_input.getValue().toString()).getContactId());
            System.out.println(query);
            if (checksToggle8 == 8) {
                ps.execute();
                toMain(actionEvent);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Appointment Updated Successfully");
                Optional<ButtonType> confirm = alert.showAndWait();
            } else {
                checksToggle8 = 0;
            }
        } catch(Exception err){
            System.out.println(err);
        }

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
