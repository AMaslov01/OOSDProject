package newGUI;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

/**
 * The entry point of the application that sets up the look and feel of the UI and initializes the main application window.
 */
public class Main {

    /**
     * Main method to launch the application.
     * Sets the UI theme using FlatLightLaf and creates the main application window (MainFrame).
     * @param args The command line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        }catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        MainFrame mainFrame = new MainFrame();
    }
}
