package newGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoadingPanel extends JPanel {
    private JLabel loadingLabel;

    public LoadingPanel() {
        setLayout(new BorderLayout());
        Icon loadingIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("loding.gif")));
        loadingLabel = new JLabel("Loading...", loadingIcon, JLabel.CENTER);
        add(loadingLabel, BorderLayout.CENTER);
        setBackground(Color.BLACK);
    }
}