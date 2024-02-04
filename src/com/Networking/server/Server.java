package com.Networking.server;

import java.io.InputStream;
import java.net.ServerSocket;

public class Server {
    public Server(){
        try{
            ServerSocket server = new ServerSocket(666);
            server.accept();

        } catch (Exception e){
            e.printStackTrace(); //robust logging and better EXCEPTION handling
        }
    }
    public static void main(String[] args) {
        Server sever = new Server();
        InputStream in;

    }
}
