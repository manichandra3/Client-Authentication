package com.Networking.client;

import com.gui.GUI;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public Client(String IP) {
        try {
            Socket client = new Socket(IP, 666);

            SwingUtilities.invokeLater(() -> {
                GUI gui = new GUI();
                Thread show = new Thread(gui);
                show.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost");
        OutputStream out;

    }
}
