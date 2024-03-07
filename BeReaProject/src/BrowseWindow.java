package GUI;

import javax.swing.*;
import java.awt.*;
public class BrowseWindow extends JFrame{
    JFrame frame = new JFrame();
    public BrowseWindow(){
        JFrame frame = new JFrame();
        frame.setTitle("Browse Pictures");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.black);
        frame.setSize(300, 533);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
