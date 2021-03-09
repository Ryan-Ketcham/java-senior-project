import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel
{
    private GameSave save;
    private JPanel backgroundPanel;
    private World world;
    private EntityLayer entities;
    private Timer entityTask;
    private Timer game;
    private JPanel sidePanel;
    private JPanel inventoryPanel;
    private JLabel healthLabel;
    private JLabel xpLabel;
    private JLabel levelLabel;
    private JButton exitButton;
    private boolean keyPressed = false;
    private boolean isUpdating = false;
    private boolean paused = false;

    /**
     * Creates a new Game instance using the save provided, including generating a new EntityLayer and starting the update Timer
     * @param   save  GameSave object that stores the save data that will be used by this Game object
     */
    public Game(GameSave save)
    {
        setLayout(new BorderLayout());

        this.save = save;
        world = save.getWorld();
        entities = save.getEntityLayer();

        backgroundPanel = new JPanel();

        sidePanel = new JPanel(new BorderLayout());
        inventoryPanel = new JPanel();

        JPanel labelPanel = new JPanel(new GridLayout(3, 1));
        healthLabel = new JLabel("Health: " + entities.getPlayer().getHealth());
        xpLabel = new JLabel("Experience: " + entities.getPlayer().getXP());
        levelLabel = new JLabel("Level " + entities.getPlayer().getLevel());
        labelPanel.add(healthLabel);
        labelPanel.add(xpLabel);
        labelPanel.add(levelLabel);
        sidePanel.add(labelPanel);

        exitButton = new JButton("Save & Exit");
        exitButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                closeWindow();
            }
        });
        exitButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        exitButton.setFocusable(false);

        sidePanel.add(exitButton, BorderLayout.SOUTH);
        add(sidePanel, BorderLayout.CENTER);

        entityTask = new Timer();
        entityTask.scheduleAtFixedRate(new UpdateTask(), 0, 1000);

        game = new Timer();
        game.scheduleAtFixedRate(new EntityUpdateTask(), 0, 1000);

        setVisible(true);
        backgroundPanel.requestFocusInWindow();
    }

    /**
     * Updates the screen, displaying the images of the Tiles and Entities that are currently visible (within an 11x11 range of the Player)
     * If the Player is too close to one edge, or too close to two edges and therefore there is no 11x11 with the Player in the center,
     * the method will paint the closest to being centered 11x11 square that exists.
     * Also updates the Player's inventory and stats display
     */
    public void update()
    {
        isUpdating = true;
        remove(backgroundPanel);
        backgroundPanel = new JPanel(new GridLayout(11, 11, 0, 0));
        backgroundPanel.addKeyListener(new MoveListener());
        backgroundPanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 2));

        ImageIcon[][] grid = world.getImageArray();

        boolean inWidth = entities.getPlayer().getX() > 4 && entities.getPlayer().getX() < world.getWidth()-5; //Checks if the player is within the normal display world.getWidth()
        boolean inHeight = entities.getPlayer().getY() > 4 && entities.getPlayer().getY() < world.getHeight()-5; //Checks if the player is withing the normal display world.getHeight()
        if (inWidth && inHeight)
        {
            for (int h = entities.getPlayer().getY()-5; h <= entities.getPlayer().getY() + 5; h++)
            {
                for (int w = entities.getPlayer().getX()-5; w <= entities.getPlayer().getX() + 5; w++)
                {
                    JLabel label = new JLabel();
                    if (entities.checkForEntity(w, h))
                    {
                        label.setIcon(entities.getEntityAt(w, h).getImage());
                    }
                    else
                    {
                        label.setIcon(grid[h][w]);
                    }
                    backgroundPanel.add(label);
                }
            }
        }
        else
        {
            if (entities.getPlayer().getY() <= 4) //If the player is too close to the top to display normally
            {
                int h = entities.getPlayer().getY() <= 4 ? 0 : entities.getPlayer().getY()-5;
                while (h < 11)
                {
                    if (entities.getPlayer().getX() <= 4) //If the player is also too close to the left side
                    {
                        int w = (entities.getPlayer().getX() <= 4 ? 0 : entities.getPlayer().getX()-5);
                        while (w < 11)
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                    else if (!inWidth) //If the player is too close to the right side
                    {
                        int w = world.getWidth()-11;
                        while (w < world.getWidth())
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                    else //If the player is in the normal display width
                    {
                        for (int w = entities.getPlayer().getX()-5; w <= entities.getPlayer().getX() + 5; w++)
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                        }
                    }
                    h++;
                }
            }
            else if (!inHeight) //If the player is too close to the bottom to display normally
            {
                int h = world.getHeight()-11;
                while (h < world.getHeight())
                {
                    if (entities.getPlayer().getX() <= 4) //If the player is also too close to the left
                    {
                        int w = (entities.getPlayer().getX() <= 4 ? 0 : entities.getPlayer().getX()-5);
                        while (w < 11)
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                    else if (!inWidth) //If the player is too close to the right
                    {
                        int w = world.getWidth()-11;
                        while (w < world.getWidth())
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                    else //If the player is in the normal display width
                    {
                        for (int w = entities.getPlayer().getX()-5; w <= entities.getPlayer().getX() + 5; w++)
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                        }
                    }
                    h++;
                }
            }
            else //If the player is in the normal display height
            {
                for (int h = entities.getPlayer().getY()-5; h <= entities.getPlayer().getY() + 5; h++)
                {
                    if (entities.getPlayer().getX() <= 4) //If the player is too close to the left
                    {
                        int w = (entities.getPlayer().getX() <= 4 ? 0 : entities.getPlayer().getX()-5);
                        while (w < 11)
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                    else if (!inWidth) //If the player is too close to the right
                    {
                        int w = world.getWidth()-11;
                        while (w < world.getWidth())
                        {
                            JLabel label = new JLabel();
                            if (entities.checkForEntity(w, h))
                            {
                                label.setIcon(entities.getEntityAt(w, h).getImage());
                            }
                            else
                            {
                                label.setIcon(grid[h][w]);
                            }
                            backgroundPanel.add(label);
                            w++;
                        }
                    }
                }
            }
        }


        //Adds Player's inventory to the side panel
        sidePanel.remove(inventoryPanel);
        inventoryPanel = new JPanel(new GridLayout(3, 3, 0, 0));
        inventoryPanel.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK, 1));
        for (int i = 8; i >= 0; i--)
        {
            JLabel label = new JLabel();
            label.setIcon(entities.getPlayer().getInventoryItemAt(i).getImage());
            inventoryPanel.add(label);
        }
        sidePanel.add(inventoryPanel, BorderLayout.NORTH);
        healthLabel.setText("Health: " + entities.getPlayer().getHealth());
        xpLabel.setText("Experience: " + entities.getPlayer().getXP() + "/" + entities.getPlayer().getLevel()*100);
        levelLabel.setText("Level " + entities.getPlayer().getLevel());

        add(backgroundPanel, BorderLayout.WEST);
        backgroundPanel.requestFocusInWindow();
        validate();
        isUpdating = false;
    }

    /**
     * Pauses the game, stopping the movement of entities and updating of the screen
     */
    public void pause()
    {
        game.cancel();
        game = new Timer();
        entityTask.cancel();
        entityTask = new Timer();
        paused = true;
    }

    /**
     * Unpauses the game, restarting the movement of entities and updating of the screen
     */
    public void unpause()
    {
        game.scheduleAtFixedRate(new UpdateTask(), 0, 1000);
        entityTask.scheduleAtFixedRate(new EntityUpdateTask(), 0, 1000);
        paused = false;
    }

    /**
     * Ends the game by displaying a JDialog and stopping the updating of the screen
     * @param  type  Tells the method what type of ending the game had. 0 is for victory, 1 is for losing.
     */
    public void endGame(int type)
    {
        String titleText = type == 0 ? "Victory" : "Game Over";
        String labelText = type == 0 ? "You Win!" : "You Lose";

        game.cancel();
        entityTask.cancel();

        backgroundPanel.removeKeyListener(backgroundPanel.getKeyListeners()[0]);

        JDialog window = new JDialog();
        window.setTitle(titleText);
        window.setSize(250, 65);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(label);
        JButton closeButton = new JButton("Return to Main Menu");
        closeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ae)
            {
                JButton b = (JButton) ae.getSource();
                JDialog d = (JDialog) b.getTopLevelAncestor();
                d.dispose();
                closeWindow();
            }
        });

        closeButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        panel.add(closeButton);
        window.add(panel);

        window.setVisible(true);
    }

    public void closeWindow()
    {
        game.cancel();
        entityTask.cancel();
        save.setEntityLayer(entities);
        save.setWorld(world);
        save.saveToFile();

        JFrame parent = (JFrame) this.getTopLevelAncestor();

        parent.dispose();
    }

    /**
     * Task for the Timer to update the display
     */
    public class UpdateTask extends TimerTask
    {
        /**
         * Calls the update method every second to display all non-Player Entity movements. After updating, checks to see if the Player is alive and if the Player has won.
         */
        public void run()
        {
            if (!isUpdating)
            {
                update();
            }
            if (!entities.getPlayer().isAlive())
            {
                endGame(1);
            }
            if (entities.getPlayer().hasWon())
            {
                endGame(0);
            }
        }
    }

    /**
     * Task for the Timer to move the Entities
     */
    public class EntityUpdateTask extends TimerTask
    {
        /**
         * Moves the Entities
         */
        public void run()
        {
            entities.moveEntities();
        }
    }

    /**
     * KeyListener that checks for the user imput through the chosen control keys
     */
    class MoveListener implements KeyListener
    {
        /**
         * Checks which key is typed, then performs an action based on which key it is
         * @param   ke  KeyEvent that allows the method to determine which key was pressed
         */
        public void keyTyped(KeyEvent ke)
        {
            char id = ke.getKeyChar();
            if (!paused)
            {
                if (!keyPressed)
                {
                    if (id == 'w' || id == 'W')
                    {
                        if (entities.getPlayer().getOrientation() == 0)
                        {
                            entities.movePlayer(entities.getPlayer().getX(), entities.getPlayer().getY()-1);
                        }
                        else
                        {
                            entities.getPlayer().turnTo(0);
                        }
                        keyPressed = true;
                    }

                    if (id == 's' || id == 'S')
                    {
                        if (entities.getPlayer().getOrientation() == 180)
                        {
                            entities.movePlayer(entities.getPlayer().getX(), entities.getPlayer().getY()+1);
                        }
                        else
                        {
                            entities.getPlayer().turnTo(180);
                        }
                        keyPressed = true;
                    }

                    if (id == 'd' || id == 'D')
                    {
                        if (entities.getPlayer().getOrientation() == 90)
                        {
                            entities.movePlayer(entities.getPlayer().getX()+1, entities.getPlayer().getY());
                        }
                        else
                        {
                            entities.getPlayer().turnTo(90);
                        }
                        keyPressed = true;
                    }

                    if (id == 'a' || id == 'A')
                    {
                        if (entities.getPlayer().getOrientation() == 270)
                        {
                            entities.movePlayer(entities.getPlayer().getX()-1, entities.getPlayer().getY());
                        }
                        else
                        {
                            entities.getPlayer().turnTo(270);
                        }
                        keyPressed = true;
                    }
                }

                if (id == ' ')
                {
                    if (entities.checkForEntity(entities.getPlayer().getXInFront(), entities.getPlayer().getYInFront()))
                    {
                        entities.getPlayer().attack(entities.getEntityAt(entities.getPlayer().getXInFront(), entities.getPlayer().getYInFront()));
                        if (!entities.getEntityAt(entities.getPlayer().getXInFront(), entities.getPlayer().getYInFront()).isAlive())
                        {
                            entities.remove(entities.getEntityAt(entities.getPlayer().getXInFront(), entities.getPlayer().getYInFront()));
                        }
                    }
                    else
                    {
                        entities.getPlayer().breakBlockInFrontOf(world);
                    }
                }

                if (id == 'e' || id == 'E')
                {
                    entities.getPlayer().tryUseObjectInFront(world);
                }

                if (id == '1')
                {
                    entities.getPlayer().useItem(2);
                }

                if (id == '2')
                {
                    entities.getPlayer().useItem(1);
                }

                if (id == '3')
                {
                    entities.getPlayer().useItem(0);
                }

                if (id == '4')
                {
                    entities.getPlayer().useItem(5);
                }

                if (id == '5')
                {
                    entities.getPlayer().useItem(4);
                }

                if (id == '6')
                {
                    entities.getPlayer().useItem(3);
                }

                if (id == '7')
                {
                    entities.getPlayer().useItem(8);
                }

                if (id == '8')
                {
                    entities.getPlayer().useItem(7);
                }

                if (id == '9')
                {
                    entities.getPlayer().useItem(6);
                }
            }

            if (id == 'p' || id == 'P')
            {
                if (!paused)
                {
                    pause();
                }
                else
                {
                    unpause();
                }
            }

            if (!isUpdating)
            {
                update();
            }
        }

        public void keyPressed(KeyEvent ke) {}

        public void keyReleased(KeyEvent ke) {  keyPressed = false; }
    }
}
