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
import java.util.Arrays;



public class PostPanel extends JPanel {

    private String userName;
    private JButton postButton;
    private JLabel postLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public PostPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        stylePanel(this);


        // Label "Welcome!"
        postLabel = new JLabel();
        Dimension preferredSize = postLabel.getPreferredSize();
        postLabel.setSize(preferredSize);
        styleLabel(postLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(postLabel, gbc);


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
        add(postButton, gbc);
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

    public void setUserName(String userName) {
        this.userName = userName;
    }
}