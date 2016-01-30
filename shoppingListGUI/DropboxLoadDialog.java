package shoppingListGUI;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxAuthInfo;
import com.dropbox.core.DbxStreamReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxDownloadStyleBuilder;
import com.dropbox.core.v2.DbxFiles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.prefs.Preferences;
import listControl.ListController;
import listControl.MyLinkedList;
import shoppingList.ShopItem;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1204
 * @since   1.8
 */
public class DropboxLoadDialog extends JDialog implements ActionListener {

    /**
     * Holds the main GUI.
     */
    private GUInterface gui;

    /** 
     * Holds the panel in the the Dialog.
     */
    private JPanel panel;

    /**
     * Holds the list that shows files inside dropbox app folder.
     */
    private JList<String> files;

    /**
     * Holds the label for the list.
     */
    private JLabel listLabel;

    /**
     * Holds the button for opening selected list.
     */
    private JButton open;

    /**
     * Holds the button for canceling the dialog.
     */
    private JButton cancel;

    /**
     * Holds the Preferences location of the user.
     */
    private Preferences prefs;

    /**
     * Holds the app config info.
     */
    private DbxRequestConfig conf;

    /**
     * Creates a new prompt for opening a file from Dropbox.
     *
     * @param gui the main GUI
     */
    public DropboxLoadDialog(GUInterface gui) {

        super(gui);
        setTitle("Loading...");
        this.gui = gui;

        // Disable main GUI
        gui.setEnabled(false);

        // Get the preferences from the user for token.
        try {
            
            prefs = Preferences.userRoot();
        } catch (SecurityException e) {

            e.printStackTrace();
        }

        // Config this dialog
        setSize(400, 350);
        setResizable(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Config the panel
        panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.setPreferredSize(new Dimension(360, 50));

        // Config the label
        listLabel = new JLabel("Files: ");
        add(listLabel);

        // Config the list
        files = new JList<>();
        files.setVisibleRowCount(-1);
        JScrollPane filesPane = new JScrollPane(files);
        filesPane.setPreferredSize(new Dimension(360, 220));
        add(filesPane);

        // Config buttons
        open = new JButton("Open");
        cancel = new JButton("Cancel");
        open.addActionListener(this);
        cancel.addActionListener(this);
        panel.add(open);
        panel.add(cancel);

        add(panel);

        // Setup app config info
        String locale = Locale.getDefault().toString();
        conf = new DbxRequestConfig(
            "ShoppingListAppNiemiVaino", locale);

        // If the dialog is closed, return main GUI
        addWindowListener(new WindowAdapter() {

            /**
             * Fires when the window is closed.
             *
             * @param e the window event
             */
            public void windowClosing(WindowEvent e) {
                gui.setEnabled(true);
            }
        });

        // Check if token is found. If not, retrieve new token
        if (prefs.get("DbxToken", "").equals("")) {

            new DropboxAuthDialog(gui, this);
        } else {

            setVisible(true);
            loadList();
        }
    }

    /**
     * Loads the list of files inside app folder in Dropbox.
     */
    public void loadList() {

        // Try accessing the app with the token in prefs.
        DbxClientV2 client = new DbxClientV2(
            conf, prefs.get("DbxToken", ""));

        // Retrieve files data from app folder
        DbxFiles boxFiles = client.files;

        try {

            // Get metadata from the files data and use it to create
            // list of file names.
            String[] boxFileList =
                new String[boxFiles.listFolder("").entries.size()];
            int listIndex = 0;

            for (DbxFiles.Metadata meta : boxFiles.listFolder("").entries) {

                if (meta.name.contains(".sli")) {

                    boxFileList[listIndex++] = meta.name;
                }
            }

            files.setListData(boxFileList);
            setTitle("Open file from Dropbox");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * Opens the selected file from the dropbox file list.
     */
    public void openFile() {

        MyLinkedList<ShopItem> newList = new MyLinkedList<>();
        String value = files.getSelectedValue();

        // Check if user has selected a file.
        if (value != null) {

            // Prepare for download
            String selectFile = value;

            DbxClientV2 client = new DbxClientV2(
                conf, prefs.get("DbxToken", ""));

            DbxDownloadStyleBuilder download =
                client.files.downloadBuilder("/" + selectFile);

            // Download the file
            try {

                DbxStreamReader.OutputStreamCopier streamIn =
                    new DbxStreamReader.OutputStreamCopier(
                        Files.newOutputStream(
                            Paths.get(gui.tempFileOutput + selectFile)));

                download.start();
                download.run(streamIn);

                String path = gui.tempFileOutput + selectFile;

                Path openPath = Paths.get(path);
                String data = "";

                try (BufferedReader reader = 
                    Files.newBufferedReader(
                        openPath,
                        Charset.defaultCharset())) {

                    data = reader.readLine();
                } catch (IOException e) {

                    gui.print("fail opening");
                }

                newList = ListController.txtToList(data);
            } catch (Throwable e) {

                e.printStackTrace();
            }
        }
    
        gui.refreshTable(newList);
    }

    /**
     * Fires when one of the buttons is pressed on the screen.
     *
     * @param e the event that occured
     */
    public void actionPerformed(ActionEvent e) {

        Object target = e.getSource();

        if (target == open || target == cancel) {

            if (target == open) {

                openFile();
            }

            gui.setEnabled(true);
            dispose();
        }
    }
}
