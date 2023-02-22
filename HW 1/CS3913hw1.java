/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

package CS3913hw1;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random; // Random Package

/**
 *
 * @author tkong
 */

public class CS3913hw1 {
    public static final int numB = 8; // number of Button can be changed for future versions
    public static Random rand;  // random value
    public static JButton[] arr = new JButton[numB]; // array of buttons created
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        rand = new Random(); // new random generator
        
        JFrame jf = new JFrame("My First window!"); // Java Frame
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Frame close operation
        
        JButton jb = new JButton(); // Initialize button
        // Random Color Method -> new Color(random red value, random green value, random blue value) = Random Color
        jb.setBackground((new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)))); // Random Color Method
        jf.setLayout(new GridLayout(4,2,4,4)); // 4 by 2 Grid Frame
        
        for (int i = 0; i< 8; i++){ // For each grid space...
            arr[i] = new JButton("Button " + (i+1)); // add button and name it based on its creation 
            arr[i].setBackground(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))); // set random background color
            arr[i].addActionListener(new ButtonPressed()); // when button is pressed featured
            jf.add(arr[i]); // add button to frame
        }
        
        jf.setSize(1000,400); // set frame size
        jf.setVisible(true); // Setting frame visibility = TRUE
        System.out.println("The end of main!"); // END CHECK
    }
}

class ButtonPressed extends CS3913hw1 implements ActionListener { // Button Pressed extends to access public static variables
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource(); // Button that was clicked
        for (int i = 0; i < numB; i++) { // for every button
            if (source != arr[i]) { // except button that was clicked...
                // change button color background 
                arr[i].setBackground(new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256))); // Random Color Method
            }
        }
    }
}