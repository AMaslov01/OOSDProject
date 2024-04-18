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
    private JButton postButton;
    private JLabel postLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public PostPanel(MainFrame mainFrame, String userName) {
        this.mainFrame = mainFrame;
        this.userName = userName;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        //setBackground(Color.black);
        //setSize(500, 888);

        // Label "Welcome!"
        postLabel = new JLabel("Welcome, " + (userName.isEmpty() ? "User" : userName) + "!");
        //postLabel.setBounds(161, 295, 195, 60);
        //postLabel.setForeground(Color.white);
        //postLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        //postLabel.setHorizontalTextPosition(JLabel.CENTER);
        add(postLabel);

        // Post Button
        postButton = new JButton("Post");
        postButton.setBounds(160, 360, 190, 75);
        postButton.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        postButton.setFocusable(false);
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                postImage();
            }
        });
        add(postButton);
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
                SessionManager.setCurrentFile(selectedFile);
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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}