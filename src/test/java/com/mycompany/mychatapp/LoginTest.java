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

    // ---------- checkUserName() ----------

    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();
        login.setUserName("kyl_1");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        login.setUserName("kyle!!!!!!");
        assertFalse(login.checkUserName());
    }

    // ---------- checkPasswordComplexity() ----------

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        Login login = new Login();
        login.setPassword("Ch&&sec@ke99!");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        Login login = new Login();
        login.setPassword("password");
        assertFalse(login.checkPasswordComplexity());
    }

    // ---------- checkCellPhoneNumber() ----------

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        Login login = new Login();
        login.setCellPhoneNumber("+27838968976");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        Login login = new Login();
        login.setCellPhoneNumber("08966553");
        assertFalse(login.checkCellPhoneNumber());
    }

    // ---------- registerUser() ----------

    @Test
    public void testRegisterUserUsernameInvalid() {
        Login login = new Login();
        login.setUserName("kyle!!!!!!");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        assertEquals(
                "Username is not correctly formatted; please ensure that your "
                + "username contains an underscore and is no more than five "
                + "characters in length.",
                login.registerUser()
        );
    }

    @Test
    public void testRegisterUserPasswordInvalid() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("password");
        login.setCellPhoneNumber("+27838968976");
        assertEquals(
                "Password is not correctly formatted; please ensure that the "
                + "password contains at least eight characters, a capital "
                + "letter, a number, and a special character.",
                login.registerUser()
        );
    }

    @Test
    public void testRegisterUserCellNumberInvalid() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("08966553");
        assertEquals(
                "Cell number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.",
                login.registerUser()
        );
    }

    @Test
    public void testRegisterUserSuccess() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        assertEquals("User has been registered successfully.", login.registerUser());
    }

    // ---------- loginUser() ----------

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        login.registerUser();
        assertTrue(login.loginUser());
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        login.registerUser();
        // Change the password after registering, so it no longer matches
        // the stored details
        login.setPassword("WrongPass1!");
        assertFalse(login.loginUser());
    }

    // ---------- returnLoginStatus() ----------

    @Test
    public void testReturnLoginStatusSuccessful() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser();
        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus()
        );
    }

    @Test
    public void testReturnLoginStatusFailed() {
        Login login = new Login();
        login.setUserName("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        login.setCellPhoneNumber("+27838968976");
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser();
        // Change the password after registering, so login should fail
        login.setPassword("WrongPass1!");
        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus()
        );
    }
}