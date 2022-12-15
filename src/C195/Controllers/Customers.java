package C195.Controllers;

import C195.DAO.CustomerDAO;
import C195.DAO.appointmentDAO;
import C195.Helpers.Utility;
import C195.Models.Appointment;
import C195.Models.Country;
import C195.Models.Customer;
import C195.Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Customers implements Initializable {
    public TableView customer_table;
    public TableColumn id_col;
    public TableColumn name_col;
    public TableColumn address_col;
    public TableColumn postal_col;
    public TableColumn phone_col;
    public TableColumn division_col;
    public TableColumn country_col;

    public static Utility.CustomerTable selectedCustomer;
    public ObservableList<Customer> customers = FXCollections.observableArrayList();
    public ObservableList<Division> divisions = FXCollections.observableArrayList();
    public ObservableList<Country> countries = FXCollections.observableArrayList();
    public ObservableList<Utility.CustomerTable> customerTableMembers = FXCollections.observableArrayList();

    /**
     * navigates to customer create page
     * @param actionEvent
     * @throws IOException
     */
    public void toCreateCustomer(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent,"/C195/Views/CreateCustomer.fxml" );
    }

    /**
     * navigates to update customer page and sets the selected customer
     * @param actionEvent
     * @throws IOException
     */
    public void UpdateCustomer(ActionEvent actionEvent) throws IOException {
        selectedCustomer = (Utility.CustomerTable) customer_table.getSelectionModel().getSelectedItem();
        Utility.changeScene(actionEvent,"/C195/Views/UpdateCustomer.fxml" );
    }

    /**
     * deletes a selected customer and checks if the customer does not have any associated appointments
     * @param actionEvent
     * @throws SQLException
     */
    public void deleteCustomer(ActionEvent actionEvent) throws SQLException {
        selectedCustomer = (Utility.CustomerTable) customer_table.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments(Login.getLoggedInUser().userId);
        Boolean toggle = false;
        for (Appointment appt: appointments){
            if (appt.getCustomerId() == selectedCustomer.getCustomerId()) {
                toggle = true;
                Utility.confirmDelete("Cannot Delete Customer", "Customer has appointments that must be removed before deletion.");
                return;
            };
        }
        if (Utility.confirmDelete("Delete Customer", "Are you sure you want to delete '" + selectedCustomer.getName()) && !toggle){
            CustomerDAO.deleteCustomer(selectedCustomer.getCustomerId());
            customerTableMembers.remove(customerTableMembers.indexOf(selectedCustomer));
            customer_table.setItems(customerTableMembers);
        }
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
     * method that returns all customers
     * @return
     * @throws SQLException
     */
    public ObservableList<Customer> getCustomers() throws SQLException {
        return CustomerDAO.getAllCustomers();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customers = getCustomers();
            divisions = Utility.getAllDivisions();
            countries = Utility.getAllCountries();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (Customer customer: customers){
            /**
             * the lambda expression to obtain the first level division allows for one simple line of code rather than a manual for loop filter
             */
            Optional<Division> firstLevelDiv = divisions.stream().filter(div -> div.getDivisionId() == customer.getDivisionId()).findFirst();
            /**
             * the lambda expression to obtain the country allows for one simple line of code rather than a manual for loop filter
             */
            Optional<Country> country = countries.stream().filter(con -> con.getCountryId() == firstLevelDiv.get().getCountryId()).findFirst();
            Utility.CustomerTable tableMember = new Utility.CustomerTable(
                    customer.getCustomerId(),
                    customer.getCustomerName(),
                    customer.getAddress(),
                    customer.getPostalCode(),
                    customer.getPhone(),
                    firstLevelDiv.get().getDivision(),
                    country.get().getCountry()
            );
            customerTableMembers.add(tableMember);
        }

        customer_table.setItems(customerTableMembers);
        id_col.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        address_col.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal_col.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone_col.setCellValueFactory(new PropertyValueFactory<>("phone"));
        division_col.setCellValueFactory(new PropertyValueFactory<>("division"));
        country_col.setCellValueFactory(new PropertyValueFactory<>("country"));
    }


}
