import java.io.Serializable;
import javax.swing.ImageIcon;

public class Tile implements Serializable
{
    private ImageIcon image;
    private boolean walkable; //Can the tile be walked through
    private boolean breakable;  //Can the tile be broken
    private int durability;
    private char type;

    /**
     * Default Constructor
     * Makes a grass Tile by default
     */
    public Tile()
    {
        this('g');
    }

    /**
     * Constructor that specifies what type of Tile to create
     * @param   t  the type of Tile to be created. 'b' is for a boulder, 'g' is for grass, 't' is for a tree
     */
    public Tile(char t)
    {
        type = t;

        if (t == 'b')
        {
            durability = 5;
            breakable = true;
            walkable = false;
            setImage("Images/Boulder.png");
        }
        else if (t == 't')
        {
            breakable = false;
            walkable = false;
            setImage("Images/Tree1.png");
        }
        else
        {
            breakable = false;
            walkable = true;

            if (t == 'g')
            {
                setImage("Images/Grass.png");
            }
        }
    }

    /**
     * Sets the image of the Tile that is to be displayed
     * @param   img  ImageIcon to be displayed
     */
    public void setImage(String img)
    {
        image = new ImageIcon(getClass().getResource(img));
    }

    /**
     * Returns the image of this Tile
     * @return  ImageIcon of the Tile
     */
    public ImageIcon getImage()
    {
        return image;
    }

    /**
     * Decides whether or not Entities can walk through this Tile
     * @param   walkable  boolean that determines whether or not Entities can walk through this Tile
     */
    public void setWalkable(boolean w)
    {
        walkable = w;
    }

    /**
     * Returns a boolean saying whether or not this Tile can be walked through by an Entity
     * @return  boolean saying whether or not this Tile can be walked through by an Entity
     */
    public boolean getWalkable()
    {
        return walkable;
    }

    /**
     * Damages the Tile by one point. Only to be called if the Tile is breakable
     */
    public void damage()
    {
        durability -= 1;
        if (durability <= 0)
        {
            type = 'g';
            setImage("Images/Grass.png");
            walkable = true;
            breakable = false;
        }
    }

    /**
     * Returns whether or not the Tile is breakable
     * @return  whether or not the Tile is breakable
     */
    public void setBreakable(boolean b)
    {
        breakable = b;
    }

    /**
     * Returns whether or not the Tile is breakable
     * @return  whether or not the Tile is breakable
     */
    public boolean getBreakable()
    {
        return breakable;
    }
}