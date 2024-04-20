package myGUI;

import CRUD.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Register extends JFrame implements ActionListener {
    JFrame register = new JFrame();
    JLabel userName = new JLabel("Username");
    JLabel passWord = new JLabel("Password");
    JLabel confirm = new JLabel("Confirm");
    JTextField userNameField = new JTextField();
    JTextField passWordField = new JPasswordField();
    JTextField confirmField = new JPasswordField();
    JButton registerButton = new JButton("Register");
    final int FRAME_WIDTH = 600;
    final int FRAME_HEIGHT = 450;
    public Register (){
        register.setTitle("Registration");
        register.setDefaultCloseOperation(EXIT_ON_CLOSE);
        register.setResizable(false);
        register.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        register.setLayout(null);
        register.getContentPane().setBackground(Color.black);
        userName.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        passWord.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        confirm.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        userNameField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        passWordField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        confirmField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        userName.setForeground(Color.white);
        passWord.setForeground(Color.white);
        confirm.setForeground(Color.white);
        userName.setBounds(50, 70, 150, 50);
        passWord.setBounds(50, 140, 150, 50);
        confirm.setBounds(50, 210, 150, 50);
        userNameField.setBounds(250, 70, 300, 50);
        passWordField.setBounds(250, 140, 300, 50);
        confirmField.setBounds(250, 210, 300, 50);


        registerButton.setFont(new Font("JetBrains Mono", Font.BOLD, 25));
        registerButton.setForeground(Color.black);
        registerButton.setBounds(220, 300, 150, 50);
        registerButton.addActionListener(this);

        register.setLocationRelativeTo(null);
        register.add(userName);
        register.add(passWord);
        register.add(confirm);
        register.add(userNameField);
        register.add(passWordField);
        register.add(confirmField);
        register.add(registerButton);
        register.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == registerButton){
            String userName = userNameField.getText();
            String password = passWordField.getText();
            String confirm = confirmField.getText();
            userNameField.setText("");
            passWordField.setText("");
            confirmField.setText("");
            if(userName.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(null, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(confirm.equals(password) == false){
                JOptionPane.showMessageDialog(null, "Passwords Do Not Match!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                Query query = new Query();
                String sql1 = "SELECT * FROM `User` WHERE `username` = '" + userName + "';";
                //System.out.println(sql1);
                Object[][] array = query.retrieve(sql1);
                //System.out.println("hello");
                if(array.length == 0){
                    String sql2 = "INSERT INTO User(username, password) VALUES('" + userName + "','" + password + "')";
                    //System.out.println(sql2);
                    query.execute(sql2);
                    //JOptionPane.showMessageDialog(null, "Registration Successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                    register.dispose();
                    JFrame logIn = new LogIn();
                }
                else{
                    JOptionPane.showMessageDialog(null, "This Username Is Taken!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        }
    }
}




