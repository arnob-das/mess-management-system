package com.example.messmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import org.bson.Document;
import util.DatabaseController;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;


public class SignUpController {
    @FXML
    private TextField displayMessNameField;
    @FXML
    private Label messNameErrorField;
    @FXML
    private TextField displayNameField;
    @FXML
    private Label fullNameErrorField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneNoField;
    @FXML
    private Label phoneNoErrorField;

    @FXML
    private TextField nidField;
    @FXML
    private Label signUpErrorLabel;
    @FXML
    private Label emailErrorField;
    @FXML
    private Label nidErrorField;


    @FXML
    protected void signUpActionForUser(ActionEvent event) throws Exception {
        try{
            String name = displayNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String messName = displayMessNameField.getText();

            DatabaseController db = new DatabaseController();

            db.database("users");

            if(db.getMessNameForUser(messName)){
                messNameErrorField.setText("Mess Does Not Name Exists!");
                messNameErrorField.setVisible(true);
                return;
            }else {
                messNameErrorField.setVisible(false);
            }

            if(db.getFullName(name)){
                fullNameErrorField.setText("Name Exists!");
                fullNameErrorField.setVisible(true);
                return;
            }else {
                fullNameErrorField.setVisible(false);
            }

            if(db.getEmail(email)) {
                emailErrorField.setText("Email Already Registered!");
                emailErrorField.setVisible(true);
                return;
            } else {
                emailErrorField.setVisible(false);
            }
            if(db.getNidNo(nidField.getText())){
                nidErrorField.setText("Nid No Exists!");
                nidErrorField.setVisible(true);
                return;
            }
            else{
                nidErrorField.setVisible(false);
            }
            if(db.getphoneNo(phoneNoField.getText())){
                phoneNoErrorField.setText("Phone No Exists!");
                phoneNoErrorField.setVisible(true);
                return;
            }
            else{
                phoneNoErrorField.setVisible(false);
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Document user = new Document()
                    .append("displayName", name)
                    .append("email", email)
                    .append("password", hashedPassword)
                    .append("phoneNo", phoneNoField.getText())
                    .append("nidNo", nidField.getText())
                    .append("messName",messName);
            db.insertUSer(user);

            // close the database after operation
            db.close();
//                new FirebaseConfig().initFirebase();
//                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
//                        .setEmail(email)
//                        .setEmailVerified(false)
//                        .setPassword(hashedPassword)
//                        .setDisabled(false);
//
//                UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
//                System.out.println("Successfully created new user: " + userRecord.getUid());

            // clear the field after successful sign up
            displayNameField.clear();
            emailField.clear();
            passwordField.clear();
            displayMessNameField.clear();

            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception ex) {
            signUpErrorLabel.setText(ex.getMessage());
            signUpErrorLabel.setVisible(true);
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    protected void signUpActionForManager(ActionEvent event) {
        try{
            String name = displayNameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String messName = displayMessNameField.getText();

            DatabaseController db = new DatabaseController();

            db.database("users");

            if(db.getMessNameForManager(messName)){
                messNameErrorField.setText("Mess Name Exists!");
                messNameErrorField.setVisible(true);
                return;
            }else {
                messNameErrorField.setVisible(false);
            }

            if(db.getFullName(name)){
                fullNameErrorField.setText("Name Exists!");
                fullNameErrorField.setVisible(true);
                return;
            }else {
                fullNameErrorField.setVisible(false);
            }

            if(db.getEmail(email)) {
                emailErrorField.setText("Email Already Registered!");
                emailErrorField.setVisible(true);
                return;
            } else {
                emailErrorField.setVisible(false);
            }
            if(db.getNidNo(nidField.getText())){
                nidErrorField.setText("Nid No Exists!");
                nidErrorField.setVisible(true);
                return;
            }
            else{
                nidErrorField.setVisible(false);
            }
            if(db.getphoneNo(phoneNoField.getText())){
                phoneNoErrorField.setText("Phone No Exists!");
                phoneNoErrorField.setVisible(true);
                return;
            }
            else{
                phoneNoErrorField.setVisible(false);
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            Document user = new Document()
                    .append("displayName", name)
                    .append("email", email)
                    .append("password", hashedPassword)
                    .append("phoneNo", phoneNoField.getText())
                    .append("nidNo", nidField.getText())
                    .append("messName",messName)
                    .append("role", "admin");
            db.insertUSer(user);

            // close the database after operation
            db.close();
//                new FirebaseConfig().initFirebase();
//                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
//                        .setEmail(email)
//                        .setEmailVerified(false)
//                        .setPassword(hashedPassword)
//                        .setDisabled(false);
//
//                UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
//                System.out.println("Successfully created new user: " + userRecord.getUid());

            // clear the field after successful sign up
            displayNameField.clear();
            emailField.clear();
            passwordField.clear();
            displayMessNameField.clear();

            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception ex) {
            signUpErrorLabel.setText(ex.getMessage());
            signUpErrorLabel.setVisible(true);
            System.out.println(ex.getMessage());
        }
    }
    @FXML
    public void gotoSignInPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Scene scene = new Scene(root,1000,600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}