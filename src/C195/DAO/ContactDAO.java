package C195.DAO;

import C195.Helpers.JDBC;
import C195.Models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class ContactDAO {
    /**
     * method to get all contacts
     * @return returns a list of contacts
     * @throws SQLException
     */
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        ObservableList<Contact> contactsResult = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM contacts";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            int contactId = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            Contact contact = new Contact(contactId, name, email);
            contactsResult.add(contact);
        }
        return contactsResult;
    }

    /**
     * method to get all contact names
     * @return returns a list of strings
     */
    public static ObservableList<String> getAllContactNames(){
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        try {
            contactNames = getAllContacts().stream().map(Contact::getContactName).collect(Collectors.toCollection(FXCollections::observableArrayList));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactNames;
    }

    /**
     * gets a specific contact by name
     * @param contactName name of a contact
     * @return returns one Contact
     * @throws SQLException
     */
    public static Contact getContactByName(String contactName) throws SQLException {
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM contacts WHERE Contact_Name = '" + contactName + "'";
        ResultSet rs = stmt.executeQuery(query);
        Contact contact = null;
        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");
            contact = new Contact(contactId, name, email);
        }
        return contact;
    }
}
