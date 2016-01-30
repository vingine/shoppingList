README

    File       : README.txt
    Time-stamp : 2016-01-30T14:30 Vaino Niemi
    Description: General information of the project.

GENERAL INFO

    Product name     : Shopping list application
    Developer        : Vaino Niemi, vaino.a.niemi@gmail.com
    Device target(s) : PC
    Price            : free

DESCRIPTION OF THE PRODUCT

    A shopping list application for the PC. User can add items to a shopping
    list via Command Line Interface(CLI) or a Graphical User Interface (GUI).

    User can create a list. Then user can add items to the list using both
    of the aforementioned interfaces. If the item is already on the list,
    a new value is calculated from the earlier value and input value. For eg.
    inputting "3 tomato" into the list while it already has "2 tomato" results
    in the list having a total of "5 tomato".

    User can print out the results to the CLI, or if the user wants he/she can
    save the list into a .txt file. Another possibility later on is to save the
    file into own dropbox account using Dropbox Java Core API. The user can also
    generate the list using CLI options while launching the app.

    The GUI can be used to do the aforementioned things aswell. The GUI shows
    list items in a JTable, and the table can be modified through clicking the
    approppriate parts of the GUI. GUI also has dropdown menu for saving and
    loading a list to/from a file.

FILES

    shoppingList/
    |
    +-- README.txt              // This file
    |
    +-- dropbox.jar             // .jar libraries used by the app
    +-- jackson.jar
    |
    +-- runShop.bat             // Use this from windows cmd to compile and launch the app
    |
    +-- ShoppingListApp.jar     // You can launch the app with this.
    |
    +-- doc/                    // Javadoc documentation
    |
    +-- res/                    // Icons used by the app
    |
    +-- // Rest of the folders include code files
    +-- // Rest of single files are just used by the app

End of file.\n
