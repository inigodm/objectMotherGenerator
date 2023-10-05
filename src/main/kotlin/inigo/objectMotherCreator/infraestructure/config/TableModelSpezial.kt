package inigo.objectMotherCreator.infraestructure.config

import java.util.*
import javax.swing.table.DefaultTableModel

class TableModelSpezial(data: Vector<Vector<String>>, columnNames: Vector<String>) : DefaultTableModel(data, columnNames) {
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun getValueAt(row: Int, column: Int): Any {
        return super.getValueAt(row, column)
    }
    override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
        println(aValue)
        println(rowIndex)
        println(columnIndex)
        (dataVector as Vector<Vector<String>>)[rowIndex][columnIndex] = "$aValue"

        // fireTableCellUpdated(rowIndex, columnIndex);  
    }

}
