package newGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

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
        logoutButton.setFocusable(false);
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "webp");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Show loading panel
            mainFrame.showLoadingPanel();

            // Create a SwingWorker to handle the iage upload asynchronously
            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                        Query query = new Query();
                        String sql = "INSERT INTO `Image`(`image`) VALUES (?)";
                        query.blobExecute(sql, fileContent);

                        sql = "SELECT MAX(`imageID`) FROM `Image`";
                        Object[][] imageID = new Object[1][1];
                        imageID = query.retrieve(sql);

                        sql = "INSERT INTO `BeReal`(`imageID`, `userID`) VALUES ("+ imageID[0][0] +"," + SessionManager.getInstance().getCurrentUserId() + ")";
                        query.execute(sql);

                        sql = "SELECT MAX(`berealID`) FROM `BeReal`";
                        Object[][] beRealId = new Object[1][1];
                        beRealId = query.retrieve(sql);
                        SessionManager.getInstance().setCurrentBeRealId((long) beRealId[0][0]);

                        SessionManager.getInstance().setCurrentFile(selectedFile);
                        System.out.println("Successful upload");
                        //mainFrame.getFeedPanel().updateContent(); // Direct call to update content
                        System.out.println("Updated feed panel");

                        mainFrame.updateUserSession(SessionManager.getInstance().getCurrentUserName());
                        System.out.println("Updated user session");
                        return true; // Successful upload
                    } catch (IOException error) {
                        JOptionPane.showMessageDialog(null, "Failed to upload image.", "Error", JOptionPane.ERROR_MESSAGE);
                        error.printStackTrace();
                        return false; // Upload failed
                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (get()) {
                            mainFrame.showFeedPanel(selectedFile); // This displays the FeedPanel with the new file
                        } else {
                            mainFrame.showPostPanel(); // Return to the post panel on failure
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        mainFrame.showPostPanel(); // Return to the post panel on error
                    }
                }
            };
            worker.execute(); // Start the worker thread
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