import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /* BASICS
        JFrame frame = new JFrame();
        frame.setTitle("title");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.getContentPane().setBackground(new Color(0x525252));
        // OR: new MyFrame();
        */

        /* LABEL
        ImageIcon image = new ImageIcon("meowboom.gif");

        JLabel label = new JLabel(); // text can be passed as an argument
        label.setText("Some JLabel text");
        label.setIcon(image);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setForeground(Color.blue);
        label.setIconTextGap(0);
        label.setBackground(new Color(0x525252));
        label.setOpaque(true);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(50, 50, 500, 500);

        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        frame.add(label);
        */

        /* PANEL
        //ImageIcon icon = new ImageIcon("meowboom.gif");
        JLabel label = new JLabel();
        label.setText("some text");
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setHorizontalAlignment(JLabel.RIGHT);
        //label.setIcon(icon);

        JPanel redPanel = new JPanel();
        redPanel.setBackground(Color.red);
        redPanel.setBounds(0, 0, 200, 200);
        redPanel.setLayout(new BorderLayout());

        JPanel bluePanel = new JPanel();
        bluePanel.setBackground(Color.blue);
        bluePanel.setBounds(200, 0, 200, 200);

        JPanel greenPanel = new JPanel();
        greenPanel.setBackground(Color.green);
        greenPanel.setBounds(0, 200, 400, 200);
        greenPanel.setLayout(new BorderLayout());

        JFrame frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);

        redPanel.add(label);
        frame.add(redPanel);
        frame.add(bluePanel);
        frame.add(greenPanel);
        */

        /* BUTTON
        new MyFrame2();
        */

        
    }
}
