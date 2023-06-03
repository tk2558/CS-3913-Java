/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author tkong
 */

import java.net.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CS3913hw5 {

    /**
     * @param args the command line arguments
     */
    static Scanner listen_to_server; 
    static PrintStream send_to_server;
    static String ip;
    static String user;
    static String password;
    static TextField tf1, tf2, tf3, tf4;
    static JPanel jp;
    static JPanel chatPanel;
    static JButton enterCredentials;
    static TextArea groupMessages;
    static Socket s;
    static JButton send;
    
    public static void main(String[] args) {
        JFrame jf = new JFrame("Chat Server");
        jf.setSize(400,400);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        
        jp = new JPanel();
        jf.add(jp);
        jp.setLayout(new GridLayout(4,1));   
        
        tf1 = new TextField("Enter Server IP: ", 30);
        tf2 = new TextField("Enter Username: ", 30);
        tf3 = new TextField("Enter password: ", 30);
        jp.add(tf1);
        jp.add(tf2);
        jp.add(tf3);
        
        enterCredentials = new JButton("Connect");
        jp.add(enterCredentials);
        
        enterCredentials.addActionListener(new ButtonPress());
        
        /*Initially hidden chat panel*/
        chatPanel = new JPanel();
        jf.add(chatPanel);
        chatPanel.setVisible(false);
        chatPanel.setLayout(new GridLayout(3,1));
        groupMessages = new TextArea();
        groupMessages.setEditable(false);
        tf4 = new TextField("Enter Message Here", 30);
        send = new JButton("Send");
        chatPanel.add(groupMessages);
        chatPanel.add(tf4);
        chatPanel.add(send);
        send.addActionListener(new ButtonPress());        
    }
    
    static class reciever extends Thread{
        public void run(){
            try{
                while(true){
                    groupMessages.append("\n" + listen_to_server.nextLine());
                } 
            }
            catch(Exception e){}
        }
    }
    
    static class ButtonPress implements ActionListener {  
        @Override
        public void actionPerformed(ActionEvent ae) {
           JButton jb = (JButton) ae.getSource();
           if (jb.equals(enterCredentials)){
               ip = tf1.getText();
               user = tf2.getText();
               password = tf3.getText(); 
                //System.out.println(usernameVal +" " + ipAddress);
               try {
                    s = new Socket(ip, 5190);
                    jp.setVisible(false);
                    chatPanel.setVisible(true);
                    send_to_server = new PrintStream(s.getOutputStream());
                    listen_to_server = new Scanner(s.getInputStream());
                    groupMessages.setText("Welcome to the chat, "  + user );
                    send_to_server.println(user);
                    reciever updateMessages = new reciever();
                    updateMessages.start();
                  /*  if(s.isConnected())
                    {
                        send_to_server.print(user);
                    }*/
                }
                catch(IOException ie){
                    System.out.println("Cant connect to server");
                }
           }
           else if(jb.equals(send)){//when send
               String msg;
               msg = tf3.getText();
               send_to_server.println(msg);
               tf3.setText("");
               //groupMessages.append("\n"+ user + ": "+ msg);
           }
        }
    }
}
