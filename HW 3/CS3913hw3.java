/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */

/**
 *
 * @author tkong
 */
package CS3913hw3;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class CS3913hw3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] questions = { 
            "The Sky is blue", 
            "The air is 21% oxygen", 
            "This exam is in C++", "5+5 = 55", 
            "Spring break was last week", 
            "Charlie Chaplain is President of the USA" 
        };
        boolean[] answers = { true, true, false, false, false, false };
        ExamQ e2 = new ExamQ(questions, answers);
        e2.startQuiz();
    }
}

class ExamQ {
  JFrame jf; // variable for JFrame
  Random rand; // random variable
  
  int num; // counter for number of questions answered
  int correct; // counter for correct answers, (incorrect answers = num - correct)
  
  String[] questions = new String[3]; // questions for user to be tested
  boolean[] answers = new boolean[3]; // answers corresponding to questions 
  boolean[] answered = new boolean[3]; // True = question was answered, False = question was NOT answer (i.e. user did not select an option)
  
  ExamQ(String[] q, boolean[] a) {
    rand = new Random(); // random variable initialized
    for (int i = 0; i < 3; i++) { // loop 3 times for 3 random questions (duplicate questions allowed)
        int r = rand.nextInt(q.length); // get random number within range of number of available questions
        questions[i] = q[r]; // add random question to question array
        answers[i] = a[r]; // add random question's respective answer to answer array
    }
    correct = 0; // default set to 0
    num = 0; // default set to 0
    // array of booleans like answers and answered default to array of falses
  }
  
  public void startQuiz() {
    if (num == 3) { // END SCREEN (when all 3 questions are done)
      // SET FRAME UP
      jf = new JFrame("Quiz complete"); // frame title
      jf.setDefaultCloseOperation(3); // JFrame.DO_NOTHING_ON_CLOSE - ignore click
      jf.setSize(800,800); // set frame size
      
      // SET RESULT UP
      JLabel result = new JLabel("You got " + correct + " out of 3!"); // result screen (correct answers / 3 total questions)
      jf.add(result); // showcase result in frame
      jf.setVisible(true); // Setting frame visibility = TRUE (Open Window)
    }
    
    else { // still have questions that need to be answered
      // SET FRAME UP
      jf = new JFrame("Quiz Question " + (num + 1)); // frame title (tracking current question number)
      jf.setDefaultCloseOperation(3); // JFrame.DO_NOTHING_ON_CLOSE - ignore click
      jf.setSize(800,800); // set frame size
      JPanel jp = new JPanel(); // set up panel
      
      // SET QUESTION UP
      jf.add(jp); // add panel
      jp.setLayout(new GridLayout(2, 3)); // set 2 by 3 grid for question, true button and false button
      jp.add(new JLabel()); // add to panel...
      jp.add(new JLabel(questions[num])); // add question to panel
      
      // SET TRUE BUTTON UP
      jp.add(new JLabel()); // add to panel....
      JButton tb = new JButton("True"); // initialize tb (true button)
      tb.addActionListener(new TB_pressed()); // true button has function when pressed
      jp.add(tb); // add true button to panel
      
      // SET FALSE BUTTON UP
      jp.add(new JLabel()); // add to panel...
      JButton fb = new JButton("False"); // initialize fb (false button)
      fb.addActionListener(new FB_pressed()); // false button has function when pressed
      jp.add(fb); // add false button to panel
      
      // SET TIMER UP
      runTimer timer = new runTimer(); // time limit for selecting an answer
      timer.start(); // start time limit
      jf.setVisible(true); // Setting frame visibility = TRUE (open window)
      // System.out.println(num); // END CHECK
    }
  }
  
  class TB_pressed implements ActionListener { // True Button ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) { // when pressed
        if (answers[num] == true) { // check if answer is true
            correct++;  // if true, increment num of questions user got correct
        }
        answered[num] = true; // mark down that this question was answered
        num++; // moving on to next question or END screen
        jf.setVisible(false); // Setting frame visibility = FALSE (close window)
        startQuiz(); // start quiz again
    }
  }
  
  class FB_pressed implements ActionListener { // False Button ActionListener 
    @Override
    public void actionPerformed(ActionEvent e) { // when pressed
        if (answers[num] == false) { // check if answer is false
            correct++; // if false, increment num of questions user got correct
        }
        answered[num] = true; // mark down that this question was answered
        num++; // moving on to next question or END screen
        jf.setVisible(false); // Setting frame visibility = FALSE (close window)
        startQuiz(); // start quiz again
    }
  }
  
  class runTimer extends Thread { // start a timer using a thread
    int check = num; // get current question num to check for
    @Override
    public void run() { // run timer
      try {
        sleep(5000); // waiting 5 seconds
      } 
      catch (InterruptedException ex) {}  // catch interruptions
      
      if (answered[check] == false) { // if not answered after 5 seconds
        num++; // increment num of questions answered, no answer defaults to NOT correct (so don't increment ExamQ correct)
        jf.setVisible(false); // Setting frame visibility = FALSE (close window)
        startQuiz(); // start quiz for next question or END
      } 
    }
  }
}
