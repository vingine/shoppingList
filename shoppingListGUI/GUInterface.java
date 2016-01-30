package shoppingListGUI;

import listControl.ListController;
import listControl.MyLinkedList;
import shoppingList.ShopItem;
import exceptions.NotANumberException;
import exceptions.NotProperInputException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.Charset;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1204
 * @since   1.8
 */
public class GUInterface extends JFrame implements Runnable {

    /**
     * Controls actions done before closing the GUI.
     */
    private boolean closeApp;

    /**
     * Controls when the GUI closes.
     */
    private boolean shutDown;

    /**
     * If the current list is edited, trigger to true.
     */
    private boolean listEdited;
    
    /**
     * Holds the current list under editing.
     */
    private MyLinkedList<ShopItem> currentList;

    /**
     * Holds the table for data.
     */
    private MyTable table;

    /**
     * Holds the scroll panel for the table.
     */
    private JScrollPane pane;

    /**
     * Holds the save/load dialog JFileChooser.
     */
    private JFileChooser fileDialog;

    /**
     * Holds the dropbox token location.
     */
    public String argAuthFileOutput;

    /**
     * Holds the temp files location.
     */
    public String tempFileOutput;

    /**
     * Builds the graphical user interface.
     *
     * GUI is larger in it's features than the CLI counterpart.
     * In addition to creating lists, user can save or load lists
     * to/from files.
     */
    public GUInterface() {

        super("ShoppingListApp Niemi Vaino");
        closeApp = false;
        shutDown = false;

        setSize(600, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setJMenuBar(new MyMenuBar(this));
        addWindowListener(createExitListener());

        currentList = new MyLinkedList<>();
        ListController.addItem(new ShopItem(0, ""), currentList);

        // Create initial table
        Object[][] data = iterateListData();
        Object[] headers = {"Amount", "Item"};
        table = new MyTable(data, headers, this);
        pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);

        // Create bottom buttons
        ButtonRow buttons = new ButtonRow(this);
        add(buttons, BorderLayout.SOUTH);

        setVisible(true);

        setupFileChooser();

        String workDir = System.getProperties().
            getProperty("user.dir");
        String homeDir = System.getProperties().
            getProperty("user.home");
        String separator = System.getProperties().
            getProperty("file.separator");
        argAuthFileOutput = workDir + separator + "token.app";
        tempFileOutput = homeDir + separator;
    }

    /**
     * Setups the file choose.
     */
    public void setupFileChooser() {

        fileDialog = new JFileChooser();
        fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileDialog.setDialogTitle("Choose file/location");
        fileDialog.removeChoosableFileFilter(fileDialog.getFileFilter());
        FileNameExtensionFilter sliFilter = new FileNameExtensionFilter(
        ".sli - Shopping List", "sli");
        fileDialog.addChoosableFileFilter(sliFilter); 
    }

    /**
     * Goes through current list and creates array of the data.
     *
     * Is used to create a JTable.
     *
     * @return an 2D array of data from the list
     */
    public Object[][] iterateListData() {

        Object[][] newData = null;

        if (currentList != null) {

            int size = currentList.size();
            newData = new Object[size][2];

            if (size > 0) {

                for (int loop = 0; loop < size; loop++) {

                    ShopItem item = (ShopItem) currentList.get(loop);
                    newData[loop][0] = item.getAmount();
                    newData[loop][1] = item.getItem();
                }
            }
        }

        return newData;
    }

    /**
     * Adds a new row to the table and new data to the shopping list.
     */
    public void addRow() {

        ShopItem newItem = new ShopItem(0, "");
        Object[] itemData = {newItem.getAmount(), newItem.getItem()};
        table.addRow(itemData);
        ListController.addItem(newItem, currentList);
        // System.out.println(currentList.size());
        // System.out.println("gui: add row");
    }

    /**
     * Deletes the selected row from the table and the item from the list.
     */
    public void deleteRow() {

        // System.out.println("gui: delete row");
        int selectIndex = table.getSelectedRow();

        // If row isn't selected, selectIndex is -1
        if (selectIndex != -1 && table.deleteRow(selectIndex)) {

            currentList.remove(selectIndex);
        }
    }

    /**
     * Updates the current list with new data.
     *
     * @param data new data
     */
    public void updateCurrentList(Object[][] data) {

        // print("got data");
        // print(data);
        MyLinkedList<ShopItem> newList = new MyLinkedList<>();

        for (int loop = 0; loop < data.length; loop++) {

            ShopItem newItem = 
                new ShopItem((Integer)data[loop][0], (String)data[loop][1]);
            newList.add(newItem);
        }

        currentList = newList;
        // print(currentList);
    }

