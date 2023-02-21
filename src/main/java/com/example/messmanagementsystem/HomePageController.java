package com.example.messmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;

import javafx.scene.text.Text;
import util.DatabaseController;
import util.SharedData;

public class HomePageController {
    @FXML
    private Text utilitiesPriceText;
    @FXML
    private Text mealRateText;
    @FXML
    private Text totalMealsNumberText;
    @FXML
    private Text houseRentText;
    @FXML
    private Text totalMealCostText;
    @FXML
    private Text totalOverfiewText;
    @FXML
    private Text userHouseRent;

    @FXML
    private Text userNameText;

    @FXML
    private Text userTotalCost;

    @FXML
    private Text userTotalMealCostText1;

    @FXML
    private Text userTotalMealsNumberText1;

    @FXML
    private Text userUtilitiesPriceText1;
    @FXML
    private  Text managerAccountText;
    @FXML
    private Text userDepositAmoutText;
    @FXML
    private Pane houseREntPane;
    @FXML
    private Pane userMealCostPane;
    @FXML
    private Pane userTotalCostPane;
    @FXML
    private Pane userTotalMealsPane;
    @FXML
    private Pane userUtilitiesPane;
    @FXML
    private Button adminBtn;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button showYourDataBtn;

    DatabaseController db = new DatabaseController();
    DecimalFormat df = new DecimalFormat("#.00");


    // for HOUSE
    int houseMember = 0;
    String email;
    String messName;
    double utilitiesCost=0;
    double houseRentCost=0;
    double totalMealsCost=0;
    int totalMealNumbers = 0;
    double onManagerAccountAmount = 0;
    double mealRate = 0;



    // for current user
    int currentUserMealNumber = 0;
    double currentUserUtilityBill = 0;
    double currentUserHouseBill = 0;
    double currentUserMealBill = 0;
    double currentUserTotalBill = 0;
    double totalDepositAmountForMealMarket = 0;
    double currentUserTotalDeposit = 0;


    @FXML
    public void initialize() {

        DatabaseController db = new DatabaseController();
        db.database();
        // getting current user email and mess name
        email = db.getCurrentUserEmail();
        messName = db.getMessName(email);

        // hide show your data button
        showYourDataBtn.setVisible(false);

        // if admin, then visible the admin button
        if(db.isAdmin(email)){
            adminBtn.setVisible(true);
        }
        else{
            adminBtn.setVisible(false);
        }

        // get local date
        LocalDate date = LocalDate.now();
        Month month = date.getMonth();
        int year = date.getYear();

        // set heading for total overview
        totalOverfiewText.setText("Total Overview, " + month +", " + year);

        // set house rent
        try{
            houseRentCost = db.getHouseRentForCurrentMonth(messName);
            String  houseRent = Double.toString(houseRentCost);
            houseRentText.setText(houseRent);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        // set utilities
        try{
            // fetch data of total utilities price for current month
            utilitiesCost = (db.getTotalUtilityCostByCurrentMonth(messName));
            // convert double to string
            String utilitiesPrice = Double.toString(utilitiesCost);
            // set text to ui
            utilitiesPriceText.setText(utilitiesPrice);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        // get total meals cost by current month
        try {
            db.database();
            totalMealsCost = db.getTotalMealMarketCostByCurrentMonth();
            String totalMealCost = Double.toString(totalMealsCost);
            totalMealCostText.setText(totalMealCost);
        }
        catch (Exception exception){
            System.out.println(exception.getMessage());
        }

        // get house members number
        try{
            houseMember = db.findAllUser(messName).size();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        // get total meal numbers
        try{
            db.database();
            totalMealNumbers = db.getMealsByEmail();
            totalMealsNumberText.setText(Integer.toString(totalMealNumbers));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        // get manager's account amount
        try{
            db.database();
            totalDepositAmountForMealMarket = db.getTotalDepositByEmail(messName);
            onManagerAccountAmount = totalDepositAmountForMealMarket - totalMealsCost;
            managerAccountText.setText(String.format("%.2f",onManagerAccountAmount));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        currentUserHouseBill = houseRentCost/houseMember;
        currentUserUtilityBill = utilitiesCost/houseMember;

        mealRate = totalMealsCost/totalMealNumbers;

        String mealRateStr = String.format("%.2f",mealRate);

        mealRateText.setText(mealRateStr);

        personalCostInfo();

        // hide dashboard btn
        dashboardBtn.setVisible(false);

    }

    @FXML
    void showYourData(ActionEvent event) {
        personalCostInfo();
    }




    public void personalCostInfo(){
        // get currentUserTotalDeposit
        try{
            db.database();
            currentUserTotalDeposit = db.getTotalDepositByEmail(email,messName);
            userDepositAmoutText.setText(String.format("%.2f",currentUserTotalDeposit));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        // get meals number for current user
        try{
            currentUserMealNumber = db.getMealsByEmail(email,messName);
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        // current user meal bill
        currentUserMealBill = (mealRate * currentUserMealNumber) - currentUserTotalDeposit;

        // current user name
        db.database("users");
        String userName = db.getUserName(email);
        userNameText.setText(userName);
        userNameText.setVisible(true);

        currentUserTotalBill = currentUserHouseBill+currentUserUtilityBill+currentUserMealBill;


        String currentUserHouseBillStr = String.format("%.2f",currentUserHouseBill);
        String currentUserUtilitiesPriceStr = String.format("%.2f",currentUserUtilityBill);
        String currentUserMealBillStr = String.format("%.2f",currentUserMealBill);
        String currentUserTotalCostStr = String.format("%.2f",currentUserTotalBill);

        userHouseRent.setText(currentUserHouseBillStr);
        userHouseRent.setVisible(true);
        houseREntPane.setVisible(true);
        userMealCostPane.setVisible(true);
        userTotalCostPane.setVisible(true);
        userTotalMealsPane.setVisible(true);
        userUtilitiesPane.setVisible(true);

        userUtilitiesPriceText1.setText(currentUserUtilitiesPriceStr);

        userTotalMealCostText1.setText(currentUserMealBillStr);

        userTotalMealsNumberText1.setText(Integer.toString(currentUserMealNumber));

        userTotalCost.setText(currentUserTotalCostStr);
    }

    @FXML
    void gotoDashboardPage(ActionEvent event) throws IOException{
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
    void gotoUtilitiesPage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("UtilitiesPage.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    // go to admin page
    @FXML
    void admin(ActionEvent event) {
        try{
            SharedData.setEmail(null);
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
    void gotoProfilePage(ActionEvent event) throws IOException{
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Profile.fxml"));
            Scene scene = new Scene(root,1000,600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
        }
    }

//    void getname(String name){
//        String n = name;
//    }

}
