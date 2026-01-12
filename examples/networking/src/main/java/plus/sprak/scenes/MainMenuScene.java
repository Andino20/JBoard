package plus.sprak.scenes;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Scene;
import plus.jboard.net.session.HostConfig;
import plus.jboard.net.session.HostSession;
import plus.jboard.net.session.PlayerConfig;
import plus.jboard.net.session.PlayerSession;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


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
        JLabel title = new JLabel("Networking Demo", SwingConstants.CENTER);
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
        joinButton.addActionListener(e -> showJoinDialog());

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
            HostSession session = new HostSession(HostConfig.builder()
                    .maxClients(4)
                    .port(Integer.parseInt(portText))
                    .build());
            session.onPlayerJoin(id -> System.out.printf("Player %s joined!%n", id));
            GameApplication.getInstance().addUpdatable(session);
            session.start();
            HostScene lobby = new HostScene(session);
            GameApplication.getInstance().switchScenes(lobby);
        }
    }

    private void showJoinDialog() {
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
