/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mychatapp;

import java.io.File;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
    // Test data from the assignment brief

    private static final String RECIPIENT_1 = "+27834557896";

    private static final String MESSAGE_1 = "Did you get the cake?";

    private static final String RECIPIENT_2 = "+27838884567";

    private static final String MESSAGE_2 =

            "Where are you? You are late! I have asked you to be on time.";

    private static final String RECIPIENT_3 = "+27834484567";

    private static final String MESSAGE_3 = "Yohoooo, I am at your gate.";

    private static final String DEVELOPER = "0838884567";

    private static final String MESSAGE_4 = "It is dinner time !";

    private static final String MESSAGE_5 = "Ok, I am leaving without you.";

    private Message message1;

    private Message message2;

    private Message message3;

    private Message message4;

    private Message message5;

    // ---------- SET UP TEST DATA ----------

    @BeforeEach

    public void setUp() {

        Message.resetAll();

        message1 = new Message("1234567801", 0,

                RECIPIENT_1, MESSAGE_1);

        message2 = new Message("1234567802", 1,

                RECIPIENT_2, MESSAGE_2);

        message3 = new Message("1234567803", 2,

                RECIPIENT_3, MESSAGE_3);

        message4 = new Message(DEVELOPER, 3,

                DEVELOPER, MESSAGE_4);

        message5 = new Message("1234567805", 4,

                RECIPIENT_2, MESSAGE_5);

        // Populate messages according to their flags

        message1.SentMessage(1); // Sent

        message2.SentMessage(3); // Stored

        message3.SentMessage(2); // Disregarded

        message4.SentMessage(1); // Sent

        message5.SentMessage(3); // Stored

    }

    // ---------- CLEAN UP ----------

    @AfterEach

    public void cleanUp() {

        new File(Message.STORE_FILE).delete();

        Message.resetAll();

    }

    // ---------- TEST 1: SENT MESSAGES ARRAY ----------

    @Test

    public void testSentMessagesArrayCorrectlyPopulated() {

        assertEquals(

                Arrays.asList(MESSAGE_1, MESSAGE_4),

                Message.getSentMessageTexts()

        );

    }

    // ---------- TEST 2: DISPLAY LONGEST MESSAGE ----------

    @Test

    public void testDisplayLongestMessage() {

        assertEquals(

                MESSAGE_2,

                Message.getLongestStoredMessage()

        );

    }

    // ---------- TEST 3: SEARCH FOR MESSAGE ID ----------

    @Test

    public void testSearchForMessageID() {

        Message found = Message.findByMessageID(DEVELOPER);

        assertNotNull(found);

        assertEquals(MESSAGE_4, found.getMessage());

    }

    // ---------- TEST 4: SEARCH MESSAGES BY RECIPIENT ----------

    @Test

    public void testSearchMessagesByRecipient() {

        assertEquals(

                Arrays.asList(MESSAGE_2, MESSAGE_5),

                Message.searchByRecipient(RECIPIENT_2)

        );

    }

    // ---------- TEST 5: DELETE MESSAGE USING HASH ----------

    @Test

    public void testDeleteMessageUsingMessageHash() {

        String hash = message2.getMessageHash();

        String result = Message.deleteByMessageHash(hash);

        assertEquals(

                "Message: \"" + MESSAGE_2 + "\" successfully deleted.",

                result

        );

        assertEquals(

                Arrays.asList(MESSAGE_5),

                Message.searchByRecipient(RECIPIENT_2)

        );

    }

    // ---------- TEST 6: DISPLAY REPORT ----------

    @Test

    public void testDisplayReport() {

        String report = Message.sentReport();

        // Check Message 1

        assertTrue(report.contains(message1.getMessageHash()));

        assertTrue(report.contains(RECIPIENT_1));

        assertTrue(report.contains(MESSAGE_1));

        // Check Message 4

        assertTrue(report.contains(message4.getMessageHash()));

        assertTrue(report.contains(DEVELOPER));

        assertTrue(report.contains(MESSAGE_4));

    }

}
