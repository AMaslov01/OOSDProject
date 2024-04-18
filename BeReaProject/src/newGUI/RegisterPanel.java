package newGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegisterPanel extends JPanel {
    private JTextField userNameField;
    private JPasswordField passWordField;
    private JPasswordField confirmField;
    private JButton registerButton;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        userNameField = new JTextField(20);
        add(new JLabel("Username:"), gbc);
        add(userNameField, gbc);

        // Password
        passWordField = new JPasswordField(20);
        add(new JLabel("Password:"), gbc);
        add(passWordField, gbc);

        // Confirm Password
        confirmField = new JPasswordField(20);
        add(new JLabel("Confirm Password:"), gbc);
        add(confirmField, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    performRegistration();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(registerButton, gbc);
    }

    private void performRegistration() throws SQLException {
        String userName = userNameField.getText();
        String password = new String(passWordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!confirm.equals(password)) {
            JOptionPane.showMessageDialog(this, "Passwords Do Not Match!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Query query = new Query();
            String sql1 = "SELECT * FROM `User` WHERE `username` = '" + userName + "';";
            Object[][] array = query.retrieve(sql1);
            if (array.length == 0) {
                String sql2 = "INSERT INTO User(username, password) VALUES('" + userName + "','" + password + "')";
                query.execute(sql2);
                JOptionPane.showMessageDialog(this, "Registration Successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showLoginPanel(); // Navigate back to the login panel after successful registration
            } else {
                JOptionPane.showMessageDialog(this, "This Username Is Taken!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}