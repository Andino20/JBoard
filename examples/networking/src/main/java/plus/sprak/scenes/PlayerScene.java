package plus.sprak.scenes;

import plus.jboard.core.GameApplication;
import plus.jboard.core.Scene;
import plus.jboard.net.NetworkEnvelope;
import plus.jboard.net.handler.MessageHandler;
import plus.jboard.net.session.PlayerSession;
import plus.sprak.messages.GameStartMessage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PlayerScene extends Scene implements MessageHandler<GameStartMessage> {

    private final Scene nextScene;

    private JLabel centerText;

    public PlayerScene(PlayerSession session, Scene nextScene) {
        this.nextScene = nextScene;

        init();
        session.onJoinSuccess(id -> SwingUtilities.invokeLater(() -> centerText.setText(String.format("You are %s!", id))));
        session.start();
    }

    private void init() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Player Session", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 15, 0);
        root.add(title, gbc);

        centerText = new JLabel("Waiting for host response ...", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(centerText, gbc);

        setUI(root);
    }

    @Override
    public Class<GameStartMessage> getAssociatedMessageType() {
        return GameStartMessage.class;
    }

    @Override
    public void handle(NetworkEnvelope<GameStartMessage> messageContext) {
        GameApplication.getInstance().switchScenes(nextScene);
    }

}
