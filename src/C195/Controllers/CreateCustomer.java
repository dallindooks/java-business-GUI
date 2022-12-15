package C195.Controllers;

import C195.Helpers.JDBC;
import C195.Helpers.Utility;
import C195.Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CreateCustomer implements Initializable {
    public TextField id_input;
    public TextField name_input;
    public TextField address_input;
    public TextField postal_input;
    public TextField phone_input;
    public ComboBox division_input;
    public Label name_error;
    public Label address_error;
    public Label postal_error;
    public Label phone_error;
    public Label division_error;

    public ObservableList<Division> divisions = FXCollections.observableArrayList();

    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Customers.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_error.setText("");
        address_error.setText("");
        postal_error.setText("");
        phone_error.setText("");
        division_error.setText("");
        try{
            divisions = Utility.getAllDivisions();
            id_input.setText(String.valueOf(getNewCustomerId()));
            division_input.setItems(divisions.stream().map((div) -> div.getDivision()).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * this method is used to create and add a customer to the database. It also contains the logical checks to ensure input validity.
     * @param actionEvent
     * @throws SQLException
     */
    public void addCustomer(ActionEvent actionEvent) throws SQLException {
        try {
            int checksToggle5 = 0;

//            input validation begins here
            if (name_input.getText().equals("")) {
                name_error.setText("Please enter a Name");
            } else {
                checksToggle5++;
                name_error.setText("");
            }

            if (address_input.getText().equals("")) {
                address_error.setText("Please enter an Address");
            } else {
                checksToggle5++;
                address_error.setText("");
            }

            if (postal_input.getText().equals("")) {
                postal_error.setText("Please enter a Postal Code");
            } else {
                checksToggle5++;
                postal_error.setText("");
            }

            if (phone_input.getText().equals("")) {
                phone_error.setText("Please enter a Phone");
            } else {
                checksToggle5++;
                phone_error.setText("");
            }

            if (division_input.getValue().equals(null)){
                division_error.setText("Please select a division");
            } else {
                checksToggle5++;
                division_error.setText("");
            }

//          end of validation
//          upload new appointment
            Connection connection = JDBC.getConnection();
            String query = "INSERT INTO customers VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(id_input.getText()));
            ps.setString(2, name_input.getText());
            ps.setString(3, address_input.getText());
            ps.setString(4, postal_input.getText());
            ps.setString(5, phone_input.getText());
            ps.setDate(6, Date.valueOf(LocalDate.now()));
            ps.setString(7, Login.currentUser.username);
            ps.setDate(8, Date.valueOf(LocalDate.now()));
            ps.setString(9, Login.currentUser.username);
            ps.setInt(10, divisions.stream().filter((div) -> div.getDivision() == division_input.getValue()).findFirst().get().getDivisionId());
            if (checksToggle5 == 5) {
                ps.execute();
                toCustomers(actionEvent);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("Customer Created Successfully");
                Optional<ButtonType> confirm = alert.showAndWait();
            } else {
                checksToggle5 = 0;
            }
        } catch(Exception err){
            System.out.println(err);
        }

    }

    /**
     * method that returns a unique customer id
     * @return
     * @throws SQLException
     */
    public int getNewCustomerId() throws SQLException {
        int lastCustomerId = 0;
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT MAX(Customer_ID) FROM customers";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            lastCustomerId = rs.getInt("max(Customer_ID)");
        }
        return lastCustomerId + 1;
    }
}
