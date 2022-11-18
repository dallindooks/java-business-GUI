package C195.Helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Utility {

    public static boolean confirmDelete(String title, String header){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.get() == ButtonType.OK){
            return true;
        } return false;
    }
}
