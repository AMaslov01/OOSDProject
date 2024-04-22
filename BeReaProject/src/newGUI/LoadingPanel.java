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
        setLayout(new BorderLayout());
        //Image loadingIcon = new ImageIcon(getClass().getResource("/loading.gif")).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/loading3.gif"));

        //loadingLabel = new JLabel("Loading...", loadingIcon, JLabel.CENTER);
        loadingLabel = new JLabel(loadingIcon);
        //loadingLabel.setBounds(200, 460, 100, 100);
        setBackground(new Color(0, 0, 0, 255));
        add(loadingLabel, BorderLayout.CENTER);

        loadingLabel = new JLabel("Loading");
        loadingLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        loadingLabel.setForeground(Color.white);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(loadingLabel, BorderLayout.NORTH);
    }
}