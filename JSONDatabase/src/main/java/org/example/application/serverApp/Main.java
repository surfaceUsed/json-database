package org.example.application.serverApp;

import org.example.application.serverApp.server.Server;
import org.example.application.serverApp.server.util.Directory;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5555)) {

            // Receives path to server directory from command line.
            Server server = new Server(serverSocket, Directory.loadDirectory(args[0]));

            server.start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
