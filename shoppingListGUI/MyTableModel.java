package shoppingListGUI;

import javax.swing.table.DefaultTableModel;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1202
 * @since   1.8
 */
public class MyTableModel extends DefaultTableModel {

    /**
     * Holds the data of this table.
     */
    private Object[][] data;

    /**
     * Holds the headers for the table.
     */
    private Object[] headers;

    /**
     * Creates a new tablemodel based on the Default model.
     *
     * @param data data for the table to be shown
     * @param headers headers for the table data
     */
    public MyTableModel(Object[][] data, Object[] headers) {

        super(data, headers);
        this.data = data;
        this.headers = headers;
    }

    /**
     * Sets the object value for the cell at column and row.
     * 
     * aValue is the new value. This method will generate a tableChanged 
     * notification.
     *
     * @param aValue new value for the cell
     * @param row the row count from where to change value
     * @param column the column count from where to change value
     */
    @Override
    public void setValueAt(Object aValue, int row, int column) {

        Object newData = null;

        if (column == 0) {

            try {

                newData = Integer.parseInt((String)aValue);
            } catch (NumberFormatException e) {

                newData = 0;
            }
        } else {

            newData = (String)aValue;
        }

        super.setValueAt(newData, row, column);
    }
}
