package C195.Controllers;

import C195.Helpers.JDBC;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField username_input;
    public PasswordField password_input;
    public Button login_submit;
    public Label welcome_label;
    public Label login_label;
    public Label username_label;
    public Label password_label;
    public Label or_label;
    public Hyperlink signup_link;
    public Label forgot_label;
    public Hyperlink reset_user_link;
    public Hyperlink reset_password_link;
    public Label your_location_label;
    public Label location_label;
    public Label error_label;

    public void login(){
        String username = username_input.getText();
        String db_username = "";
        String password = password_input.getText();
        String db_userPass = "";
        ResultSet rs;
        Boolean pass_toggle = false, user_toggle = false;
        try{
            Connection connection = JDBC.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT User_Name, Password FROM users WHERE User_Name = '" + username + "'";
            rs = stmt.executeQuery(query);
            while ( rs.next() ){
                db_username = rs.getString("User_Name");
                db_userPass = rs.getString("Password");
            }

            if (db_userPass.equals(password) && db_userPass.length() > 0) {
                pass_toggle = true;
            } else if (db_username.length() > 0 && !(db_userPass.equals(password) && db_userPass.length() > 0)) {
                error_label.setText("wrong password buckaroo!");
            }

            if (db_username.equals(username) && db_username.length() > 0){
                user_toggle = true;
            } else {
                error_label.setText("no user found");
            }

            if (user_toggle && pass_toggle){
                System.out.println("login successful!");
            }

        } catch(Exception err) {
            System.out.println("houston we have a problem");
            System.out.println(err);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error_label.setText("");
        String currentLang = Locale.getDefault().getDisplayLanguage();
        ZoneId z = ZoneId.systemDefault();
        location_label.setText(z.toString());
        System.out.println(currentLang);
        if (currentLang.equals("French")){
            welcome_label.setText("Interface graphique métier Java");
            welcome_label.setFont(new Font("System", 20));
            login_label.setText("Connexion");
            or_label.setText("ou");
            signup_link.setText("S'inscrire");
            your_location_label.setText("Votre emplacement");
            your_location_label.setFont(new Font("System", 12));
            username_label.setText("Nom d'utilisateur");
            password_label.setText("le mot de passe");
            forgot_label.setText("oublié votre");
            reset_password_link.setText("ou mot de passe");
            reset_user_link.setText("Nom d'utilisateur");
        }
    }
}
