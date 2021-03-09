import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class LoadGameWindow extends JFrame
{
    private JFrame previousWindow;
    private JButton backButton;
    private JButton helpButton;
    private JButton startButton;
    private JList fileList;

    /**
     * Creates and displays a window to load a previously saved game
     * @param   frame  JFrame that the call to create the LoadGameWindow came from. Used to change its ContentPane to a loaded Game
     */
    public LoadGameWindow(JFrame frame)
    {
        previousWindow = frame;
        setSize(350, 250);
        setResizable(false);
        setTitle("Load Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel listPanel = new JPanel();
        ArrayList<File> files = getFileNames();
        String[] names = new String[files.size()];
        for (int i = 0; i < files.size(); i++)
        {
            names[i] = files.get(i).toString();
        }
        fileList = new JList(names);
        //TODO add a TextArea on right to show info about saved game, like skyrim

        listPanel.add(fileList);

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
        add(fileList);
        add(Box.createRigidArea(new Dimension(0, 50)));
        add(buttonPanel);
        add(Box.createRigidArea(new Dimension(0, 50)));

        setVisible(true);
    }

    /**
     * Loads the last played game, used when "Continue" button is pressed
     * @param   frame  JFrame that the call to create the LoadGameWindow came from. Used to change its ContentPane to a loaded Game
     * @param   cont   boolean, true if the game is going to be continued, used to differentiate it from primary constructor
     */
    public LoadGameWindow(JFrame frame, boolean cont)
    {
        previousWindow = frame;
        ArrayList<File> files = getFileNames();
        ArrayList<GameSave> saves = new ArrayList<GameSave>();
        for (int i = 0; i < files.size(); i++)
        {
            saves.add(loadGame(files.get(0).toString()));
        }

        for (int i = 0; i < saves.size(); i++)
        {

        }

        if (cont)
        {
            previousWindow.setContentPane(new Game(loadGame(files.get(0).toString())));
            previousWindow.validate();
            dispose();
        }
    }

    /**
     * Returns a list of all the files in the Saves directory
     * @return  ArrayList of type File of the files in the Saves directory
     */
    public ArrayList<File> getFileNames()
    {
        ArrayList<File> files = new ArrayList<File>();

        File directory = new File("Saves/");
        File[] fList = directory.listFiles();

        for (File file : fList)
        {
            if (file.isFile())
            {
                files.add(file);
            }
        }

        return files;
    }

    /**
     * Loads the selected game from file if it exists
     * @param   saveName  String name of the file attempting to be loaded
     */
    private GameSave loadGame(String saveName)
    {
        GameSave save;
        try
        {
            FileInputStream fin = new FileInputStream(saveName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            save = (GameSave) ois.readObject();
            //fin.close();
            //ois.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            save = null;
            System.out.println("Error loading game save " + saveName);
            e.printStackTrace();
        }

        return save;
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
                previousWindow.setContentPane(new Game(loadGame((String) fileList.getSelectedValue())));
                System.out.println(fileList.getSelectedValue());
                previousWindow.validate();

                parent.dispose();
            }
        }
    }
}