/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author tkong
 */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CS3913hw4 {
    static int port = 5190; // PORT NUMBER CAN BE CHANGED IN THE FUTURE
    
    public static void main(String[] args) {
        try  { // trying to establish server in port
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) { // While established...
                Socket user = serverSocket.accept(); // accepting users
                System.out.println("Chat Server connected on port 5190"); // SUCCESS MSG
                // Create a new thread to handle this client's requests
                new ProcessConnection(user).start();
            }
        } catch (IOException e) { // ERROR
            System.err.println("Error: Unable to bind to port!"); // ERROR MSG
        }
    }
}

class ProcessConnection extends Thread{
    Socket user; // individual socket or user
    static ArrayList<ProcessConnection> users = new ArrayList<ProcessConnection>(); // list of users
    static HashMap<String, String> database = new HashMap<String, String>();
    static LocalDateTime loginTime = LocalDateTime.now(); // current time
    
    ProcessConnection(Socket newUser){
        user = newUser;
    }
    
    @Override
    public void run(){
        try {            
            Scanner sin = new Scanner(user.getInputStream());
            PrintStream sout = new PrintStream(user.getOutputStream());
            // Getting username and password
            String username = sin.next(); // taking the first thing user says as username
            String password = sin.next(); // taking the next thing user says as password
            
            // Authenticate user
            if (authenticate(username, password)) { // check database based on user data
                users.add(this); // Add user to array list of all users
                sout.println("200"); // Success MSG
                
                // Listen for messages send to all clients
                String message = ""; // next thing user says is saved
                while (!message.equalsIgnoreCase("EXIT")){ // As long as it isn't "EXIT"
                    message = sin.next();
                    for (ProcessConnection p : users) {
                        p.send_msg(message); // send the message to all users in form "User: hello"
                        message = sin.next(); // next thing user says is saved
                    }
                }
                send_msg(username + " disconnected");
            }
            else {
                sout.println("500"); // Error MSG
                user.close(); // close socket
           }
            
        } catch(Exception e){
            System.out.println(user.getInetAddress() + " disconnected"); // User disconnects MSG
        }
    }
    
    private boolean authenticate(String username, String password) { // authenticating user
        try {
            Connection conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1/cs3913", "cs3913", "abc123");
            String ip_address = user.getInetAddress().getHostAddress(); // log IP address
            String login_time = loginTime.format(DateTimeFormatter.ISO_DATE_TIME); // log time
            String query = "INSERT INTO LOGINS(" + ip_address + ", " + login_time + ", " + username + ")";
            
            //PreparedStatement insert = conn.prepareStatement("INSERT INTO LOGINS (username, ip_address, login_time) VALUES (?, ?, ?)");
            if (database.containsKey(username)) { // CHECK after executing
                if (!database.get(username).equals(password)) { // Right Username, Wrong Password
                    return false; // return false
                }
            } 
            else { // New Username & Password
                database.put(username, password); // New entry to database
            }
            // Log to SQL
            Statement s = conn.createStatement(); // Statement
            s.executeUpdate(query); // UPDATE SQL DATABASE
        } 
        catch (Exception e) { // SQL ERROR CHECK
            System.out.println("Error in SQL!"); // ERROR MSG
        }
        return true; // Authentication Good To Go!
    }
    
    private void send_msg(String message) { // send message to chat
        try {
            PrintStream sout = new PrintStream(user.getOutputStream());
            sout.println(message); // print msg
        } catch (Exception e) { // ERROR
            System.err.println("Error: could not send message!"); // ERROR MSG
        }
    }
}