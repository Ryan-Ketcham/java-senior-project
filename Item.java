import java.io.Serializable;
import javax.swing.ImageIcon;

public class Item implements Serializable
{
    private ImageIcon image;

    /**
     * Default Constructor
     * Sets the image for the Item to an empty inventory slot
     */
    public Item()
    {
        setImage("Images/EmptySlot.png");
    }

    /**
     * Sets the image of the Item that is to be displayed
     * @param   img  ImageIcon to be displayed
     */
    public void setImage(String img)
    {
        image = new ImageIcon(getClass().getResource(img));
    }

    /**
     * Returns the image of this Item
     * @return  ImageIcon of the Item
     */
    public ImageIcon getImage()
    {
        return image;
    }
}