import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class EntityLayer implements Serializable
{
    private ArrayList<Entity> entities;
    private World world;
    private Player player;

    /**
     * Creates a new layer of Entities to be added on top of the World
     * @param   numEntities  the number of Entities in the EntityLayer
     * @param   w            the World that the Entities will occupy, used to get the width and the height constraints of the World
     */
    public EntityLayer(int numEntities, World w)
    {
        entities = new ArrayList<Entity>();
        world = w;
        generate(numEntities, world);
    }

    /**
     * Generates the specified amount of Entities with random positions within the World borders
     * @param   numEntities  the number of Entities to be generated
     * @param   world  the World that the Entities will occupy
     */
    private void generate(int numEntities, World world)
    {
        Random rng = new Random();

        for (int i = 0; i < numEntities; i++)
        {
            int x = rng.nextInt(world.getWidth()-2)+1;
            int y = rng.nextInt(world.getHeight()-2)+1;
            while (!world.getTileAt(x, y).getWalkable() && !checkForEntity(x, y))
            {
                x = rng.nextInt(world.getWidth()-2)+1;
                y = rng.nextInt(world.getHeight()-2)+1;
            }
            Entity e = new Entity(100, 'e', x, y);
            addEntity(e);
        }

        int x = rng.nextInt(world.getWidth()-2)+1;
        int y = rng.nextInt(world.getHeight()-2)+1;
        while (!world.getTileAt(x, y).getWalkable() && !checkForEntity(x, y))
        {
            x = rng.nextInt(world.getWidth()-2)+1;
            y = rng.nextInt(world.getHeight()-2)+1;
        }
        player = new Player(100, x, y);
        addEntity(player);
    }

    /**
     * Adds the entity to the EntityLayer
     * @param   entity  the Entity to be added to the EntityLayer
     */
    public void addEntity(Entity entity)
    {
        entities.add(entity);
    }

    /**
     * Checks for an Entity at the given x and y coordinate
     * @param   x  the x value to check for an Entity at
     * @param   y  the y value to check for an Entity at
     * @return     returns true if there is an Entity at the given coordinate
     */
    public boolean checkForEntity(int x, int y)
    {
        boolean isEntity = false;

        for (int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i); //NULLPOINTER
            if (e.isAlive() && e.getX() == x && e.getY() == y)
            {
                isEntity = true;
            }
        }

        return isEntity;
    }

    /**
     * Returns the Entity at the given x and y coordinate
     * @param   x  the x value of the Entity
     * @param   y  the y value of the Entity
     * @return     the Entity at the given coordinate
     */
    public Entity getEntityAt(int x, int y)
    {
        Entity e = null;

        for (int i = 0; i < entities.size(); i++)
        {
            e = entities.get(i);
            if (e.getX() == x && e.getY() == y)
            {
                break;
            }
        }

        return e;
    }

    /**
     * Removes the input Entity from the EntityLayer
     */
    public void remove(Entity e)
    {
        entities.remove(e);
    }

    /**
     * Returns the Player within the EntityLayer
     * @return  the Player within the EntityLayer
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Used for all non-player Entities, randomly moves the Entity one space in one direction
     * @param   world  the World in which the Entity is located, used to check whether or not the Tile being moved into can be walked through
     */
    private void randomMove(Entity e)
    {
        Random rng = new Random();

        int x = rng.nextInt(2); //Randomly generates a new x coordinate
        int y = rng.nextInt(2); //Randomly generates a new y coordinate

        int whichMove = rng.nextInt(2); //Determines which move to make, x movements or y movements, so the AI cannot move diagonally
        if (whichMove == 1) //Moves along the x-axis
        {
            if (x == 1)
            {
                e.turnTo(90);
                if (!checkForEntity(e.getX()+1, e.getY()))
                {
                    e.setPosition(e.getX()+1, e.getY(), world);
                }
            }
            if (x == 0)
            {
                e.turnTo(270);
                if (!checkForEntity(e.getX()-1, e.getY()))
                {
                    e.setPosition(e.getX()-1, e.getY(), world);
                }
            }
        }
        else //Moves along the y-axis
        {
            if (y == 1)
            {
                e.turnTo(0);
                if (!checkForEntity(e.getX(), e.getY()-1))
                {
                    e.setPosition(e.getX(), e.getY()-1, world);
                }
            }
            if (y == 0)
            {
                e.turnTo(180);
                if (!checkForEntity(e.getX(), e.getY()+1))
                {
                    e.setPosition(e.getX(), e.getY()+1, world);
                }
            }
        }
    }

    /**
     * Moves the Player to the desired x and y value
     * @param   x  the desired x coordinate
     * @param   y  the desired y coordinate
     */
    public void movePlayer(int x, int y)
    {
        if (!checkForEntity(x, y))
        {
            player.setPosition(x, y, world);
        }
    }

    /**
     * Moves one Entity towards another Entity
     * @param   e   the Entity to be moved
     * @param   e2  the Entity to move towards
     */
    public void moveTowardsEntity(Entity e, Entity e2)
    {
        int x = e.getX();
        int y = e.getY();
        int eX = e2.getX();
        int eY = e2.getY();

        boolean hasMoved = false;
        if (eX < x)
        {
            e.turnTo(270);
            if (!checkForEntity(x-1, y))
            {
                e.setPosition(x-1, y, world);
                hasMoved = true;
            }
        }
        if (eY < y && !hasMoved)
        {
            e.turnTo(0);
            if (!checkForEntity(x, y-1))
            {
                e.setPosition(x, y-1, world);
                hasMoved = true;
            }
        }
        if (eX > x && !hasMoved)
        {
            e.turnTo(90);
            if (!checkForEntity(x+1, y))
            {
                e.setPosition(x+1, y, world);
                hasMoved = true;
            }
        }
        if (eY > y && !hasMoved)
        {
            e.turnTo(180);
            if (!checkForEntity(x, y+1))
            {
                e.setPosition(x, y+1, world);
                hasMoved = true;
            }
        }
    }

    /**
     * Returns whether or not one Entity is facing another Entity, used for attacking
     * @param   e   the Entity to check the orientation of
     * @param   e2  the Entity that e is trying to look at
     * @return      whether or not one Entity is facing another Entity
     */
    public boolean isFacingEntity(Entity e, Entity e2)
    {
        boolean isFacing = false;
        int xDif = e.getX()-e2.getX();
        int yDif = e.getY()-e2.getY();

        if ((Math.abs(xDif) == 1 && Math.abs(yDif) == 0) || (Math.abs(xDif) == 0 && Math.abs(yDif) == 1))
        {
            if (xDif == -1 && yDif == 0 && e.getOrientation()== 90)
            {
                isFacing = true;
            }
            if (xDif == 0 && yDif == 1 && e.getOrientation()== 0)
            {
                isFacing = true;
            }
            if (xDif == 1 && yDif == 0 && e.getOrientation()== 270)
            {
                isFacing = true;
            }
            if (xDif == 0 && yDif == -1 && e.getOrientation()== 180)
            {
                isFacing = true;
            }
        }
        return isFacing;
    }

    /**
     * Moves all Entities in the EntityLayer except for the Player
     */
    public void moveEntities()
    {
        for (int i = 0; i < entities.size(); i++)
        {
            Entity e = entities.get(i);
            if (!(e instanceof Player))
            {
                if (!e.isAlive())
                {
                    entities.remove(e);
                }
                else
                {
                    if (e.isInRangeOf(11, player))
                    {
                        if (isFacingEntity(e, player))
                        {
                            e.attack(player);
                        }
                        moveTowardsEntity(e, player);
                    }
                    else
                    {
                        randomMove(e);
                    }
                }
            }
        }
    }
}