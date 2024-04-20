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
        setPreferredSize(new Dimension(500,888));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        stylePanel(this);
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Username
        JLabel label1 = new JLabel("Username:");
        add(label1, gbc);
        styleLabel(label1);
        gbc.gridx = 1;
        userNameField = new JTextField(20);
        add(userNameField, gbc);
        styleText(userNameField);

        // Password
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel label2 = new JLabel("Password:");
        add(label2, gbc);
        styleLabel(label2);
        gbc.gridx = 1;
        passWordField = new JPasswordField(20);
        add(passWordField, gbc);
        styleText(passWordField);

        // Confirm Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel label3 = new JLabel("Confirm:");
        add(label3, gbc);
        styleLabel(label3);
        gbc.gridx = 1;
        confirmField = new JPasswordField(20);
        add(confirmField, gbc);
        styleText(confirmField);

        // Register Button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        registerButton = new JButton("Register");
        styleButton(registerButton);
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
                //JOptionPane.showMessageDialog(this, "Registration Successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.showLoginPanel(); // Navigate back to the login panel after successful registration
            } else {
                JOptionPane.showMessageDialog(this, "This Username Is Taken!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void styleLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
    }
    private void styleText(JTextField text){
        text.setPreferredSize(new Dimension(100, 35));
        text.setFont(new Font("JetBrains Mono", Font.PLAIN, 22));
    }
    private void styleButton(JButton button){
        button.setPreferredSize(new Dimension(160, 50));
        button.setFont(new Font("JetBrains Mono", Font.PLAIN, 30));
    }
    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }
}