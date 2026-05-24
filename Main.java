/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.loginapp;

import java.util.Scanner;

/**
 *
 * @author Karabo
 */
public class Main {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("=== WELCOME TO THE LOGIN SYSTEM ===");

        //  Collecting the name
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        Registration reg = new Registration(firstName, lastName);

        // The Registration loop 
        System.out.println("\n--- REGISTRATION ---");

        while (true) {
            System.out.print("Enter username: ");
            String username = input.nextLine();

            System.out.print("Enter password: ");
            String password = input.nextLine();

            System.out.print("Enter cell phone number (e.g. +27791839683): ");
            String cellNumber = input.nextLine();

            String registrationResult = reg.registerUser(username, password, cellNumber);
            System.out.println(registrationResult);

            if (registrationResult.contains("successfully")) {
                break;
            }
            System.out.println("Please try again.\n");
        }

        //  Login loop 
        System.out.println("\n--- LOGIN ---");
        Login login = new Login(reg);

        while (true) {
            System.out.print("Enter username: ");
            String enteredUsername = input.nextLine();

            System.out.print("Enter password: ");
            String enteredPassword = input.nextLine();

            String loginResult = login.returnLoginStatus(enteredUsername, enteredPassword);
            System.out.println(loginResult);

            if (loginResult.contains("Welcome")) {
                break;
            }
            System.out.println("Please try again.\n");
        }

        // QuickChat menu (only reached after successful login)
        System.out.println("\nWelcome to QuickChat.");

        // asking how many messages the user wants to send this session
        System.out.print("How many messages would you like to send? ");
        int maxMessages = Integer.parseInt(input.nextLine().trim());
        int messageCount = 0;

        while (true) {
            // Main menu
            System.out.println("\n1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose option: ");
            int choice = Integer.parseInt(input.nextLine().trim());

            if (choice == 1) {

                // Checking if the user has reached their message limit
                if (messageCount >= maxMessages) {
                    System.out.println("You have reached your message limit of " + maxMessages + ".");
                    continue;
                }

                // Collecting the recipients number
                System.out.print("Enter recipient cell number (e.g. +27831234567): ");
                String recipient = input.nextLine();

                // Collecting message text
                System.out.print("Enter your message: ");
                String messageText = input.nextLine();

                // Check message length before proceeding
                String lengthCheck = Message.checkMessageLength(messageText);
                System.out.println(lengthCheck);

                if (!lengthCheck.equals("Message ready to send.")) {
                    System.out.println("Please shorten your message and try again.");
                    continue;
                }

                // Increment message count and create the message object
                messageCount++;
                Message msg = new Message(recipient, messageText, messageCount);

                // Checking recipients number format
                System.out.println(msg.checkRecipientCell());

                if (!msg.checkRecipientCell().equals("Cell phone number successfully captured.")) {
                    messageCount--; // don't count this failed attempt
                    continue;
                }

                // displaying auto generated details
                System.out.println("Message ID generated: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());

                // asking user what to do with the message
                System.out.println("\n1. Send Message");
                System.out.println("2. Disregard Message");
                System.out.println("3. Store Message");
                System.out.print("Choose option: ");
                int sendChoice = Integer.parseInt(input.nextLine().trim());

                System.out.println(msg.sentMessage(sendChoice));

            } else if (choice == 2) {
                // feature still in development
                System.out.println("Coming Soon.");

            } else if (choice == 3) {
                // Display all sent messages before quitting
                System.out.println("\n--- Messages Sent ---");
                Message anyMsg = new Message("", "", 0);
                System.out.println(anyMsg.printMessages());
                System.out.println("Total messages sent: " + Message.returnTotalMessages());
                System.out.println("Goodbye!");
                break;

            } else {
                System.out.println("Invalid option.");
            }
        }

        input.close();
    }
}