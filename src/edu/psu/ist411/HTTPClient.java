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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

// Sources:     http://proquestcombo.safaribooksonline.com.ezaccess.libraries.psu.edu/9781785885471
//              https://stackoverflow.com/questions/3114606/random-character-generator-with-a-range-of-a-z-0-9-and-punctuation/14021472
//              https://www.javatpoint.com/java-char-to-string

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
public class HTTPClient {
    public HTTPClient() {
        System.out.println("HTTP Client Started");

        // Tries a POST
        try {
            InetAddress serverInetAddress =
               InetAddress.getByName("127.0.0.1");
            Socket connection = new Socket(serverInetAddress, 80);

            try (OutputStream out = connection.getOutputStream();
                 BufferedReader in =
                     new BufferedReader(new
                         InputStreamReader(
                             connection.getInputStream()))) {
                sendPost(out);
                System.out.println(getResponse(in));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Tries a GET
        try {
            InetAddress serverInetAddress =
               InetAddress.getByName("127.0.0.1");
            Socket connection = new Socket(serverInetAddress, 80);

            try (OutputStream out = connection.getOutputStream();
                 BufferedReader in =
                     new BufferedReader(new
                         InputStreamReader(
                             connection.getInputStream()))) {
                sendGet(out);
                System.out.println(getResponse(in));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void sendPost(OutputStream out) {
        // Generates a random letter
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int N = alphabet.length();
        Random r = new Random();
        char randomLetter;

        for (int i = 0; i < 1; i++) {
            randomLetter = (alphabet.charAt(r.nextInt(N)));
            String randomLetterString = String.valueOf(randomLetter);
            String post = "POST /" + randomLetterString + "\r\n";

            // Posts a random letter to diary
            try {
                out.write(post.getBytes());
                out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendGet(OutputStream out) {
        try {
            out.write("GET /default\r\n".getBytes());
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