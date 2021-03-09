public class Potion extends Item
{
    private int amountRestored;

    /**
     * Sets the amount of health or mana the potion restores and sets the Image to a potion
     * @param  amount  the amount of health or mana the potion restores
     */
    public Potion(int amount)
    {
        amountRestored = amount;
        setImage("Images/Potion.png");
    }

    /**
     * Returns the amount of health or mana this potion restores
     * @return  int amount of health or mana this potion restores
     */
    public int getAmount()
    {
        return amountRestored;
    }
}