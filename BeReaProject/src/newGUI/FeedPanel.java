package newGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedPanel extends JPanel {
    private JPanel imagePanel;
    private final MainFrame mainFrame;
    private final Query query = new Query();
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

        // Setting Logout Button
        JButton logoutButton = new JButton("Log Out");
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

        //Setting Update Button
        JButton updateButton = new JButton("Update");
        styleButtonLogOut(updateButton); // Repurpose styleButtonLogOut for updateButton
        updateButton.setFocusable(true);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoadingPanel();
                // Create a SwingWorker to handle login asynchronously
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        // Perform the authentication check asynchronously
                        updateContent();
                        return true;
                    }

                    @Override
                    protected void done() {
                        SwingUtilities.invokeLater(mainFrame::showFeedPanel);
                    }
                };
                worker.execute(); // Start the worker thread

            }
        });
        topPanel.add(updateButton, BorderLayout.CENTER);

        // Friends button
        JButton friendsButton = new JButton("Friends");
        friendsButton.setFocusable(false);
        friendsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoadingPanel(); // Show loading panel while processing

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        mainFrame.getFriendsPanel().updateFriendsList(); // Update the friends list in the background
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Once updating is complete, switch back to the friends panel
                        // Show the friends panel
                        SwingUtilities.invokeLater(mainFrame::showFriendsPanel);
                    }
                };
                worker.execute(); // Start the worker thread
            }
        });
        styleButtonLogOut(friendsButton);
        topPanel.add(friendsButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Setting Post Button
        JButton postButton = new JButton("Post");
        postButton.setFocusable(false);
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showLoadingPanel(); // Show loading panel while processing

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        mainFrame.showPostPanel(); // Update the friends list in the background
                        return null;
                    }
                };
                worker.execute(); // Start the worker thread
            }
        });
        styleButtonLogOut(postButton);
        add(topPanel, BorderLayout.NORTH);
        add(postButton, BorderLayout.SOUTH);

        imagePanel = new JPanel();
        stylePanel(imagePanel);
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS)); // Setting up BoxLayout for vertical stacking
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        styleScroll(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateContent() {
        System.out.println("Started updateContent");
        imagePanel.removeAll();
        Image image = SessionManager.getInstance().getCurrentImage();
        if (image != null) {
            displayImage("My Picture");
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

    private void displayImage(String label) {
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
            Image image = SessionManager.getInstance().getCurrentImage();
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
            panel.add(picLabel, BorderLayout.CENTER);

            // Setting Comment Panel
            JPanel commentPanel = new JPanel(new BorderLayout());
            stylePanel(commentPanel);

            // Setting Array for comments
            long beRealId = SessionManager.getInstance().getCurrentBeRealId();
            String[][] comments = query.fetchUserComments(beRealId);

            // Setting Comment Button for West Comment Panel
            JPanel westPanel = new JPanel();
            westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
            styleWestPanel(westPanel);
            JButton comment = new JButton("Comment");
            styleButton(comment);
            westPanel.add(comment);

            // Setting UserName Labels for West Comment Panel
            for (String[] strings : comments) {
                JLabel userName = new JLabel((String) strings[0]);
                styleCommentLabel(userName);
                westPanel.add(userName);
                //System.out.println(i);
            }

            commentPanel.add(westPanel, BorderLayout.WEST);

            // Setting Input Field for East Comment Panel
            JPanel eastPanel = new JPanel();
            eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
            styleWestPanel(eastPanel);
            JTextField commentFields = new JTextField();
            styleTextField(commentFields);
            eastPanel.add(commentFields);

            // Setting Comments Labels for East Comment Panel
            for (String[] strings : comments) {
                JLabel commentLabel = new JLabel((String) strings[1]);
                styleCommentLabel(commentLabel);
                eastPanel.add(commentLabel);
            }

            commentPanel.add(eastPanel, BorderLayout.CENTER);

            // Comment Button Action Listener
            comment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String commentText = commentFields.getText();
                    if(!commentText.isEmpty() && commentText.length() <= 32) {
                        mainFrame.showLoadingPanel();
                        long userID = SessionManager.getInstance().getCurrentUserId();
                        long beRealId = SessionManager.getInstance().getCurrentBeRealId();
                        //System.out.println("text: " + commentText);

                        query.executeComment(commentText, userID, beRealId);
                        System.out.println("Comment text: " + commentText);
                        System.out.println("UserID: " + userID);
                        System.out.println("BeRealID: " + beRealId);
                        commentFields.setText("");
                        // Create a SwingWorker to handle login asynchronously
                        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                            @Override
                            protected Boolean doInBackground() {
                                // Perform the authentication check asynchronously
                                updateContent();
                                return true;
                            }

                            @Override
                            protected void done() {
                                SwingUtilities.invokeLater(mainFrame::showFeedPanel);
                                System.out.println("Comment added");
                            }
                        };
                        worker.execute(); // Start the worker thread
                    }
                    else if(commentText.length() > 32){
                        JOptionPane.showMessageDialog(mainFrame, "Your comment is too long! 32 characters max!", "Comment Error", JOptionPane.ERROR_MESSAGE);
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
        try {
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
                float heightDivisor = (height / FRAME_HEIGHT_WITH_GAP);
                float widthDivisor = (width / FRAME_WIDTH_WITH_GAP);

                if (width > FRAME_WIDTH_WITH_GAP) {
                    height = height / widthDivisor;
                    width = width / widthDivisor;
                }

                if (height > FRAME_HEIGHT_WITH_GAP) {
                    width = width / heightDivisor;
                    height = height / heightDivisor;
                }

                int rdWidth = Math.round(width);
                int rdHeight = Math.round(height);

                Image scaledImage = image.getScaledInstance(rdWidth, rdHeight, Image.SCALE_SMOOTH);
                picLabel.setHorizontalTextPosition(JLabel.CENTER);
                picLabel.setVerticalTextPosition(JLabel.TOP);
                styleLabel(picLabel);
                picLabel.setIcon(new ImageIcon(scaledImage));
                panel.add(picLabel, BorderLayout.CENTER);
                stylePanel(panel);

                // Setting Comment Panel
                JPanel commentPanel = new JPanel(new BorderLayout());
                stylePanel(commentPanel);

                // Setting Array for comments
                long beRealId = query.fetchUserBeRealId(friendId);
                String[][] comments = query.fetchUserComments(beRealId);

                // Setting Comment Button for West Comment Panel
                JPanel westPanel = new JPanel();
                westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
                styleWestPanel(westPanel);
                JButton comment = new JButton("Comment");
                styleButton(comment);
                westPanel.add(comment);

                // Setting UserName Labels for West Comment Panel
                for (String[] strings : comments) {
                    JLabel userName = new JLabel(strings[0]);
                    styleCommentLabel(userName);
                    westPanel.add(userName);
                    //System.out.println(i);
                }

                commentPanel.add(westPanel, BorderLayout.WEST);

                // Setting Input Field for East Comment Panel
                JPanel eastPanel = new JPanel();
                eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
                styleWestPanel(eastPanel);
                JTextField commentFields = new JTextField();
                styleTextField(commentFields);
                eastPanel.add(commentFields);

                // Setting Comments Labels for East Comment Panel
                for (String[] strings : comments) {
                    JLabel commentLabel = new JLabel(strings[1]);
                    styleCommentLabel(commentLabel);
                    eastPanel.add(commentLabel);
                }

                commentPanel.add(eastPanel, BorderLayout.CENTER);

                // Comment Button Action Listener
                comment.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String commentText = commentFields.getText();
                        if (!commentText.isEmpty() && commentText.length() <= 32) {
                            mainFrame.showLoadingPanel();
                            long userID = SessionManager.getInstance().getCurrentUserId();
                            //System.out.println("text: " + commentText);

                            query.executeComment(commentText, userID, beRealId);
                            commentFields.setText("");

                            // Create a SwingWorker to handle login asynchronously
                            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                                @Override
                                protected Boolean doInBackground() {
                                    // Perform the authentication check asynchronously
                                    updateContent();
                                    return true;
                                }

                                @Override
                                protected void done() {
                                    SwingUtilities.invokeLater(mainFrame::showFeedPanel);
                                }
                            };
                            worker.execute(); // Start the worker thread
                        } else if (commentText.length() > 32) {
                            JOptionPane.showMessageDialog(mainFrame, "Your comment is too long! 32 characters max!", "Comment Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(mainFrame, "Empty comments not allowed!", "Comment Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                panel.add(commentPanel, BorderLayout.SOUTH);

                imagePanel.add(panel);
            } else {
                System.out.println("No image found for friend ID: " + friendId);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(this, "Error displaying image for friend: " + friendId, "Image Error", JOptionPane.ERROR_MESSAGE);
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

    private void stylePanel(JPanel panel) {
        panel.setBackground(Color.black);
    }

    private void styleWestPanel(JPanel panel) {
        panel.setBackground(Color.black);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 5); // 2-pixel width white line border
        panel.setBorder(border);

    }

    private void styleScroll(JScrollPane scroll) {
        scroll.setBackground(Color.black);
        scroll.setViewportBorder(null);
        scroll.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    private void styleLabel(JLabel label) {
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
    }

    private void styleCommentLabel(JLabel label) {
        label.setForeground(Color.white);
        label.setFont(new Font("JetBrains Mono", Font.BOLD, 15));
    }

    private void styleButton(JButton button){
        button.setBounds(10, 10, 100, 55);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
    }

    private void styleTextField(JTextField text){
        text.setSize(new Dimension(320, 32));
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