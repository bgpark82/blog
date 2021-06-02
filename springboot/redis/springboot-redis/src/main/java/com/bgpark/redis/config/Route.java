package com.bgpark.redis.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Route {

    private static final String HOST = "125.7.208.52";
    private static final int PORT = 9710;
    private static final String REQUEST = "<xml><svc svr=\"RPS\" fn=\"FindRoute\" method=\"sht-path\" via=\"\" pts=\"true\"><start x=\"940370.0\" y=\"1969051.3\" /><goal x=\"946443.1\" y=\"1963041.6\" /></svc></xml>";

    public static void main(String[] args) {
        try(Socket socket = new Socket(HOST, PORT)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);

            pr.print(REQUEST);
            pr.flush();

            System.out.println("Response: " + br.readLine());
        } catch (UnknownHostException e) {
            System.out.println("Server not found: " + e.getMessage());
        } catch (IOException e){
            System.out.println("I/O error: " + e.getMessage());
        }
    }
}
