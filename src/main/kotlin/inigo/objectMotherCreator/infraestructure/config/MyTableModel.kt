package inigo.objectMotherCreator.infraestructure.config

import javax.swing.table.AbstractTableModel
//https://www.tutorialspoint.com/how-to-add-a-new-row-to-jtable-with-insertrow-in-java-swing
internal class MyTableModel : AbstractTableModel() {
    private val columnNames: Array<String> = arrayOf()
    private val data: Array<Array<String>> = arrayOf()
    override fun getColumnCount(): Int {
        return columnNames.size
    }

    override fun getRowCount(): Int {
        return data.size
    }

    override fun getColumnName(col: Int): String {
        return columnNames[col]
    }

    override fun getValueAt(row: Int, col: Int): Any {
        return data[row][col]
    }

    override fun getColumnClass(c: Int): Class<*> {
        return getValueAt(0, c).javaClass
    }

    override fun isCellEditable(row: Int, col: Int) = true

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    override fun setValueAt(value: Any, row: Int, col: Int) {
        data[row][col] = value as String
        fireTableCellUpdated(row, col)
    }
}
