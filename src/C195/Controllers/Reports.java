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
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

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

    public void toAppointments(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Appointments.fxml");
    }

    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Customers.fxml");
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/login.fxml");
    }

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

    }
}
