/*
 * Copyright 2018 Group 5.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.psu.ist411;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

// Sources: http://proquestcombo.safaribooksonline.com.ezaccess.libraries.psu.edu/9781785885471

/**
 * Group project.
 * Course: IST 411
 * Semester: Summer 2018
 * Instructor: Jeff Rimland
 *
 * @author Tyler Suehr
 * @author Win Ton
 * @author Steven Weber
 * @author David Wong
 */
public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(final Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("\nClientHandler Started for " +
            this.socket);
        handleRequest(this.socket);
        System.out.println("ClientHandler Terminated for "
            + this.socket + "\n");
    }

    public void handleRequest(final Socket socket) {
        try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            final String headerLine = in.readLine();
            final StringTokenizer tokenizer = new StringTokenizer(headerLine);
            final String httpMethod = tokenizer.nextToken();

            if (httpMethod.equals("GET")) {
                System.out.println("Get method processed.");

                // Unnecessary.
                // String httpQueryString = tokenizer.nextToken();

                // Reads diary.txt into variable.
                Scanner diary = new Scanner("diary.txt");

                // Starts responseBuffer.
                StringBuilder responseBuffer = new StringBuilder();
                responseBuffer
                    .append("<html><h1>This is a diary! </h1><br>");

                // Reads diary.txt into responseBuffer.
                while (diary.hasNextLine()) {
                    final String line = diary.nextLine();

                    responseBuffer
                        .append("<b>" + line + "</b><BR>");
                }

                // Closes "diary.txt".
                diary.close();

                // Ends responseBuffer.
                responseBuffer
                    .append("</html>");

                // Sends responseBuffer.
                sendResponse(socket, 200, responseBuffer.toString());

            } else if (httpMethod.equals("POST")) {
                System.out.println("Post method processed.");

                // Processes next token.
                final String httpQueryString = tokenizer.nextToken();

                // Creates or opens "diary.txt".
                final FileWriter fw = new FileWriter("diary.txt", true);

                // Writes token to "diary.txt".
                fw.write(httpQueryString + "\n");

                final StringBuilder responseBuffer = new StringBuilder();
                responseBuffer
                    .append("<html><h1> Posted to diary: </h1><br>")
                    .append("<b>" + httpQueryString + "</b><BR>")
                    .append("</html>");
                sendResponse(socket, 200, responseBuffer.toString());
            } else {
                System.out.println("The HTTP method is not recognized.");
                sendResponse(socket, 405, "Method Not Allowed");
            }

        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }

    public void sendResponse(
        final Socket socket,
        final int statusCode,
        final String responseString
    ) {
        String statusLine;
        final String serverHeader = "Server: WebServer\r\n";
        final String contentTypeHeader = "Content-Type: text/html\r\n";

        try (final DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
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
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }
}