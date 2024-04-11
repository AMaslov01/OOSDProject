package myGUI;

import CRUD.Query;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Register extends JFrame implements ActionListener {
    JFrame register = new JFrame();
    JLabel userName = new JLabel("Username");
    JLabel passWord = new JLabel("Password");
    JTextField userNameField = new JTextField();
    JTextField passWordField = new JPasswordField();
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
        userNameField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        passWordField.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        userName.setForeground(Color.white);
        passWord.setForeground(Color.white);
        userName.setBounds(50, 100, 150, 50);
        passWord.setBounds(50, 170, 150, 50);
        userNameField.setBounds(250, 100, 300, 50);
        passWordField.setBounds(250, 170, 300, 50);


        registerButton.setFont(new Font("JetBrains Mono", Font.BOLD, 25));
        registerButton.setForeground(Color.black);
        registerButton.setBounds(220, 250, 150, 50);
        registerButton.addActionListener(this);

        register.setLocationRelativeTo(null);
        register.add(userName);
        register.add(passWord);
        register.add(userNameField);
        register.add(passWordField);
        register.add(registerButton);
        register.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == registerButton){
            String userName = userNameField.getText();
            String password = passWordField.getText();
            Query query = new Query();
            String sql = "INSERT INTO User(username, password) VALUES('" + userName + "','" + password + "')";
            System.out.println(sql);
            query.execute(sql);
        }
    }
}




