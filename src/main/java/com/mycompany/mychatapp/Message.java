/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

import java.util.Random;

/**
 *
 * @author Student
 */
public class Message {

    public static final String STORE_FILE = "messages.json";

    // Shared state across all messages
    private static final java.util.List<Message> sentMessages = new java.util.ArrayList<>();
    private static final java.util.List<Message> storedMessages = new java.util.ArrayList<>();
    private static int totalMessages = 0;

    // Auto-incremented message number (starts at 0 for the first message)
    private static int messageCounter = 0;

    private final String messageID;
    private final int numMessagesSent;
    private final String recipient;
    private final String message;
    private final String messageHash;

    // Auto-numbered constructor: the message ID is randomly generated
    // and the message number is generated and incremented automatically.
    public Message(String recipient, String message) {
        this(generateMessageID(), nextMessageNumber(), recipient, message);
    }

    // Constructor where the caller supplies the message number;
    // the message ID is randomly generated
    public Message(int numMessagesSent, String recipient, String message) {
        this(generateMessageID(), numMessagesSent, recipient, message);
    }

    // Constructor that accepts an ID (used for unit testing with known data)
    public Message(String messageID, int numMessagesSent, String recipient, String message) {
        this.messageID = messageID;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // Generates a random ten-digit ID
    private static String generateMessageID() {
        long number = (long) (new Random().nextDouble() * 10_000_000_000L);
        return String.format("%010d", number);
    }

    // Returns the current message number and then increments the counter
    private static int nextMessageNumber() {
        return messageCounter++;
    }

    // ---------- Required methods ----------

    // Ensures the message ID is not more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Ensures the recipient number starts with an international code (+)
    // and that the number is no more than ten digits after the code.
    public String checkRecipientCell() {
        // "+" then a 1-3 digit country code and up to 10 digits (2 to 13 digits in total)
        if (recipient != null && recipient.matches("\\+\\d{2,13}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    // Checks the 250 character limit (messages used by the unit tests)
    public static String checkMessageLength(String text) {
        if (text != null && text.length() > 250) {
            int over = text.length() - 250;
            return "Message exceeds 250 characters by " + over
                    + ", please reduce the size.";
        }
        return "Message ready to send.";
    }

    // Checks the 250 character limit (messages shown to the user in the app)
    public static String messageLengthFeedback(String text) {
        if (text != null && text.length() > 250) {
            return "Please enter a message of less than 250 characters.";
        }
        return "Message sent";
    }

    // Hash = first two ID digits : message number : FIRSTWORDLASTWORD
    public String createMessageHash() {
        String firstTwo = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String text = (message == null) ? "" : message.trim();
        String[] words = text.split("\\s+");
        String first = clean(words[0]);
        String last = clean(words[words.length - 1]);
        return (firstTwo + ":" + numMessagesSent + ":" + first + last).toUpperCase();
    }

    // Removes punctuation from a word
    private static String clean(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // Lets the user choose to send, disregard or store the message
    // 1 = Send, 2 = Disregard, 3 = Store
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Returns all messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n");
        }
        return sb.toString();
    }

    // Returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Stores all "store for later" messages in a JSON file
    public boolean storeMessage() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageID\": \"").append(escape(m.messageID)).append("\",\n")
                .append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escape(m.message)).append("\"\n")
                .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]");
        try (java.io.FileWriter writer = new java.io.FileWriter(STORE_FILE)) {
            writer.write(json.toString());
            return true;
        } catch (java.io.IOException e) {
            System.out.println("Could not write " + STORE_FILE + ": " + e.getMessage());
            return false;
        }
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    // ---------- Helpers ----------

    public String getMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

    // Order required by the brief: ID, Hash, Recipient, Message
    public String getDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public int getNumMessagesSent() { return numMessagesSent; }

    // Clears all shared state (used between unit tests)
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessages = 0;
        messageCounter = 0;
    }
}