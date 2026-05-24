/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginapp;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Karabo
 */
public class Message {

    // Stores all successfully sent messages across the session
    private static ArrayList<Message> sentMessages = new ArrayList<>();

    // each message has these fields
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private int messageNumber;

    // this tracks how many messages have been sent
    private static int totalMessagesSent = 0;

    // Constructor - this assigns a number to this message and auto generates its ID
    public Message(String recipient, String messageText, int messageNumber) {
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageNumber = messageNumber;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    // Auto-generates a random 10-digit message ID
    private String generateMessageID() {
        Random random = new Random();
        long id = (long) (random.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // Checking that the message ID is no more than 10 characters
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // checking that the recipient cell number starts with an international code
    // and is no more than 10 characters long (excluding the + sign)
    // Reference: regex adapted from regexlib.com for international numbers
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number incorrectly formatted or does not contain "
                 + "an international code. Please correct the number and try again.";
        }
        // Must start with + followed by country code digits, total max 13 chars (+27 + 9 digits)
        if (recipient.matches("^\\+[0-9]{10,12}$")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain "
                 + "an international code. Please correct the number and try again.";
        }
    }

    // Creates and returns the message hash in the format:
    // first two digits of ID : message number : FIRST WORD LAST WORD (all caps)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].toUpperCase();
        String lastWord  = words[words.length - 1].toUpperCase();

        // remove trailing punctuation from last word if present
        lastWord = lastWord.replaceAll("[^A-Z0-9]", "");

        String idPrefix = messageID.substring(0, 2);
        return idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    // this allows the user to choose what to do with the message
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                // send the message - add to sent list and increment counter
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                // Discard the message - do not store it
                return "Press 0 to delete the message.";
            case 3:
                // Storing the message to send later
                sentMessages.add(this);
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    // Checking that the message does not exceed 250 characters
    public static String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess
                 + " [enter number here]; please reduce the size.";
        }
    }

    // Returns all messages that have been sent or stored during this session
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n");
            sb.append("Message Hash: ").append(m.messageHash).append("\n");
            sb.append("Recipient: ").append(m.recipient).append("\n");
            sb.append("Message: ").append(m.messageText).append("\n");
            sb.append("---\n");
        }
        return sb.toString();
    }

    // this returns the total number of messages sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Stores the message details as a JSON-style string
    // research reference: JSON formatting manually constructed as a String
    public String storeMessage() {
        return "{"
             + "\"messageID\": \"" + messageID + "\", "
             + "\"messageHash\": \"" + messageHash + "\", "
             + "\"recipient\": \"" + recipient + "\", "
             + "\"message\": \"" + messageText + "\""
             + "}";
    }

    // Getters
    public String getMessageID()   { return messageID;   }
    public String getMessageHash() { return messageHash; }
    public String getRecipient()   { return recipient;   }
    public String getMessageText() { return messageText; }
    public int getMessageNumber()  { return messageNumber; }
}