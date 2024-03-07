import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.*;

public class Post {
    public static void main(String[] args){
        JFrame frame= new JFrame();
        frame.setTitle("Posting BeReal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(500, 500);
        frame.setVisible(true);


        frame.getContentPane().setBackground(Color.BLACK);
    }
}
