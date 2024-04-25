package newGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Class representing a panel used to display a loading screen.
 * This panel is shown during operations that require waiting, such as loading data or processing actions.
 */
public class LoadingPanel extends JPanel {
    private JLabel loadingLabel;
    private MainFrame mainFrame; // Reference to the main application window for navigation


    /**
     * Constructor for LoadingPanel.
     * Initializes the user interface components to display loading information.
     * @param mainFrame The main application window that contains this panel.
     */
    public LoadingPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    /**
     * Initializes the user interface components of the panel.
     * Sets up the layout and styles of the loading animation and label.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());
        ImageIcon loadingIcon = new ImageIcon(getClass().getResource("/loading3.gif"));

        loadingLabel = new JLabel(loadingIcon);
        setBackground(new Color(0, 0, 0, 255));
        add(loadingLabel, BorderLayout.CENTER);

        loadingLabel = new JLabel("Loading");
        loadingLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 40));
        loadingLabel.setForeground(Color.white);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(loadingLabel, BorderLayout.NORTH);
    }
}