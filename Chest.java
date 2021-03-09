public class Chest extends Tile
{
    private Item contents;
    private boolean isOpen;

    /**
     * Default Constructor
     * Creates a closed Chest with a Key inside
     */
    public Chest()
    {
        setImage("Images/Chest.png");
        contents = new Key();
        isOpen = false;
        setWalkable(false);
    }

    /**
     * Opens the Chest and returns the Item inside
     * @return  the Item inside of the Chest
     */
    public Item open()
    {
        setImage("Images/ChestOpen.png");
        isOpen = true;
        return contents;
    }

    /**
     * Returns whether or not the Chest is open
     * @return  whether or not the Chest is open
     */
    public boolean isOpen()
    {
        return isOpen;
    }
}