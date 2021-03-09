import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewGameWindow extends JFrame
{
    private JFrame previousWindow;
    private JTextField widthField;
    private JTextField heightField;
    private JButton backButton;
    private JButton helpButton;
    private JButton startButton;

    /**
     * Creates and displays a window to enter options required for starting a new Game
     * @param   frame  JFrame that the call to create the NewGameWindow came from. Used to change its ContentPane to a new Game
     */
    public NewGameWindow(JFrame frame)
    {
        previousWindow = frame;
        setSize(350, 250);
        setResizable(false);
        setTitle("New Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        inputPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        JLabel widthLabel = new JLabel("World width: ");
        widthField = new JTextField(6);
        inputPanel.add(widthLabel);
        inputPanel.add(widthField);

        inputPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        JLabel heightLabel = new JLabel("World height: ");
        heightField = new JTextField(6);
        inputPanel.add(heightLabel);
        inputPanel.add(heightField);

        inputPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        backButton = new JButton("Back");
        backButton.setMinimumSize(new Dimension(80, 30));
        backButton.setPreferredSize(new Dimension(80, 30));
        backButton.setMaximumSize(new Dimension(80, 30));
        backButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        backButton.addActionListener(new ButtonListener());
        buttonPanel.add(backButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(25, 0)));

        helpButton = new JButton("Help");
        helpButton.setMinimumSize(new Dimension(80, 30));
        helpButton.setPreferredSize(new Dimension(80, 30));
        helpButton.setMaximumSize(new Dimension(80, 30));
        helpButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        helpButton.addActionListener(new ButtonListener());
        buttonPanel.add(helpButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(25, 0)));

        startButton = new JButton("Start");
        startButton.setMinimumSize(new Dimension(80, 30));
        startButton.setPreferredSize(new Dimension(80, 30));
        startButton.setMaximumSize(new Dimension(80, 30));
        startButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        startButton.addActionListener(new ButtonListener());
        buttonPanel.add(startButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(30, 0)));

        add(Box.createRigidArea(new Dimension(0, 80)));
        add(inputPanel);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        setVisible(true);
    }

    class ButtonListener implements ActionListener
    {
        /**
         * Performs different functions based off of which button the ActionEvent comes from
         * @param   ae  ActionEvent that allows the ActionListener to determine which button the click came from
         */
        public void actionPerformed(ActionEvent ae)
        {
            JFrame parent = (JFrame) backButton.getTopLevelAncestor();

            if (ae.getSource() == backButton)
            {
                parent.dispose();
            }
            if (ae.getSource() == helpButton)
            {
                //new HelpWindow(); 
            }
            if (ae.getSource() == startButton)
            {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                Date d = new Date();
                long saveNumber = d.getTime();

                previousWindow.setContentPane(new Game(new GameSave(width, height, "save" + saveNumber)));
                previousWindow.validate();

                parent.dispose();
            }
        }
    }
}