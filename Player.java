import java.util.Random;

public class Player extends Entity
{
    private Item[] inventory;
    private boolean hasWon = false;

    /**
     * Creates a Player with a specified amount of maximum health, a starting x, and a starting y
     * @param   maxH    the maximum amount of health this Player can have
     * @param   startX  the starting x coordinate of the Player
     * @param   startY  the starting y coordinate of the Player
     */
    public Player(int maxH, int startX, int startY)
    {
        super(maxH, 'p', startX, startY);
        inventory = new Item[] {new Item(), new Item(), new Item(), new Item(), new Item(), new Item(), new Potion(25), new Potion(25), new Potion(25)};
    }

    /**
     * Sets the position of the Player to an x and a y value. If the desired location within the world is occupied
     * by a Tile that cannot be passed through, the Player does not move
     * If the Player moves onto an opened Gate, the Player wins the game
     * @param   newX  the desired x value of the Player
     * @param   newY  the desired y value of the Player
     * @param   world the World that the P is in
     */
    public void setPosition(int newX, int newY, World world)
    {
        if (newX < world.getWidth() && newX >= 0 && newY < world.getHeight() && newY >= 0)
        {
            if (world.getTileAt(newX, newY).getWalkable())
            {
                setX(newX);
                setY(newY);
                if (world.getTileAt(newX, newY) instanceof Gate)
                {
                    hasWon = true;
                }
            }
        }
    }

    /**
     * Attacks the input Entity, giving the Player the amount of experience the Entity is worth if it dies, and adding the Entity's drop Item to the Player's inventory
     * @param   e  the Entity to attack
     */
    public void attack(Entity e)
    {
        int damage = getAttackDamage();
        e.setHealth(e.getHealth()-damage);
        if (!e.isAlive())
        {
            setXP(getXP() + e.getXPWorth());
            int dropChance = new Random().nextInt(10);
            if (dropChance == 0)
            {
                if (hasOpenInventorySlot())
                {
                    addItemToInventory(e.dropItem(), nextOpenInventorySlot());
                }
            }
        }
    }

    /**
     * Attempts to break the block in front of the Player in the World if it is breakable, damaging the player by 5 each hit
     * @param   world  the World the Player is in
     */
    public void breakBlockInFrontOf(World world)
    {
        Tile t = world.getTileAt(getXInFront(), getYInFront());
        if (t.getBreakable())
        {
            t.damage();
            setHealth(getHealth()-5);
        }
    }

    /**
     * Attempts to use the object the Player is facing, such as a Chest or a Gate
     * @param   world  the World the Player is in
     */
    public void tryUseObjectInFront(World world)
    {
        if (world.getTileAt(getXInFront(), getYInFront()) instanceof Chest) //If it is a Chest
        {
            Chest c = (Chest) world.getTileAt(getXInFront(), getYInFront());
            if (!c.isOpen()) //If the Chest isn't already opent then open it
            {
                if (hasOpenInventorySlot())
                {
                    addItemToInventory(c.open(), nextOpenInventorySlot());
                }
                else
                {
                    addItemToInventory(c.open(), 9);
                }
            }
        }
        if (world.getTileAt(getXInFront(), getYInFront()) instanceof Gate) //If it is a Gate
        {
            if (hasKey()) //If the Player has a Key then open the Gate
            {
                int i = 0;
                while (i < inventory.length)
                {
                    if (inventory[i] instanceof Key)
                    {
                        break;
                    }
                    i++;
                }
                inventory[i] = new Item();
                Gate g = (Gate) world.getTileAt(getXInFront(), getYInFront());
                g.open();
            }
        }
    }

    /**
     * Adds the given Item to the inventory at the give position
     * @param   item      the Item to add to the inventory
     * @param   itemSlot  the position to add the Item to
     */
    public void addItemToInventory(Item item, int itemSlot)
    {
        inventory[itemSlot] = item;
    }

    /**
     * Removes an Item from a certain point in the Player's inventory
     * @param   itemSlot  the position in the inventory to remove an Item from
     */
    public void removeItem(int itemSlot)
    {
        inventory[itemSlot] = new Item();
    }

    /**
     * Returns the Item at the requested inventory slot
     * @param   itemSlot  the inventory slot to return the Item from
     * @return  the Item at the given inventory slot
     */
    public Item getInventoryItemAt(int itemSlot)
    {
        return inventory[itemSlot];
    }

    /**
     * Uses the Item in the designated slot if it can be used, then removes it from the inventory
     * @param   itemSlot  the item slot that the Item is in
     */
    public void useItem(int itemSlot)
    {
        if (inventory[itemSlot] instanceof Potion)
        {
            usePotion((Potion) inventory[itemSlot], itemSlot);
        }
    }

    /**
     * Uses a potion by adding the amount of health or mana it restores to the Player's current health or mana
     * @param   potion    the Potion being used
     * @param   itemSlot  the itemSlot that the Potion is in
     */
    public void usePotion(Potion potion, int itemSlot)
    {
        if (getHealth() < getMaxHealth())
        {
            setHealth(getHealth() + potion.getAmount());
            removeItem(itemSlot);
        }
    }

    /**
     * Returns the position of the next open inventory slot
     * @return  the position of the next open inventory slot
     */
    public int nextOpenInventorySlot()
    {
        int i = inventory.length-1;
        while (i >= 0)
        {
            if (!(inventory[i] instanceof Potion) && !(inventory[i] instanceof Key))
            {
                break;
            }
            i--;
        }
        return i;
    }

    /**
     * Returns whether or not the Player has an open inventory slot
     * @return  whether or not the Player has an open inventory slot
     */
    public boolean hasOpenInventorySlot()
    {
        boolean hasSlot = false;
        for (int i = 0; i < inventory.length; i++)
        {
            if (!(inventory[i] instanceof Potion) && !(inventory[i] instanceof Potion))
            {
                hasSlot = true;
            }
        }
        return hasSlot;
    }

    /**
     * Returns whether or not the Player has a Key
     * @return  whether or not the Player has a Key
     */
    public boolean hasKey()
    {
        boolean hasKey = false;
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] instanceof Key)
            {
                hasKey = true;
            }
        }
        return hasKey;
    }

    /**
     * Returns whether or not the Player has stepped on a Gate and won the game
     * @return  whether or not the Player has won
     */
    public boolean hasWon()
    {
        return hasWon;
    }
}