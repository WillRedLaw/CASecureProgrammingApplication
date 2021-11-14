package com.example.casecureprogramming;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentViewerController {

    static String FirstName;
    static String LastName;
    static String StudentClasses;
    static String StudentYear;
    static String SearchFirstName;
    static String SearchLastName;

    static boolean Detected = false;
    static boolean Admin = WelcomeController.AdminUser;
    static boolean LoginTwo = WelcomeController.LoginTwo;

    @FXML
    public TextField FirstNameT;
    @FXML
    public TextField LastNameT;
    @FXML
    public TextField ClassT;
    @FXML
    private TextField YearT;
    @FXML
    private TextField searchFNT;
    @FXML
    private TextField searchLNT;
    @FXML
    private Button EditButton;
    @FXML
    private Button DeleteStudentB;

    public void ExitButton(ActionEvent event) {
        System.exit(0);
    }

    //Searches for a student
    public void SearchForStudent() throws SQLException {
        setSearchFirstName(searchFNT.getText());
        setSearchLastName(searchLNT.getText());

        CheckSearchField();
        if(!Detected){
            DatabaseConnection ConnectNow = new DatabaseConnection();
            Connection connectionDB = ConnectNow.getConnection();


            String searchText = "SELECT * FROM registerstudent WHERE firstname ='" + searchFNT.getText() + "' AND lastname ='" + searchLNT.getText() + "'";
            Statement statement = connectionDB.createStatement();
            ResultSet queryResult = statement.executeQuery(searchText);

            while (queryResult != null) {

                if (!queryResult.next()) break;
                {
                    FirstNameT.setText(queryResult.getString("firstname"));
                    LastNameT.setText(queryResult.getString("lastname"));
                    ClassT.setText(queryResult.getString("class"));
                    YearT.setText(queryResult.getString("year"));

                }
                if(LoginTwo){
                    EditButton.setVisible(true);
                    DeleteStudentB.setVisible(true);
                }
            }

            if(FirstNameT.getText().isBlank()){
                FirstNameT.setText("Student not found.");
                FirstName = FirstNameT.getText();
                EditButton.setVisible(false);
            }
        }

        else{
            FirstNameT.setText("Special Character detected");
            LastNameT.setText("");
            ClassT.setText("");
            YearT.setText("");
        }

    }

    //Searches for a student
    public void RegisterNewStudent() {

        if (Admin || LoginTwo) {

            setFirstName(FirstNameT.getText());
            setLastName(LastNameT.getText());
            setStudentClasses(ClassT.getText());
            setStudentYear(YearT.getText());

            DatabaseConnection ConnectNow = new DatabaseConnection();
            Connection connectionDB = ConnectNow.getConnection();

            String insertText = "INSERT INTO registerstudent(firstname,lastname,class,year) VALUES('";
            String insertvalues = FirstName + "','" + LastName + "','" + StudentClasses + "','" + StudentYear + "')";
            String insertRegister = insertText + insertvalues;

            try {
                Statement statement = connectionDB.createStatement();
                statement.executeUpdate(insertRegister);
                FirstNameT.setText("User added successful");
                LastNameT.setText("");
                ClassT.setText("");
                YearT.setText("");

            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        else{
            FirstNameT.setText("An admin is required");
            LastNameT.setText("");
            ClassT.setText("");
            YearT.setText("");
        }
    }

    //UpdateStudent
    public void UpdateStudent() {

        if(LoginTwo){

            setFirstName(FirstNameT.getText());
            setLastName(LastNameT.getText());
            setStudentClasses(ClassT.getText());
            setStudentYear(YearT.getText());

            CheckTextField();
            if(!Detected){

                String FirstName = FirstNameT.getText();
                String LastName = LastNameT.getText();
                String Classes = ClassT.getText();
                String Years = YearT.getText();
                String searchName = searchFNT.getText();


                DatabaseConnection ConnectNow = new DatabaseConnection();
                Connection connectionDB = ConnectNow.getConnection();

                //String searchText ="UPDATE registerstudent SET firstname ='" + searchFNT.getText() + "' , lastname ='" + searchLNT.getText() + "', class = '" + ClassT.getText() + "',  year ='" + YearT.getText() + "' WHERE firstname ='" + FirstName + "'";
                String UpdateText = "UPDATE registerstudent SET firstname ='" + FirstName + "', lastname ='"+ LastName + "', class ='"+ Classes + "', year ='" + Years + "' WHERE firstname ='" + searchName + "'";
                try {
                    Statement statement = connectionDB.createStatement();
                    statement.executeUpdate(UpdateText);

                    FirstNameT.setText("Update Successful");
                    LastNameT.setText("");
                    ClassT.setText("");
                    YearT.setText("");
                }
                catch(Exception e){
                    e.printStackTrace();
                    e.getCause();
                }

            }
            else{
                FirstNameT.setText("Special Character detected");
                LastNameT.setText("");
                ClassT.setText("");
                YearT.setText("");
            }
        }

        else{
            FirstNameT.setText("Two Admins required");
            LastNameT.setText("");
            ClassT.setText("");
            YearT.setText("");
        }
    }

    //DeleteStudent
    public void DeleteStudent(){

        if(LoginTwo){

            setFirstName(FirstNameT.getText());
            CheckTextField();

            if(!Detected){
                String FirstName = FirstNameT.getText();

                DatabaseConnection ConnectNow = new DatabaseConnection();
                Connection connectionDB = ConnectNow.getConnection();
                String DeleteText = "DELETE FROM registerstudent WHERE firstname = '" + FirstName + "'";

                try{
                    Statement statement = connectionDB.createStatement();
                    statement.executeUpdate(DeleteText);
                }
                catch(Exception e){
                    e.printStackTrace();
                    e.getCause();
                }
                FirstNameT.setText("Delete Successful");
                LastNameT.setText("");
                ClassT.setText("");
                YearT.setText("");
                }

            else{
                FirstNameT.setText("Special Character detected");
                LastNameT.setText("");
                ClassT.setText("");
                YearT.setText("");
            }
        }
        else{
            FirstNameT.setText("Two admins Required");
            LastNameT.setText("");
            ClassT.setText("");
            YearT.setText("");
        }

    }

    public static void CheckSearchField(){

        String test = getSearchFirstName();
        String test1 = getSearchLastName();

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(test);
        Matcher matcher1 = pattern.matcher(test1);

        boolean ContainsSpecialCharacter = matcher.find();
        boolean ContainsSpecialCharacter1 = matcher1.find();

        if(ContainsSpecialCharacter || ContainsSpecialCharacter1){
            Detected = true;
        }

        else{
            Detected = false;
        }

    }


    //CheckInput for special characters
    public static void CheckTextField(){

        String test = getFirstName();
        String test1 = getLastName();
        String test2 = getStudentClasses();
        String test3 = getStudentYear();

        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(test);
        Matcher matcher1 = pattern.matcher(test1);
        Matcher matcher2 = pattern.matcher(test2);
        Matcher matcher3 = pattern.matcher(test3);

        boolean ContainsSpecialCharacter = matcher.find();
        boolean ContainsSpecialCharacter1 = matcher1.find();
        boolean ContainsSpecialCharacter2 = matcher2.find();
        boolean ContainsSpecialCharacter3 = matcher3.find();

        if(ContainsSpecialCharacter || ContainsSpecialCharacter1 || ContainsSpecialCharacter2 || ContainsSpecialCharacter3){
            Detected = true;
        }

        else{
            Detected = false;
        }
    }

    public static String getFirstName() {
        return FirstName;
    }

    public static void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public static String getLastName() {
        return LastName;
    }

    public static void setLastName(String lastName) {
        LastName = lastName;
    }

    public static String getStudentClasses() {
        return StudentClasses;
    }

    public static void setStudentClasses(String studentClasses) {
        StudentClasses = studentClasses;
    }

    public static String getStudentYear() {
        return StudentYear;
    }

    public static void setStudentYear(String studentYear) {
        StudentYear = studentYear;
    }

    public static String getSearchFirstName() {
        return SearchFirstName;
    }

    public static void setSearchFirstName(String searchFirstName) {
        SearchFirstName = searchFirstName;
    }

    public static String getSearchLastName() {
        return SearchLastName;
    }

    public static void setSearchLastName(String searchLastName) {
        SearchLastName = searchLastName;
    }


}
