
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

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
//              https://stackoverflow.com/questions/13386107/how-to-remove-single-character-from-a-string


public class ClientHandler implements Runnable{
    
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("ClientHandler Started for " + 
            this.socket);
        handleRequest(this.socket);
        System.out.println("ClientHandler Terminated for " 
            + this.socket + "\n");
    }

    public void handleRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));) {
            String headerLine = in.readLine();
            StringTokenizer tokenizer = 
                new StringTokenizer(headerLine);
            String httpMethod = tokenizer.nextToken();
            
            if (httpMethod.equals("GET")) {
                System.out.println("Get method processed");
                
                // Reads next token
                String httpQueryString = tokenizer.nextToken();
                
                // Formats httpQueryString
                String formattedHttpQueryString = httpQueryString.replace("/", "");
                
                if (formattedHttpQueryString.equals("diary")) {
                
                    // Reads diary.txt into variable
                    Scanner diary = new Scanner("diary.txt");

                    // Starts a responseBuffer
                    StringBuilder responseBuffer = new StringBuilder();
                    responseBuffer
                        .append("<html><h1>Getting diary. </h1><br>\n");

                    // Reads diary.txt into responseBuffer
                    while (diary.hasNextLine()) {
                        String line = diary.nextLine();

                        // Formats read in string
                        String formattedLine = line.replace("/", "");
                        
                        responseBuffer
                            .append("<b>" + formattedLine + "</b><BR>\n");
                    }

                    // Closes diary.txt
                    diary.close();

                    // Ends responseBuffer
                    responseBuffer
                        .append("</html>");

                    // Sends responseBuffer
                    sendResponse(socket, 200, responseBuffer.toString());
                }
                
            } else if (httpMethod.equals("POST")) {
                System.out.println("Post method processed");
                
                // Processes next token
                String httpQueryString = tokenizer.nextToken();
                
                // Creates or opens diary.txt
                FileWriter fw = new FileWriter("diary.txt", true);
                
                // Writes token to diary.txt
                fw.write(httpQueryString + "\n");
                
                // Formats post
                String formattedHttpQueryString = httpQueryString.replace("/", "");
                
                StringBuilder responseBuffer = new StringBuilder();
                responseBuffer
                    .append("<html><h1> Posted to diary: </h1><br>\n")
                    .append("<b>" + formattedHttpQueryString + "</b><BR>\n")
                    .append("</html>");
                sendResponse(socket, 200, responseBuffer.toString());
            } else {
                System.out.println("The HTTP method is not recognized");
                sendResponse(socket, 405, "Method Not Allowed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendResponse(Socket socket, 
            int statusCode, String responseString) {
        String statusLine;
        String serverHeader = "Server: WebServer\r\n";
        String contentTypeHeader = "Content-Type: text/html\r\n";

        try (DataOutputStream out = 
                new DataOutputStream(socket.getOutputStream());) {
            
            if (statusCode == 200) {
                statusLine = "HTTP/1.0 200 OK" + "\r\n";
                String contentLengthHeader = "Content-Length: " 
                    + responseString.length() + "\r\n";

                out.writeBytes(statusLine);
                out.writeBytes(serverHeader);
                out.writeBytes(contentTypeHeader);
                out.writeBytes(contentLengthHeader);
                out.writeBytes("\r\n");
                out.writeBytes(responseString);
            } else if (statusCode == 405) {
                statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            } else {
                statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
                out.writeBytes(statusLine);
                out.writeBytes("\r\n");
            }
            
            out.close();
        } catch (IOException ex) {
            // Handle exception
        }
    }
    

}


