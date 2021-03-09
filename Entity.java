import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

public class Entity implements Serializable
{
    private ImageIcon image;
    private int maxHealth;
    private int health;
    private int damageMax;
    private int damageMin;
    private int xpWorth;
    private int xp;
    private int level;
    private int x;
    private int y;
    private int direction;
    private Item dropItem;
    private char type;
    private boolean isAlive;

    /**
     * Constructor that specifies an Entity type and a maximum health. Creates the Entity and then sets its image based on its type.
     * @param   maxH     the maximum health the Entity will have
     * @param   entType  the type of Entity, stored as a char. 'p' is for a Player, and 'e' is for an enemy
     */
    public Entity(int maxH, char entType)
    {
        maxHealth = maxH;
        health = maxH;
        xpWorth = 25;
        xp = 0;
        level = 1;
        type = entType;
        isAlive = true;
        if (type == 'p') //If the Entity is a Player it does more damage
        {
            damageMax = 40;
            damageMin = 35;
        }
        else
        {
            damageMax = 20;
            damageMin = 10;
        }
        dropItem = new Potion(25);
        setImage();
        direction = 90;
        isAlive = true;
    }

    /**
     * Constructor that specifies an Entity type, a maximum health, and a starting x and y position. Creates the Entity and then sets its image based on its type.
     * @param   maxH     the maximum health the Entity will have
     * @param   entType  the type of Entity, stored as a char. 'p' is for a Player, and 'e' is for an enemy
     * @param   startX   the starting x coordinate of the Entity
     * @param   startY   the starting y coordinate of the Entity
     */
    public Entity(int maxH, char entType, int startX, int startY)
    {
        this(maxH, entType);
        x = startX;
        y = startY;
    }

    /**
     * Sets the Image of the entity based on what type of entity it is, Player or an enemy
     */
    private void setImage()
    {
        if (type == 'p')
        {
            setImage("Images/PlayerR.png");
        }
        if (type == 'e')
        {
            setImage("Images/EnemyR.png");
        }
    }

    /**
     * Turns the Entity a certain amount of degrees and then sets the Image for that direction
     * @param   degrees  amount of degrees to turn, must be between 0 and 360 and a multiple of 90
     */
    public void turnTo(int degrees)
    {
        direction = degrees;
        if (direction == 0)
        {
            setImage(type == 'p' ? "Images/PlayerU.png" : "Images/EnemyU.png");
        }
        if (direction == 90)
        {
            setImage(type == 'p' ? "Images/PlayerR.png" : "Images/EnemyR.png");
        }
        if (direction == 180)
        {
            setImage(type == 'p' ? "Images/PlayerD.png" : "Images/EnemyD.png");
        }
        if (direction == 270)
        {
            setImage(type == 'p' ? "Images/PlayerL.png" : "Images/EnemyL.png");
        }
    }

    /**
     * Returns the orientation of the Entity in degrees
     * @return  the orientation of the entity in degrees
     */
    public int getOrientation()
    {
        return direction;
    }

    /**
     * Sets the Entity's health. If the new health is above the Entity's maximum hp, its health is set to the maximum
     * @param   newHealth  the amount of health to set
     */
    public void setHealth(int newHealth)
    {
        health = newHealth;

        if (health > maxHealth)
        {
            health = maxHealth;
        }
        if (health <= 0)
        {
            health = 0;
            isAlive = false;
        }
    }

    /**
     * Returns the amount of health the Entity currently has
     * @return  the amount of health the Entity currently has
     */
    public int getHealth()
    {
        return health;
    }

    /**
     * Returns the maximum amount of health the Entity can have
     * @return  the maximum amount of health the Entity can have
     */
    public int getMaxHealth()
    {
        return maxHealth;
    }

    /**
     * Sets the x position of the Entity
     * @param   val  the desired x value
     */
    public void setX(int val)
    {
        x = val;
    }

    /**
     * Returns the current x value of the Entity
     * @return  the current x value of the Entity
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the x value the Entity is facing; if the Entity is facing up or down, returns the x position of the Entity
     * @return  the x value the Entity is looking at
     */
    public int getXInFront()
    {
        int xInFront = x;
        if (direction == 90)
        {
            xInFront++;
        }
        if (direction == 270)
        {
            xInFront--;
        }
        return xInFront;
    }

    /**
     * Sets the y position of the Entity
     * @param   val  the desired y value
     */
    public void setY(int val)
    {
        y = val;
    }

    /**
     * Returns the current y value of the Entity
     * @return  the current y value of the Entity
     */
    public int getY()
    {
        return y;
    }

