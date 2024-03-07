package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Post extends JFrame implements ActionListener{
    JFrame frame = new JFrame();
    JButton postButton = new JButton();
    JLabel postLabel = new JLabel();
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
        frame.dispose();
        BrowseWindow browseWindow = new BrowseWindow();
    }

}
