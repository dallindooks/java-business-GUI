package C195.Helpers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
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

    public static boolean confirmDelete(String title, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Optional<ButtonType> confirm = alert.showAndWait();
        return confirm.get() == ButtonType.OK;
    }

    public static void changeScene(ActionEvent actionEvent, String location) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Utility.class.getResource(location)));
        Stage stage = (Stage)((javafx.scene.Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    //    Logic to convert EST business hours to users local time
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

    public static LocalDateTime dateStringFormatter(LocalDate localDate, String stringToDate){
        String[] stringArr = stringToDate.split(" ");
        String formattedTime = "";
        if (stringArr[1].equals("PM")) {
            String[] stringNumArr = stringArr[0].split(":");
            int stringTimeNum = parseInt(stringNumArr[0]) + 12;
            formattedTime = String.valueOf(stringTimeNum).concat(":").concat(stringNumArr[1]);
        } else {
            String[] stringNumArr = stringArr[0].split(":");
            formattedTime = (parseInt(stringNumArr[0]) >= 10) ? stringArr[0] : "0".concat(stringArr[0]);
        };
        LocalDateTime formattedDateTime = LocalDateTime.of(localDate, LocalTime.parse(formattedTime));
        return formattedDateTime;
    }
}
