package newGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class FeedPanel extends JPanel {
    private JPanel imagePanel;
    private JScrollPane scrollPane;
    private Query query = new Query();
    final float FRAME_WIDTH_WITH_GAP = 333;
    final float FRAME_HEIGHT_WITH_GAP = 592;

    public FeedPanel(MainFrame mainFrame) {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        imagePanel = new JPanel();
        stylePanel(imagePanel);
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Setting up BoxLayout for vertical stacking
        scrollPane = new JScrollPane(imagePanel);
        styleScroll(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        Timer timer = new Timer(10000, e -> {
            updateContent();
            System.out.println("Panel repainted!");
        });
        timer.start();
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
            // Setting panel for label
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Setting textlabel
            JLabel textLabel = new JLabel();
            textLabel.setText(label);
            styleLabel(textLabel);
            panel.add(textLabel, BorderLayout.NORTH);

            // Setting picLabel
            JLabel picLabel = new JLabel();
            Image image = new ImageIcon(file.getAbsolutePath()).getImage();
            float width = image.getWidth(null);
            float height = image.getHeight(null);
            float heightDivisor = (height/FRAME_HEIGHT_WITH_GAP);
            float widthDivisor = (width/FRAME_WIDTH_WITH_GAP);

            if(width > FRAME_WIDTH_WITH_GAP){
                height = height/widthDivisor;
                width = width/widthDivisor;
            }

            if(height > FRAME_HEIGHT_WITH_GAP){
                width = width/heightDivisor;
                height = height/heightDivisor;
            }

            int rdWidth =  Math.round(width);
            int rdHeight = Math.round(height);

            Image scaledImage = image.getScaledInstance(rdWidth, rdHeight, Image.SCALE_SMOOTH);
            picLabel.setIcon(new ImageIcon(scaledImage));
            styleLabel(picLabel);
            Border border = BorderFactory.createLineBorder(Color.WHITE, 1); // 2-pixel width white line border
            panel.setBorder(border);
            panel.add(picLabel, BorderLayout.CENTER);
            //addMargin(picLabel);
            stylePanel(panel);
            imagePanel.add(panel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying image: " + label, "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayFriendImage(long friendId) {
        Image image = query.fetchFriendImage(friendId);
        if (image != null) {
            // Setting panel for label
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Setting textlabel
            JLabel textLabel = new JLabel();
            textLabel.setText(String.valueOf(query.fetchUserNameFromDatabase(friendId)));
            styleLabel(textLabel);
            panel.add(textLabel, BorderLayout.NORTH);

            // Setting picLabel
            JLabel picLabel = new JLabel();

            float width = image.getWidth(null);
            float height = image.getHeight(null);
            float heightDivisor = (height/FRAME_HEIGHT_WITH_GAP);
            float widthDivisor = (width/FRAME_WIDTH_WITH_GAP);

            if(width > FRAME_WIDTH_WITH_GAP){
                height = height/widthDivisor;
                width = width/widthDivisor;
            }

            if(height > FRAME_HEIGHT_WITH_GAP){
                width = width/heightDivisor;
                height = height/heightDivisor;
            }

            int rdWidth =  Math.round(width);
            int rdHeight = Math.round(height);

            Image scaledImage = image.getScaledInstance(rdWidth, rdHeight, Image.SCALE_SMOOTH);
            picLabel.setHorizontalTextPosition(JLabel.CENTER);
            picLabel.setVerticalTextPosition(JLabel.TOP);
            styleLabel(picLabel);
            picLabel.setIcon(new ImageIcon(scaledImage));
            panel.add(picLabel, BorderLayout.CENTER);

            // Adding panel to imagePanel
            Border border = BorderFactory.createLineBorder(Color.WHITE, 1); // 2-pixel width white line border
            panel.setBorder(border);
            stylePanel(panel);
            imagePanel.add(panel);
        } else {
            System.out.println("No image found for friend ID: " + friendId);
        }
    }

    private void stylePanel(JPanel panel) {
        panel.setBackground(Color.black);
    }

    private void styleScroll(JScrollPane scroll) {
        scroll.setBackground(Color.black);
    }

    private void styleLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
    }
}