    /**
     * Runs when a thread is created from this class.
     */
    public void run() {

        while (!shutDown) {

            // TODO: If user wants to close app, do something
            // TODO: If a list is open and not saved, prompt user
            if (closeApp) {

                shutDown = true;
            }
        }

        // Imitate the effect of pressing the X of the window.
        dispatchEvent(
            new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Refreshes the table that shows the list with new list.
     *
     * Used from dropbox open dialog.
     *
     * @param newList the new data to be inserted into the table.
     */
    public void refreshTable(MyLinkedList<ShopItem> newList) {

        table.refreshTable(newList);
    }

    /**
     * Creates a listener for exitting the program.
     *
     * User can now choose to save the list before quitting.
     *
     * @return the created listener
     */
    public WindowListener createExitListener() {

        WindowListener quitListener = new WindowAdapter() {

            /**
             * Fires off when the window is closed.
             *
             * @param e the window event.
             */
            @Override
            public void windowClosing(WindowEvent e) {

                // Ask user if they want to save before quitting
                Object[] options = {"Save & Quit", "Just Quit", "Cancel"};

                int selection = JOptionPane.showOptionDialog(
                    null,
                    "Save the list before quitting?",
                    "Quitting...",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

                // Check what user selected
                if (selection == 0) {

                    // Get save data ready and then handle the chooser
                    String saveData = ListController.listToTxt(currentList);
                    // print(saveData);
                    int status = fileDialog.showSaveDialog(GUInterface.this);

                    // If the user confirms the selections inside dialog, save
                    if (status == JFileChooser.APPROVE_OPTION) {

                        // print("save");
                        String path = fileDialog.
                            getSelectedFile().getAbsolutePath();

                        if (!path.matches("[\\.sli]$")) {

                            path = path + ".sli";
                        }

                        // print(path + ".sli");

                        try (PrintWriter pw = new PrintWriter(
                            new File(path))) {
                            
                            pw.write(saveData);
                        } catch (IOException exc) {

                            print("Cant save");
                        }

                    // If user cancels the dialog
                    } else if (status == JFileChooser.CANCEL_OPTION) {

                        // print("cancel");

                        // Ask if user really wants to quit without saving
                        Object[] reallyOpt = {"Yes", "No"};

                        int reallySel = JOptionPane.showOptionDialog(
                            null,
                            "Really Quit without saving?",
                            "Warning",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            reallyOpt,
                            reallyOpt[0]);

                        if (reallySel == 0) {

                            // If user confirms quitting without saving
                            System.exit(0);
                        }
                    } else {

                        // Error, (do something and) don't quit
                        print("error");
                    }
                } else if (selection == 1) {

                    System.exit(0);
                }
            }
        };

        return quitListener;
    }

    /**
     * Saves file to chosen location.
     *
     * Used for the non-quitting versions of save.
     *
     * @param openNew if this prompt is launched by opening a new list,
     * ask user if user wants to save current list.
     */
    public void saveFile(boolean openNew) {

        // If user doesn't want to save when opening new list,
        // set this to false.
        boolean save = true;

        if (openNew) {

            Object[] options = {"Yes", "No"};

            int selection = JOptionPane.showOptionDialog(
                null,
                "Save your current list first?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

            if (selection == 1) {

                save = false;
            }
        }

        if (save) {

            // Get save data ready and then handle the chooser
            String saveData = ListController.listToTxt(currentList);

            // print(saveData);
            int status = fileDialog.showSaveDialog(this);

            // If the user confirms the selections inside dialog, save
            if (status == JFileChooser.APPROVE_OPTION) {

                // print("save");
                String path = fileDialog.
                    getSelectedFile().getAbsolutePath();

                print(path);

                if (!path.contains(".sli")) {

                    path = path + ".sli";
                }

                try (PrintWriter pw = new PrintWriter(
                    new File(path))) {
                    
                    pw.write(saveData);
                } catch (IOException exc) {

                    print("Cant save");
                }

                // If user cancels the dialog, do nothing
                // If error happens, do nothing
            }
        }

        if (openNew) {

            table.clearTable();
        }
    }

    /**
     * Opens file from chosen location.
     */
    public void loadFile() {

        // If user doesn't want to save when opening new list,
        // set this to false.
        boolean save = true;

        Object[] options = {"Yes", "No"};

        int selection = JOptionPane.showOptionDialog(
            null,
            "Save your current list first?",
            "Warning",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (selection == 1) {

            save = false;
        }

        if (save) {

            // Get save data ready and then handle the chooser
            String saveData = ListController.listToTxt(currentList);

            // print(saveData);
            int status = fileDialog.showSaveDialog(this);

            // If the user confirms the selections inside dialog, save
            if (status == JFileChooser.APPROVE_OPTION) {

                // print("save");
                String path = fileDialog.
                    getSelectedFile().getAbsolutePath();

                print(path);

                if (!path.contains(".sli")) {

                    path = path + ".sli";
                }

                try (PrintWriter pw = new PrintWriter(
                    new File(path))) {
                    
                    pw.write(saveData);
                } catch (IOException exc) {

                    print("Cant save");
                }

                // If user cancels the dialog, do nothing
                // If error happens, do nothing
            }
        }

        int loadStatus = fileDialog.showOpenDialog(this);

        if (loadStatus == JFileChooser.APPROVE_OPTION) {

            String path = fileDialog.
                getSelectedFile().getAbsolutePath();

            Path openPath = Paths.get(path, "");
            String data = "";

            try (BufferedReader reader = 
                Files.newBufferedReader(
                    openPath,
                    Charset.defaultCharset())) {

                data = reader.readLine();
            } catch (IOException e) {

                print("fail opening");
            }

            currentList = ListController.txtToList(data);
            table.refreshTable(currentList);
        }
    }

    /**
     * Sets the GUI's closeApp control.
     *
     * @param closeApp the new state of the closeApp
     */
    public void setCloseApp(boolean closeApp) {

        this.closeApp = closeApp;
    }

    /**
     * Shortens System.out.println() command for developing.
     *
     * @param line the object to be printed on the console.
     */
    public void print(Object line) {

        System.out.println(line);
    }

    /**
     * Returns the current list.
     *
     * @return the current list
     */
    public MyLinkedList<ShopItem> getCurrentList() {

        return currentList;
    }
}
