public class Gate extends Tile
{
    private boolean isOpen;

    /**
     * Default Constructor
     * Creates a closed Gate
     */
    public Gate()
    {
        setImage("Images/GateClosed.png");
        isOpen = false;
        setWalkable(false);
    }

    /**
     * Opens the Gate, making it able to be walked through
     */
    public void open()
    {
        setImage("Images/GateOpen.png");
        isOpen = true;
        setWalkable(true);
    }

    /**
     * Checks to see if the Gate is open
     * @return  boolean saying whether or not the Gate is open
     */
    public boolean isOpen()
    {
        return isOpen;
    }
}