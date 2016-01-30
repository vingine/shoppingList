package shoppingList;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1127
 * @since   1.8
 */
public class ShopItem {

    /**
     * Holds the item of this pair.
     */
    private String item;

    /**
     * Holds the amount of the item.
     */
    private int amount;

    /**
     * Creates a new pair of item and amount.
     *
     * Amount is used for calculations,
     * mostly for combining two of the same item (or 
     * subtracting from an amount of a ShopItem).
     *
     * @param amount the amount of the item.
     * @param item a item of any type.
     */
    public ShopItem(int amount, String item) {

        this.amount = amount;
        this.item = item;
    }

    /**
     * Sets the item of this pair.
     *
     * @param item the new item
     */
    public void setItem(String item) {

        this.item = item;
    }

    /**
     * Sets the amount of the item.
     *
     * @param amount the new amount
     */
    public void setAmount(int amount) {

        this.amount = amount;
    }

    /**
     * Returns the item.
     *
     * @return the item
     */
    public String getItem() {

        return item;
    }

    /**
     * Returns the amount of the item.
     *
     * @return the amount of the item.
     */
    public int getAmount() {

        return amount;
    }

    /**
     * Returns the item name and amount as a String.
     *
     * Overrides the superclass method.
     *
     * @return the data of this item.
     */
    @Override
    public String toString() {

        String str = " " + amount + " " + item;
        return str;
    }
}
