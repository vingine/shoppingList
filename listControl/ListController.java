package listControl;

import shoppingList.ShopItem;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1203
 * @since   1.8
 */
public class ListController {

    /**
     * Creates a controller for a certain list.
     *
     * This controller is given to another thread, and that thread
     * can use this controller to modify the list. Controller holds
     * a few checks and methods to allow multi-threading and importing/
     * exporting the list to/from a file.
     */
    public ListController() {

    }

    /**
     * Appends an item into the end of the list.
     * 
     * @param newItem the item to append into this list.
     * @param list the list to add the new item to
     */
    public static synchronized < T extends ShopItem > void addItem(T newItem,
        MyList<T> list) {

        list.add(newItem);

        // System.out.println("ListCtrl: " + newItem);
    }

    /**
     * Combines two lists of the same type.
     *
     * Checks through the items inside the list and then if there
     * are same items it combines their amounts.
     *
     * @param exhaust list that is combined to the other
     * @param total list to combine with
     */
    public static synchronized void combine(MyLinkedList<ShopItem> exhaust, 
        MyLinkedList<ShopItem> total) {   
    
        for (ShopItem item : exhaust) {

            boolean combined = false;

            Inner:

            for (ShopItem item2 : total) {

                if (item.getItem().equalsIgnoreCase(item2.getItem())) {

                    item2.setAmount(item2.getAmount() + item.getAmount());
                    combined = true;
                    break Inner;
                }
            }

            if (!combined) {

                addItem(item, total);
            }
        }
    }

    /**
     * Converts the list into a text file.
     *
     * Text file can be opened then in the GUI.
     *
     * @param list the list to convert.
     * @return the converted string
     */
    public static synchronized String listToTxt(MyLinkedList<ShopItem> list) {

        String returnString = "";

        for (ShopItem item : list) {

            returnString = returnString + item.getAmount() + " " +
                item.getItem() + ";";
        }

        return returnString;
    }

    /**
     * Converts a string into a shopping list.
     *
     * .sli file can be edited then in the GUI.
     *
     * @param str the string to convert.
     * @return the converted list
     */
    public static synchronized MyLinkedList<ShopItem> txtToList(String str) {

        MyLinkedList<ShopItem> returnList = new MyLinkedList<>();

        String[] rows = str.split(";");

        for (int loop = 0; loop < rows.length; loop++) {

            String[] itemData = rows[loop].split(" ");
            ShopItem newItem = new ShopItem(
                Integer.parseInt(itemData[0]),
                itemData[1]);
            returnList.add(newItem);
        }

        return returnList;
    }
}
