/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
public class Message {

    public static final String STORE_FILE = "messages.json";
    private static String storeFile = STORE_FILE;

    // ---------- Part 3 arrays (populated at run time, nothing hard-coded) ----------
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> disregardedMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static final List<String> messageHashes = new ArrayList<>();
    private static final List<String> messageIDs = new ArrayList<>();
    private static int totalMessages = 0;

    // Name of the logged-in user, shown as the "sender" of stored messages
    private static String sender = "You";

    private final String messageID;
    private final int numMessagesSent;
    private final String recipient;
    private final String message;
    private final String messageHash;

    // Normal constructor: the message ID is randomly generated.
    public Message(int numMessagesSent, String recipient, String message) {
        this(generateMessageID(), numMessagesSent, recipient, message);
    }

    // Constructor that accepts an ID (used for unit testing and for reading the JSON file)
    public Message(String messageID, int numMessagesSent, String recipient, String message) {
        this.messageID = messageID;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    private static String generateMessageID() {
        String digits = String.format("%019d", new Random().nextLong() & Long.MAX_VALUE);
        return digits.substring(0, 10);
    }

    // ---------- Required methods ----------

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("\\+\\d{2,13}")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    public static String checkMessageLength(String text) {
        if (text != null && text.length() > 250) {
            int over = text.length() - 250;
            return "Message exceeds 250 characters by " + over
                    + ", please reduce the size.";
        }
        return "Message ready to send.";
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

    private static String clean(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // 1 = Send, 2 = Disregard, 3 = Store
    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessages++;
                register();
                return "Message successfully sent.";
            case 2:
                disregardedMessages.add(this);
                register();
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                register();
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Adds this message's hash and ID to the hash / ID arrays (no duplicates)
    private void register() {
        if (!messageHashes.contains(messageHash)) {
            messageHashes.add(messageHash);
        }
        if (!messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }
    }

    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getDetails()).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    // ---------- JSON storage ----------
    // Attribution: JSON structure follows the JSON specification (https://www.json.org).
    // Writing uses java.io.FileWriter
    // (https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html).
    // Reading uses java.nio.file.Files.readAllBytes
    // (https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/nio/file/Files.html)
    // and java.util.regex.Pattern
    // (https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html).
    // TODO: replace/add the sources you actually used when researching JSON storage.
    public boolean storeMessage() {
        return writeStoreFile();
    }

    private static boolean writeStoreFile() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                    .append("    \"messageID\": \"").append(escape(m.messageID)).append("\",\n")
                    .append("    \"numMessagesSent\": ").append(m.numMessagesSent).append(",\n")
                    .append("    \"messageHash\": \"").append(escape(m.messageHash)).append("\",\n")
                    .append("    \"recipient\": \"").append(escape(m.recipient)).append("\",\n")
                    .append("    \"message\": \"").append(escape(m.message)).append("\"\n")
                    .append("  }").append(i < storedMessages.size() - 1 ? "," : "").append("\n");
        }
        json.append("]");
        try (FileWriter writer = new FileWriter(storeFile)) {
            writer.write(json.toString());
            return true;
        } catch (IOException e) {
            System.out.println("Could not write " + storeFile + ": " + e.getMessage());
            return false;
        }
    }

    // Reads the JSON file into the stored messages array. Returns how many were loaded.
    public static int loadStoredMessages() {
        File file = new File(storeFile);
        if (!file.exists()) {
            return 0;
        }
        try {
            String json = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            storedMessages.clear();
            // matches  "key": "text"  or  "key": 123
            Pattern pattern = Pattern.compile(
                    "\"(\\w+)\"\\s*:\\s*(?:\"((?:[^\"\\\\]|\\\\.)*)\"|(\\d+))");
            Matcher matcher = pattern.matcher(json);
            String id = null;
            String recipient = null;
            int number = 0;
            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2) != null
                        ? unescape(matcher.group(2)) : matcher.group(3);
                switch (key) {
                    case "messageID":
                        id = value;
                        break;
                    case "numMessagesSent":
                        number = Integer.parseInt(value);
                        break;
                    case "recipient":
                        recipient = value;
                        break;
                    case "message": {
                        // "message" is the last field of each object; the hash is rebuilt
                        Message m = new Message(id, number, recipient, value);
                        storedMessages.add(m);
                        m.register();
                        break;
                    }
                    default:
                        break;
                }
            }
            return storedMessages.size();
        } catch (IOException e) {
            System.out.println("Could not read " + storeFile + ": " + e.getMessage());
            return 0;
        }
    }

    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(++i);
                if (next == 'n') {
                    sb.append('\n');
                } else if (next == 'r') {
                    sb.append('\r');
                } else {
                    sb.append(next);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // ---------- Stored Messages menu features ----------

    // a. Sender and recipient of all stored messages
    public static String displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(sender)
                    .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString().trim();
    }

    // b. Longest stored message
    public static String getLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.message.length() > longest.message.length()) {
                longest = m;
            }
        }
        return longest.message;
    }

    // c. Search for a message ID (sent, stored and disregarded); null if not found
    public static Message findByMessageID(String id) {
        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        for (Message m : storedMessages) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        for (Message m : disregardedMessages) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        return null;
    }

    // d. All messages sent or stored for a particular recipient
    public static List<String> searchByRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) {
                found.add(m.message);
            }
        }
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) {
                found.add(m.message);
            }
        }
        return found;
    }

    // e. Delete a stored message using its hash
    public static String deleteByMessageHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            if (m.messageHash.equalsIgnoreCase(hash)) {
                storedMessages.remove(i);
                messageHashes.remove(m.messageHash);
                messageIDs.remove(m.messageID);
                writeStoreFile();
                return "Message: \"" + m.message + "\" successfully deleted.";
            }
        }
        return "No stored message found with that hash.";
    }

    // f. Report with the full details of all stored messages
    public static String storedReport() {
        if (storedMessages.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append(m.getDetails()).append("\n\n");
        }
        return sb.toString().trim();
    }

    // Report of all sent messages: hash, recipient and message
    public static String sentReport() {
        if (sentMessages.isEmpty()) {
            return "There are no sent messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message Hash: ").append(m.messageHash)
                    .append("\nRecipient: ").append(m.recipient)
                    .append("\nMessage: ").append(m.message).append("\n\n");
        }
        return sb.toString().trim();
    }

    // ---------- Helpers ----------

    public String getMessageIDStatus() {
        return "Message ID generated: " + messageID;
    }

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

    // Array getters (copies, so outside code cannot change the arrays)
    public static List<Message> getSentMessages() { return new ArrayList<>(sentMessages); }
    public static List<Message> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }
    public static List<Message> getStoredMessages() { return new ArrayList<>(storedMessages); }
    public static List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }
    public static List<String> getMessageIDs() { return new ArrayList<>(messageIDs); }

    public static List<String> getSentMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message m : sentMessages) {
            texts.add(m.message);
        }
        return texts;
    }

    public static void setSender(String name) { sender = name; }
    public static void setStoreFile(String path) { storeFile = path; }

    // Clears all shared state (used between unit tests)
    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessages = 0;
        sender = "You";
    }
}