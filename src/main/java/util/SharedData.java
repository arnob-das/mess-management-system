package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SharedData {
    public static String email;
    public static boolean isUserAdmin;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        SharedData.email = email;
    }

}
