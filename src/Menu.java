import javax.swing.*;

public class Menu extends Scene {
    private JLabel text = new JLabel("welcome to Simple Snake, press space to start");
    private GameStartedListener gameListener = new GameStartedListener();

    @Override
    public void runScene() {
        super.runScene();
        Game.window.frame.getContentPane().add(text);
        Game.window.frame.addKeyListener(gameListener);
        Game.window.frame.setVisible(true);
        while (running) {
            Game.window.frame.repaint();
        }
    }
}
