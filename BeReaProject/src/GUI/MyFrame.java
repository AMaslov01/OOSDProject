import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame(){
        this.setTitle("title");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(400, 400);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(0x525252));
    }
}
