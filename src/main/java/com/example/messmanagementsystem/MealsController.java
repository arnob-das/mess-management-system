package com.example.messmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.DatabaseController;
import util.SharedData;

import java.io.IOException;
import java.time.LocalDate;


public class MealsController {

    DatabaseController db = new DatabaseController();


    @FXML
    private DatePicker mealDate;
    @FXML
    private Label addMealError;

    @FXML
    private TextField mealNumbersField;
    @FXML
    private TextField depositAmountText;
    @FXML
    private TextField marketCost;
    @FXML
    private Text nextMarketerText;
    @FXML
    private Text currentMarketerText;

    @FXML
    private TextField nameListForAddMeal;
    @FXML
    private TextField nameForMealMarketCostField;

    @FXML
    private TextField nameForDepositAmount;
    @FXML
    private Button adminBtn;
    @FXML
    private Label marketCostError;
    @FXML
    private Label depositAmountError;


    String messName;
    String email;
    public void initialize(){

        // hide all errors message
        marketCostError.setVisible(false);
        depositAmountError.setVisible(false);
        addMealError.setVisible(false);


        try{
            DatabaseController db = new DatabaseController();
            db.database();

            email = db.getCurrentUserEmail();
            messName= db.getMessName(email);

            if(db.isAdmin(email)){
                adminBtn.setVisible(true);
            }
            else{
                adminBtn.setVisible(false);
            }

            // initialize local date with current date
//            mealDate.setValue(LocalDate.now());
//
//            // initially set meal number text field for current date meal number of the user
//            try{
//                mealNumbersField.setText(Integer.toString(db.findMealNumber(email,messName,LocalDate.now())));
//            }catch (Exception ex){
//                System.out.println(ex.getMessage());
//            }

            mealDate.setOnAction(event -> {
                LocalDate selectedDate = mealDate.getValue();

                mealNumbersField.setText(Integer.toString(db.findMealNumber(email,messName,selectedDate)));
            });



            nameListForAddMeal.setText(db.getUserName(email));
            nameForMealMarketCostField.setText(db.getUserName(email));
            nameForDepositAmount.setText(db.getUserName(email));

            currentMarketerText.setText(db.getCurrentMealMarketer(messName));
            nextMarketerText.setText(db.getNextMealMarketer(messName));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    void addMealAction(ActionEvent event) {

        if(mealNumbersField.getText().isEmpty() || mealDate.getValue() == null){
            addMealError.setText("Meal Number or Date is Invalid");
            addMealError.setVisible(true);
            return;
        }
        else {
            addMealError.setVisible(false);
        }

        db.database();
        LocalDate date = mealDate.getValue();

        int mealNumForUser = Integer.parseInt(mealNumbersField.getText());

        db.addMeal(email,mealNumForUser,date,messName);
    }

    @FXML
    void addMealMarketAction(ActionEvent event) {
        if(marketCost.getText().isEmpty()){
            marketCostError.setText("Field Empty or Invalid Amount");
            marketCostError.setVisible(true);
            return;
        }
        else{
            marketCostError.setVisible(false);
        }
        db.database();
        double mealMarketCost = Double.parseDouble(marketCost.getText());
        db.addMealMarketCostByCurrentMonth(email,mealMarketCost,messName);
    }

    @FXML
    void addDepositAction(ActionEvent event) {
        if(depositAmountText.getText().isEmpty()){
            depositAmountError.setText("Field Empty or Invalid Amount");
            depositAmountError.setVisible(true);
            return;
        }
        else{
            depositAmountError.setVisible(false);
        }
        try{
            db.database();
            db.addDepositMoneyForMealMarket(email,Double.parseDouble(depositAmountText.getText()),messName);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
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
    void gotoUtilitiesPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("UtilitiesPage.fxml"));
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

    @FXML
    void gotoProfilePage(ActionEvent event) {
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

    @FXML
    void gotoAdminPage(ActionEvent event) {
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
