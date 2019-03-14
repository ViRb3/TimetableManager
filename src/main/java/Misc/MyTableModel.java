package Misc;

import javax.swing.table.DefaultTableModel;


/**
 * Custom table with non-editable cells
 */
public class MyTableModel extends DefaultTableModel {
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
