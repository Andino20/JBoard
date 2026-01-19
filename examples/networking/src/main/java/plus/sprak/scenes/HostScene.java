package plus.sprak.scenes;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Scene;
import plus.jboard.net.session.HostSession;
import plus.sprak.messages.GameStartMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

public class HostScene extends Scene {

    private final HostSession session;
    private final Scene nextScene;

    private JButton startButton;

    private DefaultListModel<String> playerListModel;

    public HostScene(HostSession session, Scene nextScene) {
        this.session = session;
        this.nextScene = nextScene;

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
        startButton.setEnabled(false);
        startButton.addActionListener(e -> GameApplication.getInstance().switchScenes(nextScene));

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        root.add(startButton, gbc);

        setUI(root);
    }

    public void addPlayer(String name) {
        if (!playerListModel.contains(name)) {
            SwingUtilities.invokeLater(() -> playerListModel.addElement(name));
        }
    }


    private void onPlayerJoin(UUID id) {
        this.addPlayer(id.toString());
        if (session.isFull()) {
            this.startButton.setEnabled(true);
            this.session.broadcast(new GameStartMessage());
        }
    }

}
