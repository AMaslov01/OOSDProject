package newGUI;

import CRUD.Query;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PostPanel extends JPanel {

    private String userName;
    private JButton postButton, logoutButton;
    private JLabel postLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public PostPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        // Set the overall panel layout
        setLayout(new BorderLayout());

        // Panel for the logout button in the top right corner
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.black);
        logoutButton = new JButton("Log Out");
        logoutButton.setFocusable(true);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        styleButtonLogOut(logoutButton);
        topPanel.add(logoutButton, BorderLayout.EAST);
        topPanel.setOpaque(true);
        add(topPanel, BorderLayout.NORTH);

        // Center panel for other UI components
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        stylePanel(centerPanel);

        // Welcome label
        postLabel = new JLabel();
        Dimension preferredSize = postLabel.getPreferredSize();
        postLabel.setSize(preferredSize);
        styleLabel(postLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(postLabel, gbc);

        // Post Button
        postButton = new JButton("Post");
        styleButton(postButton);
        gbc.gridy = 1;
        postButton.setFocusable(false);
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postImage();
            }
        });
        centerPanel.add(postButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateUserName(){
        String userName = SessionManager.getInstance().getCurrentUserName();
        postLabel.setText("Welcome, " + (userName.isEmpty() ? "User" : userName) + "!");
    }

    private void styleLabel(JLabel label){
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
    }

    private void styleButton(JButton button){
        button.setPreferredSize(new Dimension(190, 75));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
    }

    private void styleButtonLogOut(JButton button){
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(120, 45));
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void stylePanel(JPanel panel){
        panel.setBackground(Color.black);
    }

    private void postImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", ".webp");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                Query query = new Query();
                String sql = "INSERT INTO `Image`(`image`) VALUES (?)";
                query.blobExecute(sql, fileContent);
                SessionManager.getInstance().setCurrentFile(selectedFile);
                //mainFrame.postSuccessful(selectedFile);
                mainFrame.showFeedPanel(selectedFile); // This updates and displays the FeedPanel with the new file
            } catch (IOException error) {
                JOptionPane.showMessageDialog(this, "Failed to upload image.", "Error", JOptionPane.ERROR_MESSAGE);
                error.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void logout() {
        // Clear session data
        SessionManager.getInstance().setCurrentUserName(null);
        SessionManager.getInstance().setCurrentUserId(-1);
        SessionManager.getInstance().setFriends(null);
        SessionManager.getInstance().setCurrentFile(null);

        // Navigate back to the Login panel
        mainFrame.showLoginPanel();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}