package newGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class RegisterPanel extends JPanel {
    private JTextField userNameField;
    private JPasswordField passWordField;
    private JPasswordField confirmField;
    private JButton registerButton;
    private JButton backButton;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        //setPreferredSize(new Dimension(500, 888));
        setLayout(new BorderLayout());

        // Panel for the logout button in the top left corner
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.black);
        backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoginPanel(); // Navigate back to the login panel
            }
        });
        styleButtonBack(backButton);
        topPanel.add(backButton, BorderLayout.EAST);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setOpaque(true);
        add(topPanel, BorderLayout.NORTH);

        // Panel for other UI components using GridBagLayout
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        stylePanel(centerPanel);
        gbc.insets = new Insets(10, 5, 10, 5);

        // Username label and field
        JLabel label1 = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(label1, gbc);
        styleLabel(label1);
        gbc.gridx = 1;
        userNameField = new JTextField(20);
        centerPanel.add(userNameField, gbc);
        styleText(userNameField);

        // Password label and field
        JLabel label2 = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(label2, gbc);
        styleLabel(label2);
        gbc.gridx = 1;
        passWordField = new JPasswordField(20);
        centerPanel.add(passWordField, gbc);
        styleText(passWordField);

        // Confirm Password label and field
        JLabel label3 = new JLabel("Confirm:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(label3, gbc);
        styleLabel(label3);
        gbc.gridx = 1;
        confirmField = new JPasswordField(20);
        centerPanel.add(confirmField, gbc);
        styleText(confirmField);

        // Register Button
        registerButton = new JButton("Register");
        styleButton(registerButton);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });
        centerPanel.add(registerButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void performRegistration() {
        String userName = userNameField.getText();
        String password = new String(passWordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (userName.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Empty Fields Allowed!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!confirm.equals(password)) {
            JOptionPane.showMessageDialog(this, "Passwords Do Not Match!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            mainFrame.showLoadingPanel();

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    Query query = new Query();
                    return query.registerUser(userName, password);
                }

                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        if (success) {
                            mainFrame.showLoginPanel(); // Navigate back to the login panel after successful registration
                        } else {
                            JOptionPane.showMessageDialog(RegisterPanel.this, "This Username Is Taken!", "Error", JOptionPane.ERROR_MESSAGE);
                            mainFrame.showRegisterPanel();
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(RegisterPanel.this, "Registration Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        mainFrame.showRegisterPanel();
                    }
                }
            };
            worker.execute();
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
        button.setFont(new Font("JetBrains Mono", Font.PLAIN, 25));
    }

    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }

    private void styleButtonBack(JButton button){
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }
}