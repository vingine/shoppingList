import listControl.MyList;
import listControl.MyLinkedList;
import listControl.ListController;
import shoppingList.ShopItem;
import shoppingListCLI.CLInterface;
import shoppingListGUI.GUInterface;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1203
 * @since   1.8
 */
public class ShoppingListApp {

    /**
     * Starts up and handles the shopping list application.
     *
     * This main thread is used for handling the list-based
     * actions. For the CLI or GUI, a separate thread is started.
     *
     * @param args Command line parameters, not used.
     */
    public static void main(String[] args) {

        CLInterface cli = new CLInterface();
        cli.start();

        GUInterface gui = new GUInterface();
        new Thread(gui).start();
    }
}
