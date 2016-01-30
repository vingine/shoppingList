package shoppingListGUI;

import com.dropbox.core.*;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Desktop;
import java.net.URL;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1204
 * @since   1.8
 */
public class DropboxAuthDialog extends JDialog {

    /**
     * Holds the main GUI.
     */
    private GUInterface gui;

    /**
     * Holds the open/save dialog.
     */
    private JDialog tokenReady;

    /**
     * Holds the instructions text.
     */
    private JTextPane instructions;

    /**
     * Holds a panel for the instructions to be in scrollable area.
     */
    private JScrollPane scrollPane;

    /**
     * Holds a divider line element.
     */
    private JSeparator sep;

    /**
     * Holds the panel to draw textbox for auth code in.
     */
    private JPanel panel;

    /**
     * Holds the field to write in the auth code.
     */
    private JTextField authCode;

    /**
     * Holds a label for the textfield.
     */
    private JLabel authLabel;

    /**
     * Holds ok button.
     */
    private JButton ok;

    /**
     * Holds cancel button.
     */
    private JButton cancel;

    /**
     * Holds browser access button.
     */
    private JButton browser;

    /**
     * Holds the url to the authorization page.
     */
    private String authUrl;

    /**
     * Holds the app key/secret.
     */
    private DbxAppInfo appInfo;

    /**
     * Holds the web authorization service.
     */
    private DbxWebAuthNoRedirect webAuth;

    /**
     * Holds the desktop retrieved from current PC.
     */
    private Desktop desktop;

    /**
     * Creates a dialog for authentication.
     *
     * @param gui the main GUI
     * @param tokenReady the window that is used for saving / loading
     * from/to Dropbox
     */
    public DropboxAuthDialog(GUInterface gui, JDialog tokenReady) {

        super(gui);

        this.gui = gui;
        this.tokenReady = tokenReady;

        // Make the main frame to not work
        gui.setEnabled(false);

        // Setup the dialog
        setTitle("Dropbox");
        setSize(400, 250);
        setResizable(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        authUrl = initAuth();

        // Setup the instructions
        instructions = new JTextPane(new DefaultStyledDocument());
        instructions.setEditable(false);
        instructions.setText("Go to URL: \n" +
        authUrl + "\n" + "and grab your authorization code. \n" +
        "Paste the code into the field below and press OK");
        scrollPane = new JScrollPane(instructions);
        scrollPane.setPreferredSize(new Dimension(360, 85));

        add(scrollPane);

        desktop = Desktop.getDesktop();
        
        if (desktop != null &&
            desktop.isSupported(Desktop.Action.BROWSE)) {

            instructions.setText("Press the button below to get " +
                "your authentication code. \n" +
                "Paste the code into the field below and press OK");
            browser = new JButton("Open authentication page in browser");
            add(browser);
        }

        // Add separator between the instructions and auth field.
        sep = new JSeparator();
        sep.setOrientation(SwingConstants.HORIZONTAL);
        sep.setPreferredSize(new Dimension(380, 10));

        add(sep);

        // Setup the panel
        panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setPreferredSize(new Dimension(360, 100));

        authLabel = new JLabel("Authorization code:");
        panel.add(authLabel);

        authCode = new JTextField(25);
        panel.add(authCode);

        ok = new JButton("OK");
        panel.add(ok);

        cancel = new JButton("Cancel");
        panel.add(cancel);

        add(panel);

        // If the window is closed, re-enable main GUI
        addWindowListener(new WindowAdapter() {
            
            /**
             * Fires if the window is about to close.
             *
             * @param e the window event
             */
            public void windowClosing(WindowEvent e) {
                gui.setEnabled(true);
            }
        });

        setListeners();
        setVisible(true);
    }

    /**
     * Initializes the authentication service.
     *
     * @return the auth url
     */
    public String initAuth() {

        // Initialize authorization url
        String authorizeUrl = "";

        try {

            // Figure out the location of the auth data file
            String workDir = System.getProperties().
                getProperty("user.dir");
            String separator = System.getProperties().
                getProperty("file.separator");
            String appInfoFilePath = workDir + separator + "test.app";
            gui.print(appInfoFilePath);

            // Get app key and secret from a file
            appInfo = DbxAppInfo.Reader.readFromFile(appInfoFilePath);

            // Get own pc locale
            String userLocale = Locale.getDefault().toString();

            // Configure the authentication service
            DbxRequestConfig reqConfig = new DbxRequestConfig(
                "ShoppingListAppNiemiVaino", userLocale);
            webAuth = new DbxWebAuthNoRedirect(
                reqConfig, appInfo);
            // Start the authentication service
            authorizeUrl = webAuth.start();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return authorizeUrl;
    }
    
    /**
     * Finalizes the authentication.
     */
    public void finalizeAuth() {

        // Get the authentication code
        String authStr = authCode.getText().trim();

        DbxAuthFinish authData = null;

        try {

            // Try getting a token.
            authData = webAuth.finish(authStr);
            // gui.print("- User ID: " + authData.userId);
            // gui.print("- Access Token: " + authData.accessToken);
 
            // Save token information to output file.
            DbxAuthInfo authInfo = new DbxAuthInfo(
                    authData.accessToken, appInfo.host);

            Preferences prefs = Preferences.userRoot();
            prefs.put("DbxToken", authInfo.accessToken);
            prefs.sync();
 
            tokenReady.setVisible(true);
        } catch (Exception ex) {

            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sets listeners and actions to buttons.
     */
    public void setListeners() {

        ok.addActionListener(e -> finalizeAuth());

        cancel.addActionListener(e -> {
            gui.setEnabled(true);
            dispose();
        });

        browser.addActionListener(e -> {
            try {
                desktop.browse(new URL(authUrl).toURI());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
    }
}
