package myGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Feed extends JFrame implements ActionListener {
    File file;
    JFrame frame = new JFrame();
    final int FRAME_WIDTH = 500;
    final int FRAME_HEIGHT = 888;
    final float FRAME_WIDTH_WITH_GAP = 333;
    final float FRAME_HEIGHT_WITH_GAP = 592;

    public Feed(File file){
        this.file = file;
        frame.setTitle("Feed");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        JLabel label = new JLabel();
        Image image = new ImageIcon(file.getAbsolutePath()).getImage();
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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
