package C195.Controllers;

import C195.DAO.appointmentDAO;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;
import java.util.*;

public class Appointments implements Initializable {
    public TableColumn id_col;
    public TableColumn title_col;
    public TableColumn description_col;
    public TableColumn location_col;
    public TableColumn contact_col;
    public TableColumn type_col;
    public TableColumn start_col;
    public TableColumn end_col;
    public TableColumn customer_id_col;
    public TableColumn user_id_col;
    public TableView appointment_table;
    public ComboBox filter_selection;
    public Button increment;
    public Button decrement;
    public Button back_button;
    public Label filter_label;
    public static Appointment selectedAppointment;


    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public ObservableList<Appointment> filteredAppointments = FXCollections.observableArrayList();
    SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");

    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    Date today = new Date();
    int weekOfYear, weekAtCompare;
    int monthOfYear, monthAtCompare;

    /**
     * returns a list of all appointments for the logged in user
     * @return
     * @throws SQLException
     */
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
            appointments = appointmentDAO.getAllAppointments(Login.getLoggedInUser().userId);
        return appointments;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendar.setTime(today);
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        weekAtCompare = weekOfYear;
        monthOfYear = calendar.get(Calendar.MONTH);
        monthAtCompare = monthOfYear;
        try {
            getAllAppointments();
        } catch (SQLException throwables) {
//            System.out.println(throwables);
        }
        filter_selection.setItems(FXCollections.observableArrayList("All", "Week","Month"));
        appointment_table.setItems(appointments);
        id_col.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
        description_col.setCellValueFactory(new PropertyValueFactory<>("description"));
        location_col.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact_col.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        start_col.setCellValueFactory(new PropertyValueFactory<>("start"));
        end_col.setCellValueFactory(new PropertyValueFactory<>("end"));
        customer_id_col.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        user_id_col.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    /**
     * navigates to reports page
     * @param actionEvent
     * @throws IOException
     */
    public void toReports(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Reports.fxml");
    }

    /**
     * navigates the create appointment page
     * @param actionEvent
     * @throws IOException
     */
    public void toCreateAppointment(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/CreateAppointment.fxml");
    }

    /**
     * contains logic to delete a selected appointment and asks user for permission to do so
     * @throws SQLException
     */
    public void deleteAppointment() throws SQLException {
        selectedAppointment = (Appointment) appointment_table.getSelectionModel().getSelectedItem();
        if (Utility.confirmDelete("Delete Appointment", "Are you sure you want to delete '" + selectedAppointment.getTitle() + "' with Appointment ID " +
                selectedAppointment.getAppointmentId() + " and type " + selectedAppointment.getType() + "?")){
            appointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId());
            getAllAppointments();
            appointment_table.setItems(appointments);
        }

    }

    /**
     * method used to decrement which month or year to sort by
     * @param actionEvent
     */
    public void decrement(ActionEvent actionEvent) {
        filterAppointments(-1);
    }

    /**
     * method to increment which month or year to sort by
     * @param actionEvent
     */
    public void increment(ActionEvent actionEvent) {
        filterAppointments(1);
    }

    /**
     * changes the label used to indicate what month or week is being sorted
     * @param value
     * @param period
     */
    public void setFilter_label(int value, String period){
        Calendar cal = Calendar.getInstance();
        if (period == "Week") {
            cal.set(Calendar.WEEK_OF_YEAR, value);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            filter_label.setText("Week of : " + sdf.format(cal.getTime()));
        } else if (period == "Month"){
            SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy");
            cal.set(Calendar.MONTH, value);
            filter_label.setText("Month of : " + sdf.format(cal.getTime()));
        } else filter_label.setText("");
    }

    /**
     * used to filter the appointments on selection of month or year
     * @param actionEvent
     */
    public void initialFilterAppointments(ActionEvent actionEvent) {
        filteredAppointments.clear();
        if (filter_selection.getValue().toString().equals("All")) {
            appointment_table.setItems(appointments);
            filter_label.setText("");
        }
        if (filter_selection.getValue().toString().equals("Week")){
            setFilter_label(weekOfYear, filter_selection.getValue().toString());
            for (Appointment appt: appointments){
                LocalDateTime apptDate = appt.getStart();
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekNumber = apptDate.get(weekFields.weekOfWeekBasedYear());
                if (weekNumber == weekAtCompare) filteredAppointments.add(appt);
            }
            appointment_table.setItems(filteredAppointments);
        }
        if (filter_selection.getValue().toString().equals("Month")){
            setFilter_label(monthOfYear, filter_selection.getValue().toString());
            for (Appointment appt: appointments){
                int apptMonth = appt.getStart().getMonthValue() - 1; //minus one because Calendar is 0 indexed and Dates are indexed at 1
                if (apptMonth == monthOfYear) filteredAppointments.add(appt);
            }
            appointment_table.setItems(filteredAppointments);
        }
    }

    /**
     * used to filter appointments after the decrement or increment buttons have been pressed
     * @param slide
     */
    public void filterAppointments(int slide){
        Calendar cal = Calendar.getInstance();
        filteredAppointments.clear();
        if (filter_selection.getValue().toString().equals("Week")){
            weekAtCompare += slide;
            cal.set(Calendar.WEEK_OF_YEAR, weekAtCompare);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            setFilter_label(weekAtCompare, filter_selection.getValue().toString());
            for (Appointment appt: appointments){
                LocalDateTime apptDate = appt.getStart();
                Calendar apptCal = Calendar.getInstance();
                apptCal.setTime(Date.from(apptDate.atZone(ZoneId.systemDefault()).toInstant()));
                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int weekNumber = apptDate.get(weekFields.weekOfWeekBasedYear());
                apptCal.set(Calendar.WEEK_OF_YEAR, weekNumber);
                apptCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                if (sdf.format(cal.getTime()).equals(sdf.format(apptCal.getTime()))) filteredAppointments.add(appt);
            }
            appointment_table.setItems(filteredAppointments);
        } else if (filter_selection.getValue().toString().equals("Month")){
            monthAtCompare += slide;
            cal.set(Calendar.MONTH, monthAtCompare);
            setFilter_label(monthAtCompare, filter_selection.getValue().toString());
            for (Appointment appt: appointments){
                LocalDateTime apptDate = appt.getStart();
                Calendar apptCal = Calendar.getInstance();
                apptCal.setTime(Date.from(apptDate.atZone(ZoneId.systemDefault()).toInstant()));
                if ((apptCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && (apptCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR)))) filteredAppointments.add(appt);
            }
            appointment_table.setItems(filteredAppointments);
        }
    }

    /**
     * navigates to update appointment and sets the selected appointment
     * @param actionEvent
     * @throws IOException
     */
    public void toUpdateAppointment(ActionEvent actionEvent) throws IOException {
        selectedAppointment = (Appointment) appointment_table.getSelectionModel().getSelectedItem();
        Utility.changeScene(actionEvent, "/C195/Views/UpdateAppointment.fxml");
    }
}
