import java.io.*;
import java.util.Date;

public class GameSave implements Serializable
{
    private String name; //The name of the save
    private Date dateCreated; //The Date the save was created
    private Date dateLastPlayed; //The last time the save was played
    private World world; //The world in the save
    private EntityLayer entities; //The entities in the save

    /**
     * Creates a new GameSave using the given input world width and height, and then generates a new World and EntityLayer object
     * @param   width   int that determines the width, in Tiles, of the new World to be generated
     * @param   height  int that determines the height, in Tiles, of the new World to be generated
     * @param   n       String name of the save and the save file
     */
    public GameSave(int width, int height, String n)
    {
        name = n;
        dateCreated = new Date();
        dateLastPlayed = dateCreated;
        world = new World(width, height);
        entities = new EntityLayer((world.getWidth()+world.getHeight()), world);
        saveToFile();
    }

    /**
     * Saves the game to the file, overwriting any previous saves with the same name
     */
    public void saveToFile()
    {
        try
        {
            dateLastPlayed = new Date();
            File saveDirectory = new File("Saves");
            if (!saveDirectory.exists()){
                saveDirectory.mkdir();
            }
            File file = new File("Saves/" + name + ".ser");
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(this);
            oos.close();
            fout.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving game to file");
            e.printStackTrace();
        }
    }

    /**
     * Sets the saved World to a new one
     * @param   w   World to be saved in this GameSave
     */
    public void setWorld(World w)
    {
        world = w;
    }

    /**
     * Returns the World stored in this save
     * @return  the World in this save
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * Sets the saved EntityLayer to a new one
     * @param   e   EntityLayer to be saved in this GameSave
     */
    public void setEntityLayer(EntityLayer e)
    {
        entities = e;
    }

    /**
     * Returns the EntityLayer stored in this save
     * @return  the EntityLayer in this save
     */
    public EntityLayer getEntityLayer()
    {
        return entities;
    }
}