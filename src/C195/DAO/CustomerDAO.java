package C195.DAO;

import C195.Helpers.JDBC;
import C195.Models.Contact;
import C195.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class CustomerDAO {
    /**
     * method to get all customers
     * @return returns a list of customers
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customersResult = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM customers";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");
            Customer customer = new Customer(customerId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
            customersResult.add(customer);
        }
        return customersResult;
    }

    /**
     * method to get a list of customer names
     * @return returns a list of strings
     */
    public static ObservableList<String> getAllCustomerNames(){
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        try {
            customerNames = getAllCustomers().stream().map(Customer::getName).collect(Collectors.toCollection(FXCollections::observableArrayList));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerNames;
    }

    /**
     * method to get a single customer by name
     * @param customerName customer name string input
     * @return returns a single customer
     * @throws SQLException
     */
    public static Customer getCustomerByName(String customerName) throws SQLException {
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM customers WHERE Customer_Name = '" + customerName + "'";
        ResultSet rs = stmt.executeQuery(query);
        Customer customer = null;
        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");
            customer = new Customer(customerId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
        }
        return customer;
    }

    /**
     * method to delete a customer
     * @param Id customer id input
     * @throws SQLException
     */
    public static void deleteCustomer(int Id) throws SQLException {
        Statement statement = JDBC.getConnection().createStatement();
        String query = "DELETE FROM customers WHERE Customer_ID=" + Id;
        statement.execute(query);
    }
}
