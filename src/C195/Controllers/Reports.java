package C195.Controllers;

import C195.DAO.ContactDAO;
import C195.DAO.CustomerDAO;
import C195.DAO.appointmentDAO;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import C195.Models.Contact;
import C195.Models.Customer;
import com.sun.source.tree.Tree;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Map.Entry;

public class Reports implements Initializable {
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    public BarChart appointments_graph = new BarChart<String,Number>(xAxis,yAxis);;
    public Label avg_appt_length;

    public ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public ObservableList<Customer> customers = FXCollections.observableArrayList();
    public ObservableList<Contact> contacts = FXCollections.observableArrayList();
    public TreeTableColumn contact_col;
    public TreeTableColumn appt_ID_col;
    public TreeTableColumn title_col;
    public TreeTableColumn type_col;
    public TreeTableColumn description_col;
    public TreeTableColumn start_col;
    public TreeTableColumn end_col;
    public TreeTableColumn customer_ID_col;
    public TreeTableView contacts_table;
    XYChart.Series series = new XYChart.Series();

    /**
     * navigates to appointments page
     * @param actionEvent
     * @throws IOException
     */
    public void toAppointments(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Appointments.fxml");
    }

    /**
     * navigates to customers page
     * @param actionEvent
     * @throws IOException
     */
    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Customers.fxml");
    }

    /**
     * navigates to login page and requires the use to login once again to access the app
     * @param actionEvent
     * @throws IOException
     */
    public void logout(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/login.fxml");
    }

    /**
     * custom class to display contact data inside of the tree table
     */
    class treeMember {
        String contactName;
        SimpleIntegerProperty apptID;
        String apptTitle;
        String apptType;
        String apptDescription;
        String apptStart;
        String apptEnd;
        SimpleIntegerProperty apptCustomerID;

        public String getContactName() {
            return contactName;
        }

        public SimpleIntegerProperty getApptID() {
            return apptID;
        }

        public String getApptTitle() {
            return apptTitle;
        }

        public String getApptType() {
            return apptType;
        }

        public String getApptDescription() {
            return apptDescription;
        }

        public String getApptStart() {
            return apptStart;
        }

        public String getApptEnd() {
            return apptEnd;
        }

        public SimpleIntegerProperty getApptCustomerID() {
            return apptCustomerID;
        }

        public treeMember(String contactName, SimpleIntegerProperty apptID, String apptTitle, String apptType, String apptDescription, String apptStart, String apptEnd, SimpleIntegerProperty apptCustomerID) {
            this.contactName = contactName;
            this.apptID = apptID;
            this.apptTitle = apptTitle;
            this.apptType = apptType;
            this.apptDescription = apptDescription;
            this.apptStart = apptStart;
            this.apptEnd = apptEnd;
            this.apptCustomerID = apptCustomerID;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        xAxis.setLabel("Appointment Type");
        yAxis.setLabel("# of Appointments");
        try {
            appointments = appointmentDAO.getAllAppointments(Login.currentUser.userId);
            customers = CustomerDAO.getAllCustomers();
            contacts = ContactDAO.getAllContacts();
            avg_appt_length.setText(getAvgApptLength().toString()
                    .substring(2)
                    .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                    .toLowerCase());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        TreeItem<treeMember> root = new TreeItem<treeMember>(new treeMember("Contacts", null, "", "","", "", "", null));
        for (Contact contact: contacts){
            TreeItem contactMember = new TreeItem<treeMember>(new treeMember(contact.getContactName(), null, "", "","", "", "", null));
            for (Appointment appt : appointments){
                if (appt.getContactId() == contact.getContactId()) {
                    treeMember test = new treeMember(contact.getContactName(), new SimpleIntegerProperty(appt.getAppointmentId()), appt.getTitle(), appt.getType(), appt.getDescription(), appt.getStart().toString(), appt.getEnd().toString(),new SimpleIntegerProperty(appt.getCustomerId()));
                    TreeItem<treeMember> apptMember = new TreeItem<treeMember>(new treeMember(contact.getContactName(), new SimpleIntegerProperty(appt.getAppointmentId()), appt.getTitle(), appt.getType(), appt.getDescription(), appt.getStart().toString(), appt.getEnd().toString(),new SimpleIntegerProperty(appt.getCustomerId())));
                    contactMember.getChildren().add(apptMember);
                }
            }
            root.getChildren().add(contactMember);
        }

        contact_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getContactName()));
        appt_ID_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, Number>, ObservableValue<Number>>) cellDataFeatures -> (ObservableValue<Number>) cellDataFeatures.getValue().getValue().getApptID());
        title_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getApptTitle()));
        type_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getApptType()));
        description_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getApptDescription()));
        start_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getApptStart()));
        end_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, String>, ObservableValue<String>>) cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getValue().getApptEnd()));
        customer_ID_col.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<treeMember, Number>, ObservableValue<Number>>) cellDataFeatures -> (ObservableValue<Number>) cellDataFeatures.getValue().getValue().getApptCustomerID());


        contacts_table.setRoot(root);
        contacts_table.setShowRoot(false);
//      getting and setting date for bar graph
//      uses a hashmap to get a quantity of each appointment type
        /**
         * creating a hashmap to display key value pairs in the bar graph
         */
        HashMap<String, Integer> apptTypes = new HashMap<String, Integer>();
        for (Appointment appt : appointments) {
            String type = appt.getType();
            if (apptTypes.containsKey(type)) {
                apptTypes.put(type, apptTypes.get(type) + 1);
            } else {
                apptTypes.put(type, 1);
            }
        }
        Iterator<Entry<String, Integer>> new_Iterator
                = apptTypes.entrySet().iterator();
        while (new_Iterator.hasNext()) {
            Map.Entry<String, Integer> new_Map = (Map.Entry<String, Integer>)
                    new_Iterator.next();
            series.getData().add(new XYChart.Data(new_Map.getKey(), new_Map.getValue()));
        }
        appointments_graph.getData().addAll(series);
        appointmentAlert();
    }

    /**
     * method that returns the avg appointment length for all of a users appointments
     * @return
     */
    public Duration getAvgApptLength(){
        Duration sum = Duration.ZERO;
        for (Appointment appt: appointments){
            Instant start = appt.getStart().atZone(ZoneId.systemDefault()).toInstant();
            Instant end = appt.getEnd().atZone(ZoneId.systemDefault()).toInstant();
            Duration res = Duration.between(start, end);
            sum = sum.plus(res);
        }
        return sum.dividedBy(appointments.size());
    }

    /**
     * method to alert the user if they have an appointment in 15 minutes from login
     */
    public void appointmentAlert(){
        LocalDateTime now = LocalDateTime.now();
        Duration timeBetween = Duration.ZERO;
        for (Appointment appt: appointments){
            timeBetween = Duration.between(now,appt.getStart());
            if (timeBetween.toMinutes() <=15 && timeBetween.toMinutes() >= 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment Imminent");
                alert.setHeaderText("you have an appointment: " + appt.getAppointmentId());
                Optional<ButtonType> confirm = alert.showAndWait();
            }
        }
    }
}
