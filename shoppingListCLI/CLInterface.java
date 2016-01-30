package shoppingListCLI;

import listControl.ListController;
import listControl.MyLinkedList;
import shoppingList.ShopItem;
import exceptions.NotANumberException;
import exceptions.NotProperInputException;
import java.util.Scanner;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1130
 * @since   1.8
 */
public class CLInterface extends Thread {

    /**
     * Used as a delimiter character for input.
     */
    private final String delimiter = ";";

    /**
     * Used as a regex for removing extra spaces from input.
     */
    private final String replaceStr = "\\s+";

    /**
     * Used for controlling the application.
     */
    private boolean appClose;

    /**
     * Holds the main list.
     */
    private MyLinkedList<ShopItem> listItems;

    /**
     * Holds the scanner.
     */
    Scanner sc;

    /**
     * Creates a new CLInterface.
     *
     * User will control this interface from command line.
     * User can add items to pre-existing shopping list.
     * User can also quit the application from command line.
     */
    public CLInterface() {

        listItems = new MyLinkedList<ShopItem>();
        appClose = false;
        sc = new Scanner(System.in);
    }

    /**
     * Progresses through user input and parse.
     *
     * Thread will call this automatically when a thread is created from this
     * class.
     */
    public void run() {

        System.out.println("\nSHOPPING LIST");
        System.out.println("Tampere University of Applied Sciences \n");

        while (!appClose) {

            System.out.println(
            "Give shopping list (example: 1 milk;2 tomato;3 carrot;)");

            checkInput();

            if (!appClose) {

                printCurrentList();                
            }
        }
    }

    /**
     * Asks user for input.
     *
     * Also checks what the input is and if it is valid, does parsing of
     * the input and action.
     */
    public synchronized void checkInput() {

        if (sc.hasNext()) {

            String str = sc.nextLine();

            if (!str.equalsIgnoreCase("exit")) {

                try {

                    parseInput(str);
                } catch (NotProperInputException | NotANumberException e) {

                    System.out.println(e.getMessage());
                }
            } else {

                appClose = true;
            }
        }
    }

    /**
     * Parses the input and if it is correct, does action.
     *
     * Else the application will inform the user of correct syntax.
     * If the input is incorrect nothing is done for the shopping list.
     *
     * @param input the input to be parsed
     * @throws NotANumberException if the amount of the item cannot be parsed
     * @throws NotProperInputException if the input isnt in proper syntax
     */
    public void parseInput(String input) 
        throws NotANumberException, NotProperInputException {

        // Split the input from delimiter characters
        String[] inputs = input.split(delimiter);

        // A shopping list for storing the new items
        MyLinkedList<ShopItem> newItems = new MyLinkedList<>();

        // Parses the table of inputs into item-amount pairs
        for (String str : inputs) {

            // System.out.println(str);

            // Trim out all the whitespace from the start and end of the string
            // and also replace all extra whitespace in the middle with one
            // space character, if applicable.
            String trimmed = str.trim().replaceAll(replaceStr, " ");
            String[] partial = trimmed.split(" ");

            // THROWS AN EXCEPTION if the parameters aren't pairs
            if (partial.length % 2 != 0) {

                throw new NotProperInputException("Check your input!");
            }

            ShopItem newItem;
            
            // THROWS AN EXCEPTION if the parameter pair isn't the right type
            // <amount integer> <item string>
            try {

                newItem = new ShopItem(
                    Integer.parseInt(partial[0]), 
                    partial[1]);
            } catch (NumberFormatException e) {

                throw new NotANumberException("Amount is not number!");
            }

            ListController.addItem(newItem, newItems);
        }

        /*// Test printing out results
        for (ShopItem item : newItems) {

            System.out.println(item);
        }*/

        // Finally combines the new list with the old list.
        ListController.combine(newItems, listItems);
    }

    /**
     * Prints the current list and a title for the list.
     */
    public void printCurrentList() {

        System.out.println("Your Shopping List now:");

        for (ShopItem item : listItems) {

            System.out.println(item);
        }

        System.out.println();
    }
}
