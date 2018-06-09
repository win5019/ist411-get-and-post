
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

/*
 * Course: IST 411
 * Semester: Summer 2018
 * Instructor: Jeff Rimland
 * Group 5:    Tyler Suehr
 *             Win Ton
 *             Steven Weber
 *             David Wong
 */

// Sources:     http://proquestcombo.safaribooksonline.com.ezaccess.libraries.psu.edu/9781785885471
//              https://stackoverflow.com/questions/3114606/random-character-generator-with-a-range-of-a-z-0-9-and-punctuation/14021472
//              https://www.javatpoint.com/java-char-to-string

public class HTTPClient {

    public HTTPClient() {
        System.out.println("HTTP Client Started");
        
        // Tries to open a socket connection and POST to diary
        try {
            InetAddress serverInetAddress = 
               InetAddress.getByName("127.0.0.1");
            Socket connection = new Socket(serverInetAddress, 80);

            try (OutputStream out = connection.getOutputStream();
                 BufferedReader in = 
                     new BufferedReader(new 
                         InputStreamReader(
                             connection.getInputStream()))) {
                sendPostToDiary(out);
                System.out.println(getResponse(in));
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        // Tries to GET diary
        try {

            InetAddress serverInetAddress = 
               InetAddress.getByName("127.0.0.1");
            Socket connection = new Socket(serverInetAddress, 80);

            try (OutputStream out = connection.getOutputStream();
                 BufferedReader in = 
                     new BufferedReader(new 
                         InputStreamReader(
                             connection.getInputStream()))) {
                sendGetDiary(out);
                System.out.println(getResponse(in));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendPostToDiary(OutputStream out) {
        // Generates a random letter
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int N = alphabet.length();
        Random r = new Random();
        char randomLetter;
        
        for (int i = 0; i < 1; i++) {
            randomLetter = (alphabet.charAt(r.nextInt(N)));
            String randomLetterString = String.valueOf(randomLetter);
            String post = "POST /RandomlyGeneratedLetter:" + randomLetterString + "\r\n";

            // Posts a random letter to diary
            try {
                out.write(post.getBytes());
                out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void sendGetDiary(OutputStream out) {
        try {
            out.write("GET /diary\r\n".getBytes());
            out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private String getResponse(BufferedReader in) {
        try {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            return response.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    
    public static void main(String[] args) {
        new HTTPClient();
    }
    
}
