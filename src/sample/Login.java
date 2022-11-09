package sample;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.ResultSet;

public class Login {
    public TextField username_input;
    public PasswordField password_input;
    public Button login_submit;

    public boolean login(){
        String username = username_input.getText();
        String password = password_input.getText();
        ResultSet rs;
        try{

        } catch(Exception err) {
            System.out.println("houston we have a problem");
            System.out.println(err);
        }
        return false;
    }
}
