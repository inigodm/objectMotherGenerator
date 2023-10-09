package inigo.objectMotherCreator.infraestructure.config

import java.util.*
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.table.DefaultTableModel

class TableModelSpecial(data: Vector<Vector<String>>, columnNames: Vector<String>) : DefaultTableModel(data, columnNames) {
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

    fun listSelectionListenerCreator(): ListSelectionListenerSpecial {
        return ListSelectionListenerSpecial(this)
    }
    class ListSelectionListenerSpecial(val tableModel: TableModelSpecial): ListSelectionListener {

        override fun valueChanged(e: ListSelectionEvent?) {
            println("changing" + e?.source)
            val lsm = e?.source as ListSelectionModel
            if (!lsm.valueIsAdjusting) {
                println("Selection changed")
                tableModel.dataVector.map { println(it) }
            }
            tableModel.dataVector.map { println(it) }
        }

    }


}

