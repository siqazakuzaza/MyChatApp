/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.mychatapp;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class MyChatApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Chat App: Registration ===");

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username (it must contain '_' and be <= 5 characters): ");
        String username = scanner.nextLine();

        System.out.print("Enter a password (8+ characters, capital, number, special char): ");
        String password = scanner.nextLine();

        System.out.print("Enter cell number (e.g. +2780034321): ");
        String cellNumber = scanner.nextLine();
        Login login = new Login(
                username,
                password,
                cellNumber,
                firstName,
                lastName
        );
         // Link and call username method
        if (login.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println(
                    "Username is incorrectly formatted, please make sure that your username contains an underscore and is not longer than five characters in length. "
            );
        }
        // Link and call password method
        if (login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println(
                    "Password does not meet the complexity requirements; please ensure that the password contains at least eight characters, a captital letter, a number and a special character. "
            );
        }
        // Link and call cellphone method
        if (login.checkCellPhoneNumber()) {
            System.out.println("Cell number successfully captured.");
        } else {
            System.out.println(
                    " "
                    
            );
        }

        String registrationResult = login.registerUser();

        System.out.println(registrationResult);

        if (registrationResult.startsWith("Registration successful")) {

            System.out.println();
            System.out.println("=== Chat App: Login ===");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            String loginStatus = login.returnLoginStatus();

            System.out.println(loginStatus);
        }
        //include methods.
        scanner.close();
    }
}
