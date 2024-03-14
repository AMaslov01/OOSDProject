package myGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Post extends JFrame implements ActionListener{
    JFrame frame = new JFrame();
    JButton postButton = new JButton();
    JLabel postLabel = new JLabel();
    final float FRAME_WIDTH = 300;
    final float FRAME_HEIGHT = 533;
    final float FRAME_WIDTH_WITH_GAP = 200;
    final float FRAME_HEIGHT_WITH_GAP = 351.8f;
    public Post(){
        frame.setTitle("Post a Picture");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(300, 533);
        frame.setVisible(true);


        postLabel.setText("Welcome!");
        postLabel.setBounds(105, 180, 100, 20);
        postLabel.setHorizontalTextPosition(JLabel.CENTER);
        postLabel.setForeground(Color.white);
        postLabel.setFont(new Font("JetBrains Mono", Font.BOLD,20));

        postButton.setBounds(100, 220, 100, 50);
        postButton.setText("Post");
        postButton.setFont(new Font("JetBrains Mono", Font.BOLD,20));
        postButton.setFocusable(false);
        postButton.addActionListener(this);



        frame.add(postLabel);
        frame.add(postButton);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            frame.setTitle("Browse Pictures");
            resetToDefaults(frame);

            JLabel label = new JLabel();

            File selectedFile = fileChooser.getSelectedFile();
            Image image = new ImageIcon(selectedFile.getAbsolutePath()).getImage();
            float width = image.getWidth(null);
            float height = image.getHeight(null);
            System.out.println(width);
            System.out.println(height);
            float height_divisor = (height/FRAME_HEIGHT_WITH_GAP);
            float width_divisor = (width/FRAME_WIDTH_WITH_GAP);
            if(width > FRAME_WIDTH_WITH_GAP){
                height = height/width_divisor;
                width = width/width_divisor;

            }
            if(height > FRAME_HEIGHT_WITH_GAP){
                width = width/height_divisor;
               height = height/height_divisor;
            }
            int intWidth =  Math.round(width);
            int intHeight = Math.round(height);
            int x_left_top_corner = (int)(FRAME_WIDTH/2) - (intWidth/2);
            int y_left_top_corner = (int)(FRAME_HEIGHT/2) - (intHeight/2);
            label.setBounds(x_left_top_corner, y_left_top_corner, intWidth, intHeight);

            Image scaledImage = image.getScaledInstance(intWidth, intHeight, Image.SCALE_SMOOTH);
            System.out.println(width);
            System.out.println(height);
            label.setIcon(new ImageIcon(scaledImage));
            frame.add(label);
        }
    }
    public void resetToDefaults(JFrame frame) {
        // Set default attributes of the JFrame
        frame.setSize(300, 533);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        // You can add other default attributes here if needed
    }
}
