package shoppingListGUI;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1204
 * @since   1.8
 */
public class MyMenuBar extends JMenuBar {

    /**
     * References the main gui object.
     */
    private GUInterface gui;

    /**
     * Hold the new list button.
     */
    private JMenuItem newList;

    /**
     * Hold the save list button.
     */
    private JMenuItem saveList;

    /**
     * Hold the save to dropbox button.
     */
    private JMenuItem dropboxSave;

    /**
     * Hold the load from dropbox button.
     */
    private JMenuItem dropboxLoad;

    /**
     * Hold the load button.
     */
    private JMenuItem loadList;

    /**
     * Hold the exit button.
     */
    private JMenuItem exitButton;

    /**
     * Buils a new menu bar and the buttons in it.
     *
     * With the menu items you can control the GUI.
     *
     * @param gui the main object of the gui.
     */
    public MyMenuBar(GUInterface gui) {

        super();

        this.gui = gui;

        createFileMenu();
        createListeners();
    }

    /**
     * Creates the file menu and the buttons in it.
     *
     * Few of the buttons have neumonics and shortcuts user can use.
     */
    public void createFileMenu() {

        // Create file menu
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.getAccessibleContext().setAccessibleDescription(
        "Menu for loading or saving a shop list or quitting the app.");
        add(file);

        // ------
        // Add items to the menus
        // ------

        // Get working dir -> res -> icons
        String path = System.getProperties().getProperty("user.dir");
        String sep = System.getProperties().getProperty("file.separator");
        String icons = path + sep + "res" + sep + "icons" + sep;

        // New - button
        newList = new JMenuItem("New", KeyEvent.VK_N);
        newList.setIcon(new ImageIcon(icons + "New16.gif"));
        newList.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newList.getAccessibleContext().setAccessibleDescription(
        "Starts a blank shopping list.");
        file.add(newList);

        file.addSeparator();

        // Save to PC - button
        saveList = new JMenuItem("Save to PC...", KeyEvent.VK_S);
        saveList.setIcon(new ImageIcon(icons + "SaveAs16.gif"));
        saveList.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveList.getAccessibleContext().setAccessibleDescription(
        "Saves the current list to a location on PC...");
        file.add(saveList);

        // Load - button
        loadList = new JMenuItem("Open list from PC...",
            KeyEvent.VK_O);
        loadList.setIcon(new ImageIcon(icons + "Open16.gif"));
        loadList.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        loadList.getAccessibleContext().setAccessibleDescription(
        "Loads a list from a location...");
        file.add(loadList);

        file.addSeparator();

        // Save to Dropbox - button
        dropboxSave = new JMenuItem("Save to Dropbox...");
        dropboxSave.setIcon(new ImageIcon(icons + "SaveAs16.gif"));
        dropboxSave.getAccessibleContext().setAccessibleDescription(
        "Saves the current list to dropbox...");
        file.add(dropboxSave);

        // Load from Dropbox - button
        dropboxLoad = new JMenuItem("Open list from Dropbox...");
        dropboxLoad.setIcon(new ImageIcon(icons + "Open16.gif"));
        dropboxLoad.getAccessibleContext().setAccessibleDescription(
        "Loads a list from a location...");
        file.add(dropboxLoad);

        file.addSeparator();

        // Exit - button
        exitButton = new JMenuItem("Close", KeyEvent.VK_X);
        exitButton.setIcon(new ImageIcon(icons + "Stop16.gif"));
        exitButton.setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
        exitButton.getAccessibleContext().setAccessibleDescription(
        "Exits the application.");
        file.add(exitButton);
    }

    /**
     * Creates listeners for the button presses.
     */
    public void createListeners() {

        exitButton.addActionListener(e -> gui.dispatchEvent(
                    new WindowEvent(gui, WindowEvent.WINDOW_CLOSING)));

        saveList.addActionListener(e -> gui.saveFile(false));

        newList.addActionListener(e -> gui.saveFile(true));

        loadList.addActionListener(e -> gui.loadFile());

        dropboxLoad.addActionListener(e -> new DropboxLoadDialog(
            gui));

        dropboxSave.addActionListener(e -> new DropboxSaveDialog(
            gui));
    }
}
