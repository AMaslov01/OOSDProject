package myGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import CRUD.*;

public class Post extends JFrame implements ActionListener{

    JFrame frame = new JFrame();
    JButton postButton = new JButton();
    JLabel postLabel = new JLabel();
    final int FRAME_WIDTH = 500;
    final int FRAME_HEIGHT = 888;
    final float FRAME_WIDTH_WITH_GAP = 333;
    final float FRAME_HEIGHT_WITH_GAP = 592;
    public Post(){
        frame.setTitle("Post");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        postLabel.setText("Welcome!");
        postLabel.setBounds(161, 295, 195, 60);
        postLabel.setHorizontalTextPosition(JLabel.CENTER);
        postLabel.setForeground(Color.white);
        postLabel.setFont(new Font("JetBrains Mono", Font.BOLD,40));

        postButton.setBounds(160, 360, 190, 75);
        postButton.setText("Post");
        postButton.setFont(new Font("JetBrains Mono", Font.BOLD,40));
        postButton.setFocusable(false);
        postButton.addActionListener(this);

        frame.add(postLabel);
        frame.add(postButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            frame.dispose();
            JFrame feed = new Feed(selectedFile);
        }
    }
    public void resetToDefaults(JFrame frame) {
        // Set default attributes of the JFrame
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        // You can add other default attributes here if needed
    }
}
