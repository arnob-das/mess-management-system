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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.DatabaseController;
import util.SharedData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

public class AdminController {
    @FXML
    private ComboBox<String> currentBazarEmailList;
    @FXML
    private ComboBox<String> addAdminEmailList;
    @FXML
    private ComboBox<String> removeAdminEmailList;
    @FXML
    private TextField houseRentForCurrentMonthText;

    @FXML
    private Text houseRentMonthYearText;

    @FXML
    private ComboBox<String> nextBazarEmailList;

    ObservableList<String> usersEmail;

    ObservableList<String> adminsEmail;

    ObservableList<String> regularUsersEmail;

    @FXML
    private Label houseRentUpdateError;

    @FXML
    private Label inactiveMemberError;
    @FXML
    private Label removeAdminError;
    @FXML
    private Label thisWeekShopError;

    @FXML
    private Label userInfoError;
    @FXML
    private Label nextWeekShopError1;
    @FXML
    private Label addAdminError;


    @FXML
    private ComboBox<String> removeUsersEmailList;
    @FXML
    private TextArea showDataArea;
    @FXML
    private ComboBox<String> usersInfoNameList;




    String currentUserEmail;
    String currentUserMessName;


    @FXML
    public void initialize(){

        LocalDate date = LocalDate.now();
        int year = date.getYear();
        Month month = date.getMonth();

        // set current month and year for house rent
        houseRentMonthYearText.setText("House Rent For " + month.toString() + ", " + Integer.toString(year));

        DatabaseController db = new DatabaseController();
        db.database();

        currentUserEmail=db.getCurrentUserEmail();
        currentUserMessName=db.getMessName(currentUserEmail);

        System.out.println(currentUserMessName + " from admin");

        usersEmail = FXCollections.observableArrayList();
        usersEmail.addAll(db.getUsersName(currentUserMessName));

        removeUsersEmailList.setItems(usersEmail);
        usersInfoNameList.setItems(usersEmail);


        currentBazarEmailList.setItems(usersEmail);
        nextBazarEmailList.setItems(usersEmail);

        // email lists for who are admins
        adminsEmail = FXCollections.observableArrayList();
        adminsEmail.addAll(db.getAdminsEmail(currentUserMessName));
        removeAdminEmailList.setItems(adminsEmail);

        // email lists for who are not admins
        regularUsersEmail = FXCollections.observableArrayList();
        regularUsersEmail.addAll(db.getRegularUsers(currentUserMessName));
        addAdminEmailList.setItems(regularUsersEmail);

        //default value for current bazar
        currentBazarEmailList.setValue(db.getCurrentMealMarketer(currentUserMessName));
        // default value for next bazar
        nextBazarEmailList.setValue(db.getNextMealMarketer(currentUserMessName));


        // hide show information of specific user field
        showDataArea.setVisible(false);

        // hide errors label
        houseRentUpdateError.setVisible(false);
        inactiveMemberError.setVisible(false);
        removeAdminError.setVisible(false);
        thisWeekShopError.setVisible(false);
        userInfoError.setVisible(false);
        nextWeekShopError1.setVisible(false);
        addAdminError.setVisible(false);
    }

