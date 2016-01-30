package shoppingListGUI;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1201
 * @since   1.8
 */
public class ButtonRow extends JPanel {

    /**
     * Holds the main GUI.
     */
    private GUInterface gui;

    /**
     * Holds the button for adding a row.
     */
    private JButton addRow;

    /**
     * Holds the button for deleting a row.
     */
    private JButton deleteRow;

    /**
     * Creates the button row to control list actions other than editing.
     *
     * @param gui the main gui
     */
    public ButtonRow(GUInterface gui) {

        super(new FlowLayout(FlowLayout.CENTER));

        this.gui = gui;
        createButtons();
        createListeners();
    }

    /**
     * Creates buttons to this button row.
     */
    public void createButtons() {

        addRow = new JButton("Add row");
        add(addRow);

        deleteRow = new JButton("Delete row");
        add(deleteRow);
    }

    /**
     * Creates action listeners for buttons.
     */
    public void createListeners() {

        addRow.addActionListener(e -> gui.addRow());
        deleteRow.addActionListener(e -> gui.deleteRow());
    }
}
