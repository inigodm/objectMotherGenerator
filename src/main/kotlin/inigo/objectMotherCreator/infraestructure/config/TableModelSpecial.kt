package inigo.objectMotherCreator.infraestructure.config

import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
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
        (dataVector as Vector<Vector<String>>)[rowIndex][columnIndex] = "$aValue"
        println("in row: $rowIndex and column: $columnIndex we have value: $aValue")
        println("in dataVector row: $rowIndex and column: $columnIndex we have value: ${(dataVector as Vector<Vector<String>>)[rowIndex][columnIndex]}")
        // fireTableCellUpdated(rowIndex, columnIndex);  
    }

    fun toCollection() : MutableList<Collection<String>> {
        val res : MutableList<Collection<String>> = mutableListOf()
        (dataVector as Vector<Vector<String>>).forEach {
            res.add(it.toList())
        }
        return res;
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

