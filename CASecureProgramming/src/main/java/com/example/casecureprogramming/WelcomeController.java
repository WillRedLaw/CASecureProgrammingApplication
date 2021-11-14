package com.example.casecureprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WelcomeController {



    //Variables
    static boolean Login = false;
    static boolean LoginTwo = false;
    static boolean AdminUser = false;
    static boolean AdminLoginScreen = false;

    @FXML
    private TextField UserNameTextField;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private Label LoginText;
    @FXML
    private Label usernameA;
    @FXML
    private Label passwordA;
    @FXML
    private TextField UserNameTFA;
    @FXML
    private PasswordField PasswordTFA;

    private Stage stage;
    private Scene scene;
    private Parent root;


    //Two - People Login
    public void AdminLoginButton(){
        usernameA.setVisible(true);
        passwordA.setVisible(true);
        UserNameTFA.setVisible(true);
        PasswordTFA.setVisible(true);
        AdminLoginScreen = true;
    }

    //Check
    public void checkifadminuser() throws SQLException {
        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB = connectionNow.getConnection();
        String CheckifAdmin = "SELECT count(1) FROM studentdata WHERE username = '" + UserNameTextField.getText() + "' AND admin = '1'";
        Statement statement = connectionDB.createStatement();
        ResultSet queryResult = statement.executeQuery(CheckifAdmin);
        while (queryResult.next()) {
            if (queryResult.getInt(1) == 1) {
                AdminUser = true;
            }
        }

    }

    //Checks that the user logging in with the right details
    public void LoginButton(ActionEvent event) throws IOException, SQLException {


        if(AdminLoginScreen == false){
            if (UserNameTextField.getText().isBlank() == false && PasswordField.getText().isBlank() == false) {

                validateLogin();

                if (Login) {

                    checkifadminuser();
                    root = FXMLLoader.load(getClass().getResource("StudentViewer.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();

                }

            } else {
                LoginText.setText("Failed");
            }
        }

        if(AdminLoginScreen){
            validateLogin();
            AdminLoginCheck();
            if(Login && LoginTwo){

                root = FXMLLoader.load(getClass().getResource("StudentViewer.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


                scene = new Scene(root);
                stage.setScene(scene);
            }

            else{
                LoginText.setText("Failed");
            }
        }
    }

    //checks if everything is true
    public void AdminLoginCheck(){

        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB = connectionNow.getConnection();

        String adminLogin = "SELECT count(1) FROM studentdata WHERE username ='" + UserNameTFA.getText() + "' AND password ='" + PasswordTFA.getText() + "'";

        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(adminLogin);



            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    LoginTwo = true;
                } else {
                    LoginTwo= false;
                    LoginText.setText("Failed");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    //Single user validatelogin
    public void validateLogin() {

        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectionDB = connectionNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM studentdata WHERE username ='" + UserNameTextField.getText() + "' AND password ='" + PasswordField.getText() + "'";

        try {
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);



            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    Login = true;

                } else {
                    LoginText.setText("Unable to Login please try again!");
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

}//End of Class