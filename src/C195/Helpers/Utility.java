package C195.Helpers;

import C195.Models.Country;
import C195.Models.Customer;
import C195.Models.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

public class Utility {
    /**
     * confrim delete alert method
     * @param title set title parameter
     * @param header set header parameter
     * @return returns a boolean value
     */
    public static boolean confirmDelete(String title, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Optional<ButtonType> confirm = alert.showAndWait();
        return confirm.get() == ButtonType.OK;
    }

    /**
     * method to change scenes. Condenses 4 lines of code into one throughout the app
     * @param actionEvent a button click
     * @param location string with destination scene
     * @throws IOException
     */
    public static void changeScene(ActionEvent actionEvent, String location) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Utility.class.getResource(location)));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    //    Logic to convert EST business hours to users local time

    /**
     * method to get the open and close time of a users local time given that appointments can
     * only be scheduled from 8:00 AM to 10:00PM EST
     * @param open local time open
     * @param close local time close
     * @return returns two integers representing the new open and close times
     */
    public static int[] getBusinessHours(int open, int close){
        TimeZone estTZ = TimeZone.getTimeZone("EST");
        Calendar estCalEight= Calendar.getInstance(estTZ);
        Calendar estCalTen= Calendar.getInstance(estTZ);
        estCalEight.set(Calendar.HOUR_OF_DAY,open);
        estCalEight.set(Calendar.MINUTE,00);
        estCalTen.set(Calendar.HOUR_OF_DAY,close);
        estCalTen.set(Calendar.MINUTE,00);
        java.util.Date estEight = estCalEight.getTime();
        java.util.Date estTen = estCalTen.getTime();
        int openLocal = estEight.getHours();
        int closeLocal = estTen.getHours();
        int[] outArr = new int[]{openLocal, closeLocal};
        return outArr;
    }

    /**
     * custom method to convert a string of time formatted as 0:00 AM into a localdatetime
     * @param localDate localdate to tell what locale the new date should be in
     * @param stringToDate string to tear up and piece back together
     * @return returns a localdatetime
     */
    public static LocalDateTime dateStringFormatter(LocalDate localDate, String stringToDate){
        String[] stringArr = stringToDate.split(" ");
        String formattedTime = "";
        if (stringArr[1].equals("PM")) {
            String[] stringNumArr = stringArr[0].split(":");
            int stringTimeNum = parseInt(stringNumArr[0]) == 12 ? parseInt(stringNumArr[0]) : parseInt(stringNumArr[0]) + 12;
            formattedTime = String.valueOf(stringTimeNum).concat(":").concat(stringNumArr[1]);
        } else {
            String[] stringNumArr = stringArr[0].split(":");
            int stringTimeNum = parseInt(stringNumArr[0]) == 12 ? 0 : parseInt(stringNumArr[0]);
            formattedTime = (stringTimeNum >= 10) ? stringArr[0] : "0".concat(stringArr[0]);
        };
        LocalDateTime formattedDateTime = LocalDateTime.of(localDate, LocalTime.parse(formattedTime));
        return formattedDateTime;
    }

    /**
     * method to get all divisions
     * @return returns a list of divisions
     * @throws SQLException
     */
    public static ObservableList<Division> getAllDivisions() throws SQLException {
        ObservableList<Division> divisionResult = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM first_level_divisions";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            Date createDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");
            Division div = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            divisionResult.add(div);
        }
        return divisionResult;
    }

    /**
     * method to get all countries
     * @return returns a list of countries
     * @throws SQLException
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> countryResult = FXCollections.observableArrayList();
        Connection connection = JDBC.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM countries";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){
            int countryId = rs.getInt("Country_ID");
            String country = rs.getString("Country");
            Date createDate = rs.getDate("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            Country newCountry = new Country(countryId, country, createDate, createdBy, lastUpdate, lastUpdatedBy);
            countryResult.add(newCountry);
        }
        return countryResult;
    }

    /**
     * custom class to populate the customer table with their division and country
     */
    public static class CustomerTable{
        public int customerId;
        public String name;
        public String address;
        public String postalCode;
        public String phone;
        public String division;
        public String country;


        public CustomerTable(int customerId, String name, String address, String postalCode, String phone, String division, String country) {
            this.customerId = customerId;
            this.name = name;
            this.address = address;
            this.postalCode = postalCode;
            this.phone = phone;
            this.division = division;
            this.country = country;
        }

        public String getDivision() {
            return division;
        }

        public String getCountry() {
            return country;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public String getPhone() {
            return phone;
        }
    }
}
