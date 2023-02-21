package com.example.messmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseController;

import java.io.IOException;

public class ProfileController {
    @FXML
    private Label updatePasswordLabel;

    @FXML
    private Text displayEmail;

    @FXML
    private Text displayName;

    @FXML
    private Text messName;

    @FXML
    private TextField newPassword;
    @FXML
    private Button adminBtn;

    @FXML
    private Text userRole;

    String email;

    public void initialize(){
        DatabaseController db = new DatabaseController();
        db.database();

        updatePasswordLabel.setVisible(false);

        email = db.getCurrentUserEmail();

        String role;

        if(db.isAdmin(email)){
            role="ADMIN";
            adminBtn.setVisible(true);
        }
        else {
            role = "USER";
            adminBtn.setVisible(false);
        }

        displayName.setText(db.getUserName(email));
        messName.setText("Mess Name : "+ db.getMessName(email));
        displayEmail.setText(email);
        userRole.setText("Role : "+role);
    }

    @FXML
    void updateNewPassword(ActionEvent event) {
        String password = newPassword.getText();

        if(password == null || password.isEmpty()){
            updatePasswordLabel.setVisible(true);
            return;
        }
        else {
            updatePasswordLabel.setVisible(false);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


        DatabaseController db = new DatabaseController();
        db.database();

        db.updateUsersPassword(email,hashedPassword);
    }

    @FXML
    void gotoDashboardPage(ActionEvent event) throws IOException {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void gotoAdminPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    void gotoMealPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Meals.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void gotoUtilitiesPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Meals.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void signOutUser(ActionEvent event) {
        try{
            // delete current user
            DatabaseController db = new DatabaseController();
            db.database();
            db.deleteCurrentUser();

            Parent root = FXMLLoader.load(getClass().getResource("welcome.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Hi");
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        finally {
            DatabaseController db = new DatabaseController();
            db.database();
            db.deleteCurrentUser();
        }
    }

}
