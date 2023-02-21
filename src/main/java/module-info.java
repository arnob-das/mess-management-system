module com.example.messmanagementsystem {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires firebase.admin;
//    requires com.google.auth.oauth2;

    requires javafx.controls;
    requires javafx.fxml;
    requires firebase.admin;
    requires google.cloud.firestore;
    requires com.google.auth.oauth2;
    requires com.google.auth;
    requires mongo.java.driver;
    requires jbcrypt;
    requires passay;
    requires activation;
    requires appengine.api;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires org.apache.commons.codec;
//    requires com.google.api.services.gmail;
    requires twilio;
//    requires mailgun.java;
//    requires java.mail;


    opens com.example.messmanagementsystem to javafx.fxml;
    exports com.example.messmanagementsystem;
    exports util;
    opens util to javafx.fxml;
}