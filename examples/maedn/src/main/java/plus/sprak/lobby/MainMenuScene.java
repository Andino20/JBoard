package plus.sprak.lobby;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Scene;
import plus.jboard.net.session.HostConfig;
import plus.jboard.net.session.HostSession;
import plus.jboard.net.session.PlayerConfig;
import plus.jboard.net.session.PlayerSession;
import plus.sprak.app.MaednScene;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;


public class MainMenuScene extends Scene {

    private final JPanel root;

    public MainMenuScene() {
        root = new JPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title
        JLabel title = new JLabel("Mensch ärgere dich nicht!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        root.add(title, gbc);

        // Buttons panel (keeps buttons compact)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JButton hostButton = new JButton("Host Session");
        JButton joinButton = new JButton("Join Session");

        hostButton.addActionListener(e -> showHostDialog());
        joinButton.addActionListener(e -> {
            try {
                showJoinDialog();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(hostButton);
        buttonPanel.add(joinButton);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(buttonPanel, gbc);

        setUI(root);
    }

    private void showHostDialog() {
        JTextField portField = new JTextField();

        Object[] message = {
                "Port:", portField
        };

        int result = JOptionPane.showConfirmDialog(
                root,
                message,
                "Host Session",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String portText = portField.getText();

            GameApplication app = GameApplication.getInstance();

            // TODO: Here you can change how many clients can join
            HostSession session = new HostSession(HostConfig.builder()
                    .maxClients(3)
                    .port(Integer.parseInt(portText))
                    .build());
            app.addUpdatable(session);
            session.start();

            HostScene lobby = new HostScene(session);
            app.switchScenes(lobby);
        }
    }

    private void showJoinDialog() throws IOException {
        JTextField ipField = new JTextField("127.0.0.1");
        JTextField portField = new JTextField();

        Object[] message = {
                "Host IP:", ipField,
                "Port:", portField
        };

        int result = JOptionPane.showConfirmDialog(
                root,
                message,
                "Join Session",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String ip = ipField.getText();
            String portText = portField.getText();
            PlayerSession session = new PlayerSession(PlayerConfig.builder()
                    .targetHost(ip)
                    .targetPort(Integer.parseInt(portText))
                    .build());
            PlayerScene lobby = new PlayerScene(session);
            GameApplication.getInstance().switchScenes(lobby);
        }
    }
}