    @FXML
    void saveNextBazar(ActionEvent event) {

        if(nextBazarEmailList.getValue() == null){
            nextWeekShopError1.setText("Select Member");
            nextWeekShopError1.setVisible(true);
            return;
        }
        else {
            nextWeekShopError1.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();


        db.upsertNextMealMarketer(nextBazarEmailList.getValue(),currentUserMessName);
    }

    @FXML
    void setCurrentBazar(ActionEvent event) {
        if(currentBazarEmailList.getValue()== null){
            thisWeekShopError.setText("Select Member");
            thisWeekShopError.setVisible(true);
            return;
        }
        else {
            thisWeekShopError.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();

        db.upsertCurrentMealMarketer(currentBazarEmailList.getValue(),currentUserMessName);
    }

    @FXML
    void setHouseRent(ActionEvent event) {
        if(houseRentForCurrentMonthText.getText().isEmpty()){
            houseRentUpdateError.setText("Enter House Rent");
            houseRentUpdateError.setVisible(true);
            return;
        }
        else {
            houseRentUpdateError.setVisible(false);
        }
        int houseRentAmount = Integer.parseInt(houseRentForCurrentMonthText.getText());



        DatabaseController db = new DatabaseController();
        db.database();

        db.upsertHouseRent(currentUserMessName,houseRentAmount);

    }

    @FXML
    void makeAdminAction(ActionEvent event) {

        if(addAdminEmailList.getValue()==null){
            addAdminError.setText("Select Member");
            addAdminError.setVisible(true);
            return;
        }
        else {
            addAdminError.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();

        db.addAdmin(addAdminEmailList.getValue());
    }

    @FXML
    void removeAdminAction(ActionEvent event) {
        if(removeAdminEmailList.getValue()==null){
            removeAdminError.setText("Select Member");
            removeAdminError.setVisible(true);
            return;
        }
        else {
            removeAdminError.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();

        db.removeAdmin(removeAdminEmailList.getValue());
    }

    @FXML
    void removeInactiveUserAction(ActionEvent event) {
        if(removeUsersEmailList.getValue()==null){
            inactiveMemberError.setText("Select Member");
            inactiveMemberError.setVisible(true);
            return;
        }
        else {
            inactiveMemberError.setVisible(false);
        }

        DatabaseController db = new DatabaseController();
        db.database();
        String removeUserName = removeUsersEmailList.getValue();
        String removeUserEmail = db.getUserEmail(removeUserName);

        LocalDate date = LocalDate.now();

        db.deleteUserAndMoveToInactive(removeUserEmail,removeUserName,currentUserMessName,date);
    }

    @FXML
    void showUsersInfoAction(ActionEvent event) {
        if(usersInfoNameList.getValue()==null){
            userInfoError.setText("Select Member");
            userInfoError.setVisible(true);
            return;
        }
        else {
            userInfoError.setVisible(false);
        }
        // store name
        String selectedUserName = usersInfoNameList.getValue();
        //db connection
        DatabaseController db = new DatabaseController();
        db.database();
        // fetching email
        String selectedUserEmail = db.getUserEmail(selectedUserName);

        // fetching house info
        int houseMember = db.findAllUser(currentUserMessName).size();
        double houseRentCost = db.getHouseRentForCurrentMonth(currentUserMessName);
        double utilitiesCost = (db.getTotalUtilityCostByCurrentMonth(currentUserMessName));
        double totalMealsCost = db.getTotalMealMarketCostByCurrentMonth();
        int totalMealNumbers = db.getMealsByEmail();
        double mealRate = totalMealsCost/totalMealNumbers;


        // fetching info for selected user
        double currentUserTotalDeposit = db.getTotalDepositByEmail(selectedUserEmail,currentUserMessName);
        int currentUserMealNumber = db.getMealsByEmail(selectedUserEmail,currentUserMessName);
        double currentUserMealBill = (mealRate * currentUserMealNumber) - currentUserTotalDeposit;

        double currentUserHouseBill = houseRentCost/houseMember;
        double currentUserUtilityBill = utilitiesCost/houseMember;

        double currentUserTotalBill = currentUserHouseBill+currentUserUtilityBill+currentUserMealBill;

        String selectedUserPhoneNo = db.getUserPhoneNo(selectedUserEmail);
        String selectedUserNidNo = db.getNidNoThroughEmail(selectedUserEmail);

        // set information into text field
        showDataArea.setVisible(true);

        showDataArea.setText("");
        showDataArea.setText(
                "Name: " + selectedUserName + "\n" +
                "Nid: " + selectedUserNidNo + "\n" +
                "Phone No: " + selectedUserPhoneNo + "\n" +
                "Email: " + selectedUserEmail + "\n\n" +
                "House Rent: " + currentUserHouseBill + " \n" +
                "Utility Bill: " + currentUserUtilityBill + " \n" +
                "Total Deposit: " + currentUserTotalDeposit + " \n" +
                "Meal Numbers: " + currentUserMealNumber + " \n" +
                "Meal Cost: " + currentUserMealBill + " \n\n" +
                "Total Cost: " + currentUserTotalBill
        );

    }

    @FXML
    void clearUsersDataAction(ActionEvent event) {
        showDataArea.setVisible(false);
    }




    @FXML
    void gotoMealsPage(ActionEvent event) {
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
    void gotoProfilePage(ActionEvent event){
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
    void gotoUtilitiesPage(ActionEvent event) {
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
    void dashboard(ActionEvent event) {
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
    void signOut(ActionEvent event) {
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
