import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame2 extends JFrame implements ActionListener {

    JButton button;
    JLabel label;

    public MyFrame2() {
        ImageIcon icon = new ImageIcon("icon.png");
        ImageIcon icon2 = new ImageIcon("meowboom.gif");

        label = new JLabel();
        label.setIcon(icon2);
        label.setBounds(0, 0, 400, 400);
        label.setVisible(false);

        button = new JButton();
        button.setBounds(100, 100, 100, 50);
        button.addActionListener(this);
        button.setText("im a button");
        button.setFocusable(false);
        button.setIcon(icon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setBackground(Color.lightGray);

        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setVisible(true);

        this.add(button);
        this.add(label);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
            System.out.println("0_0");
            button.setEnabled(false);
            label.setVisible(true);
        }
    }
}
