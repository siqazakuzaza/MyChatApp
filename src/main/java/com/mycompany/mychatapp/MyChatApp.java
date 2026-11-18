/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.mychatapp;


import java.util.List;
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

        Login login = new Login(username, password, cellNumber, firstName, lastName);

        if (login.checkUserName()) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is incorrectly formatted, please make sure that your "
                    + "username contains an underscore and is not longer than five characters in length.");
        }

        if (login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password does not meet the complexity requirements; please "
                    + "ensure that the password contains at least eight characters, a captital letter, "
                    + "a number and a special character.");
        }

        if (login.checkCellPhoneNumber()) {
            System.out.println("Cell number successfully captured.");
        } else {
            System.out.println("Cell number is incorrectly formatted or does not contain an "
                    + "international code, please correct the number and try again.");
        }

        String registrationResult = login.registerUser();
        System.out.println(registrationResult);

        if (registrationResult.equals("User has been registered successfully.")) {
            System.out.println();
            System.out.println("=== Chat App: Login ===");
            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            login.setUserName(loginUsername);
            login.setPassword(loginPassword);

            String loginStatus = login.returnLoginStatus();
            System.out.println(loginStatus);

            if (login.loginUser()) {
                Message.setSender(firstName + " " + lastName);
                runQuickChat(scanner);
            }
        }
        scanner.close();
    }

    // ================= QUICK CHAT =================
    public static void runQuickChat(Scanner scanner) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // Part 3: read the JSON file of stored messages into the stored messages array
        int loaded = Message.loadStoredMessages();
        if (loaded > 0) {
            System.out.println(loaded + " stored message(s) loaded from " + Message.STORE_FILE + ".");
        }

        int maxMessages = 0;
        while (maxMessages <= 0) {
            System.out.println("How many messages do you want to enter?");
            try {
                maxMessages = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                maxMessages = 0;
            }
            if (maxMessages <= 0) {
                System.out.println("Please enter a whole number greater than 0.");
            }
        }

        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    if (entered >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }
                    for (int i = entered; i < maxMessages; i++) {
                        enterMessage(scanner, i);
                    }
                    entered = maxMessages;
                    System.out.println();
                    System.out.println("Total number of messages sent: "
                            + Message.returnTotalMessages());
                    break;
                case "2":
                    String sent = Message.printMessages();
                    if (sent.isEmpty()) {
                        System.out.println("No messages have been sent yet.");
                    } else {
                        System.out.println(sent);
                    }
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    running = false;
                    break;
                case "4":
                    storedMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, 3 or 4.");
            }
        }
    }

    // ================= PART 3: STORED MESSAGES MENU =================
    private static void storedMessagesMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("=== Stored Messages ===");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages stored for a recipient");
            System.out.println("5) Delete a message using its message hash");
            System.out.println("6) Display a report of all stored messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    System.out.println(Message.displaySenderAndRecipient());
                    break;
                case "2":
                    System.out.println(Message.getLongestStoredMessage());
                    break;
                case "3":
                    System.out.print("Enter the message ID: ");
                    Message found = Message.findByMessageID(scanner.nextLine().trim());
                    if (found == null) {
                        System.out.println("No message found with that ID.");
                    } else {
                        System.out.println("Recipient: " + found.getRecipient());
                        System.out.println("Message: " + found.getMessage());
                    }
                    break;
                case "4":
                    System.out.print("Enter the recipient cell number: ");
                    List<String> results = Message.searchByRecipient(scanner.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : results) {
                            System.out.println(text);
                        }
                    }
                    break;
                case "5":
                    System.out.print("Enter the message hash: ");
                    System.out.println(Message.deleteByMessageHash(scanner.nextLine().trim()));
                    break;
                case "6":
                    System.out.println(Message.storedReport());
                    break;
                case "7":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
            }
        }
    }

    // Captures a single message and lets the user send, disregard or store it
    private static void enterMessage(Scanner scanner, int index) {
        System.out.println();
        System.out.println("--- Message " + (index + 1) + " ---");

        String recipient;
        while (true) {
            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            String result = new Message(index, recipient, "x").checkRecipientCell();
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                break;
            }
        }

        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = scanner.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("The message cannot be empty.");
            } else if (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.println(Message.checkMessageLength(text));
            } else {
                System.out.println(Message.checkMessageLength(text));
                break;
            }
        }

        Message msg = new Message(index, recipient, text);

        System.out.println();
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = 0;
        while (choice < 1 || choice > 3) {
            System.out.print("Choose an option: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = 0;
            }
        }

        System.out.println(msg.SentMessage(choice));

        if (choice == 1) {
            System.out.println(msg.getDetails());
        } else if (choice == 2) {
            System.out.print("> ");
            if (scanner.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }
}