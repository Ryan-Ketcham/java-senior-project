import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

public class World implements Serializable
{
    private int width;
    private int height;
    private Tile[][] map;

    /**
     * Creates a new instance of World with the given x and y sizes
     * @param   xSize   int that determines the width, in Tiles, of the World
     * @param   ySize   int that determines the height, in Tiles, of the World
     */
    public World(int xSize, int ySize)
    {
        width = xSize;
        height = ySize;
        map = new Tile[height][width];
        generate();
    }

    /**
     * Randomly generates a new World using the World size. The boundaries are made of Trees, and the rest of the World is randomly composed of Grass or Boulders
     */
    private void generate()
    {
        Random rng = new Random();

        boolean chestSpawned = false;
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                if (w == 0 || w == width-1 || h == 0 || h == height-1)
                {
                    map[h][w] = new Tile('t');
                    if (w == width/2)
                    {
                        map[h][w] = new Gate();
                    }
                }
                else
                {
                    if (rng.nextInt(1000) == 0 && !chestSpawned)
                    {
                        map[h][w] = new Chest();
                        chestSpawned = true;
                    }
                    else if (rng.nextInt(10) == 0 && w != width/2 && h != height/2)
                    {
                        map[h][w] = new Tile('b');
                    }
                    else
                    {
                        map[h][w] = new Tile('g');
                    }
                }
            }
        }
        if (!chestSpawned)
        {
            generate();
        }
    }

    /**
     * Returns the width of the World
     * @return  the width of the World
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Returns the height of the World
     * @return  the height of the World
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * Returns the array of Tiles that make up the World
     * @return  the array of Tiles that make up the World
     */
    public Tile[][] getMap()
    {
        return map;
    }

    /**
     * Returns the Tile at a given x and y coordinate
     * @param   x  the x value of the desired Tile
     * @param   y  the y value of the desired Tile
     * @return     the Tile at the given x and y coordinate
     */
    public Tile getTileAt(int x, int y)
    {
        return map[y][x];
    }

    /**
     * Returns a two-dimensional array of ImageIcons that correspond to the Tiles in the grid
     * @return  A two-dimensional array of ImageIcons
     */
    public ImageIcon[][] getImageArray()
    {
        ImageIcon[][] images = new ImageIcon[height][width];
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                images[h][w] = map[h][w].getImage();
            }
        }
        return images;
    }
}
