package shoppingListGUI;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxStreamWriter;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxFiles;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.prefs.Preferences;
import listControl.ListController;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1204
 * @since   1.8
 */
public class DropboxSaveDialog extends JDialog implements ActionListener {

    /**
     * Holds the main GUI.
     */
    private GUInterface gui;

    /**
     * Holds the panel for the dialog.
     */
    private JPanel panel;

    /**
     * Holds the filename field.
     */
    private JTextField fileName;

    /**
     * Holds the filename field label.
     */
    private JLabel fileLabel;

    /**
     * Holds the button for saving.
     */
    private JButton save;

    /**
     * Holds the button for canceling.
     */
    private JButton cancel;

    /**
     * Holds preferences.
     */
    private Preferences prefs;

    /**
     * Opens up a new dropbox save dialog.
     *
     * @param gui the main GUI
     */
    public DropboxSaveDialog(GUInterface gui) {

        super(gui);

        this.gui = gui;
        gui.setEnabled(false);

        // Config the dialog
        setTitle("Save to Dropbox");
        setSize(400, 200);
        setResizable(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        // Config the panel
        panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setPreferredSize(new Dimension(380, 150));

        // Config the textfield label
        fileLabel = new JLabel("File name: ");
        panel.add(fileLabel);

        // Config the textfield
        fileName = new JTextField(25);
        panel.add(fileName);

        // Config buttons
        save = new JButton("Save");
        cancel = new JButton("Cancel");
        save.addActionListener(this);
        cancel.addActionListener(this);
        panel.add(save);
        panel.add(cancel);

        add(panel);

        // If the dialog is closed, return main GUI
        addWindowListener(new WindowAdapter() {

            /**
             * Fires when the window is closing.
             *
             * @param e the event of close
             */
            public void windowClosing(WindowEvent e) {
                gui.setEnabled(true);
            }
        });

        // Try to get preferences
        try {
            
            prefs = Preferences.userRoot();
        } catch (SecurityException e) {

            e.printStackTrace();
        }

        // Check if token is found. If not, retrieve new token
        if (prefs.get("DbxToken", "").equals("")) {

            new DropboxAuthDialog(gui, this);
        } else {

            setVisible(true);
        }
    }

    /**
     * Saves the list to dropbox.
     *
     * @return true if the file was saved
     */
    private boolean saveToDropbox() {

        boolean fileAlreadyExists = false;
        boolean saveOk = true;
        boolean readySave = true;

        String fileNameStr = fileName.getText();
        String locale = Locale.getDefault().toString();

        // Add .sli if needed
        if (!fileNameStr.contains(".sli")) {

            fileNameStr = fileNameStr + ".sli";
        }

        // Set the app, locale and then send token over.
        DbxRequestConfig conf = new DbxRequestConfig(
            "ShoppingListAppNiemiVaino", locale);
        DbxClientV2 client = new DbxClientV2(
            conf, prefs.get("DbxToken", ""));

        // Check if file exists already
        try {

            for (DbxFiles.Metadata meta : 
                client.files.listFolder("").entries){

                if (meta.name.equalsIgnoreCase(fileNameStr)) {

                    fileAlreadyExists = true;
                    break;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            saveOk = false;
        }

        // If file exists, do something
        if (fileAlreadyExists) {

            Object[] options = {"YES", "NO"};

            int selection = 
                JOptionPane.showConfirmDialog(
                this,
                "Overwrite earlier file with same name?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null);

            if (selection == 1) {

                saveOk = false;
                readySave = false;
            }
        }

        // Save the file
        if (readySave) {

            // First, create temporary shopping list file
            String tempName = "" + Math.round(Math.random() * 100000) + 
                "tempList.sli";
            Path tempFile = Paths.get(gui.tempFileOutput + tempName);
            String listData = ListController.listToTxt(
                gui.getCurrentList());

            gui.print(listData);
            gui.print(tempFile);

            // Write the list to the temp file and upload it.
            try {

                PrintWriter out = new PrintWriter(tempFile.toString());
                out.write(listData);
                out.close();
                DbxStreamWriter.InputStreamCopier inDrop =
                    new DbxStreamWriter.InputStreamCopier(
                        Files.newInputStream(tempFile));

                DbxFiles.UploadBuilder up = 
                    client.files.uploadBuilder("/" + fileNameStr);

                up.run(inDrop);
            } catch (Exception e) {

                e.printStackTrace();
                saveOk = false;
            }

            // Delete the temp file
            try {

                // BUG File deletion happens after app close.(?)
                gui.print("deletion");
                Files.delete(tempFile);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return saveOk;
    }

    /**
     * Makes actions based on the button pressed.
     *
     * @param e the event that has happened in the Dialog
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Object test = e.getSource();
        boolean saveOk = true;

        if (test == save || test == cancel) {

            if (test == save) {

                saveOk = saveToDropbox();
            }

            if (saveOk) {

                gui.setEnabled(true);
                dispose();
            }
        }
    }
}
