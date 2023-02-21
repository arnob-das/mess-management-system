package com.example.messmanagementsystem;

import com.google.firebase.auth.FirebaseAuth;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



import org.bson.Document;
import util.DatabaseController;
import org.mindrot.jbcrypt.BCrypt;
import util.SharedData;

import java.io.IOException;


public class LoginPageController {

    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label loginErrorField;


    @FXML
    void loginAction(ActionEvent event) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        try{
            String email = emailField.getText();
            String password = passwordField.getText();

            DatabaseController db = new DatabaseController();
            db.database("users");

            if(!db.getEmail(email) || emailField.getText().isEmpty()){
                loginErrorField.setText("Email or Password Is Incorrect");
                loginErrorField.setVisible(true);
                return;
            }
            else{
                loginErrorField.setVisible(false);
            }
            String hashedPassword=null;

            if(password!=null){
                hashedPassword = db.getPassword(email);
            }

            if (hashedPassword != null) {
                boolean isPasswordMatched = BCrypt.checkpw(password, hashedPassword);

                if (isPasswordMatched) {
                    try{
                        db.insertCurrentUser(email);
                    }
                    catch(Exception ex){
                        System.out.println(ex.getMessage() + "from login page");
                    }

                    loginErrorField.setVisible(false);

                    Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
//                    HomePageController h = root.getClass();
//                    h.getname("jsankajn");

                    Scene scene = new Scene(root,1000,600);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    // close the database after operation
                    //db.close();

                    // Show the home page
//                    stage.setScene(scene);
//                    stage.show();
                } else {
                    loginErrorField.setText("Email or Password Is Incorrect");
                    loginErrorField.setVisible(true);
                    System.out.println("password not matched");
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void gotoSignUpPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("signUpPage.fxml"));
        Scene scene = new Scene(root,1000,600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoForgetPasswordPage(ActionEvent event) throws IOException {
        try{
            SharedData.setEmail(null);
            Parent root = FXMLLoader.load(getClass().getResource("ForgetPasswordPage.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            System.out.println("Clicked");

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

}
