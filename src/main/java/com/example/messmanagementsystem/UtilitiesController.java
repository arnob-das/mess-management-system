package com.example.messmanagementsystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.DatabaseController;
import util.SharedData;

import java.io.IOException;
import java.util.Optional;

public class UtilitiesController {

    @FXML
    private TextField utilityNameField;

    @FXML
    private TextField utilitiesCostField;
    @FXML
    private ComboBox<String> selectUtilityName;
    @FXML
    private TextField selectedUtilityAmount;

    ObservableList<String> allUtilitiesNames;
    @FXML
    private Button adminBtn;

    String email,messName,selectedUtility;
    @FXML
    private Label addUtilityError;
    @FXML
    private Label updateDeleteUtilityError;
    double selectedUtilityPrice= 0.0;

    @FXML
    public void initialize(){
        DatabaseController db = new DatabaseController();
        db.database();

        // hide error labels
        addUtilityError.setVisible(false);
        updateDeleteUtilityError.setVisible(false);

        email = db.getCurrentUserEmail();
        messName = db.getMessName(email);

        if(db.isAdmin(email)){
            adminBtn.setVisible(true);
        }
        else{
            adminBtn.setVisible(false);
        }

        allUtilitiesNames = FXCollections.observableArrayList();
        allUtilitiesNames.addAll(db.getUtilityNamesByCurrentMonth(messName));

        // set items to combobox
        selectUtilityName.setItems(allUtilitiesNames);


        // Add a selection listener to the ComboBox
        selectUtilityName.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedUtility = newValue;
                selectedUtilityPrice = db.getUtilityPriceByCurrentMonthAndMessName(selectedUtility,messName);
                selectedUtilityAmount.setText(Double.toString(selectedUtilityPrice));
            }
        });
    }

    @FXML
    void deleteSelectedUtility(ActionEvent event) {

        if(selectUtilityName.getValue() == null){
            updateDeleteUtilityError.setText("Select Utility");
            updateDeleteUtilityError.setVisible(true);
            return;
        }
        else{
            updateDeleteUtilityError.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();

        db.deleteUtility(selectedUtility,messName);
    }
    @FXML
    void updateSelectedUtility(ActionEvent event) {

        if(selectUtilityName.getValue() == null || selectedUtilityAmount.getText().isEmpty()){
            updateDeleteUtilityError.setText("Select Utility");
            updateDeleteUtilityError.setVisible(true);
            return;
        }else{
            updateDeleteUtilityError.setVisible(false);
        }

        String selectedUtilityPrice = selectedUtilityAmount.getText().toString();
        double newPrice = Double.parseDouble(selectedUtilityPrice);
        DatabaseController db = new DatabaseController();
        db.database();

        db.updateUtilityPriceByCurrentMonthAndMessName(selectedUtility,messName,newPrice);
    }
    @FXML
    void addUtilities(ActionEvent event) {
        if(utilityNameField.getText().isEmpty() || utilitiesCostField.getText().isEmpty()){
            addUtilityError.setText("Utility Name or Utility cost is empty !");
            addUtilityError.setVisible(true);
            return;
        }
        else{
            addUtilityError.setVisible(false);
        }
        DatabaseController db = new DatabaseController();
        db.database();
        String utilityName = utilityNameField.getText();
        double utilityPrice = Double.parseDouble(utilitiesCostField.getText());
        db.addUtilitiesMarketCostByCurrentDate(utilityName,utilityPrice,messName);

    }

    @FXML
    void gotoDashboardPage(ActionEvent event) throws IOException {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Home.fxml"));
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
    @FXML
    void gotoMealPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Meals.fxml"));
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
    @FXML
    void signOutAction(ActionEvent event) throws  IOException{
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

    public void gotoProfilePage(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
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

    public void gotoAdminPage(ActionEvent event) {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
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