    /**
     * Returns the y value the Entity is facing; if the Entity is facing left or right, returns the y position of the Entity
     * @return  the y value the Entity is looking at
     */
    public int getYInFront()
    {
        int yInFront = y;
        if (direction == 0)
        {
            yInFront--;
        }
        if (direction == 180)
        {
            yInFront++;
        }
        return yInFront;
    }

    /**
     * Sets the position of the Entity to an x and a y value. If the desired location within the world is occupied
     * by a Tile that cannot be passed through, the Entity does not move
     * @param   newX  the desired x value of the Entity
     * @param   newY  the desired y value of the Entity
     * @param   world the World that the Entity is in
     */
    public void setPosition(int newX, int newY, World world)
    {
        if (newX < world.getWidth() && newX >= 0 && newY < world.getHeight() && newY >= 0)
        {
            if (world.getTileAt(newX, newY).getWalkable())
            {
                x = newX;
                y = newY;
            }
        }
    }

    /**
     * Attacks the input Entity, giving this Entity the amount of experience the other Entity is worth if it dies
     * @param   e  the Entity to attack
     */
    public void attack(Entity e)
    {
        int damage = getAttackDamage();
        e.setHealth(e.getHealth()-damage);
        if (!e.isAlive())
        {
            setXP(getXP() + e.getXPWorth());
        }
    }

    /**
     * Returns the amount of damage the Entity deals, which is a random number between the Entitiy's maximum and minimum damage
     * @return  the amount of damage the Entity deals
     */
    public int getAttackDamage()
    {
        Random rng = new Random();
        return damageMin+rng.nextInt(damageMax-damageMin);
    }

    /**
     * Returns whether or not the Entity is currently alive
     * @return  whether or not the Entity is currently alive
     */
    public boolean isAlive()
    {
        return isAlive;
    }

    /**
     * Sets the image of the Entity that is to be displayed
     * @param   img  ImageIcon to be displayed
     */
    public void setImage(String img)
    {
        image = new ImageIcon(getClass().getResource(img));
    }

    /**
     * Returns the image of this Entity
     * @return  ImageIcon of the Entity
     */
    public ImageIcon getImage()
    {
        return image;
    }

    /**
     * Sets the xp of the Entity to the specified value, and then checks for a level up
     * @param   value  the new xp value
     */
    public void setXP(int value)
    {
        xp = value;
        checkForLevelUp();
    }

    /**
     * Returns the current experience of the Entity
     * @return  the current experience of the Entity
     */
    public int getXP()
    {
        return xp;
    }

    /**
     * Returns the current level of the Entity
     * @return  the current level of the Entity
     */
    public int getLevel()
    {
        return level;
    }

    /**
     * Returns the amount of experience this Entity is worth
     * @return  the amount of experience this Entity is worth
     */
    public int getXPWorth()
    {
        return xpWorth;
    }

    /**
     * Checks to see if the Entity can level up. The amount of xp required is 100*(the Entity's current level)
     */
    private void checkForLevelUp()
    {
        while (xp >= level*100)
        {
            xp -= level*100;
            level++;
            damageMin += 5;
            damageMax += 5;
            maxHealth += 25;
            health += 25;
        }
    }

    /**
     * Moves this Entity towards the specified Entity
     * @param   e      the Entity being moved towards
     * @param   world  the World the Entities inhabit
     */
    public void moveTowardsEntity(Entity e, World world)
    {
        int eX = e.getX();
        int eY = e.getY();
        if (eX < x)
        {
            turnTo(270);
            setPosition(x-1, y, world);
        }
        else if (eY < y)
        {
            turnTo(0);
            setPosition(x, y-1, world);
        }
        else if (eX > x)
        {
            turnTo(90);
            setPosition(x+1, y, world);
        }
        else if (eY > y)
        {
            turnTo(180);
            setPosition(x, y+1, world);
        }
        else
        {
            setPosition(x, y, world);
        }
    }

    /**
     * Returns whether or not this Entity is within radius units of the input Entity
     * @param   radius  the radius in Tiles to check for the Entity
     * @param   e       the Entity the method is checking for
     * @return          whether or not the given Entity is within the radius
     */
    public boolean isInRangeOf(int radius, Entity e)
    {
        boolean inRange = false;
        if (Math.abs(x-e.getX()) <= radius && Math.abs(y-e.getY()) <= radius)
        {
            inRange = true;
        }
        return inRange;
    }

    /**
     * Drops the Entity's Item to drop on death
     * @return  the Entity's drop Item
     */
    public Item dropItem()
    {
        return dropItem;
    }
}