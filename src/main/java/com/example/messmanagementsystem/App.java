package com.example.messmanagementsystem;

import com.google.firebase.auth.FirebaseAuthException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FirebaseConfig;

import java.io.IOException;


public class App extends Application {
    private static String root;

    public static void setRoot(String root) {
        App.root = root;
    }

    public static String getRoot() {
        return root;
    }


    @Override   
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("welcome.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Mess Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws FirebaseAuthException {
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        firebaseConfig.initFirebase();

        launch();

    }
}