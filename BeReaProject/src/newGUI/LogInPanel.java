package newGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LogInPanel extends JPanel {
    private JTextField userNameField;
    private JPasswordField passWordField;
    private JButton logInButton;
    private JButton registerButton;
    private JButton logoutButton;
    private MainFrame mainFrame;  // Reference to the main application window

    public LogInPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setPreferredSize(new Dimension(500,888));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        stylePanel(this);
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Username
        JLabel labelUserName = new JLabel("Username:");
        styleLabel(labelUserName);
        add(labelUserName, gbc);
        gbc.gridx = 1;
        userNameField = new JTextField(20);
        styleText(userNameField);
        add(userNameField, gbc);

        // Password
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel labelPassWord = new JLabel("Password:");
        styleLabel(labelPassWord);
        add(labelPassWord, gbc);
        gbc.gridx = 1;
        passWordField = new JPasswordField(20);
        styleText(passWordField);
        add(passWordField, gbc);

        // Login Button
        gbc.gridy = 2;
        gbc.gridx = 0;
        logInButton = new JButton("Login");
        styleButton(logInButton);
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = userNameField.getText().trim();
                String password = new String(passWordField.getPassword());
                if (userName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    mainFrame.showLoadingPanel();

                    // Create a SwingWorker to handle login asynchronously
                    SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                        @Override
                        protected Boolean doInBackground() throws Exception {
                            // Perform the authentication check asynchronously
                            mainFrame.updateUserSession(userName);
                            Query query = new Query();
                            return query.authenticateUser(userName, password);
                        }

                        @Override
                        protected void done() {
                            try {
                                if (get()) {
                                    SwingUtilities.invokeLater(() -> mainFrame.showPostPanel());
                                } else {
                                    SwingUtilities.invokeLater(() -> {
                                        mainFrame.showLoginPanel();
                                        JOptionPane.showMessageDialog(null, "Incorrect Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                                    });
                                }
                            } catch (InterruptedException | ExecutionException ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                    worker.execute(); // Start the worker thread
                }
            }
        });
        add(logInButton, gbc);

        // Register Button
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        registerButton = new JButton("Register");
        styleButton(registerButton);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showRegisterPanel();
            }
        });
        add(registerButton, gbc);
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
        button.setFont(new Font("JetBrains Mono", Font.PLAIN, 25));
    }

    public void clearTextFields() {
        this.userNameField.setText("");
        this.passWordField.setText("");
    }

    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }

    public JTextField getUserNameField() {
        return userNameField;
    }
}