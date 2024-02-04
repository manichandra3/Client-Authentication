package com.gui;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lib.org.mindrot.jbcrypt.BCrypt;
//Hash the passwords.
public class GUI implements ActionListener, Runnable {

    private static JTextField userText;
    private static JPasswordField passwordText;
    private static final String USER_CREDS_FILE_PATH = "/Users/manichandraganapathri/second_year/cs202/GUIcoolness/src/com/gui/UserCreds.txt";
    private static final String LOGS_FILE_PATH = "/Users/manichandraganapathri/second_year/cs202/GUIcoolness/src/Logs.txt";
    public GUI() {
    }
    public void run(){
        SwingUtilities.invokeLater(GUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel userLabel = new JLabel("User:");
        userText = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new GUI());

        JButton newUserButton = new JButton("New User");
        newUserButton.addActionListener(e -> saveUser());

        panel.add(userLabel);
        panel.add(userText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        panel.add(loginButton);
        panel.add(newUserButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userText.getText();
        String password = new String(passwordText.getPassword());

        if (authenticateUser(user, password)) {

            try (FileWriter writer = new FileWriter(LOGS_FILE_PATH, true)) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                writer.write(user+"," + dtf+"\n" );
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Login successful!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password. Please check the requirements.");
        }
    }

    private static boolean validateUser(String username, String password) {
        // Simple validation: Username must have at least 4 characters, password must have at least 6 characters and must not have a comma
        return username.length() >= 4 && password.length() >= 6;
    }


    private static boolean authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].trim().equals(username)) {
                    // Check if the entered password matches the stored hashed password
                    if (BCrypt.checkpw(password, parts[1].trim())) {
                        return true; // Passwords match
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false; // No matching user or password found
    }



    private static void saveUser() {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());

        if (validateUser(username, password)) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    saveUserToFile(username, BCrypt.hashpw(password,BCrypt.gensalt()));
                    return null;
                }

                @Override
                protected void done() {
                    userText.setText("");
                    passwordText.setText("");
                }
            };
            JOptionPane.showMessageDialog(null, "Successfully Registered.");
            worker.execute();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password. Please check the requirements.");
        }
    }

    private static void saveUserToFile(String username, String password) {
        try (FileWriter writer = new FileWriter(USER_CREDS_FILE_PATH, true)) {
            writer.write(username + "," + password + "\n");
            System.out.println("New USER saved to file UserCreds.txt!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}