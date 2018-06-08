
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Course: IST 411
 * Semester: Summer 2018
 * Instructor: Jeff Rimland
 * Group 5:    Tyler Suehr
 *             Win Ton
 *             Steven Weber
 *             David Wong
 */

// Sources: http://proquestcombo.safaribooksonline.com.ezaccess.libraries.psu.edu/9781785885471

public class HTTPServer {
    public HTTPServer() {
        System.out.println("HTTPserver Started");
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            while (true) {
                System.out.println("Waiting for client request");
                Socket remote = serverSocket.accept();
                System.out.println("Connection made");
                new Thread(new ClientHandler(remote)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new HTTPServer();
    }
    
}
