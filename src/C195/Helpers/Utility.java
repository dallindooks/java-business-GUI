package C195.Helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Utility {

    public static boolean confirmDelete(String title, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Optional<ButtonType> confirm = alert.showAndWait();
        return confirm.get() == ButtonType.OK;
    }

    public static String localTimeToUTC(String localInput){
       return LocalDateTime.parse(localInput, DateTimeFormatter.ofPattern( "dd-MM-uuuu HH:mm:ss" )).atOffset(ZoneOffset.UTC).toString();
    }
}
