package plus.sprak.lobby;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Scene;
import plus.jboard.net.session.HostSession;
import plus.sprak.app.BackgammonScene;
import plus.sprak.lobby.messages.GameStartMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

public class HostScene extends Scene {

    private final HostSession session;

    private JButton startButton;

    private DefaultListModel<String> playerListModel;

    public HostScene(HostSession session) {
        this.session = session;

        session.onPlayerJoin(this::onPlayerJoin);
        init();
    }

    private void init() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        // Title
        JLabel title = new JLabel("Lobby", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        root.add(title, gbc);

        // Player list
        playerListModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(playerListModel);
        playerList.setVisibleRowCount(6);

        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(200, 120));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 15, 0);
        root.add(scrollPane, gbc);

        startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            try {
                GameApplication.getInstance().switchScenes(new BackgammonScene(session, true));
            } catch (IOException ex) {
                throw new RuntimeException("");
            }
            session.broadcast(new GameStartMessage());
        });

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        root.add(startButton, gbc);

        this.addPlayer("You");

        setUI(root);
    }

    public void addPlayer(String name) {
        if (!playerListModel.contains(name)) {
            SwingUtilities.invokeLater(() -> playerListModel.addElement(name));
        }
    }

    private void onPlayerJoin(UUID id) {
        this.addPlayer(id.toString());
    }

}
