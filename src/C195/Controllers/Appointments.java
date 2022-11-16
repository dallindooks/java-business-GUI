package C195.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
    public TableColumn id_col;
    public TableColumn title_col;
    public TableColumn description_col;
    public TableColumn location_col;
    public TableColumn contact_col;
    public TableColumn type_col;
    public TableColumn start_col;
    public TableColumn end_col;
    public TableColumn customer_id_col;
    public TableColumn user_id_col;
    public TableView appointment_table;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
