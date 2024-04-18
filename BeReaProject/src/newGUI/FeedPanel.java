package newGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class FeedPanel extends JPanel {
    private JPanel imagePanel;
    private JScrollPane scrollPane;
    private Query query = new Query();

    public FeedPanel(MainFrame mainFrame) {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Setting up BoxLayout for vertical stacking
        scrollPane = new JScrollPane(imagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateContent() {
        imagePanel.removeAll();
        File file = SessionManager.getInstance().getCurrentFile();
        if (file != null) {
            displayImage(file, "My Picture");
        }

        long[] friends = SessionManager.getInstance().getFriends();
        System.out.println(Arrays.toString(friends));
        if (friends != null) {
            for (long friendId : friends) {
                displayFriendImage(friendId);
            }
        }
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private void displayImage(File file, String label) {
        try {
            ImageIcon icon = new ImageIcon(file.getAbsolutePath());
            Image image = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            JLabel picLabel = new JLabel(new ImageIcon(image), SwingConstants.CENTER);
            picLabel.setText(label);
            styleLabel(picLabel);
            picLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Ensure alignment in BoxLayout
            //addMargin(picLabel);
            imagePanel.add(picLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying image: " + label, "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFriendImage(long friendId) {
        Image image = query.fetchFriendImage(friendId);
        if (image != null) {
            ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon, SwingConstants.CENTER);
            imageLabel.setText(String.valueOf(friendId));
            styleLabel(imageLabel);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Ensure alignment in BoxLayout
            //addMargin(imageLabel);
            imagePanel.add(imageLabel);
        } else {
            System.out.println("No image found for friend ID: " + friendId);
        }
    }

    private void styleLabel(JLabel label) {
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        //label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
    }
}