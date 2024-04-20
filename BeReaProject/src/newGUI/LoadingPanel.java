package newGUI;

import javax.swing.*;
import java.awt.*;


public class LoadingPanel extends JPanel {
    private JLabel loadingLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation

    public LoadingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(null);
        //Image loadingIcon = new ImageIcon(getClass().getResource("/loading.gif")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/loading.gif"));

        //loadingLabel = new JLabel("Loading...", loadingIcon, JLabel.CENTER);
        loadingLabel = new JLabel(loadingIcon);
        loadingLabel.setBounds(95, 260, 300, 300);
        add(loadingLabel);
        setBackground(Color.BLACK);
    }
}