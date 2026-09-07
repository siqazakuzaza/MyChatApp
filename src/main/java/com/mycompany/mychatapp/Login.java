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
    private String storedCellNumber;
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
        // Default constructor
    public Login() {
    }
    // Setter for username
    public void setUserName(String username) {
        this.username = username;
    }
    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
    // Setter for cell phone number
    public void setCellPhoneNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }
    // Setter for first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    // Setter for last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //Checking username
    public boolean checkUserName(){
       if (username == null) {
           return false;
       }
       return username.contains("_") && username.length()<=5;
    }
    //checking password complexity
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
    //check that the remaining characters are digits
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
          return "Username is not correctly formatted; please make sure that your "
                  + "username contains an underscore and is no more than five "
                  + "characters in length.";
      }
      //checking password
      if (!checkPasswordComplexity()){
          return "Password is not correctly formatted; please ensure that the "
                  + "password contains at least eight characters, a captital " 
                  + "letter, a number and a special character.";
      }
      //checking cell number
      if (!checkCellPhoneNumber()){
          return "Cell number is incorectly formatted or does not contain an "
                  + "international code, please correct the number and try again ";
      }
      //All condtions are met, register the user
      storedUsername = username;
      storedPassword = password;
      storedCellNumber = cellNumber;
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
 public String returnLoginStatus() {
    if (loginUser()) {
        return "Welcome " + firstName + ", " + lastName
                + " it is great to see you again.";
    } else {
        return "Username or password incorrect, please try again.";
    }
  }
}
