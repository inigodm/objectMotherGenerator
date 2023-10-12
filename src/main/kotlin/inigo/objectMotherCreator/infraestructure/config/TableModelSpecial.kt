package inigo.objectMotherCreator.infraestructure.config

import inigo.objectMotherCreator.application.values.JavaFakeValuesGenerator
import java.util.*
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.DefaultTableModel

class TableModelSpecial(data: Vector<Vector<String>>, columnNames: Vector<String>) : DefaultTableModel(data, columnNames) {

   /* init {
        this.addTableModelListener {
            fun tableChanged(e: TableModelEvent) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    val row = e.getFirstRow();
                    val column = e.getColumn();
                    val newValue: String = this.getValueAt(row, column) as String

                    // Update your underlying data structure (e.g., vectors) with the new value.
                    // For example, if you have a List of Vectors, you can do:
                    (this.dataVector.get(row) as Vector<String>).setElementAt(newValue, column);
                }
            }
        };
    }*/

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
        return true
    }

    override fun setValueAt(aValue: Any, rowIndex: Int, columnIndex: Int) {
        (dataVector as Vector<Vector<String>>)[rowIndex][columnIndex] = "$aValue"
        fireTableCellUpdated(rowIndex, columnIndex)
    }

    fun listSelectionListenerCreator(): ListSelectionListenerSpecial {
        return ListSelectionListenerSpecial(this)
    }

    fun cleanVoids() {
        var i = 0
        val toRemove = mutableListOf<Int>()
        for (vector in (dataVector as Vector<Vector<String>>)) {
            if (!vector.any { !it.isNullOrEmpty() }) {
                toRemove.addFirst(i)
            }
            i++
        }
        toRemove.forEach { dataVector.removeAt(it) }
        if (!toRemove.isEmpty()) {
            fireTableDataChanged()
        }
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

fun <E> MutableList<E>.addFirst(e: E) {
    this.add(0, e)
}
