package myGUI;

import CRUD.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogIn extends JFrame implements ActionListener {
    JFrame logIn = new JFrame();
    JLabel userName = new JLabel("Username");
    JLabel passWord = new JLabel("Password");
    JTextField userNameField = new JTextField();
    JTextField passWordField = new JPasswordField();
    JButton logInButton = new JButton("LogIn");
    JButton registerButton = new JButton("Register");
    final int FRAME_WIDTH = 600;
    final int FRAME_HEIGHT = 450;
    public LogIn (){
        logIn.setTitle("LogIn");
        logIn.setDefaultCloseOperation(EXIT_ON_CLOSE);
        logIn.setResizable(false);
        logIn.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        logIn.setLayout(null);
        logIn.getContentPane().setBackground(Color.black);
        userName.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        passWord.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        userNameField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        passWordField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        userName.setForeground(Color.white);
        passWord.setForeground(Color.white);
        userName.setBounds(50, 100, 150, 50);
        passWord.setBounds(50, 170, 150, 50);
        userNameField.setBounds(250, 100, 300, 50);
        passWordField.setBounds(250, 170, 300, 50);


        logInButton.setFont(new Font("JetBrains Mono", Font.BOLD, 25));
        logInButton.setForeground(Color.black);
        logInButton.setBounds(120, 250, 130, 50);
        logInButton.addActionListener(this);

        registerButton.setFont(new Font("JetBrains Mono", Font.BOLD, 25));
        registerButton.setForeground(Color.black);
        registerButton.setBounds(300, 250, 150, 50);
        registerButton.addActionListener(this);

        logIn.setLocationRelativeTo(null);
        logIn.add(userName);
        logIn.add(passWord);
        logIn.add(userNameField);
        logIn.add(passWordField);
        logIn.add(logInButton);
        logIn.add(registerButton);
        logIn.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == logInButton){
            String userName = userNameField.getText();
            String password = passWordField.getText();
            userNameField.setText("");
            passWordField.setText("");
            if(userName.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(null, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Query query = new Query();
                String sql = "SELECT * FROM `User` WHERE `username` = '" + userName + "';";
                Object[][] array = query.retrieve(sql);
                if(array.length != 0){
                    sql = "SELECT * FROM `User` WHERE `username` = '" + userName + "' and `password` = '" + password + "';";
                    array = query.retrieve(sql);
                    if(array.length != 0){
                        JOptionPane.showMessageDialog(null, "LogIn Successful!", "Welcome Back!", JOptionPane.INFORMATION_MESSAGE);
                        logIn.dispose();
                        PostBase post = new PostBase(userName);
                        //System.out.println(post.getUserName());
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Incorrect Password!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    int choice = JOptionPane.showConfirmDialog(null, "This Username Is Not Registered \n Do You Want To Register?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION){
                        logIn.dispose();
                        JFrame register = new Register();
                    }
                }
            }
        }
        if(e.getSource() == registerButton){
            logIn.dispose();
            JFrame register = new Register();
        }
    }
}

