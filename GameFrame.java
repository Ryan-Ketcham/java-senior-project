import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame
{
    private JButton continueButton;
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton exitButton;

    /**
     * Creates and displays the frame that initially holds the Main Menu, and that will later hold the Game panel
     */
    public GameFrame()
    {
        setSize(700, 550);
        setTitle("Game");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 250)));

        continueButton = new JButton("Continue");
        continueButton.setMinimumSize(new Dimension(400, 50));
        continueButton.setPreferredSize(new Dimension(400, 50));
        continueButton.setMaximumSize(new Dimension(400, 50));
        continueButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        continueButton.addActionListener(new ButtonListener());
        buttonPanel.add(continueButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        newGameButton = new JButton("New Game");
        newGameButton.setMinimumSize(new Dimension(400, 50));
        newGameButton.setPreferredSize(new Dimension(400, 50));
        newGameButton.setMaximumSize(new Dimension(400, 50));
        newGameButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        newGameButton.addActionListener(new ButtonListener());
        buttonPanel.add(newGameButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        loadGameButton = new JButton("Load Game");
        loadGameButton.setMinimumSize(new Dimension(400, 50));
        loadGameButton.setPreferredSize(new Dimension(400, 50));
        loadGameButton.setMaximumSize(new Dimension(400, 50));
        loadGameButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        loadGameButton.addActionListener(new ButtonListener());
        buttonPanel.add(loadGameButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        exitButton = new JButton("Exit");
        exitButton.setMinimumSize(new Dimension(400, 50));
        exitButton.setPreferredSize(new Dimension(400, 50));
        exitButton.setMaximumSize(new Dimension(400, 50));
        exitButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        exitButton.addActionListener(new ButtonListener());
        buttonPanel.add(exitButton);

        add(buttonPanel);

        setVisible(true);
    }

    class ButtonListener implements ActionListener
    {
        /**
         * Performs different functions based on which button the ActionEvent comes from
         * @param   ae  ActionEvent that allows the ActionListener to determine which button the click came from
         */
        public void actionPerformed(ActionEvent ae)
        {
            JFrame parent = (JFrame) newGameButton.getTopLevelAncestor();
            if (ae.getSource() == continueButton)
            {
                new LoadGameWindow(parent, true);
            }
            if (ae.getSource() == newGameButton)
            {
                new NewGameWindow(parent);
            }
            if (ae.getSource() == loadGameButton)
            {
                new LoadGameWindow(parent);
            }
            if (ae.getSource() == exitButton)
            {
                parent.dispose();
            }
        }
    }
}
