package C195.Controllers;

import C195.DAO.userDAO;
import C195.Helpers.JDBC;
import C195.Helpers.Utility;
import C195.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
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
    String currentLang = Locale.getDefault().getDisplayLanguage();
    public static User currentUser;

    /**
     * This is the method that perfroms logical checks to compare the username and password inputs against the database
     * @param actionEvent
     * @throws IOException
     */
    public void login(ActionEvent actionEvent) throws IOException {
        PrintWriter loginTracker = new PrintWriter(new FileWriter("login_activity.txt", true));
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
                if (currentLang.equals("French")){
                    error_label.setText("Mot de passe incorrect");
                } else {
                    error_label.setText("wrong password buckaroo!");
                }
            }

            if (db_username.equals(username) && db_username.length() > 0){
                user_toggle = true;
            } else {
                if (currentLang.equals("French")){
                    error_label.setText("aucun utilisateur trouv??");
                } else {
                    error_label.setText("no user found");
                }
                loginTracker.println("User login attempt! userName Input = " + username + " FAILED to log in at: " + Timestamp.valueOf(LocalDateTime.now()));

            }

            if (user_toggle && pass_toggle){
                System.out.println("login successful!");
                loginTracker.println("User login attempt! userName Input = " + username + " SUCCESSFULLY logged in at: " + Timestamp.valueOf(LocalDateTime.now()));
                currentUser = userDAO.getUser(db_username);
                System.out.println(currentUser.username + " is currently logged in");
                toAppointments(actionEvent);
            }

        } catch(Exception err) {
            System.out.println("houston we have a problem");
            System.out.println(err);
        }
        loginTracker.close();
    }

    /**
     * navigates to the appointments page
     * @param actionEvent
     * @throws IOException
     */
    private void toAppointments(ActionEvent actionEvent) throws IOException {
        Utility.changeScene(actionEvent, "/C195/Views/Reports.fxml");
    }

    /**
     * gets the currently logged in user. This static method is references throughout the app.
     * @return
     */
    public static User getLoggedInUser(){
        return currentUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error_label.setText("");
        ZoneId z = ZoneId.systemDefault();
        location_label.setText(z.toString());
        System.out.println(currentLang);
        if (currentLang.equals("French")){
            welcome_label.setText("Interface graphique m??tier Java");
            welcome_label.setFont(new Font("System", 20));
            login_label.setText("Connexion");
            or_label.setText("ou");
            signup_link.setText("S'inscrire");
            your_location_label.setText("Votre emplacement");
            your_location_label.setFont(new Font("System", 12));
            username_label.setText("Nom d'utilisateur");
            password_label.setText("le mot de passe");
            forgot_label.setText("oubli?? votre");
            reset_password_link.setText("ou mot de passe");
            reset_user_link.setText("Nom d'utilisateur");
        }
    }
}
