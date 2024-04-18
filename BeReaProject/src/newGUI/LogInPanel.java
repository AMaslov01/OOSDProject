package newGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;

public class LogInPanel extends JPanel {
    private JTextField userNameField;
    private JPasswordField passWordField;
    private JButton logInButton;
    private JButton registerButton;
    private MainFrame mainFrame;  // Reference to the main application window

    public LogInPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        userNameField = new JTextField(20);
        add(new JLabel("Username:"), gbc);
        add(userNameField, gbc);

        // Password
        passWordField = new JPasswordField(20);
        add(new JLabel("Password:"), gbc);
        add(passWordField, gbc);

        // Login Button
        logInButton = new JButton("Login");
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText();
                String password = passWordField.getText();
                if(userName.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(null, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Query query = new Query();
                    String sql = "SELECT * FROM `User` WHERE `username` = '" + userName + "' AND `password` = '" + password + "';";
                    Object[][] user = new Object[0][];
                    try {
                        user = query.retrieve(sql);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(user.length != 0){
                        mainFrame.updateUserSession(userName);
                        JOptionPane.showMessageDialog(null, "LogIn Successful!", "Welcome Back!", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println(Arrays.toString(SessionManager.getFriends()));
                        mainFrame.showPostPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        add(logInButton, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegisterPanel();
            }
        });
        add(registerButton, gbc);
    }

    public JTextField getUserNameField() {
        return userNameField;
    }
}