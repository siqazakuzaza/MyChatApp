/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

/**
 *
 * @author Student
 */
public class Login {
           //stored registered user details
    private String storedUsername;
    private String storedPassword;
    private String storedcellNumber;
    private boolean userRegistered = false;
    //registration details
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;
    //Constructor
    public Login (String username, String password, String cellNumber, String firstName, String lastName){
        this. username = username;
        this. password = password;
        this. cellNumber = cellNumber;
        this. firstName = firstName;
        this. lastName = lastName;
    }
    //Checking username
    public boolean checkUserName(){
       if (username == null) {
           return false;
       }
       return username.contains("_") && username.length()<=5;
    }
   public boolean checkPasswordComplexity() {
    if (password == null) {
        return false;
    }
    int length = password.length();
    boolean hasUpper = false;
    boolean hasDigit = false;
    boolean hasSpecial = false;
    // Allowed special characters
    String specials = "!@#$%^&*()-_+=";
    for (int i = 0; i < length; i++) {
        char c = password.charAt(i);
        if (Character.isUpperCase(c)) {
            hasUpper = true;
        }
        if (Character.isDigit(c)) {
            hasDigit = true;
        }
        if (specials.indexOf(c) != -1) {
            hasSpecial = true;
        }
    }
    return length >= 8 && hasUpper && hasDigit && hasSpecial;
 }
   public boolean checkCellPhoneNumber() {
    if (cellNumber == null || cellNumber.isEmpty()) {
        return false;
    }
    // Must start with +27
    if (!cellNumber.startsWith("+27")) {
        return false;
    }
    // Get the part after +27
    String numberPart = cellNumber.substring(3);
    // Check that the rest contains only digits and has reasonable length
    if (numberPart.length() < 1 || numberPart.length() > 10) {
        return false;
    }
    for (int i = 0; i < numberPart.length(); i++) {
        if (!Character.isDigit(numberPart.charAt(i))) {
            return false;
        }
    }
    return true;
}
  public String registerUser(){
      //checking user name
      if (!checkUserName()){
          return "Username is incorrectly formatted, please make sure that your username contains an underscore and is not longer than five characters in length.";
      }
      //checking password
      if (!checkPasswordComplexity()){
          return "Password does not meet the complexity requirements; please ensure that the password contains at least eight characters, a captital letter, a number and a special character.";
      }
      //checking cell number
      if (!checkCellPhoneNumber()){
          return "Cell number is incorectly formatted or does not contain an international code, please correct the number and try again";
      }
      //All condtions are met, register the user
      storedUsername = username;
      storedPassword = password;
      storedcellNumber = cellNumber;
      userRegistered = true;
      return "User has been registered successfully.";
   } 
  public boolean loginUser(){
      //Check if the user has been registered
      if(!userRegistered){
          return false;
      }
      //Compare entered details either stored details
      if (username != null && password != null &&username.equals(storedUsername) &&password.equals(storedPassword)){
          return true;
      }
      return false;
  }
  public String returnLoginStatus(){
      if(loginUser()){
          //successful login
          return "Welcome" + firstName + "," + lastName + "it is good to see you.";
      }else {
          //failed login
          return "Username or password is incorrect, please try again.";
      }
  }
    
}
