package newGUI;

import CRUD.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

/**
 * Class representing a login panel where users can enter their credentials to log into the system.
 * It provides fields for entering a username and password, and buttons for submitting credentials or registering a new account.
 */
public class LogInPanel extends JPanel {
    private JTextField userNameField;
    private JPasswordField passWordField;
    private JButton logInButton;
    private JButton registerButton;
    private MainFrame mainFrame;  // Reference to the main application window

    /**
     * Constructor for LogInPanel.
     * Initializes the user interface components to facilitate user login.
     * @param mainFrame The main application window that holds this panel.
     */
    public LogInPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    /**
     * Initializes the user interface components of the Login panel.
     * Configures layout, adds text fields for username and password, and buttons for login and registration.
     * Sets action listeners for the buttons to handle user interaction.
     */
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
                                    SwingUtilities.invokeLater(() -> mainFrame.showFeedPanel());
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

    /**
     * Styles JLabels used in this panel.
     * Configures text color and font to match the application's design.
     * @param label The JLabel to style.
     */
    private void styleLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
    }

    /**
     * Styles JTextFields used in this panel for entering username and password.
     * Sets preferred size and font.
     * @param text The JTextField to style.
     */
    private void styleText(JTextField text){
        text.setPreferredSize(new Dimension(100, 35));
        text.setFont(new Font("JetBrains Mono", Font.PLAIN, 22));
    }

    /**
     * Styles JButtons used in this panel for login and registration actions.
     * Configures size and font to enhance user interaction.
     * @param button The JButton to style.
     */
    private void styleButton(JButton button){
        button.setPreferredSize(new Dimension(160, 50));
        button.setFont(new Font("JetBrains Mono", Font.PLAIN, 25));
    }

    /**
     * Clears the text fields for username and password.
     * This method is typically used to reset the form after a login attempt or when switching user accounts.
     */
    public void clearTextFields() {
        this.userNameField.setText("");
        this.passWordField.setText("");
    }

    /**
     * Sets the style for the entire panel.
     * Configures the background color.
     * @param panel The JPanel to style.
     */
    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }
}