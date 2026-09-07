/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.mychatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginTest {

    // Test setUserName() and checkUserName()
    @Test
    public void testValidUsername() {
        Login login = new Login();

        login.setUserName("ku_");

        boolean result = login.checkUserName();

        assertTrue(result);
    }

    // Test invalid username
    @Test
    public void testInvalidUsername() {
        Login login = new Login();

        login.setUserName("kunga");

        boolean result = login.checkUserName();

        assertFalse(result);
    }

    // Test setPassword() and checkPasswordComplexity()
    @Test
    public void testValidPassword() {
        Login login = new Login();

        login.setPassword("Kungothando08*");

        boolean result = login.checkPasswordComplexity();

        assertTrue(result);
    }

    // Test invalid password
    @Test
    public void testInvalidPassword() {
        Login login = new Login();

        login.setPassword("Kungothando");

        boolean result = login.checkPasswordComplexity();

        assertFalse(result);
    }

    // Test setCellPhoneNumber() and checkCellPhoneNumber()
    @Test
    public void testValidCellPhoneNumber() {
        Login login = new Login();

        login.setCellPhoneNumber("+27821234567");

        boolean result = login.checkCellPhoneNumber();

        assertTrue(result);
    }

    // Test invalid cell phone number
    @Test
    public void testInvalidCellPhoneNumber() {
        Login login = new Login();

        login.setCellPhoneNumber("0821234567");

        boolean result = login.checkCellPhoneNumber();

        assertFalse(result);
    }

    // Test successful registration
    @Test
    public void testSuccessfulRegistration() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");

        String result = login.registerUser();

        assertEquals(
            "User has been registered successfully.",
            result
        );
    }

    // Test unsuccessful registration because of invalid username
    @Test
    public void testUnsuccessfulRegistrationUsername() {
        Login login = new Login();

        login.setUserName("kunga");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");

        String result = login.registerUser();

        assertEquals(
            "Username is not correctly formatted; please make sure that your "
            + "username contains an underscore and is no more than five "
            + "characters in length.",
            result
        );
    }

    // Test unsuccessful registration because of invalid password
    @Test
    public void testUnsuccessfulRegistrationPassword() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando");
        login.setCellPhoneNumber("+27821234567");

        String result = login.registerUser();

        assertEquals(
            "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a captital "
            + "letter, a number and a special character.",
            result
        );
    }

    // Test unsuccessful registration because of invalid cell phone number
    @Test
    public void testUnsuccessfulRegistrationCellNumber() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("0821234567");

        String result = login.registerUser();

        assertEquals(
            "Cell number is incorectly formatted or does not contain an "
            + "international code, please correct the number and try again ",
            result
        );
    }

    // Test successful login
    @Test
    public void testSuccessfulLogin() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");

        login.registerUser();

        boolean result = login.loginUser();

        assertTrue(result);
    }

    // Test unsuccessful login
    @Test
    public void testUnsuccessfulLogin() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");

        login.registerUser();

        login.setPassword("Wrong123!");

        boolean result = login.loginUser();

        assertFalse(result);
    }

    // Test login before registration
    @Test
    public void testLoginBeforeRegistration() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");

        boolean result = login.loginUser();

        assertFalse(result);
    }

    // Test setFirstName(), setLastName() and returnLoginStatus()
    @Test
    public void testSuccessfulLoginStatus() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");
        login.setFirstName("Kungothando");
        login.setLastName("Siqaza");

        login.registerUser();

        String result = login.returnLoginStatus();

        assertEquals(
            "Welcome Kungothando, Siqaza it is great to see you again.",
            result
        );
    }

    // Test unsuccessful returnLoginStatus()
    @Test
    public void testUnsuccessfulLoginStatus() {
        Login login = new Login();

        login.setUserName("ku_");
        login.setPassword("Kungothando08*");
        login.setCellPhoneNumber("+27821234567");
        login.setFirstName("Kungothando");
        login.setLastName("Siqaza");

        login.registerUser();

        login.setPassword("Wrong123!");

        String result = login.returnLoginStatus();

        assertEquals(
            "Username or password incorrect, please try again.",
            result
        );
    }
}