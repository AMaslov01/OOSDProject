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
    private JLabel postLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public PostPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        // Set the overall panel layout
        setLayout(new BorderLayout());

        // Panel for the buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.black);

        // Logout Button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        styleButtonLogOut(logoutButton);
        topPanel.add(logoutButton, BorderLayout.EAST);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });
        styleButtonLogOut(backButton);
        topPanel.add(backButton, BorderLayout.WEST);


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
        JButton postButton = new JButton("Post");
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

    private void postImage(){
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "webp");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());

                // Show loading panel
                mainFrame.showLoadingPanel();

                // Create a SwingWorker to handle the iage upload asynchronously
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        try {
                            Query query = new Query();
                            query.blobExecute(fileContent);

                            long imageID = query.fetchUserImageId();

                            query.executeBeReal(imageID, SessionManager.getInstance().getCurrentUserId());
                            long beRealId = query.fetchUserBeRealId(SessionManager.getInstance().getCurrentUserId());
                            SessionManager.getInstance().setCurrentBeRealId(beRealId);

                            Image image = query.fetchUserImage(SessionManager.getInstance().getCurrentUserId());

                            SessionManager.getInstance().setCurrentImage(image);
                            System.out.println("Successful upload");
                            System.out.println("Updated feed panel");

                            mainFrame.updateUserSession(SessionManager.getInstance().getCurrentUserName());
                            System.out.println("Updated user session");
                            return true; // Successful upload
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }

                    @Override
                    protected void done() {
                        try {
                            if (get()) {
                                mainFrame.showFeedPanel(); // This displays the FeedPanel with the new file
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void logout() {
        // Clear session data
        SessionManager.getInstance().setCurrentUserName(null);
        SessionManager.getInstance().setCurrentUserId(-1);
        SessionManager.getInstance().setFriends(null);
        SessionManager.getInstance().setCurrentImage(null);

        // Navigate back to the Login panel
        mainFrame.showLoginPanel();
    }

    private void back() {
        // Navigate back to the Feed panel
        mainFrame.showFeedPanel();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}