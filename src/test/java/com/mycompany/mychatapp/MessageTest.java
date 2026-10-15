/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

import java.io.File;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 *
 * @author Student
 */
public class MessageTest {
    // Test data from the brief
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String MESSAGE_1 = "Hi Mike, can you join us for dinner tonight?";

    private static final String RECIPIENT_2 = "08575975889";
    private static final String MESSAGE_2 = "Hi Keegan, did you receive the payment?";

    private static final Random RANDOM = new Random();

    private Message message1;
    private Message message2;

    // Generates a ten-digit message ID with java.util.Random.
    // It starts with "00" so the hash prefix is predictable (00:0:HITONIGHT).
    private static String randomMessageID() {
        return String.format("00%08d", RANDOM.nextInt(100_000_000));
    }

    @BeforeEach
    public void setUp() {
        Message.resetAll();
        message1 = new Message(randomMessageID(), 0, RECIPIENT_1, MESSAGE_1);
        message2 = new Message(randomMessageID(), 1, RECIPIENT_2, MESSAGE_2);
    }

    @AfterEach
    public void cleanUp() {
        // Remove the JSON file created by the "store" test
        new File(Message.STORE_FILE).delete();
        Message.resetAll();
    }

    // ---------- Message length ----------

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", Message.checkMessageLength(MESSAGE_1));
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10, please reduce the size.",
                Message.checkMessageLength(longMessage));
    }

    // ---------- Recipient number ----------

    @Test
    public void testRecipientNumberSuccess() {
        assertEquals("Cell phone number successfully captured.",
                message1.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    // ---------- Message hash ----------

    @Test
    public void testMessageHashTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.getMessageHash());
    }

    @Test
    public void testMessageHashLoop() {
        String[] messages = {
            MESSAGE_1,
            MESSAGE_2,
            "It is dinner time!"
        };
        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT",
            "00:2:ITTIME"
        };

        for (int i = 0; i < messages.length; i++) {
            Message m = new Message(randomMessageID(), i, RECIPIENT_1, messages[i]);
            assertEquals(expectedHashes[i], m.getMessageHash());
        }
    }

    // ---------- Message ID ----------

    @Test
    public void testMessageIDCreated() {
        // Fully auto-generated ID (Message uses java.util.Random): ten digits
        Message m = new Message(RECIPIENT_1, MESSAGE_1);
        assertTrue(m.checkMessageID());
        assertTrue(m.getMessageID().matches("\\d{10}"));
        assertEquals("Message ID generated: " + m.getMessageID(), m.getMessageIDStatus());
    }

    // ---------- MessageSent: send, disregard, store ----------

    @Test
    public void testMessageSentSend() {
        assertEquals("Message successfully sent.", message1.SentMessage(1));
    }

    @Test
    public void testMessageSentDisregard() {
        assertEquals("Press 0 to delete the message.", message2.SentMessage(2));
    }

    @Test
    public void testMessageSentStore() {
        assertEquals("Message successfully stored.", message1.SentMessage(3));
        assertTrue(new File(Message.STORE_FILE).exists());
    }

    // ---------- Total messages ----------

    @Test
    public void testReturnTotalMessages() {
        message1.SentMessage(1); // sent
        message2.SentMessage(2); // disregarded, so not counted
        assertEquals(1, Message.returnTotalMessages());
    }
}
