//todo fix points mechanism

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level1 extends Scene {
    private Snake snake;
    private ArrayList<ItemComponent> items;
    private BufferedImage snakeImage;
    private BufferedImage itemImage;
    public static int points = 0;
    public static JLabel pointsLabel = new JLabel("Punkty: "+points);
    GridBagConstraints c = new GridBagConstraints();

    private DrawLevel1 drawLevel;

    private boolean isGameLost(Snake snake){
        return (snake.wallColisionCheckUpdate() || snake.selfCollisionCheckUpdate());
    }
    public void runScene() {
        super.runScene();
        drawLevel = new DrawLevel1();
        drawLevel.setLayout(new GridBagLayout());
        c.anchor = GridBagConstraints.NORTHWEST;
        c.weightx = 3;
        c.weighty = 3;
        c.gridx = 1;
        c.gridy = 1;
        drawLevel.add(pointsLabel,c);
        Game.window.frame.getContentPane().removeAll();
        snakeImage = loadImage("leszcz.png");
        itemImage = loadImage("wudeczka.png");
        snake = new Snake(this);
        items = new ArrayList<>();

        for (int i = 0; i < Game.NUMBER_OF_ITEMS; i++) {
            addItemComponent(items);
        }
        Game.window.frame.getContentPane().add(drawLevel);
        Game.window.frame.addKeyListener(snake.directionListener);

        snake.addComponent(20);

        Game.window.frame.setVisible(true);
        while (running) {
            snake.update();
            snake.selfCollisionCheckUpdate();
            snake.itemCollisionCheckUpdate(items);
            snake.IncrementFramesSinceLastTurn();

            drawLevel.repaint();

            if (isGameLost(snake)) {
                running = false;
                Scene.activeScene = 2;
                c.gridy = 2;
                c.gridx = 2;
                JLabel label1 = new JLabel("<html><center>Przegranko <br> Naciśnij spacje aby zacząć od nowa</center></html>");
                drawLevel.add(label1,c);
                points = 0;
                pointsLabel.setText("Punkty: "+points);
            }

            try {
                Thread.sleep(Game.DELTA_T_IN_MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public boolean canThisItemBeAdded(ItemComponent itemToAdd, ArrayList<ItemComponent> items) {
        for (ItemComponent item : items) {
            if (ItemComponent.doComponentsColide(item, itemToAdd)) {
                return false;
            }
            for (SnakeComponent component : snake.getSnakeComponents()) {
                if (ItemComponent.doComponentsColide(component, itemToAdd)) {
                    return false;
                }
            }
        }
        return true;
    }
    public void addItemComponent(ArrayList<ItemComponent> items){
        while(true) {
            ItemComponent itemToAdd = new ItemComponent();
            if(canThisItemBeAdded(itemToAdd,items)){
                items.add(itemToAdd);
                break;
            }
        }
    }
    public class DrawLevel1 extends DrawScene{
        protected void paintComponent(Graphics g) {
           super.paintComponent(g);
           Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.PINK);
            g2d.fillRect(0, 0, 1000, 1000);

            g2d.setColor(Color.RED);
            for (ItemComponent c : items) {
                //g2d.drawImage(itemImage, c.x, c.y, c.size, c.size, this);
                g2d.fillRect(c.getX(), c.getY(), c.getSize(), c.getSize());
            }
            g2d.setColor(Color.BLACK);
            for (SnakeComponent c : snake.getSnakeComponents()) {
                //g2d.drawImage(snakeImage, c.getX(), c.getY(), c.getSize() + 5, c.getSize() + 5, this);
                g2d.fillRect(c.getX(), c.getY(), c.getSize(), c.getSize());
            }
            //g.drawImage(snakeImage, snake.getSnakeHead().getX(), snake.getSnakeHead().getY(), snake.getSnakeHead().getSize() + 5, snake.getSnakeHead().getSize() + 5, this);
        }
    }
}