package shoppingListGUI;

import listControl.MyLinkedList;
import shoppingList.ShopItem;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1201
 * @since   1.8
 */
public class MyTable extends JTable {

    /**
     * Holds the model of this table.
     */
    private MyTableModel model;

    /**
     * Holds the main GUI.
     */
    private GUInterface gui;

    /**
     * Creates a new JTable from given data and headers.
     *
     * @param data the data to insert into the table.
     * @param headers table headers for the data.
     * @param gui the main GUI
     */
    public MyTable(Object[][] data, Object[] headers, GUInterface gui) {

        super();

        model = new MyTableModel(data, headers);
        setModel(model);
        // Set the amount row to be of certain size.
        getColumnModel().getColumn(0).setPreferredWidth(80);
        getColumnModel().getColumn(0).setMaxWidth(120);

        this.gui = gui;
    }

    /**
     * Adds a new row to the end of the table.
     *
     * @param newItem new data on the list
     */
    public void addRow(Object[] newItem) {

        model.addRow(newItem);
    }

    /**
     * Deletes the last row of the table.
     *
     * @param selectRow the row that is supposed to be deleted
     * @return true if the removal was successful
     */
    public boolean deleteRow(int selectRow) {

        // System.out.println("table: " + model.getRowCount() + " rows");
        // int lastRow = model.getRowCount() - 1;
        model.removeRow(selectRow);
        return true;
    }

    /**
     * Clears the table of data and sets it to starting state.
     */
    public void clearTable() {

        int maxRows = model.getRowCount();

        for (int loop = maxRows - 1; loop >= 0; loop--) {

            model.removeRow(loop);
        }

        Object[] newItem = {0, " "};
        model.addRow(newItem);
    }

    /**
     * Inserts the current list into the table.
     *
     * Used when opening a list from a file.
     *
     * @param list the data to include in the table.
     */
    public void refreshTable(MyLinkedList<ShopItem> list) {

        clearTable();
        model.removeRow(0);
        int rows = list.size();

        for (int loop = 0; loop < rows; loop++) {

            ShopItem itemData = list.get(loop);
            Object[] newItem = {
                itemData.getAmount(),
                itemData.getItem()};
            model.addRow(newItem);
        }
    }

    /**
     * Triggers on table change.
     *
     * @param e the event that is triggered.
     */
    @Override
    public void tableChanged(TableModelEvent e) {

        super.tableChanged(e);

        if (gui != null && model != null) {

            int maxRows = model.getRowCount();
            int maxCols = model.getColumnCount();

            Object[][] newData = 
                new Object[maxRows][maxCols];

            for (int rows = 0; rows < maxRows; rows++) {

                for (int cols = 0; cols < maxCols; cols++) {

                    newData[rows][cols] = model.getValueAt(rows, cols);
                }
            }

            gui.updateCurrentList(newData);
        }
    }
}
