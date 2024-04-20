package newGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;

public class FeedPanel extends JPanel {
    private JPanel imagePanel;
    private JScrollPane scrollPane;
    private JButton logoutButton;
    private MainFrame mainFrame;
    private Query query = new Query();
    final float FRAME_WIDTH_WITH_GAP = 333;
    final float FRAME_HEIGHT_WITH_GAP = 592;

    public FeedPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Panel for the logout button in the top right corner
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.black);
        logoutButton = new JButton("Log Out");
        styleButtonLogOut(logoutButton);
        logoutButton.setFocusable(true);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);
        topPanel.setOpaque(true);
        add(topPanel, BorderLayout.NORTH);

        imagePanel = new JPanel();
        stylePanel(imagePanel);
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Setting up BoxLayout for vertical stacking
        scrollPane = new JScrollPane(imagePanel);
        styleScroll(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
/*        Timer timer = new Timer(100000, e -> {
            updateContent();
            System.out.println("Panel repainted!");
        });
        timer.start();*/
    }

    public void updateContent() {
        System.out.println("Started updateContent");
        imagePanel.removeAll();
        File file = SessionManager.getInstance().getCurrentFile();
        if (file != null) {
            displayImage(file, "My Picture");
        }

        long[] friends = SessionManager.getInstance().getFriends();
        //System.out.println(Arrays.toString(friends));
        if (friends != null) {
            for (long friendId : friends) {
                displayFriendImage(friendId);
            }
        }

        imagePanel.revalidate();
        imagePanel.repaint();
        System.out.println("Finished updateContent");
    }

    private void displayImage(File file, String label) {
        System.out.println("Started displayImage");
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
            //Border border = BorderFactory.createLineBorder(Color.WHITE, 0); // 2-pixel width white line border
            //panel.setBorder(border);
            panel.add(picLabel, BorderLayout.CENTER);

            // Setting Comment Panel
            JPanel commentPanel = new JPanel(new BorderLayout());
            stylePanel(commentPanel);

            // Setting West Comment Panel
            JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            stylePanel(westPanel);
            JButton comment = new JButton("Comment");
            styleButton(comment);
            westPanel.add(comment);
            commentPanel.add(westPanel, BorderLayout.WEST);

            // Setting East Comment Panel
            JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            stylePanel(eastPanel);
            JTextField commentFields = new JTextField();
            styleTextField(commentFields);
            eastPanel.add(commentFields);
            commentPanel.add(eastPanel, BorderLayout.CENTER);

            // Comment Button Action Listener
            comment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String commentText = commentFields.getText();
                    if(!commentText.isEmpty()) {
                        long userID = SessionManager.getInstance().getCurrentUserId();
                        long beRealId = SessionManager.getInstance().getCurrentBeRealId();
                        System.out.println("text: " + commentText);
                        String sql = "INSERT INTO `Comment`( `text`, `userID`, `berealID`) VALUES ('" + commentText + "','" + userID + "', '" + beRealId + "')";
                        commentFields.setText("");
                        try {
                            query.execute(sql);
                        } catch (SQLException er) {
                            er.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(mainFrame, "Empty comments not allowed!", "Comment Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            panel.add(commentPanel, BorderLayout.SOUTH);

            stylePanel(panel);
            imagePanel.add(panel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error displaying image: " + label, "Image Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Finished displayImage");
    }

    private void displayFriendImage(long friendId) {
        System.out.println("Fetching friend image");
        Image image = query.fetchFriendImage(friendId);
        System.out.println("Fetched friend image");
        if (image != null) {
            // Setting panel for label
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            // Setting textlabel
            JLabel textLabel = new JLabel();
            System.out.println("Fetching friend name");
            textLabel.setText(String.valueOf(query.fetchUserNameFromDatabase(friendId)));
            System.out.println("Fetched friend name");
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
            //Border border = BorderFactory.createLineBorder(Color.WHITE, 0); // 2-pixel width white line border
            //panel.setBorder(border);
            stylePanel(panel);

            // Setting Comment Panel
            JPanel commentPanel = new JPanel(new BorderLayout());
            stylePanel(commentPanel);

            // Setting West Comment Panel
            JPanel westPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            stylePanel(westPanel);
            JButton comment = new JButton("Comment");
            styleButton(comment);
            westPanel.add(comment);
            commentPanel.add(westPanel, BorderLayout.WEST);

            // Setting East Comment Panel
            JPanel eastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            stylePanel(eastPanel);
            JTextField commentFields = new JTextField();
            styleTextField(commentFields);
            eastPanel.add(commentFields);
            commentPanel.add(eastPanel, BorderLayout.CENTER);

            // Comment Button Action Listener
            comment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String commentText = commentFields.getText();
                    if(!commentText.isEmpty()) {
                        long userID = SessionManager.getInstance().getCurrentUserId();
                        String sql = "SELECT MAX(`berealID`) FROM `BeReal` WHERE `userID` = " + friendId;
                        Object[][] beRealId = new Object[1][1];
                        try {
                            beRealId = query.retrieve(sql);
                            System.out.println("text: " + commentText);
                            sql = "INSERT INTO `Comment`( `text`, `userID`, `berealID`) VALUES ('" + commentText + "','" + userID + "', '" + beRealId[0][0] + "')";
                            commentFields.setText("");
                            query.execute(sql);
                        } catch (SQLException er) {
                            er.printStackTrace();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(mainFrame, "Empty comments not allowed!", "Comment Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            panel.add(commentPanel, BorderLayout.SOUTH);

            imagePanel.add(panel);
        } else {
            System.out.println("No image found for friend ID: " + friendId);
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

    private void styleButton(JButton button){
        button.setSize(new Dimension(100, 55));
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void styleTextField(JTextField text){
        text.setPreferredSize(new Dimension(320, 32));
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.WHITE, 1); // 2-pixel width white line border
        text.setBorder(border);
        text.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void styleButtonLogOut(JButton button){
        button.setPreferredSize(new Dimension(120, 45));
        button.setBackground(Color.black);
        button.setForeground(Color.white);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }
}