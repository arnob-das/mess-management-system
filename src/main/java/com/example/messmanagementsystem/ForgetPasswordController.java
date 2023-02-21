package com.example.messmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseController;

import java.io.IOException;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;

import java.util.Random;

public class ForgetPasswordController {

    @FXML
    private TextField resetPasswordEmailText;
    @FXML
    private Label emailErrorLabel;

    String email;

    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    @FXML
    public void initialize(){
        emailErrorLabel.setVisible(false);
    }

    @FXML
    void sendResetPasswordAction(ActionEvent event) {

        DatabaseController db = new DatabaseController();
        db.database();

        email = resetPasswordEmailText.getText();

        if(!db.getEmail(email)){
            emailErrorLabel.setText("Email Address does not exists !");
            emailErrorLabel.setVisible(true);
            return;
        }
        else{
            emailErrorLabel.setVisible(false);
        }

        String password = generatePassword(10);
        //String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        String phoneNum = "+88"+db.getUserPhoneNo(email);

        db.updateUsersPassword(email,hashedPassword);

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("whatsapp:"+phoneNum),
                        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                        "Your Mess Management System Password is "+password)
                .create();

        System.out.println(message.getSid());
        System.out.println(password); // PV&FM8P_j)


    }

    public static String generatePassword(int length) {
        String password = "";

        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_-+=?";

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password += characters.charAt(index);
        }

        return password;
    }

    @FXML
    void gotoLoginPage(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
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



