package C195.Controllers;

import C195.DAO.appointmentDAO;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

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
    public static Appointment selectedAppointment;


    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public ObservableList<Appointment> getAllAppointments() throws SQLException {
            appointments = appointmentDAO.getAllAppointments(Login.getLoggedInUser().userId);
        return appointments;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getAllAppointments();
        } catch (SQLException throwables) {
//            System.out.println(throwables);
        }

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

    public void logout(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/C195/Views/login.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Welcome Back!");
        stage.setScene(scene);
    }

    public void toCreateAppointment(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/C195/Views/CreateAppointment.fxml")));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Create an Appointment");
        stage.setScene(scene);
    }

    public void deleteAppointment() throws SQLException {
        selectedAppointment = (Appointment) appointment_table.getSelectionModel().getSelectedItem();
        if (Utility.confirmDelete("Delete Appointment", "Are you sure you want to delete '" + selectedAppointment.getTitle() + "' with Appointment ID " +
                selectedAppointment.getAppointmentId() + " and type " + selectedAppointment.getType() + "?")){
            appointmentDAO.deleteAppointment(selectedAppointment.getAppointmentId());
            getAllAppointments();
            appointment_table.setItems(appointments);
        }

    }
}
