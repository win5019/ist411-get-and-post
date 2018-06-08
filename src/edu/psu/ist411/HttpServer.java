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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
public class HttpServer {
    public HttpServer() {
        System.out.println("HTTP server started.");

        try (final ServerSocket serverSocket = new ServerSocket(80)) {
            while (true) {
                System.out.println("Waiting for client request...");
                Socket remote = serverSocket.accept();
                System.out.println("Connection made.");
                new Thread(new ClientHandler(remote)).start();
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new HttpServer();
    }
}